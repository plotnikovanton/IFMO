module Prog where

data Expr = Constant Integer | Variable String | Operator String [Expr] deriving (Show, Eq)
data Progr = Skip | Assign String Expr | Sequence [Progr] |
             If Expr Progr Progr | While Expr Progr deriving (Show, Eq)
