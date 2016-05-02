package calculator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calc
 *
 * Created by Plotnikov Anton on 12.04.15.
 */
public class ShuntingYard {
    private enum Associate {LEFT, RIGHT}
    private static final String DIVIDER = ",";
    private static final String ESCAPE_SET = "+*^";

    // Map name_of_function -> function properties
    final static private Map<String, FuncOptions> FUNCTIONS = new HashMap<String, FuncOptions>(){{
        put("-", new FuncOptions(2, 1));
        put("+", new FuncOptions(2, 1));
        put("/", new FuncOptions(2, 2));
        put("*", new FuncOptions(2, 2));
        put("^", new FuncOptions(Associate.RIGHT, 2, 3));
        put("sin", new FuncOptions(1));
        put("cos", new FuncOptions(1));
        put("exp", new FuncOptions(1));
        put("ln", new FuncOptions(1));
        put("lg", new FuncOptions(1));
        put("tg", new FuncOptions(1));
        put("ctg", new FuncOptions(1));
        put("arctg", new FuncOptions(1));
        put("arcctg", new FuncOptions(1));
        put("log", new FuncOptions(2));
    }};

    //Split input to tokens
    static List<String> split(String input) {
        List<String> FUNCTIONS_ESCAPED = new ArrayList<>(FUNCTIONS.keySet());
        FUNCTIONS.keySet().parallelStream().filter(s -> ESCAPE_SET.contains(s)).forEach(s -> {
            FUNCTIONS_ESCAPED.add("\\" + s);
            FUNCTIONS_ESCAPED.remove(s);
        });
        if (Main.DEBUG) System.err.println("#Availible for parser functions set: " + FUNCTIONS_ESCAPED);
        String regex =
                "(\\d+\\.?\\d*|\\(|\\)|" +
                FUNCTIONS_ESCAPED.stream().reduce((x, xs) -> xs + "|" + x).get()
                + "|\\" + DIVIDER + ")";
        Matcher m = Pattern.compile(regex).matcher(input);
        List<String> inputSplit = new LinkedList<>();
        while (m.find()) {
            inputSplit.add(m.group());
        }
        if (Main.DEBUG) {
            System.err.println("#Regex: " + regex);
            System.err.println("#Split source: " + inputSplit);
        }
        return inputSplit;
    }

    private static boolean isOperator(String token) {
        return FUNCTIONS.containsKey(token);
    }
    private static boolean isOperatorLeft(String token) {
        return FUNCTIONS.get(token).getAssociate().equals(Associate.LEFT);
    }
    private static boolean isOperatorRight(String token) {
        return FUNCTIONS.get(token).getAssociate().equals(Associate.RIGHT);
    }
    private static boolean isOpenBracket(String token) {
        return token.equals("(");
    }
    private static boolean isCloseBracket(String token) {
        return token.equals(")");
    }
    private static boolean isDivider(String token) {
        return token.equals(DIVIDER);
    }
    private static int getOpPriority(String token) {
        return FUNCTIONS.get(token).getPriority();
    }

    private static Node omnom(String token, Stack<Node> output) {
        List<Node> childs = new ArrayList<>();
        for (int i = 0; i < FUNCTIONS.get(token).getArguments(); i++) {
            childs.add(output.pop());
        }
        return new Function(token, childs);
    }
    private static boolean isNumeric(String token) {
        return Pattern.compile("^\\d+\\.?\\d*$").matcher(token).matches();
    }

    static Node convert(String input) {
        Queue<String> tokens = new ArrayDeque<>(split(input));
        Stack<Node> output = new Stack<>();
        Stack<String> stack = new Stack<>();

        while (!tokens.isEmpty()) {
            String token = tokens.poll();

            // If number put on output queue
            if (isNumeric(token)) {
                output.add(new Numeric(token));
            }
            // If operator
            else if (isOperator(token)) {
                // Put operators from stack in output while we can
                while (!stack.isEmpty() &&
                        isOperator(stack.lastElement()) && (
                        (isOperatorLeft(token) && getOpPriority(token) <= getOpPriority(stack.lastElement())) ||
                        (isOperatorRight(token) && getOpPriority(token) < getOpPriority(stack.lastElement()))
                        )){
                    output.push(omnom(stack.pop(), output));
                }
                // Put token to stack
                stack.push(token);
            }
            // If token is open bracket then pot it in stack
            else if (isOpenBracket(token)) {
                stack.push(token);
            }
            // If close bracket
            else if (isCloseBracket(token)) {
                // Put everything belongs to '(' in output
                while(!isOpenBracket(stack.lastElement())) {
                    output.push(omnom(stack.pop(), output));
                }
                // Drop '(' from stack
                stack.pop();
            }
            // If token is divider then push in out all besides (
            else if (isDivider(token)) {
                while (!isOpenBracket(stack.lastElement())) {
                    output.add(omnom(stack.pop(), output));
                }
            }
        }

        // Put tail of a stack in output
        while (!stack.isEmpty()) {
            output.add(omnom(stack.pop(), output));
        }

        return output.lastElement();
    }

    /**
     * Class with function's options
     */
    private static class FuncOptions {
        private Associate associate;
        private int arguments;
        private int priority;

        /**
         *
         * @param associate Function associative
         * @param arguments Number of function arguments
         * @param priority Function priority
         */
        public FuncOptions(Associate associate, int arguments, int priority) {
            this.associate = associate;
            this.arguments = arguments;
            this.priority = priority;
        }

        /**
         * Let associate be LEFT
         * @param arguments Number of function's arguments
         * @param priority Function priority
         */
        public FuncOptions(int arguments, int priority) {
            this.associate = Associate.LEFT;
            this.arguments = arguments;
            this.priority = priority;
        }

        /**
         * Let assotiative be LEFT and priority MAX
         * @param arguments Number of function's arguments
         */
        public FuncOptions(int arguments) {
            this.associate = Associate.LEFT;
            this.arguments = arguments;
            this.priority = Integer.MAX_VALUE;
        }

        public int getPriority() {
            return priority;
        }
        public Associate getAssociate() {
            return associate;
        }
        public int getArguments() {
            return arguments;
        }
    }
}


