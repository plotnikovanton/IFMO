package calculator;

import java.util.Arrays;

/**
 * Calc
 *
 * Created by Plotnikov Anton on 12.04.15.
 */
public class Main {
    public static final boolean DEBUG = false;

    public static void main(String[] args) {
        Node root = ShuntingYard.convert(
                args.length > 0 ?
                        Arrays.asList(args).stream().reduce((x, xs) -> x + xs).get() :
                        args[0].replaceAll("\\s", "") );
        System.out.println(root.calculate());
    }
}
