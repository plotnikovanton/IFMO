package interfaces;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 25.05.15.
 */
public interface Token {
    public Tokenizer.Types getType();
    public String getValue();
}
