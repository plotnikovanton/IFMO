import interfaces.Tokenizer;
import interfaces.Token;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 26.05.15.
 */
public class MyLittleToken implements Token {
    private final Tokenizer.Types type;
    private final String value;

    public MyLittleToken(Tokenizer.Types type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Tokenizer.Types getType() {
        return type;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
