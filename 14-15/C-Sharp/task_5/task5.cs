using System;
using System.Reflection.Emit;

class EmitCalculator
{
    private string[] tokens;
    private DynamicMethod dm;
    private ILGenerator ilGen;
    private delegate int Evaluate(int a, int b, int c, int d, int ret);
    
    public EmitCalculator(string term, int a, int b, int c, int d) 
    {
        this.dm = new DynamicMethod( "Evaluate" 
                                   , typeof(int) 
                                   , new Type[] {typeof(int),typeof(int),typeof(int),typeof(int),typeof(int)} 
                                   , typeof(int)
                                   );
        this.tokens = term.Split(new Char[] {' '});
        this.ilGen = dm.GetILGenerator();
        this.GenCode();
        Evaluate eval = (Evaluate) dm.CreateDelegate(typeof(Evaluate));
        Console.WriteLine(eval(a, b, c, d, 0));
    }

    private void GenCode()
    {
        foreach (string token in this.tokens)
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

class Tester
{
    static void Main(string[] args)
    {
        new EmitCalculator( args[0]
                          , Convert.ToInt32(args[1])
                          , Convert.ToInt32(args[2])
                          , Convert.ToInt32(args[3])
                          , Convert.ToInt32(args[4])
                          );
    }
}
