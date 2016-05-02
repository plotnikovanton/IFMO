-- Homework 1. Palindrome
-- Plotnikov Anton
-- A3401
--
-- 12.09.2015

-- Reverse Integer value
reverseInt :: Integer -> Integer
reverseInt = reverse_acc 0 where -- Helper with accumulator
    reverse_acc :: Integer -> Integer -> Integer
    reverse_acc acc 0 = acc
    reverse_acc acc a = reverse_acc (10 * acc+ a `mod` 10) (a `div` 10)

palindrom :: Integer -> Bool
palindrom a = a == reverseInt a
