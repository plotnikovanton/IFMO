module Integr (integrSin) where

-- Find intgr value with right delta
integrTrap :: (Double -> Double) -> Double -> Double -> Double -> Double
-- 'a' should be lt 'b'
integrTrap f a b delta = check 10000 where
    check n | abs (i1 - i2) < delta = i2
            | otherwise             = check $ 2 * n
            where
            i1 = integrOnce f a b n
            i2 = integrOnce f a b (2 * n)

-- Integrates with 'n' steps
integrOnce :: (Double -> Double) -> Double -> Double -> Double -> Double
integrOnce f a b n = helper n where
    l = (a + b) / n
    -- Should be optimized by compiller
    helper 0 = 0
    helper n = l * (f p + f (p+l) ) / 2 + helper (n - 1) where
        p = b - l * n

-- Calls 'integrTrap' with right bounds order
integr :: (Double -> Double) -> Double -> Double -> Double -> Double
integr f a b delta | a == b     = 0
                   | a > b      = integrTrap f b a delta
                   | otherwise  = integrTrap f a b delta


integrSin = integr (\x -> x * sin x) 0 pi
