-- Homework 3. Amount of numbers
-- Plotnikov Anton

module AmountOfNumbers  where
import Data.Char
import Data.Text (split, pack, unpack)

amountOfNumbers :: String -> Int
amountOfNumbers = length . filter' . split'

-- First of all lets simplify task by split string by lettrs
split' = filter (any isNumber) . filter (not . null) . map unpack . split (\x -> not (isNumber x) && x /= '+' && x /= '-') . pack

filter' :: [String] -> [String]
filter' = filter (\x -> lastIsDigit x && isOkBySign x)
    where
        lastIsDigit = isDigit . last
        signs = filter (not . null) . map unpack . split isNumber . pack
        isOkBySign x = null s || length (last s) == 1
            where s = signs x
