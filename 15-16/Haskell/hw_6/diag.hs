-- Homework 6. Diag
-- Plotnikov Anton
-- A3401
--
-- 10.11.2015

module Diag where

diag :: [a] -> [[a]]
diag xs = let
    stepShift = [1..]
    dropShift = scanl1 (+) [0..]
    in
    [stepFilter ss $ drop ds xs | (ss, ds) <- zip stepShift dropShift]

getMatrix :: Int -> Int -> [[a]] -> [[a]]
getMatrix h w = map (take h) . take w

stepFilter :: Int -> [a] -> [a]
stepFilter n xs = let
    ys@(l:_) = drop (n - 1) xs
    in l : stepFilter (n + 1) ys

