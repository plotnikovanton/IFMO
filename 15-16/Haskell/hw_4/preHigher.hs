preHigher :: Ord a => [a] -> [Int]
preHigher l@(x:xs) = map fst $ filter (\(_, (a, b)) -> a < b) $ zip [0..] $ zip l xs
