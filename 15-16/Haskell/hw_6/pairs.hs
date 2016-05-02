-- Homework 6. Pairs
-- Plotnikov Anton
-- A3401
--
-- 10.11.2015

module Pairs where

pairs :: [(Integer, Integer)]
pairs = [(a, s-a) | s <- [5..], a <- [2..s `div` 2], gcd a (s-a) == 1]
