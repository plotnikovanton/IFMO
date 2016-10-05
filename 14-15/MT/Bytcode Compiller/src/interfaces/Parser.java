package interfaces;

import java.io.IOException;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 25.05.15.
 */

public interface Parser {
    enum Kinds {VAR, CONST, AND, ADD, SUB, MULT, DIV, MOD, LT, SET, IF1, IF2, WHILE, DO, EMPTY, SEQ, EXPR, GT, PROG};

    public Node parse() throws IOException;
}

