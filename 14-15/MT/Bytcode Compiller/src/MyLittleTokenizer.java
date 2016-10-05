import interfaces.Tokenizer;
import interfaces.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 26.05.15.
 */
public class MyLittleTokenizer implements Tokenizer {
    private final BufferedReader input;
    private final List<Token> tokens;
    private int iter = 0;
    private final String regex = "while|if|else|\\|\\||&&|\\d+|/|\\+|\\-|\\{|\\}|\\(|\\)|%|\\*|;|==|=|<|>|\\w+";
    private final Pattern p = Pattern.compile("(" + regex + ")");
    private final Pattern correct_regex = Pattern.compile("(" + regex + "|\\s+|)+");
    private final Pattern int_regex = Pattern.compile("\\d+");
    private final Pattern char_regex = Pattern.compile("\\w");

    private static final Map<String, Types> map;

    static {
        map = new HashMap<>();
        map.put("/", Types.DIV);
        map.put("+", Types.PLUS);
        map.put("-", Types.MINUS);
        map.put("(", Types.LBRAC);
        map.put(")", Types.RBRAC);
        map.put("{", Types.LFIG);
        map.put("}", Types.RFIG);
        map.put("while", Types.WHILE);
        map.put("if", Types.IF);
        map.put("else", Types.ELSE);
        //map.put("int", Types.INT);
        map.put(";", Types.SEMICOLON);
        map.put("%", Types.MOD);
        map.put("*", Types.MULT);
        map.put("=", Types.EQUAL);
        map.put("<", Types.LESS);
        map.put(">", Types.GREATER);
        map.put("&&", Types.AND);
    }

    public MyLittleTokenizer(InputStream input) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(input));
        tokens = collectTokens();
    }

    private List<Token> collectTokens() throws IOException {
        List<Token> tokens = new ArrayList<>();
        String line;
        int cnt = 1;
        while ((line = input.readLine()) != null) {
            Matcher m = p.matcher(line);
            if (!correct_regex.matcher(line).matches()) {
                throw new IllegalArgumentException("Wrong syntax in line: " + cnt + "\n" + line);
            }
            while (m.find()) {
                String token_str = m.group();
                if (int_regex.matcher(token_str).matches()) {
                    tokens.add(new MyLittleToken(Types.INT, token_str));
                } else if (char_regex.matcher(token_str).matches()) {
                    tokens.add(new MyLittleToken(Types.ID, token_str));
                } else {
                    if(map.containsKey(token_str))
                        tokens.add(new MyLittleToken(map.get(token_str), token_str));
                }
            }
            cnt++;
        }
        tokens.add(new MyLittleToken(Types.EOF, null));
        return tokens;
    }

    @Override
    public Token nextTok() {
        if (tokens.size() == iter) {
            return null;
        } else {
            return tokens.get(iter++);
        }
    }
}
