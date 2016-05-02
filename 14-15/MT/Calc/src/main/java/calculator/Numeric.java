package calculator;

import java.util.List;

/**
 * Created by anton on 14.04.15.
 */
public class Numeric implements Node {
    private final Double value;

    public Numeric(String value) {
        this.value = Double.parseDouble(value);
    }

    @Override
    public List<Node> childs() {
        return null;
    }

    @Override
    public Double calculate() {
        return value;
    }
}
