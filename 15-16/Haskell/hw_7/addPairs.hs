-- Homework 7. Add Pairs
-- Plotnikov Anton
-- A3401
--
-- 22.11.2015

module AddPairs where
import Data.List
import Data.Char
import Data.Function
import Data.Ord

-- Works fine using brute force with 10 operators
data Pair = Operand (String, Pair, Pair) | Value String
instance Show Pair where
    show (Value x) = x
    show (Operand (op, l, r)) = "(" ++ show l ++ op ++ show r ++ ")"

addPairs :: String -> String
addPairs = show . solve . tokenize

tokenize :: String -> [String]
tokenize = unfoldr token
    where
    token :: String -> Maybe (String, String)
    token [] = Nothing
    token s@(x:xs) = Just $ if isDigit x then span isDigit s else ([x], xs)

-- In each iteration split input into 2 subarrays, surrund them with pairs and
-- maximezes rusults
evaluate :: Pair -> Int
evaluate (Operand (op, l, r)) = case op of
                               "+" -> evaluate l + evaluate r
                               "-" -> evaluate l - evaluate r
                               "*" -> evaluate l * evaluate r
evaluate (Value x) = read x :: Int

generateProposes :: [String] -> [Pair]
generateProposes [str]  = [Value str]
generateProposes xs     = [ Operand (op, lp, rp)
                          | n <- [1, 3..length xs-1]
                          , let (op, l, r) = mySplit n xs
                          , lp <- generateProposes l
                          , rp <- generateProposes r ]

mySplit :: Int -> [String] -> (String, [String], [String])
mySplit n tokens = (op, l, r)
    where
    (l, tmp) = splitAt n tokens
    op:r = tmp

solve :: [String] -> Pair
solve = maximumBy (comparing evaluate) . generateProposes
