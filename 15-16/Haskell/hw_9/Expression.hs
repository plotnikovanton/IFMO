module Expression where

data Expression = Constant Integer | Variable String | Function String |
                  If Expression Expression Expression |
                  Application Expression Expression |
                  Lambda String Expression |
                  Let [(String, Expression)] Expression deriving (Show, Eq)
