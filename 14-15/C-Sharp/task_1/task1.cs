using System;

public struct Rational
{
    private int numenator;
    private int denumenator;

    public Rational(int num, int denum) 
    {
        numenator = num;
        denumenator = denum;
    }

    public String Fraction()
    {
        return numenator.ToString() + "/" + denumenator.ToString();
    }

    public String MixedFraction()
    {
        if (numenator > denumenator)
        {
            return (numenator / denumenator).ToString() + "+(" + (numenator % denumenator).ToString() + "/" + denumenator.ToString() + ")";
        } else {
            return Fraction();
        }
    }

    private static int GCD(int a ,int b)
    {
        while (a != 0 && b != 0)
        {
            if (a > b)
            {
                a = a % b;
            } else {
                b = b % a;
            }
        }

        return a + b;
    }


    public static Rational Normalize(Rational a)
    {
        int numenator = a.GetNumenator();
        int denumenator = a.GetDenumenator();
        int gcd = GCD(numenator, denumenator);             
        return new Rational(numenator / gcd, denumenator / gcd);
    }

    public Rational Normalize()
    {
        return Normalize(this);
    }

    public int GetNumenator() { return numenator; }
    public int GetDenumenator() { return denumenator; }

    public static Rational operator + (Rational a, Rational b)
    {
        return Normalize(new Rational(a.GetNumenator() * b.GetDenumenator() + b.GetNumenator() * a.GetDenumenator(), b.GetDenumenator() * a.GetDenumenator()));   
    }

    public static Rational operator - (Rational a, Rational b) 
    {
        return Normalize(new Rational(a.GetNumenator() * b.GetDenumenator() - b.GetNumenator() * a.GetDenumenator(), b.GetDenumenator() * a.GetDenumenator()));   
    }

    
    public static Rational operator * (Rational a, Rational b) 
    {
        return Normalize(new Rational(a.GetNumenator() * b.GetNumenator(), b.GetDenumenator() * a.GetDenumenator()));   
    }
    
    public static Rational operator / (Rational a, Rational b) 
    {
        return Normalize(new Rational(a.GetNumenator() * b.GetDenumenator(), b.GetNumenator() * a.GetDenumenator()));   
    }

    public static explicit operator decimal(Rational a)
    {
        return (decimal) a.GetNumenator() / (decimal) a.GetDenumenator();
    }

    
    public static explicit operator double(Rational a)
    {
        return (double) a.GetNumenator() / (double) a.GetDenumenator();
    }
}
