module OuterRedex where

import Expression
import Data.Maybe

outerRedex :: Expression -> Maybe Expression
outerRedex e@(Application (Lambda _ _) _) = Just e
outerRedex e@(Application (Application (Function _) (Constant _)) (Constant _)) = Just e
outerRedex (Application e1 e2) = let oe1 = outerRedex e1
                              in if isJust oe1 then oe1 else outerRedex e2
outerRedex (Lambda _ e) = outerRedex e
outerRedex e = Nothing

testRegex1 = Application (Lambda "x" (Function "f")) (Variable "x")
testRegex2 = Lambda "a" (Application (Function "g") testRegex1)
testRegex3 = Application (Application (Function "+") (Constant 42)) (Constant 24)
