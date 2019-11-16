package il.co.ilrd.ComplexNumber;
import java.util.regex.*;

public class ComplexNumber implements Comparable<ComplexNumber>{
    private double real;
    private double imaginary;
    private static final int BIGGER = 1;
    private static final int SMALLER = -1;
    private static final int EQUAL = 0;

    public ComplexNumber(double real, double imaginary)
    {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal()
    {
        return real;
    }

    public double getImaginary()
    {
        return imaginary;
    }

    public ComplexNumber add(ComplexNumber number)
    {
        return new ComplexNumber(real + number.real, imaginary + number.imaginary);
    }

    public ComplexNumber subtract(ComplexNumber number)
    {
        return new ComplexNumber(real - number.real, imaginary - number.imaginary);
    }

    public void setReal(double realNumber)
    {
        real = realNumber;
    }

    public void setImaginary(double imaginaryNumber)
    {
        imaginary = imaginaryNumber;
    }

    public boolean isReal() {
        return (0 == imaginary);
    }

    public boolean isImaginary() {
        return (0 == real);
    }

    public static ComplexNumber parseComplex(String s)
    {
        String numberNoWhiteSpace = s.replaceAll("\\s","");
        Pattern patternA = Pattern.compile("([-]?[0-9]+\\.?[0-9]+?)([-|+]+[0-9]+\\.?[0-9]+?)[i$]+");
        Matcher matcherA = patternA.matcher(numberNoWhiteSpace);
        
        if (matcherA.find()) {
            ComplexNumber newNum = new ComplexNumber(0,0);
            newNum.setReal(Double.parseDouble(matcherA.group(1)));
            newNum.setImaginary(Double.parseDouble(matcherA.group(2)));

            return newNum;
        }

        System.err.println("Enter by format -N.N+/-N.N");

        return null;
    }

    @Override
    public String toString()
    {
        String realString = real + "";
        String imaginaryString = "";

        if (imaginary < 0)
        {
            imaginaryString = imaginary + "i";
        }
        else
        {
            imaginaryString = "+" + imaginary + "i";
        }

        return realString + imaginaryString;
    }

    @Override
    public boolean equals(Object cn)
    {
        if (!(cn instanceof ComplexNumber))
        {
            return false;
        }

        ComplexNumber newCn = (ComplexNumber) cn;

        return ((real == newCn.real) && (imaginary == newCn.imaginary));
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(real);

        result = 31 * result + Double.hashCode(imaginary);

        return result;
    }

    @Override
    public int compareTo(ComplexNumber o) {
        if (real == o.real) {
            return (imaginary - o.imaginary > 0) ? BIGGER : (imaginary - o.imaginary < 0) ? SMALLER : EQUAL;
        }

        return (real - o.real > 0) ? BIGGER : (imaginary - o.imaginary < 0) ? SMALLER : EQUAL;
    }
}