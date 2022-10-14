package ru.nvy.tayk;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            Scanner in = new Scanner(System.in);
            System.out.print("Input expression: ");
            String str = in.nextLine();
            System.out.println(str);
            System.out.println(Calculator.solve(str));
        }
    }
}