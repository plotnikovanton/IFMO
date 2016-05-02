-- Homework 9. Find Conflict
-- Plotnikov Anton
-- A3401
--
-- 09.12.2015

module FindConflict where
import Data.List
import Data.Maybe
import Expression

findConflict :: Expression -> Maybe String
findConflict e = head <$> res
    where
    xs = map (\(Application e1 e2) -> intersect (fst $ getVars e1) (snd $ getVars e2)) $ getApplications e
    res = find (/= []) xs

-- Returns list of applications where lambda on left
getApplications :: Expression -> [Expression]
getApplications e@(Application e1 e2) = case e1 of
    Lambda _ _ -> e : concatMap getApplications [e1, e2]
    _          -> concatMap getApplications [e1, e2]
getApplications (Lambda _ e) = getApplications e
getApplications _ = []

-- Returns «bounded» and «free» variables
getVars :: Expression -> ([String], [String])
getVars e = (bounded, vars \\ bounded)
    where
    (vars, bounded) = helper e

    -- Returns all variables from expressions and left side of lambda abstractions
    helper :: Expression -> ([String], [String])
    helper ex = case ex of
        Application e1 e2 -> (vars1 `union` vars2, abs1 `union` abs2)
            where (vars1, abs1) = helper e1
                  (vars2, abs2) = helper e2
        Lambda lam e -> (vars, lam:abs)
            where (vars, abs) = helper e
        Variable var -> ([var], [])
        _ -> ([], [])

testExpr = Lambda "a"
         ( Lambda "b"
         ( Application
           ( Lambda "d" ( Variable "d" ) )
           ( Lambda "e" ( Variable "d" ) ) ) )
