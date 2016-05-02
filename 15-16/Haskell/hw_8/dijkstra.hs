-- Homework 7. Dijkstra
-- Plotnikov Anton
-- A3401
--
-- 07.12.2015

module Dijkstra where
import Data.List
import Data.Maybe
import Data.Ord (comparing)
import qualified Data.Set as Set

inf :: Integer
inf = 9999999999999999999999999 -- Could be implemented by new type like Infinity | Only Integer

type Graph = [[(Int, Integer)]]
dijkstra :: Int -> Int -> Graph -> Maybe [Int]
dijkstra s end g = helper (g !! s) ([]) (0:take (length g - 1) (repeat inf)) ([])
    where
    ----------current vertices---visited---distances-----path-----distances--visited----path----
    --helper :: [(Int, Integer)] -> [Int] -> [Integer] -> [Int] -> ([Integer], [Int], Maybe [Int])
    helper [] _ _ _ = Nothing
    helper vs visited dist path | fst cur == end = ([], [], Just $ reverse $ fst cur : path)
                                | otherwise = helper (delete cur vs) (cur:visited) newDist (cur:path)
        where
        newDist = relax (fst cur)
        cur = minimumBy (comparing ((dist !!) . fst)) vs


        relax :: Int -> [Integer]
        relax v = foldr helper' dist childs
            where
            childs = g !! cur
            curW = dist !! cur

            helper' :: (Int, Integer) -> [Integer] -> [Integer]
            helper' (a, w) dist | curW + w < dist !! a = dist
                                | otherwise            = set dist a (curW + w)


set ys i newValue = l ++ (newValue : xs)
    where
    (l, (_:xs)) = splitAt i ys
