-- Homework 3. Equals Segments
-- Plotnikov Anton

module SegmEquals (segmEquals) where
import Data.Function
import Data.List
segmEquals :: Ord a => [a] -> [a]
segmEquals xs = maximumBy (compare `on` length) $ group xs

