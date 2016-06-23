using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection.Emit;

public class EmitCalculator<T>
{
    private string[] tokens;
    private DynamicMethod dm;
    private ILGenerator ilGen;
    private delegate T Evaluate(T a, T b, T c, T d, T ret);
    private Evaluate eval;

    /// <summary>
    /// Basic constructor
    /// </summary>
    /// <param name="term"> expression to evaluate </param>
    public EmitCalculator(string term)
    {
        this.dm = new DynamicMethod( "Evaluate"
                                   , typeof(T)
                                   , new Type[] { typeof(T), typeof(T),
                                                  typeof(T), typeof(T),
                                                  typeof(T) }
                                   , typeof(T)
                                   );
        this.tokens = term.Split(new Char[] {' '});
        this.ilGen = dm.GetILGenerator();
        this.GenCode();
        eval = (Evaluate) dm.CreateDelegate(typeof(Evaluate));
    }

    /// <summary>
    /// just evaluates constructed function
    /// </summary>
    public T Eval(T a, T b, T c, T d, T init) 
    {
        return eval(a, b, c, d, init);
    }

    private void GenCode()
    {
        // Lets build revers polish notation from infix notation
        var output = new List<string>();
        var stack = new Stack<string>();

        foreach (string token in this.tokens)
        {
            if (token.Equals("a") || token.Equals("b") ||
                    token.Equals("c") || token.Equals("d") ) {
                output.Add(token);
            } else {
                while (stack.Count != 0 && (token.Equals("+") ||
                            stack.Peek().Equals("*"))) {
                    output.Add(stack.Pop());
                }
                stack.Push(token);
            }
        }
        while (stack.Count != 0) output.Add(stack.Pop());


        // Build simple evaluation dynamic function for it
        foreach (string token in output)
        {
            if (token.Equals("+")) ilGen.Emit(OpCodes.Add);
            else if (token.Equals("*")) ilGen.Emit(OpCodes.Mul);
            else if (token.Equals("a")) ilGen.Emit(OpCodes.Ldarg_0);
            else if (token.Equals("b")) ilGen.Emit(OpCodes.Ldarg_1);
            else if (token.Equals("c")) ilGen.Emit(OpCodes.Ldarg_2);
            else if (token.Equals("d")) ilGen.Emit(OpCodes.Ldarg_3);
        }
        ilGen.Emit(OpCodes.Ret);
    }
}

/// <summary>
/// You can run this console program by passing an expression in infix notation
/// as first argument and values of `a`, `b`, `c` and `d` variables as 2nd-4th
///
/// For example `./task5.exe "a + b * b * c + d + d" 1 2 3 4`
/// output should be 21
/// </summary>
class Tester
{
    static void Main(string[] args)
    {
        var calc = new EmitCalculator<double>(args[0]);
        Console.WriteLine(calc.Eval( Convert.ToDouble(args[1])
                              , Convert.ToDouble(args[2])
                              , Convert.ToDouble(args[3])
                              , Convert.ToDouble(args[4])
                              , 0
                              ));
    }
}
