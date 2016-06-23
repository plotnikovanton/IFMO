using System;

/// <summary>
/// Represents class to store rational numbers
/// </summary>
public struct Rational
{
    private int numenator;
    private int denumenator;

    /// <summary>
    /// Naive constructor of rational number
    /// </summary>
    /// <param name="num"> The numerator of rational number </param>
    /// <param name="denum"> The denumerator of rational number </param>
    public Rational(int num, int denum)
    {
        numenator = num;
        denumenator = denum;
    }

    /// <summary>
    /// Returns string representation of rational number in
    /// `%numerator%/%denumerator%` format
    /// </summary>
    public String Fraction()
    {
        return numenator.ToString() + "/" + denumenator.ToString();
    }

    /// <summary>
    /// Returns string representation of rational number in mixed fraction form
    /// </summary>
    public String MixedFraction()
    {
        if (numenator > denumenator)
        {
            return (numenator / denumenator).ToString() +
                   (numenator % denumenator == 0 ? "" : "+(" +
                   (numenator % denumenator).ToString() + "/" +
                   denumenator.ToString() + ")");
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

    /// <summary>
    /// Returns new normalized version of fraction
    /// </summary>
    /// <param name="a"> Fraction to normalize </param>
    public static Rational Normalize(Rational a)
    {
        int numenator = a.GetNumenator();
        int denumenator = a.GetDenumenator();
        int gcd = GCD(numenator, denumenator);
        return new Rational(numenator / gcd, denumenator / gcd);
    }

    /// <summary>
    /// Calls Rational.Normalize on self
    /// </summary>
    public Rational Normalize()
    {
        return Normalize(this);
    }

    /// <summary> Returns numerator </summary>
    public int GetNumenator() { return numenator; }

    /// <summary> Returns denumerator </summary>
    public int GetDenumenator() { return denumenator; }

    /// <summary>
    /// Returns sum of two fractions as new fraction (not normalized)
    /// </summary>
    /// <param name="a"> First fraction </param>
    /// <param name="b"> Second fraction </param>
    public static Rational operator + (Rational a, Rational b)
    {
        return Normalize(new Rational(a.GetNumenator() * b.GetDenumenator() +
                            b.GetNumenator() * a.GetDenumenator(),
                            b.GetDenumenator() * a.GetDenumenator()));
    }

    /// <summary>
    /// Returns subtraction of two fractions as new fraction (not normalized)
    /// </summary>
    /// <param name="a"> First fraction </param>
    /// <param name="b"> Second fraction </param>
    public static Rational operator - (Rational a, Rational b)
    {
        return Normalize(new Rational(a.GetNumenator() * b.GetDenumenator() -
                            b.GetNumenator() * a.GetDenumenator(),
                            b.GetDenumenator() * a.GetDenumenator()));
    }


    /// <summary>
    /// Returns product of two fractions as new fraction (not normalized)
    /// </summary>
    /// <param name="a"> First fraction </param>
    /// <param name="b"> Second fraction </param>
    public static Rational operator * (Rational a, Rational b)
    {
        return Normalize(new Rational(a.GetNumenator() * b.GetNumenator(),
                            b.GetDenumenator() * a.GetDenumenator()));
    }

    /// <summary>
    /// Returns division of two fractions as new fraction (not normalized)
    /// </summary>
    /// <param name="a"> First fraction </param>
    /// <param name="b"> Second fraction </param>
    public static Rational operator / (Rational a, Rational b)
    {
        return Normalize(new Rational(a.GetNumenator() * b.GetDenumenator(),
                            b.GetNumenator() * a.GetDenumenator()));
    }

    /// <summary>
    /// Converts a given rational number to decimal
    /// </summary>
    /// <param name="a"> A rational number </param>
    public static explicit operator decimal(Rational a)
    {
        return (decimal) a.GetNumenator() / (decimal) a.GetDenumenator();
    }

    /// <summary>
    /// Converts a given rational number to double
    /// </summary>
    /// <param name="a"> A rational number </param>
    public static explicit operator double(Rational a)
    {
        return (double) a.GetNumenator() / (double) a.GetDenumenator();
    }

    /// <summary>
    /// Override of equals method
    /// </summary>
    public override bool Equals(Object obj) {
        if (obj == null || GetType() != obj.GetType())
            return false;

        Rational norm_a = this.Normalize();
        Rational norm_b = ((Rational)obj).Normalize();

        return norm_a.GetNumenator() == norm_b.GetNumenator() &&
               norm_a.GetDenumenator() == norm_b.GetDenumenator();
    }

    /// <summary>
    /// Override of hashcode
    /// </summary>
    public override int GetHashCode() {
        return ((double) this).GetHashCode();
    }

    /// <summary>
    /// Override of equals operator
    /// </summary>
    /// <param name="a"> First fraction </param>
    /// <param name="b"> Second fraction </param>
    public static bool operator ==(Rational a, Rational b) {
        return a.Equals(b);
    }

    /// <summary>
    /// Override of not equals operator
    /// </summary>
    /// <param name="a"> First fraction </param>
    /// <param name="b"> Second fraction </param>
    public static bool operator !=(Rational a, Rational b) {
        return !(a == b);
    }

    /// <summary>
    /// Override of to string
    /// </summary>
    public override string ToString() {
        return this.MixedFraction();
    }

    /// Entry point to test some implemented functions
    public static void Main(string[] args) {
        Rational a = new Rational(10, 21);
        Rational b = new Rational(90, 18);
        // Normalization
        Console.WriteLine(a.ToString());
        Console.WriteLine(b.Normalize().Fraction());
        // Equals
        Console.WriteLine(b.Normalize() == b);
        Console.WriteLine(a.Normalize() == b);
        // Sum
        Console.WriteLine(a + b);
        // Product
        Console.WriteLine(a * b);
    }
}
