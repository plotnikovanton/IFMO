-- Homework 5
-- Plotnikov Anton
-- A3401

module RemoveBy where

-- Map using one link list
infixr 5 :+:
data Map key value = Tip | (key, value) :+: (Map key value) deriving Show

-- O(N)
get :: Eq k => k -> Map k v -> Maybe v
get _ Tip = Nothing
get k ((key, value) :+: next) | k == key  = Just value
                              | otherwise = get k next
-- O(N)
put :: Eq k => (k, v) -> Map k v -> Map k v
put e@(k, v) m = case get k m of
    Nothing -> e :+: m
    Just _  -> e :+: remove k m

-- O(N)
remove :: Eq k => k -> Map k v -> Map k v
remove _ Tip = Tip
remove k (e@(key, value) :+: next) | key == k  = next
                                   | otherwise = e :+: remove k next
-- O(N)
keys :: Map k v -> [k]
keys Tip = []
keys ((k, _) :+: next) = k : keys next

-- O(N)
values :: Map k v -> [v]
values Tip = []
values ((_, v) :+: next) = v : values next

removeBy :: (k -> Bool) -> Map k v -> Map k v
removeBy _ Tip = Tip
removeBy p (e@(k,_) :+: next) | p k       = removeBy p next
                              | otherwise = e :+: removeBy p next
