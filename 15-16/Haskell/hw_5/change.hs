-- Homework 5
-- Plotnikov Anton
-- A3401

module Change where

data BinHeap e = BinHeap [e] deriving Show

change :: Ord e => Int -> e -> BinHeap e -> BinHeap e
change i e (BinHeap xs) = BinHeap $ increase i $ update i e xs

-- Push element to top
increase :: Ord e => Int -> [e] -> [e]
increase index elements = let
        current     = elements !! index
        parentIndex = index `div` 2
        parent      = elements !! parentIndex
        replaced    = update index parent $ update parentIndex current elements
    in if parent <= current then elements else increase parentIndex replaced

-- Update value in list by index
update :: Int -> a -> [a] -> [a]
update i value (x:xs) | i == 0    = value : xs
                      | otherwise = x : update (i-1) value xs
