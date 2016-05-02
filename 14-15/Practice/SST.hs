{-# LANGUAGE FlexibleContexts #-}

import Control.Monad.IO.Class
import Control.Monad.Error
import Data.List
import Data.Monoid
import Control.Monad.State.Lazy
import Data.Unique
import Data.Char
import System.Random
import System.IO.Unsafe

infixl 2 :@
infixr 3 :->

type Symb = String 

-- Терм
data Expr = 
  Var Symb 
  | Expr :@ Expr
  | Lam Symb Expr
     deriving (Eq,Show)

-- Тип
data Type = 
  TVar Symb 
  | Type :-> Type
    deriving (Eq,Show)

-- Контекст
newtype Env = Env [(Symb,Type)]
  deriving (Eq,Show)

-- Подстановка
newtype SubsTy = SubsTy [(Symb, Type)]
  deriving (Eq,Show)

-- Возвращает список свободных переменных терма
freeVars :: Expr -> [Symb] 
freeVars (Var a)    = [a]
freeVars (a :@ b)   = (freeVars a) `union` (freeVars b)
freeVars (Lam a e)  = filter (/= a) (freeVars e)

-- Возвращает список свободных переменных типа
freeTVars :: Type -> [Symb]
freeTVars (TVar a)  = [a]
freeTVars (a :-> b) = (freeTVars a) `union` (freeTVars b)

-- Расширяет контекст переменной с заданным типом 
extendEnv :: Env -> Symb -> Type -> Env
extendEnv (Env e) a t = Env (e ++ [(a, t)])

-- Возвращает список свободных типовых переменных контекста
freeTVarsEnv :: Env -> [Symb]
freeTVarsEnv (Env e) = foldr union [] $ map (freeTVars . snd) e 

-- Вовращвет тип переменной из контекста, если она там есть
appEnv :: (MonadError String m) => Env -> Symb -> m Type
appEnv (Env xs) v 
    | xs == []              = throwError $ "There is no variable \"" ++ v ++ "\" in the enviroment."
    | fst (head xs) == v    = return $ snd $ head xs 
    | otherwise             = appEnv (Env $ tail xs) v

-- Подставляет тип вместо переменных типа в тип
appSubsTy :: SubsTy -> Type -> Type
appSubsTy (SubsTy []) t = t
appSubsTy (SubsTy list) (TVar symbol) = if(fst(head list)) == symbol
                                        then (snd (head list))
                                        else appSubsTy (SubsTy(tail list)) (TVar symbol)
appSubsTy subs (a:-> b) = ((appSubsTy subs a) :-> (appSubsTy subs b))

-- Подставляет тип вместо переменных типа в контекст
appSubsEnv :: SubsTy -> Env -> Env
appSubsEnv s (Env xs) = Env $ map (\x -> (fst x, appSubsTy s (snd x))) xs

-- Выполняет композицию двух подстановок
composeSubsTy :: SubsTy -> SubsTy -> SubsTy
composeSubsTy a b = SubsTy $ sub1 ++ (helper2 a sub1) where
    sub1 = helper1 b a 
    
    helper1 :: SubsTy -> SubsTy -> [(Symb, Type)]
    helper1 (SubsTy a) subs = map (\x -> (fst x, appSubsTy subs (snd x))) a

    helper2 :: SubsTy -> [(Symb, Type)] -> [(Symb, Type)]
    helper2 (SubsTy a) b = filter (\x -> not (isIn b (fst x)) ) a 

    isIn :: [(Symb, Type)] -> Symb -> Bool 
    isIn [] a   = False
    isIn (x:xs) a = if a == fst x then True else isIn xs a

instance Monoid SubsTy where
    mappend = composeSubsTy
    mempty  = SubsTy []

-- Возвращает наиболее общий унификатор или сообщение об ошибке
unify :: (MonadError String m) => Type -> Type -> m SubsTy
unify (TVar a) (TVar b)
    | a == b = return $ SubsTy []
    | True   = return $ SubsTy [(a , TVar b)]
unify (TVar a) t
    | a `elem` freeTVars t  = throwError $ "Can't unify  ("++(show a)++") with ("++(show t)++")!"
    | otherwise             = return $ SubsTy [(a, t)]
unify (s1 :-> s2) (TVar a)  = unify (TVar a) (s1 :-> s2)
unify (s1 :-> s2) (t1 :-> t2) = do
    let tmp = unify s2 t2
    case tmp of
        Left err -> throwError $ err
        Right s  -> do 
            let tmp' = unify (appSubsTy s s1) (appSubsTy s t1)
            case tmp' of
                Left err -> throwError $ err
                Right s' -> return $ composeSubsTy s' s

-- Плохая генерация нового имени для переменной
randNumUnsafe :: (Random a, Num a) => [a]
randNumUnsafe = unsafePerformIO $ liftM (take 6 . randomRs (10, 15)) newStdGen

-- Построение уравнения на типы
equations :: (MonadError String m) => Env -> Expr -> Type -> m [(Type,Type)]
equations g (Var x) s = do
    x' <- appEnv g x
    return [(x', s)]
equations g (m :@ n) s = do
    let newvar = TVar (map intToDigit randNumUnsafe) 
    e1 <- equations g m (newvar :-> s)
    e2 <- equations g n newvar 
    return $ e1 `union` e2
equations g (Lam x m) s = do
    let a = TVar (map intToDigit randNumUnsafe)
    let b = TVar (map intToDigit randNumUnsafe)
    e1 <- equations (extendEnv g x a) m b
    return $ e1 `union` [(a :-> b, s)]

-- Решение одного уравнения
solveOne :: (Type, Type) -> SubsTy
solveOne (a,b) = do
    let Right ans = unify a b
    ans

-- Поиск главной пары
principlePair :: (MonadError String m) =>  Expr -> m (Env,Type)
principlePair exp = do
    let env = Env $ map (\x -> (x,TVar $ x ++ "'")) (freeVars exp)
    let t   = TVar (map intToDigit randNumUnsafe)
    eq <- equations env exp t
    
    let a = foldr (\x y -> ((fst x :-> fst y),(snd x :-> snd y))) (head eq) (tail eq)
    let s = unify (fst a) (snd a)

    case s of 
        Left err -> throwError $ err
        Right s  -> return $ (appSubsEnv s env, appSubsTy s t)
