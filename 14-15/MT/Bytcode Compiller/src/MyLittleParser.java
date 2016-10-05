import interfaces.Tokenizer;
import interfaces.Node;
import interfaces.Parser;
import interfaces.Token;

import java.io.IOException;

public class MyLittleParser implements Parser {

    private final Tokenizer lexer;
    private Token token;

    public MyLittleParser(Tokenizer lexer) {
        this.lexer = lexer;
    }

    private void nextToken() throws IOException {
        token = lexer.nextTok();
    }

    private Node term() throws IOException {
        if (token.getType().equals(Tokenizer.Types.ID)) {
            Node n = new MyLittleNode(Kinds.VAR, token.getValue());
            nextToken();
            return n;
        } else if (token.getType().equals(Tokenizer.Types.INT)) {
            Node n = new MyLittleNode(Kinds.CONST, token.getValue());
            nextToken();
            return n;
        }
        return parenExpr();
    }

    private Node sum() throws IOException {
        Node n = term();

        while (token.getType().equals(Tokenizer.Types.MOD)){
            Kinds kind = Kinds.MOD;
            nextToken();
            Node newOne = new MyLittleNode(kind, "");
            newOne.getNodes().add(n);
            newOne.getNodes().add(term());
            n = newOne;
        }

        while (token.getType().equals(Tokenizer.Types.MULT) || token.getType().equals(Tokenizer.Types.DIV)) {
            Kinds kind = Kinds.MULT;
            if (token.getType().equals(Tokenizer.Types.DIV)) {
                kind = Parser.Kinds.DIV;
            }
            nextToken();
            Node newOne = new MyLittleNode(kind, "");
            newOne.getNodes().add(n);
            newOne.getNodes().add(term());
            n = newOne;
        }

        while (token.getType().equals(Tokenizer.Types.PLUS) || token.getType().equals(Tokenizer.Types.MINUS)) {
            Kinds kind = Parser.Kinds.SUB;
            if (token.getType().equals(Tokenizer.Types.PLUS)) {
                kind = Parser.Kinds.ADD;
            }
            nextToken();
            Node newOne = new MyLittleNode(kind, "");
            newOne.getNodes().add(n);
            newOne.getNodes().add(term());
            n = newOne;
        }
        return n;
    }

    private Node test() throws IOException {
        Node n = sum();
        if (token.getType().equals(Tokenizer.Types.LESS)) {
            nextToken();
            Node newOne = new MyLittleNode(Kinds.LT, "LT");
            newOne.getNodes().add(n);
            newOne.getNodes().add(sum());
            n = newOne;
        } else if (token.getType().equals(Tokenizer.Types.GREATER)) {
            nextToken();
            Node newOne = new MyLittleNode(Kinds.GT, "GT");
            newOne.getNodes().add(n);
            newOne.getNodes().add(sum());
            n = newOne;
        }
        return n;
    }

    private Node expr() throws IOException {
        if (!token.getType().equals(Tokenizer.Types.ID)) {
            return test();
        }
        Node n = test();

        if (n.getKind().equals(Kinds.VAR) && token.getType().equals(Tokenizer.Types.EQUAL)) {
            nextToken();
            Node parentN = new MyLittleNode(Kinds.SET, "SET");
            parentN.getNodes().add(n);
            parentN.getNodes().add(expr());
            n = parentN;
        }
        return n;
    }

    private Node parenExpr() throws IOException {
        if (token.getType() != Tokenizer.Types.LBRAC) {
            throw new IOException(" ( expected");
        }
        nextToken();
        Node n = expr();
        if (token.getType().equals(Tokenizer.Types.AND)) {
            nextToken();
            Node newOne = new MyLittleNode(Kinds.AND, "AND");
            newOne.getNodes().add(n);
            newOne.getNodes().add(expr());
            n = newOne;
        }
        if (token.getType() != Tokenizer.Types.RBRAC) {
            throw new IOException(" ) expected");
        }
        nextToken();
        return n;
    }

    private Node statement() throws IOException {

        Node n = null;

        if (token.getType().equals(Tokenizer.Types.IF)) {
            n = new MyLittleNode(Kinds.IF1, "IF1");
            nextToken();
            n.getNodes().add(parenExpr());
            n.getNodes().add(statement());
            if (token.getType().equals(Tokenizer.Types.ELSE)) {
                n.setKind(Kinds.IF2, "IF2");
                nextToken();
                n.getNodes().add(statement());
            }
        } else if (token.getType().equals(Tokenizer.Types.WHILE)) {
            n = new MyLittleNode(Kinds.WHILE, "WHILE");
            nextToken();
            n.getNodes().add(parenExpr());
            n.getNodes().add(statement());
        } else if (token.getType().equals(Tokenizer.Types.LFIG)) {
            nextToken();
            while (!token.getType().equals(Tokenizer.Types.RFIG)) {
                n = new MyLittleNode(Kinds.SEQ, "SEQ");
                n.getNodes().add(statement());
            }
            nextToken();
        } else {
            n = new MyLittleNode(Kinds.EXPR, "EXPR");
            n.getNodes().add(expr());
            if (!token.getType().equals(Tokenizer.Types.SEMICOLON)) {
                throw new IOException("; expected");
            }
            nextToken();
        }
        return n;
    }

    @Override
    public Node parse() throws IOException {

        nextToken();
        Node node = new MyLittleNode(Kinds.PROG, "");
        while (token.getType() != Tokenizer.Types.EOF) {
            node.getNodes().add(statement());
        }
        return node;
    }
}
