-- Homework 10. Region
-- Plotnikov Anton
-- A3401
--
-- 27.12.2015

module Region where
import Prog
import Data.List
import Data.Maybe
import Dependers

region :: String -> Progr -> Maybe Progr
region v p@(Assign x _) = if v == x then Just p else Nothing
region v w@(While e p) = if v `elem` getExVars e then Just w else region v p
region v p@(If e p1 p2) | v `elem` getExVars e = Just p
                        | isJust p1res = p1res
                        | otherwise = p2res
    where
    p1res = region v p1
    p2res = region v p2
region v (Sequence ps) = case outSeq of
                            []  -> if null deeper then Nothing else head deeper
                            [p] -> region v p
                            _   -> Just $ Sequence outSeq
                       -- | any (isAssign v) ps = if null outSeq then Nothing else Just $ Sequence outSeq
                       -- | otherwise = if null resultsIfNotAssigned then Nothing else head resultsIfNotAssigned
    where
    --if we should go deeper
    deeper = filter isJust (region v <$> ps)

    --resultsIfNotAssigned = filter isJust $ map (region v) ps
    --outSeq = cutOutOfSeq (dropWhile (not . isVarIn v) ps) [] []
    outSeq = cutOutOfSeq (dropTilSeq ps) [] []

    dropTilSeq ps@(x:xs) = if isVarIn v x then ps else dropTilSeq xs
    dropTilSeq _ = []

    cutOutOfSeq :: [Progr] -> [Progr] -> [Progr] -> [Progr]
    cutOutOfSeq (x:xs) res acc | isVarIn v x = cutOutOfSeq xs newAcc newAcc
                               | otherwise = cutOutOfSeq xs res newAcc
        where newAcc = acc ++ [x]
    cutOutOfSeq [] res _ = res
region _ _ = Nothing

isVarIn :: String -> Progr -> Bool
isVarIn x (Assign v e) = x == v || x `elem` getExVars e
isVarIn x (While e p) = x `elem` getExVars e || isVarIn x p
isVarIn x (If e p1 p2) = x `elem` getExVars e || isVarIn x p1 || isVarIn x p2
isVarIn x (Sequence ps) = any (isVarIn x) ps
isVarIn _ _ = False

testProgSequence = Sequence
    [ Assign "a" (Constant 0)
    , Assign "x" (Constant 1)
    , Assign "y" (Constant 1)
    , If (Constant 1) (Assign "x" (Constant 1)) (Assign "y" (Constant 1))
    ]

testGlobalRegion = Sequence
    [ Sequence [Assign "x" (Constant 1)]
    , Sequence [Assign "x" (Constant 2)]
    ]

testProgD1 = Sequence
    [ Assign "x" (Constant 0)
    , Assign "y" (Variable "x")
    , Assign "z" (Constant 1)
    , If (Operator "<" [Variable "z", Variable "x"])
        (Assign "y" (Operator "-" [Variable "x", Constant 1]))
        (Assign "t" (Operator "+" [Variable "x", Constant 1]))
    ]

testProgD2 = Sequence
    [ Assign "x" (Constant 0)
    , Assign "y" (Variable "x")
    , Assign "z" (Constant 1)
    , If (Operator "<" [Variable "z", Variable "x"])
        (Assign "t" (Operator "-" [Variable "x", Constant 1]))
        (Assign "t" (Operator "+" [Variable "x", Constant 1]))
    , While (Constant 0) (Assign "t" (Constant 1))
    ]
