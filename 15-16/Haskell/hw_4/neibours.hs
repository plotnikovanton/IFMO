-- Homework 4. Neibours
-- Plotnikov Anton

module Neibours (neibours) where

neibours :: [Integer] -> [Int]
neibours = map fst . filter (\(i, [a, b, c]) -> b == c - a) . zip [1..] . partials

partials [_, _] = []
partials [_] = []
partials [] = []
partials xs = take 3 xs : partials (tail xs)

