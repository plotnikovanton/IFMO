# Отчет о работе
* **Выполнил:** Плотников А. М. гр.3743
* **Научный руководитель:** Кубенский А. А. - доцент кафедры высшей математики ИТМО

## Изученные материалы
Изучены основы лямбда исчисления и функционального программирования в частности стратегии редуцции, теорема Чёрча-Россера, алгоритмы унификации и Хиндли_Милнера. А так же общие принципы работы с функциональными языками программирования.

## Подробное описание

В ходе работы реализована небольшая библеотека на языке `haskell` для работы с просто типизированными лямбда термами.

### Типы данных

Для ввода термов используется следующая структура данных
```haskell
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
```
Здесь последовательность `:->` эквивалент стрелке в типе, а `:@` обозначает аппликацию.

Переменная терма имеет тип `Var`, например `Var "x"`. Переменная типа `TVar`.

Таким образом терм может быть записан в подобном виде `Lam "x" $ Var "X" :@ Var "y"`

### Функции
#### freeVars :: Expr -> [Symb]
Возвращает список свободных переменных мерма.
```haskell
λ> freeVars $ Lam "x" $ Lam "y" (Var "a" :@ Var "b" :@ Var "y") :@ Var "x"
["a","b"]
```
#### freeTVars :: Type -> [Symb]
Возвращает список свободных переменных типа(все переменные в SST свободные).
```haskell
λ> freeTVars $ TVar "a" :-> TVar "b" :-> TVar "c"
["a","b","c"]
```

#### extendEnv :: Env -> Symb -> Type -> Env
Расширяет контекст переменной с заданным типом
```haskell
λ> let myEnv = Env [("a", TVar "x")]
λ> extendEnv myEnv "b" (TVar "y")
Env [("a",TVar "x"),("b",TVar "y")]
```

#### freeTVarsEnv :: Env -> [Symb]
Возвращает список типовых переменных контекста
```haskell
λ> let myEnv = Env [("a",TVar "x" :-> TVar "y"),("b",TVar "y")] in freeTVarsEnv myEnv
["x","y"]
```

#### appEnv :: (MonadError String m) => Env ->  Symb -> m Type
Позаоляет использовать контекст как частичную функцию из переменных в типы. Если переменной в типе нет, то возвращается ошибка.
```haskell
λ> let ex = ("x", TVar "a" :-> TVar "b" :-> TVar "c")
λ> let ey = ("y", TVar "a" :-> TVar "a")
λ> let myEnv = Env [ex,ey]

λ> let Right ans = appEnv myEnv "x" in ans
TVar "a" :-> (TVar "b" :-> TVar "c")

λ> let Right ans = appEnv myEnv "y" in ans
TVar "a" :-> TVar "a"

λ> let Left ans = appEnv myEnv "z" in ans
"There is no variable \"z\" in the enviroment."
```

#### appSubsTy :: SubsTy -> Type -> Type
Подставляет тип вместо переменных типа в тип.
```haskell
λ> appSubsTy (SubsTy [("a", TVar "d" :-> TVar "c")]) (TVar "a" :-> TVar "b")
(TVar "d" :-> TVar "c") :-> TVar "b"
```

#### appSubsEnv :: SubsTy -> Env -> Env
Подставляет тип вместо переменных типа в контекст.

#### composeSubsTy :: SubsTy -> SubsTy -> SubsTy
Выполняет композицию двух подстановок, используется для тогоя, чтобы сделать `SubsTy` представителем `Monoid`

```haskell
λ> SubsTy [("a", TVar "b")] `mappend` SubsTy [("b", TVar "a")]
SubsTy [("b",TVar "b"),("a",TVar "b")]
```

#### unify :: (MonadError String m) => Type -> Type -> m SubsTy
Возвращает наиболее общий унификатор, если это возможно, иначе сообщение об ошибке.

```haskell
λ> let t1 = TVar "a" :-> TVar "b" :-> TVar "c"
λ> let t2 = (TVar "x" :-> TVar "y") :-> TVar "z"

λ> let Right ans = unify t1 t2 in ans
SubsTy [("z",TVar "b" :-> TVar "c"),("a",TVar "x" :-> TVar "y")]

λ> let Left ans = unify (TVar "a") (TVar "a" :-> TVar "a") in ans
"Can't unify  (\"a\") with (TVar \"a\" :-> TVar \"a\")!"
```

#### equations :: (MonadError String m) => Env -> Expr -> Type -> m [(Type,Type)]
Построение системы уравнений на типы
```haskell
λ> let t = Lam "y" (Var "x")
λ> let e = Env [("x", TVar "a" :-> TVar "b")]
λ> let Right ans = equations e t (TVar "o") in ans
[(TVar "a" :-> TVar "b",TVar "feffec"),(TVar "abdecd" :-> TVar "feffec",TVar "o")]
```

#### principlePair :: (MonadError String m) =>  Expr -> m (Env,Type)
Поиск главное пары
```haskell
let Right ans = principlePair (Lam "x" $ Lam "y" $ Var "y") in ans
(Env [],TVar "bcafda" :-> (TVar "fdcabd" :-> TVar "fdcabd"))

λ> let Left ans = principlePair (Var "x" :@ Var "x") in ans
"Can't unify  (\"fcbcef\") with (TVar \"fcbcef\" :-> TVar \"becaee\")!"
```

[Описание алгоритма](http://www.slideshare.net/compscicenter/2015-48092967?ref=https://compscicenter.ru/courses/func-prog/2015-spring/classes/1331/)

## Список литературы
* Курс виео-лекций Москвина Д. Н.
* ["О haskell по-человечески"](http://www.twitch.tv/317070) - Денис Шевченко
* ["Learn Haskell Fast and Hard"](http://yannesposito.com/Scratch/en/blog/Haskell-the-Hard-Way/)
* ["Learn You a Haskell for Great Good!"](http://learnyouahaskell.com/)
