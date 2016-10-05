package interfaces;

import java.io.IOException;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 25.05.15.
 */
public interface Tokenizer {
    enum Types {WHILE, INT, ID, LBRAC, RBRAC, LFIG, RFIG, IF, SEMICOLON, PLUS, MINUS, DIV, MOD, MULT, EOF, EQUAL, AND, ELSE, GREATER, LESS}

    public Token nextTok() throws IOException;
}
