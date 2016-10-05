package interfaces;

import java.util.List;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 25.05.15.
 */
public interface Node {
    public Parser.Kinds getKind();
    public void setKind(Parser.Kinds new_kind, String new_value);
    public String getValue();
    public List<Node> getNodes();
}
