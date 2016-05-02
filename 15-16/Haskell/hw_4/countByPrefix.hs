-- Homework 4. Count by prefix
-- Plotnikov Anton
-- A3401
--
-- 18.10.2015

module CountByPrefix (countByPrefix) where
import Data.List

data Trie = Empty | Node Char [Trie] deriving Show
type Dictionary = [Trie]

countByPrefix :: String -> Dictionary -> Int
countByPrefix str d = case goDeep str d of
    Nothing -> error $ "No worlds with prefix: " ++ str
    Just t  -> countChilds t

-- Dive into tree to the right node
goDeep :: String -> Dictionary -> Maybe Trie
goDeep []  d    = Just (Node '\0' d)
goDeep [x] d    = findChar x d
goDeep (x:xs) d = case findChar x d of
    Nothing          -> Nothing
    Just Empty       -> Nothing
    Just (Node _ d') -> goDeep xs d'

-- Finds tree with given char if can't then Nothing
findChar :: Char -> Dictionary -> Maybe Trie
findChar a = find helper
    where
        helper x = case x of
            Empty -> False
            Node c _ -> c == a

-- BFS into tree and count leafs
countChilds :: Trie -> Int
countChilds Empty = 1
countChilds (Node c xs) = sum (map countChilds xs)

test = [Node 'b' [Node 'i' [Node 't' [Empty,
                                      Node 'e' [Empty]]],
                  Node 'y' [Node 't' [Node 'e' [Empty]]]],
        Node 's' [Node 'i' [Node 't' [Node 'e' [Empty]]]]]
