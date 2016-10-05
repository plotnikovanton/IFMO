import interfaces.*;
import interfaces.Compiler;
import org.junit.Test;

import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 02.06.15.
 */
public class MyLittleVMTest {

    @Test
    public void testRun() throws Exception {
        Tokenizer lexer = new MyLittleTokenizer(new FileInputStream("test_prog"));
        Parser parser = new MyLittleParser(lexer);
        Compiler cmp = new MyLittleCompiler();
        MyLittleVM vm = new MyLittleVM();

        vm.run(cmp.compile(parser.parse()));
    }
}