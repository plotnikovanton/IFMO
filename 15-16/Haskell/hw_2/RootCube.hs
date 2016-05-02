module RootCube (rootCube) where

-- Newtons mhetod step
-- function, dereviate, prew point
iter :: (Double -> Double) -> (Double -> Double) -> Double -> Double
iter f f' x = x - (f x) / (f' x)

-- Finds root with accuracy
-- function, dereviate, start point, accuracy
find :: (Double -> Double) -> (Double -> Double) -> Double -> Double -> Double
find f f' x0 accuracy | abs (x0 - x1) < accuracy    = x1
                      | otherwise                   = find f f' x1 accuracy
                      where
                        x1 = iter f f' x0

rootCube :: Double -> Double
rootCube a = find (\x -> x ** 3 - a) (\x -> 3 * x ** 2) (a / 2) 0.001
