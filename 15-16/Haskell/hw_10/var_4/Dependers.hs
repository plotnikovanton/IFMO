-- Homework 10. Dependers
-- Plotnikov Anton
-- A3401
--
-- 27.12.2015

module Dependers where
import Prog
import Data.List

dependers :: String -> Progr -> [String]
dependers v p = findDep v p [v]

findDep :: String -> Progr -> [String] -> [String]
findDep v p used = clearDep `union` unclearDep used clearDep []
    where
    unclearDep :: [String] -> [String] -> [String] -> [String]
    unclearDep u (x:xs) acc | x `elem` u = unclearDep u xs acc
                            | otherwise  = unclearDep (x:u) xs (acc `union` findDep x p (x:u))
    unclearDep u [] acc = acc

    clearDep :: [String]
    clearDep = nub $ concatMap getExVars (assigns p)
    -- Returns sequence with Expressions right of 'v' assigns
    assigns :: Progr -> [Expr]
    assigns (Assign x e) = [e | x == v]
    assigns (Sequence ps) = concatMap assigns ps
    assigns (If _ p1 p2) = assigns p1 ++ assigns p2
    assigns (While _ p) = assigns p
    assigns _ = []

-- Returns all vars from Expr or Progr
getExVars :: Expr -> [String]
getExVars (Variable v) = [v]
getExVars (Operator _ es) = nub $ concatMap getExVars es
getExVars _ = []

testProg1 = Sequence [
    Assign "x" (Constant 1),
    Assign "x" (Operator "+" [Variable "y", Variable "a"]),
    Assign "y" (Operator "+" [Variable "z", Constant 1])]

testProgIfWhileSkip = Sequence 
    [ Skip
    , testProgReq
    , Assign "x" (Constant 1)
    , If (Constant 1) (Assign "y" (Variable "hello from if left side")) (Assign "x" (Variable "hello from if right side"))
    , While (Constant 1) testProg1
    , Assign "y" (Operator "+" [Variable "x", Constant 1])
    ]

-- Test recursive
testProgReq = Sequence [
    Assign "r1" (Variable "r2"),
    Assign "r2" (Variable "r1")]
