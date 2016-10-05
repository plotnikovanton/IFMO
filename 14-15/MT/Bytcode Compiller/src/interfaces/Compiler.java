package interfaces;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 25.05.15.
 */
public interface Compiler {
    List<String> compile(Node root);
}
