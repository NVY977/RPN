package ru.nvy.tayk;

public class Calc {
    public double sum(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }


    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0.0) {
            throw new ArithmeticException();
        }
        return a / b;
    }

    public double pow(double a , double b) {
        return Math.pow(a, b);
    }
}
