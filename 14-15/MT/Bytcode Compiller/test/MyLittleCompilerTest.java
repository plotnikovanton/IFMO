import interfaces.*;
import interfaces.Compiler;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 02.06.15.
 */
public class MyLittleCompilerTest {

    @Test
    public void testCompile() throws Exception {
        Tokenizer lexer = new MyLittleTokenizer(new FileInputStream("test_prog"));
        Parser parser = new MyLittleParser(lexer);
        Compiler cmp = new MyLittleCompiler();

        int cnt = 0;
        for ( String s : cmp.compile(parser.parse())) {
            System.out.println(cnt++ + "\t: " + s);
        }
    }
}