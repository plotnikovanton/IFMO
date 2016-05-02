module IsDangerous where
import Programm
import Data.List


isDangerous :: Progr -> Bool
isDangerous p = or (checkOneWhile <$> listOfWhiles p)

listOfWhiles :: Progr -> [Progr]
listOfWhiles p@(While _ ps) = p : listOfWhiles ps
listOfWhiles (Sequence ps) = concatMap listOfWhiles ps
listOfWhiles (If _ p1 p2) = listOfWhiles p1 ++ listOfWhiles p2
listOfWhiles _ = []

checkOneWhile :: Progr -> Bool
checkOneWhile (While e p) = not (and (flip isVarAssignedIn p <$> getVarsFromExpr e))
checkOneWhile _ = undefined

getVarsFromExpr :: Expr -> [String]
getVarsFromExpr (Variable v) = [v]
getVarsFromExpr (Operator _ es) = concatMap getVarsFromExpr es
getVarsFromExpr _ = []

isVarAssignedIn :: String -> Progr -> Bool
isVarAssignedIn v (Assign x _) = v == x
isVarAssignedIn v (Sequence ps) = or (isVarAssignedIn v <$> ps)
isVarAssignedIn v (If _ p1 p2) = isVarAssignedIn v p1 || isVarAssignedIn v p2
isVarAssignedIn v (While _ p) = isVarAssignedIn v p
isVarAssignedIn _ _ = False

testProg1 = Sequence
    [ While (Variable "x") (Assign "x" (Constant 1))
    , While (Variable "y") (
    Sequence
        [ Assign "y" (Constant 1)
        , While (Variable "z") (Assign "z" (Constant 1))
        ])
    ]

testProg2 = While (Operator "<" [Variable "x", Variable "m"]) (Assign "m" (Constant 1))

testProg3 = Sequence
    [ testProg1
    , testProg2
    ]
