package calculator;

import java.util.List;

/**
 * Calc
 *
 * Created by Plotnikov Anton on 14.04.15.
 */
public interface Node {
    List<Node> childs();
    Double calculate();
}
