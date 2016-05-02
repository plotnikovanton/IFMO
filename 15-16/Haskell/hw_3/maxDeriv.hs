-- Homework 3. Max Deriviate
-- Plotnikov Anton
-- A3401
--
-- 12.10.2015

module MaxDeriv where

-- Finds index of max deriviate for list of reals
maxDeriv :: Real a => [a] -> Int
maxDeriv []         = error "Empty list"
maxDeriv ys'@(y:ys) = fst $ foldl
    (\a'@(_, a) b'@(_, b) -> if a == max a b then a' else b')
    (0, x) (zip [1..] xs) where
        x:xs = zipWith (-) (ys ++ [0]) (init ys' ++ [0])
