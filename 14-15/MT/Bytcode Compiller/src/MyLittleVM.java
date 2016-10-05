import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bytecode_compiller
 * Created by Plotnikov Anton on 02.06.15.
 */
public class MyLittleVM {
    private final List<Integer> stack = new ArrayList<>();

    private int pop() {
        return stack.remove(stack.size()-1);
    }
    private void put(int i){
        stack.add(i);
    }

    void run(List<String> programm) {
        Map<String, Integer> vars = new HashMap<>();
        int cnt = 0;
        Boolean run = true;

        while (run) {
            String op = programm.get(cnt);
            String arg = null;
            if (cnt < programm.size() - 1) arg = programm.get(cnt+1);
            int b;
            int a;

            switch (op) {
                case "ifetch":
                    put(vars.get(arg));
                    cnt += 2;
                    break;
                case "istore":
                    vars.put(arg, pop());
                    cnt+=2;
                    break;
                case "ipush":
                    put(Integer.parseInt(arg));
                    cnt+=2;
                    break;
                case "ipop":
                    pop();
                    cnt++;
                    break;
                case "iadd":
                    put(pop()+pop());
                    cnt++;
                    break;
                case "isub":
                    b = pop(); a = pop();
                    put(a - b);
                    cnt++;
                    break;
                case "imult":
                    put(pop()*pop());
                    cnt++;
                    break;
                case "idiv":
                    b = pop(); a = pop();
                    put(a/b);
                    cnt++;
                    break;
                case "irem":
                    b = pop(); a = pop();
                    put(a%b);
                    cnt++;
                    break;
                case "ilt":
                    b = pop(); a = pop();
                    put(a<b ? 1 : 0);
                    cnt++;
                    break;
                case "igt":
                    b = pop(); a = pop();
                    put(a>b ? 1 : 0);
                    cnt++;
                    break;
                case "and":
                    b = pop(); a = pop();
                    put(a == 1 && b == 1 ? 1 : 0);
                    cnt++;
                    break;
                case "jz":
                    if (pop() == 0) {
                        cnt = Integer.parseInt(arg);
                    } else {
                        cnt += 2;
                    }
                    break;
                case "jnz":
                    if (pop() != 0) {
                        cnt = Integer.parseInt(arg);
                    } else {
                        cnt += 2;
                    }
                    break;
                case "jmp":
                    cnt = Integer.parseInt(arg);
                    break;
                case "HALT":
                    run = false;
                    break;
            }
        }
        System.out.println(vars);
    }

}
