-- Homework 9. Simplify
-- Plotnikov Anton
-- A3401
--
-- 12.12.2015

module Simplify where
import Data.List
import Data.Maybe
import Expression

simplify :: Expression -> Expression
simplify (Application (Application f@(Function op) e1) e2) = case op of
                                                                "+" | isZero se1 -> se2
                                                                    | isZero se2 -> se1
                                                                    | otherwise  -> otherRes
                                                                "-" | isZero se2 -> se1
                                                                    | otherwise  -> otherRes
                                                                "*" | isZero se1 || isZero se2 -> zero
                                                                    | isOne se1                -> se2
                                                                    | isOne se2                -> se1
                                                                    | otherwise                -> otherRes
                                                                _  -> otherRes
                                                             where
                                                                se1 = simplify e1
                                                                se2 = simplify e2
                                                                otherRes = Application (Application f se1) se2
simplify (Lambda var e)      = Lambda var (simplify e)
simplify (Application e1 e2) = Application (simplify e1) (simplify e2)
simplify e                   = e

isZero = (== zero)
isOne = (== one)

zero = Constant 0
one = Constant 1

testExpr1 = Application (Application (Function "+") zero) one
testExpr2 = Application (Application (Function "-") testExpr1) zero
testExpr3 = Application (Application (Function "*") testExpr2) one
testExpr4 = Application (Application (Function "*") zero) testExpr3
