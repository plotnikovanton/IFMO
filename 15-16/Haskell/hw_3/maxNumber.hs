-- Homework 3. Max Number
-- Plotnikov Anton
-- A3401
--
-- 12.10.2015

module MaxNumber (maxNumber) where
import Data.Char

-- Finds max number from given string
maxNumber :: String -> Integer
maxNumber s | any isDigit s = maximum $ splitLine s 
            | otherwise     = error "No digits here"

-- Takes Integers from given string
splitLine :: String -> [Integer]
splitLine [] = []
splitLine ss = if any isDigit ss then (read i :: Integer) : splitLine s else [] where
    (i, s) = span isDigit $ dropWhile (not . isDigit) ss
