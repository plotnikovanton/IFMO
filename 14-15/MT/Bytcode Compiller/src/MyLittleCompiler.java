import interfaces.Node;

import java.util.ArrayList;
import java.util.List;

public class MyLittleCompiler implements interfaces.Compiler {
    private final List<String> output;
    private int cnt = 0;

    public MyLittleCompiler() {
        this.output = new ArrayList<>();
    }

    private void gen(String command)  {
        output.add(command);
        cnt ++;
    }

    public void step(Node node) {

        switch (node.getKind()) {

            case VAR: {
                gen("ifetch");
                gen(node.getValue());
                break;
            }
            case CONST: {
                gen("ipush");
                gen(node.getValue());
                break;
            }
            case ADD: {
                step(node.getNodes().get(0));
                step(node.getNodes().get(1));
                gen("iadd");
                break;
            }
            case SUB: {
                step(node.getNodes().get(0));
                step(node.getNodes().get(1));
                gen("isub");
                break;
            }
            case MULT: {
                step(node.getNodes().get(0));
                step(node.getNodes().get(1));
                gen("imul");
                break;
            }
            case DIV: {
                step(node.getNodes().get(0));
                step(node.getNodes().get(1));
                gen("idiv");
                break;
            }
            case MOD: {
                step(node.getNodes().get(0));
                step(node.getNodes().get(1));
                //TODO
                gen("irem");
                break;
            }
            case LT: {
                step(node.getNodes().get(0));
                step(node.getNodes().get(1));
                gen("ilt");
                break;
            }case GT: {
                step(node.getNodes().get(0));
                step(node.getNodes().get(1));
                gen("igt");
                break;
            }
            case AND: {
                step(node.getNodes().get(0));
                step(node.getNodes().get(1));
                gen("and");
                break;
            }
            case SET: {
                step(node.getNodes().get(1));
                gen("istore");
                gen(node.getNodes().get(0).getValue());
                break;
            }
            case IF1: {
                step(node.getNodes().get(0));
                gen("jz");
                int adr = cnt;
                gen("BLANK");
                step(node.getNodes().get(1));
                output.set(adr, Integer.toString(cnt));
                break;
            }
            case IF2: {
                step(node.getNodes().get(0));
                gen("jz");
                int addr1 = cnt; gen("BLANK");
                step(node.getNodes().get(1));
                gen("jmp");
                int addr2 = cnt;
                gen("BLANK");
                output.set(addr1, Integer.toString(cnt));
                step(node.getNodes().get(2));
                output.set(addr2, Integer.toString(cnt));
                break;
            }
            case WHILE: {
                int addr1 = cnt;
                step(node.getNodes().get(0));
                gen("jz");
                int addr2 = cnt;
                gen("BLANK");
                step(node.getNodes().get(1));
                gen("jmp");
                gen(Integer.toString(addr1));
                output.set(addr2, Integer.toString(cnt));
                break;
            }

            case SEQ: {
                node.getNodes().forEach(this::step);
                break;
            }
            case EXPR: {
                step(node.getNodes().get(0));
                //gen("ipop");
                break;
            }
            case PROG: {
                node.getNodes().forEach(this::step);
                gen("HALT");
                break;
            }
        }

    }

    @Override
    public List<String> compile(Node root) {
        step(root);
        return output;
    }
}

