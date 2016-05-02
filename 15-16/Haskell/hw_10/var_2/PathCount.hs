module PathCount where

import Programm


pathCount :: Progr -> Integer
pathCount x = pathCount' x + 1

pathCount' :: Progr -> Integer
pathCount' (If _ p1 p2) = pathCount p1 + pathCount p2
pathCount' (While _ p) = pathCount p
pathCount' (Sequence ps) = sum (pathCount' <$> ps)
pathCount' _ = 0

testProg1 = Sequence
    [ Assign "x" (Constant 1)
    , While (Constant 1) (If (Constant 1) (While (Constant 1) Skip) Skip)
    ]
