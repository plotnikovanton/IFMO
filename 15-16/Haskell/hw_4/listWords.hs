-- Homework 4. List words

module ListWords (listWords, test) where
import Data.List

data Trie = Empty | Node Char [Trie] deriving Show
type Dictionary = [Trie]

listWords :: Dictionary -> [String]
listWords = concatMap (helper "")

-- Вычленяет все слова из Trie
helper :: String -> Trie -> [String]
helper acc Empty = [acc]
helper acc (Node ch trs) = concatMap (helper (acc ++ [ch])) trs
