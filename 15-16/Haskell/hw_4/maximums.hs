-- Homework 4. Maximums
-- Plotnikov Anton
-- A3401
--
-- 18.10.2015

module Maximums where

maximums :: [Integer] -> [Integer]
maximums (x'':x':xs') = ans
    where
        (ans, _, _) = foldl helper ([], x'', x') xs'
        helper (acc, x'', x') x | x'' < x' && x' > x = (x':acc, x', x)
                                | otherwise          = (acc, x', x)
maximums _ = []
