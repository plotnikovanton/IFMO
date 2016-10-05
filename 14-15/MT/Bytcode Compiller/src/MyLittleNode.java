import interfaces.Node;
import interfaces.Parser;
import java.util.ArrayList;
import java.util.List;

public class MyLittleNode implements interfaces.Node {

    private Parser.Kinds kind;
    private String value;
    private final List<Node> nodes;

    public MyLittleNode(Parser.Kinds kind, String value){
        this.kind = kind;
        this.value = value;
        nodes = new ArrayList<>();
    }

    public Parser.Kinds getKind() {
        return kind;
    }

    @Override
    public void setKind(Parser.Kinds new_kind, String new_value) {
        this.kind = new_kind;
        this.value = new_value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public List<Node> getNodes() {
        return nodes;
    }
}
