-- Homework 4. Remove word

module RemoveWords (removeWord) where
import Data.List
import Data.Maybe

data Trie = Empty | Node Char [Trie] deriving (Show, Eq)
type Dictionary = [Trie]

removeWord :: String -> Dictionary -> Dictionary
removeWord s = map fromJust . filter (/= Nothing) . map (normalize . removeLeaf s)

-- This funciton searches the given word and remove leaf (Empty entity)
-- assigned to that word. It could produce the empty list inside nodes, so
-- we need to normalyze our structure after that procedure.
removeLeaf :: String -> Trie -> Trie
removeLeaf _ Empty = Empty
removeLeaf "" tree = tree
removeLeaf ([x]) t@(Node c dict) | x == c = Node c (filter (/= Empty) dict)
                                 | otherwise = t
removeLeaf (x:xs) t@(Node c dict) | x == c = Node c (map (removeLeaf xs) dict)
                                  | otherwise = t

-- This function resloves the empty children sets in nodes problem.
normalize :: Trie -> Maybe Trie
normalize Empty = Just Empty
normalize (Node _ []) = Nothing
normalize (Node c dict) = case children of
    [] -> Nothing
    _  -> Just (Node c (map fromJust children))
    where
        children = filter (/= Nothing) (map normalize dict)

