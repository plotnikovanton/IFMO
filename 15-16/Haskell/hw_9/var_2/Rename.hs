module Rename where
import Expression
import Data.List
import Data.Maybe
import Control.Arrow

rename :: String -> Expression -> Expression
rename oldName e = helper [] e
    where
    used = getAllVars e
    newName = fromJust $ find (`notElem` used) allStrings

    helper bounded (Lambda v e) = let bounded' = v:bounded in Lambda (trueV bounded' v) (helper bounded' e)
    helper bounded (Variable v) = Variable (trueV bounded v)
    helper bounded (Application e1 e2) = Application (helper bounded e1) (helper bounded e2)
    helper bounded (If e1 e2 e3) = If (helper bounded e1) (helper bounded e2) (helper bounded e3)
    helper bounded (Let xs e) = Let (map (trueV bounded *** helper bounded) xs) (helper bounded e)
    helper _ e = e

    trueV bounded v = if v == oldName && v `notElem` bounded then newName else v

-- List which contain ALL possible strings with lat alphabet
allStrings = [ c : s | s <- "": allStrings, c <- ['a'..'z'] ]

-- Returns list with all used variables
getAllVars :: Expression -> [String]
getAllVars (Variable var) = [var]
getAllVars (Lambda var e) = [var] `union` getAllVars e
getAllVars (Application e1 e2) = getAllVars e1 `union` getAllVars e2
getAllVars (If e1 e2 e3) = getAllVars e1 `union` getAllVars e2 `union` getAllVars e3
getAllVars (Let xs e) = foldr1 union (map (\(v, e) -> v:getAllVars e) xs) `union`
                        getAllVars e
getAllVars _ = []

testRegex1 = Application (Lambda "x" (Function "f")) (Variable "x")
testRegex2 = Lambda "a" (Application (Function "g") testRegex1)
testRegex3 = Let [("a", Variable "b")] (Application (Variable "a") testRegex2)
