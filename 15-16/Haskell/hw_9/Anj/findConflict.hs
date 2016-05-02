module FindConflict where
import Data.List
import Data.Maybe

data Expression = Constant Integer | Variable String | Function String |
                  If Expression Expression Expression |
                  Application Expression Expression |
                  Lambda String Expression |
                  Let [(String, Expression)] Expression deriving (Show, Eq)

findConflict :: Expression -> Maybe String
findConflict e = head <$> res
    where
    xs = map (\(Application e1 e2) -> intersect (fst $ getVars e1) (snd $ getVars e2)) $ listOfApps e
    res = find (/= []) xs

    listOfApps :: Expression -> [Expression]
    listOfApps (Lambda _ e) = listOfApps e
    listOfApps e@(Application e1 e2) = case e1 of
        Lambda _ _ -> e : listOfApps e1 ++ listOfApps e2
        _          -> listOfApps e1 ++ listOfApps e2
    listOfApps _ = []

    getVars :: Expression -> ([String], [String])
    getVars e = (bounded, vars \\ bounded)
        where
        (vars, bounded) = helper e

        helper :: Expression -> ([String], [String])
        helper ex = case ex of
            Application e1 e2 -> (vars1 `union` vars2, abs1 `union` abs2)
                where (vars1, abs1) = helper e1
                      (vars2, abs2) = helper e2
            Lambda lam e -> (vars, lam:abs)
                where (vars, abs) = helper e
            Variable var -> ([var], [])
            _ -> ([], [])
