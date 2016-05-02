module Simplify where
import Data.List
import Control.Arrow

data Expression = Constant Integer | Variable String | Function String |
                  If Expression Expression Expression |
                  Application Expression Expression |
                  Lambda String Expression |
                  Let [(String, Expression)] Expression deriving (Show, Eq)

simplify :: Expression -> Expression
simplify (Lambda var e)      = Lambda var (simplify e)
simplify (Application (Application f@(Function op) e1) e2) = let
                                                                isZero = (== Constant 0)
                                                                isOne = (== Constant 1)
                                                                simE1 = simplify e1
                                                                simE2 = simplify e2
                                                                elsReturnThis = Application (Application f simE1) simE2 in
                                                             if op == "+" then
                                                                if isZero simE1 then simE2 else
                                                                if isZero simE2 then simE1 else
                                                                elsReturnThis
                                                             else if op == "-" then
                                                                if isZero simE2 then simE1 else elsReturnThis
                                                             else if op == "*" then
                                                                if isZero simE1 || isZero simE2 then Constant 0 else
                                                                if isOne simE1                  then simE2 else
                                                                if isOne simE2                  then simE1 else
                                                                elsReturnThis
                                                             else elsReturnThis
simplify (Let es e) = Let (map (second simplify) es) (simplify e)
simplify (If e1 e2 e3) = If (simplify e1) (simplify e2) (simplify e3)
simplify (Application e1 e2) = Application (simplify e1) (simplify e2)
simplify e = e
