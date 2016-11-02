module RootCos (rootCos) where

rootCos :: Double -> Double
rootCos epsilon = find (-1) 1 epsilon

find :: Double -> Double -> Double -> Double
find a b epsilon | (b - a) < epsilon = c
                 | (f c) * (f a) < 0 = find a c epsilon
                 | otherwise         = find c b epsilon
                  where c   = abs (a + b) / 2
                        f x = cos x - x 