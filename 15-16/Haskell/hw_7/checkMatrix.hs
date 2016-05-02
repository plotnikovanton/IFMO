-- Homework 7. Check Matrix
-- Plotnikov Anton
-- A3401
--
-- 29.11.2015

module CheckMatrix where
import Data.List
import Data.Maybe (fromJust)

-- Course of !! operator bfs works with O(n^4)
--checkMatrix :: [[Int]] -> Bool
--checkMatrix m = checkRotations m && checkSCC m

checkRotations :: [[Int]] -> Bool
checkRotations m = all (uncurry4 helper) (zip4 m m' m'' m''')
    where
    ccw = reverse.transpose

    m'   = ccw m
    m''  = ccw m'
    m''' = ccw m''

    helper [] [] [] [] = True
    helper (x:xs) (y:ys) (z:zs) (k:ks)= (x + y + z + k == 1) && helper xs ys zs ks

uncurry4 f (a,b,c,d) = f a b c d

-- Checks that random 0's Strongly Connected Component size is 3/4 n^2
checkSCC :: [[Int]] -> Bool
checkSCC m = length (component (0, fromJust (elemIndex 0 (head m))) [])
             == (n * n * 3) `div` 4
    where
    n = (length . head) m
    component :: (Int, Int) -> [(Int, Int)] -> [(Int, Int)]
    component p@(x, y) acc | p `elem` acc = acc
                           | otherwise    = foldr component (p:acc) (ngbrs p acc)

    -- Points to be checked
    ngbrs :: (Int, Int) -> [(Int, Int)] -> [(Int, Int)]
    ngbrs (x, y) acc = filter (nFilt acc) [ (x + 1, y)
                                          , (x - 1, y)
                                          , (x, y + 1)
                                          , (x, y - 1) ]
    nFilt acc p@(x, y) = x >= 0 && x < n && y >= 0 && y < n &&
                         m !! x !! y == 0

testMatrix :: [[Int]]
testMatrix = [ [0, 0, 0, 0]
             , [0, 0, 0, 0]
             , [0, 1, 0, 0]
             , [0, 0, 0, 0] ]
