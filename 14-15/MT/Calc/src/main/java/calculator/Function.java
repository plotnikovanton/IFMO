package calculator;

import java.util.List;

/**
 * Calc
 *
 * Created by Plotnikov Anton on 14.04.15.
 */
public class Function implements Node{
    private final List<Node> childs;
    private final String name;

    public Function(String name, List<Node> childs) {
        this.childs = childs;
        this.name = name;
    }

    @Override
    public List<Node> childs() {
        return childs;
    }

    @Override
    public Double calculate() {
        Double[] arg = childs.stream().map(Node::calculate).toArray(Double[]::new);

        switch (name) {
            case "sin":
                return Math.sin(arg[0]);
            case "cos":
                return Math.cos(arg[0]);
            case "exp":
                return Math.exp(arg[0]);
            case "ln":
                return Math.log(arg[0]);
            case "lg":
                return Math.log10(arg[0]);
            case "tg":
                return Math.tan(arg[0]);
            case "ctg":
                return 1 / Math.tan(arg[0]);
            case "arctg":
                return Math.atan(arg[0]);
            case "arcctg":
                return Math.PI/2 - Math.atan(arg[0]);
            case "+":
                return arg[1] + arg[0]; 
            case "-":
                return arg[1] - arg[0]; 
            case "*":
                return arg[1] * arg[0]; 
            case "/":
                return arg[1] / arg[0]; 
            case "^":
                return Math.pow(arg[1],arg[0]);
            case "log":
                return Math.log(arg[0]) / Math.log(arg[1]);
        }
        return Double.NaN;
    }
}
