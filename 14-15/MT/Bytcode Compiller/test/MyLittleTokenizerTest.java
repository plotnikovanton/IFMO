import interfaces.Tokenizer;
import interfaces.Token;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 26.05.15.
 */
public class MyLittleTokenizerTest {
    @Test
    public void testLexer() throws Exception{
        Tokenizer lexer = new MyLittleTokenizer(new FileInputStream("test_prog"));
        Token token;
        List<Token> result = new ArrayList<>();
        while((token = lexer.nextTok()).getType() != Tokenizer.Types.EOF) {
            result.add(token);
        }
        System.out.println(result);
    }
}