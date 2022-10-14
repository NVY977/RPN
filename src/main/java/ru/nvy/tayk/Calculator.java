package ru.nvy.tayk;

import java.util.Stack;
public class Calculator {
    private static int getPriority(String symbol) {
        switch (symbol) {
            case ("p") -> {
                return 5;
            }
            case ("^") -> {
                return 4;
            }
            case ("*"), ("/") -> {
                return 3;
            }
            case ("+"), ("-") -> {
                return 2;
            }
            case ("(") -> {
                return 1;
            }
            case (")") -> {
                return -1;
            }
            default -> {
                return 0;
            }
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private static void validate(String input) {

        if (input.contains("(") || input.contains(")")) {
            throw new IllegalArgumentException("Incorrect number of brackets");
        }
        input = input.replace(" ", "");
        for (int i = 0; i < input.length(); i++) {
            String curElement = String.valueOf(input.charAt(i));
            if (curElement.length() > 1) {
                if (isNumeric(curElement)) {
                    throw new IllegalArgumentException("It's not number");
                }
            } else {
                if (getPriority(curElement) == 0) {
                    if (isNumeric(curElement)) {
                        throw new NumberFormatException("Number Format Exception");
                    }
                }
            }
        }
    }

    public static String solve(String input) {
        String rpnStr;
        try {
            rpnStr = Calculator.ToRPN(input);
            System.out.println(rpnStr);
            validate(rpnStr);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
        return Calculator.ToAnswer(rpnStr);
    }

    private static String checkFunc(String input) {
        Stack<Character> bracketStack = new Stack<>();
        int count = 0;
        while (input.contains("pow")) {
            count++;
            int indexOfPow = input.indexOf("pow");
            if (indexOfPow == -1) {
                return input;
            }
            input = input.replaceFirst("pow", "");
            do {
                if (input.charAt(indexOfPow) == '(') {
                    bracketStack.push(input.charAt(indexOfPow));
                } else if (input.charAt(indexOfPow) == ',') {
                    input = input.replaceFirst(",", "p");
                    count--;
                } else if (input.charAt(indexOfPow) == ')') {
                    bracketStack.pop();
                }
                indexOfPow++;
            } while (bracketStack.size() != 0);
        }
        if (count != 0) {
            throw new IllegalArgumentException("Incorrect Function Argument Number");
        }
        return input;
    }

    private static String ToRPN(String expression) {
        StringBuilder currentString = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        expression = expression.replace("--", "");
        if (expression.charAt(0) == '-') {
            expression = "0" + expression;
        }
        expression = checkFunc(expression);

        int priorityCurrentElement;

        boolean lastElementIsOperation = true;

        for (int i = 0; i < expression.length(); i++) {
            priorityCurrentElement = getPriority(String.valueOf(expression.charAt(i)));
            switch (priorityCurrentElement) {
                case 0 -> {
                    lastElementIsOperation = false;
                    currentString.append(expression.charAt(i)); // кидаем цифры в строку
                }
                case 1 -> {
                    if (expression.charAt(i + 1) == '-') {
                        currentString.append(" 0 ");
                        stack.push(expression.charAt(i));
                        stack.push(expression.charAt(i + 1));
                        expression = expression.replaceFirst("[(]-", "");
                    }
                    currentString.append(" ");
                    if (!lastElementIsOperation) {
                        stack.push('*');
                    }
                    stack.push(expression.charAt(i)); // засунули в стек скобку
                }
                case 2, 3, 4, 5 -> {
                    if (expression.charAt(i + 1) == '-') {
                        currentString.append(" 0 ");
                        stack.push(expression.charAt(i));
                        stack.push(expression.charAt(i + 1));
                        expression = expression.replaceFirst("[*/+]-", "~");
                        break;
                    }
                    currentString.append(" ");
                    while (!stack.empty()) {
                        if (getPriority(String.valueOf(stack.peek())) >= priorityCurrentElement) {
                            currentString.append(stack.pop());
                        } else break;
                    }
                    lastElementIsOperation = true;
                    stack.push(expression.charAt(i));
                }
                case -1 -> {
                    currentString.append(" ");
                    while (getPriority(String.valueOf(stack.peek())) != 1) {
                        currentString.append(stack.pop());
                        if (stack.isEmpty()) {
                            throw new IllegalArgumentException("ERROR ()\n");
                        }
                    }
                    stack.pop();
                }
            }

        }
        while (!stack.empty()) {
            currentString.append(stack.pop());
        }
        return currentString.toString();
    }


    private static String ToAnswer(String rpn) {
        if (rpn.length() == 1) {
            return rpn;
        }
        if (rpn.length() == 2) {
            return rpn;
        }
        StringBuilder operand = new StringBuilder();
        Stack<Double> stack = new Stack<>();
        Calc calc = new Calc();
        System.out.println(rpn);


        for (int i = 0; i < rpn.length(); i++) {
            if (rpn.charAt(i) == ' ') continue;

            if (getPriority(String.valueOf(rpn.charAt(i))) > 1) {
                double a = stack.pop(), b = stack.pop();
                switch (rpn.charAt(i)) {
                    case 'p' -> stack.push(calc.pow(b, a));
                    case '+' -> stack.push(calc.sum(b, a));
                    case '-' -> stack.push(calc.subtract(b, a));
                    case '*' -> stack.push(calc.multiply(a, b));
                    case '/' -> stack.push(calc.divide(b, a));
                }
            }

            if (getPriority(String.valueOf(rpn.charAt(i))) == 0) {
                while (rpn.charAt(i) != ' ' && getPriority(String.valueOf(rpn.charAt(i))) == 0) {
                    operand.append(rpn.charAt(i++));
                }
                i--;
                stack.push(Double.parseDouble(operand.toString()));
                operand = new StringBuilder();
            }
        }
        return String.valueOf(stack.pop());
    }

}

