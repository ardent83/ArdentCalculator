package model;

import java.util.ArrayList;

public class Calculator {
    private static int pre(char operator){
        return switch (operator) {
            case '-', '+' -> 0;
            case '*', '/' -> 1;
            case '%' -> 2;
            case '^', '√' -> 3;
            case '!' -> 4;
            case '_' -> 5;
            case 's', 'c', 't', 'k' -> 6;
            default -> -1;
        };
    }
    private static MyStack<String> infixToPostfix(String exp){
        MyStack<String> result = new MyStack<>();
        MyStack<Character> operator = new MyStack<>();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (!(Character.isDigit(c) || c == '.'))
                if (number.length() > 0){
                    result.push(number.toString());
                    number = new StringBuilder();
                }
            if (Character.isDigit(c) || c == '.') {
                if (c == '.' && number.length() == 0)
                    throw new RuntimeException("error : .5");
                number.append(c);
                if (i == exp.length()-1){
                    result.push(number.toString());
                    number = new StringBuilder();
                }
            } else if (c == 'e' || c == 'π') {
                if (i > 0 && Character.isDigit(exp.charAt(i-1))){
                    throw new RuntimeException("error : 5π, 5e");
                } else if (i < exp.length()-1 && Character.isDigit(exp.charAt(i+1))){
                    throw new RuntimeException("error : π5, e5");
                } else {
                    if (c == 'e') {
                        result.push(String.valueOf(Math.E));
                    } else {
                        result.push(String.valueOf(Math.PI));
                    }
                }
            } else if (c == 's') {
                if (i > 0 && Character.isDigit(exp.charAt(i - 1))) {
                    throw new RuntimeException("error : 5sin()");
                } else if (i < exp.length() - 1 && exp.charAt(i + 1) != '(') {
                    throw new RuntimeException("error : sin5");
                } else {
                    operator.push('s');
                }
            } else if (c == 'c') {
                if (i > 0 && Character.isDigit(exp.charAt(i - 1))) {
                    throw new RuntimeException("error : 5cos()");
                } else if (i < exp.length() - 1 && exp.charAt(i + 1) != '(') {
                    throw new RuntimeException("error : cos5");
                } else {
                    operator.push('c');
                }
            } else if (c == 't') {
                if (i > 0 && Character.isDigit(exp.charAt(i - 1))) {
                    throw new RuntimeException("error : 5tan(a)");
                } else if (i < exp.length() - 1 && exp.charAt(i + 1) != '(') {
                    throw new RuntimeException("error : tan5");
                } else {
                    operator.push('t');
                }
            } else if (c == 'k') {
                if (i > 0 && Character.isDigit(exp.charAt(i - 1))) {
                    throw new RuntimeException("error : 5cot(a)");
                } else if (i < exp.length() - 1 && exp.charAt(i + 1) != '(') {
                    throw new RuntimeException("error : cot5");
                } else {
                    operator.push('k');
                }
            } else if (c == '(') {
                operator.push('(');
            } else if (c == ')') {
                if (i != 0 && !(Character.isDigit(exp.charAt(i-1)) || exp.charAt(i-1) == '!' || exp.charAt(i-1) == ')' || exp.charAt(i-1) == 'e' || exp.charAt(i-1) == 'π')) {
                    throw new RuntimeException("error : +), -), ... ");
                }
                while (!operator.isEmpty() && operator.peek() != '(') {
                    result.push(operator.peek().toString());
                    operator.pop();
                }
                if (operator.isEmpty())
                    throw new RuntimeException("error : ())");// ())
                else
                    operator.pop();
            } else {
                while (!operator.isEmpty() && pre(c) <= pre(operator.peek())){
                    result.push(operator.pop().toString());
                }
                if (c == '-' && (i == 0 || exp.charAt(i-1) == '(')){
                    operator.push('_');
                } else {
                    final boolean b = (i == exp.length() - 1 || exp.charAt(i + 1) == ')');
                    if (c == '!' && b){
                        operator.push(c);
                    } else if (b){
                        throw new RuntimeException("error : +, +), ^), ...");
                    } else {
                        operator.push(c);
                    }
                }
            }
        }
        while (!operator.isEmpty()) {
            if (operator.peek() == '(')
                throw new RuntimeException("error : (()"); // (()
            result.push(operator.pop().toString());
        }
        return result;
    }
    public static double computing(String exp){
        MyStack<String> postfix = infixToPostfix(deleteSpace(exp)), tempStack = new MyStack<>();
        ArrayList<String> stepByStep = new ArrayList<>();
        tempStack.push(postfix.pop());
        while (!postfix.isEmpty()){
            String top = postfix.pop();
            final boolean isDigit = Character.isDigit(top.charAt(top.length() - 1));
            if (tempStack.peek().charAt(0) == '_' && isDigit){
                stepByStep.add("-" + top);
                tempStack.pop();
                if (!tempStack.isEmpty())
                    postfix.push(String.valueOf(neg(Double.parseDouble(top))));
                else
                    tempStack.push(String.valueOf(neg(Double.parseDouble(top))));
            } else if (tempStack.peek().charAt(0) == '√' && isDigit){
                stepByStep.add("√" + top);
                tempStack.pop();
                if (!tempStack.isEmpty())
                    postfix.push(String.valueOf(sqrt(Double.parseDouble(top))));
                else
                    tempStack.push(String.valueOf(sqrt(Double.parseDouble(top))));
            } else if (tempStack.peek().charAt(0) == 's' && isDigit){
                stepByStep.add("sin("+top+")");
                tempStack.pop();
                final String value = String.valueOf(Math.sin(Double.parseDouble(top)));
                if (!tempStack.isEmpty())
                    postfix.push(value);
                else
                    tempStack.push(value);
            } else if (tempStack.peek().charAt(0) == 'c' && isDigit){
                stepByStep.add("cos("+top+")");
                tempStack.pop();
                final String value = String.valueOf(Math.cos(Double.parseDouble(top)));
                if (!tempStack.isEmpty())
                    postfix.push(value);
                else
                    tempStack.push(value);
            } else if (tempStack.peek().charAt(0) == 't' && isDigit){
                stepByStep.add("tan("+top+")");
                tempStack.pop();
                final String value = String.valueOf(Math.tan(Double.parseDouble(top)));
                if (!tempStack.isEmpty())
                    postfix.push(value);
                else
                    tempStack.push(value);
            } else if (tempStack.peek().charAt(0) == 'k' && isDigit){
                stepByStep.add("cot("+top+")");
                tempStack.pop();
                final String value = String.valueOf(1/Math.tan(Double.parseDouble(top)));
                if (!tempStack.isEmpty())
                    postfix.push(value);
                else
                    tempStack.push(value);
            } else if (tempStack.peek().charAt(0) == '!' && isDigit){
                tempStack.pop();
                int n = (int) Double.parseDouble(top);
                if (n != Double.parseDouble(top))
                    throw new RuntimeException("error : not integer!");
                stepByStep.add(top+"!");
                tempStack.push(String.valueOf(fact(n)));
            } else if (Character.isDigit(tempStack.peek().charAt(tempStack.peek().length()-1)) && isDigit){
                double a = Double.parseDouble(top), b = Double.parseDouble(tempStack.pop());
                stepByStep.add(a + tempStack.peek() + b);
                if (!postfix.isEmpty() && !tempStack.isEmpty()){
                    postfix.push(String.valueOf(comp(tempStack.pop().charAt(0), a, b)));
                } else {
                    tempStack.push(String.valueOf(comp(tempStack.pop().charAt(0), a, b)));
                }
            } else {
                tempStack.push(top);
            }
        }
        while (tempStack.size() > 1){
            String top = tempStack.pop();
            switch (tempStack.peek().charAt(0)) {
                case '_' -> {
                    stepByStep.add("-" + top);
                    tempStack.pop();
                    tempStack.push(String.valueOf(neg(Double.parseDouble(top))));
                }
                case '√' -> {
                    stepByStep.add("√" + top);
                    tempStack.pop();
                    tempStack.push(String.valueOf(sqrt(Double.parseDouble(top))));
                }
                case 's' -> {
                    stepByStep.add("sin("+top+")");
                    tempStack.pop();
                    tempStack.push(String.valueOf(Math.sin(Double.parseDouble(top))));
                }
                case 'c' -> {
                    stepByStep.add("cos("+top+")");
                    tempStack.pop();
                    tempStack.push(String.valueOf(Math.cos(Double.parseDouble(top))));
                }
                case 't' -> {
                    stepByStep.add("tan("+top+")");
                    tempStack.pop();
                    tempStack.push(String.valueOf(Math.tan(Double.parseDouble(top))));
                }
                case 'k' -> {
                    stepByStep.add("cot("+top+")");
                    tempStack.pop();
                    tempStack.push(String.valueOf(1/Math.tan(Double.parseDouble(top))));
                }
                case '!' -> {
                    tempStack.pop();
                    int n = (int) Double.parseDouble(top);
                    if (n != Double.parseDouble(top))
                        throw new RuntimeException("error : not integer!");
                    stepByStep.add(top+"!");
                    tempStack.push(String.valueOf(fact(n)));
                }
                default -> {
                    double a = Double.parseDouble(top), b = Double.parseDouble(tempStack.pop());
                    stepByStep.add(a + tempStack.peek() + b);
                    tempStack.push(String.valueOf(comp(tempStack.pop().charAt(0), a, b)));
                }
            }
        }
        for (int i = 0; i < stepByStep.size(); i++) {
            System.out.println("step " + i + " : " + stepByStep.get(i));
        }
        return Double.parseDouble(tempStack.pop());
    }
    private static double comp(char operator, double a, double b){
        return switch (operator) {
            case '-' -> sub(a, b);
            case '+' -> add(a, b);
            case '*' -> mult(a, b);
            case '/' -> div(a, b);
            case '^' -> pow(a, b);
            case '%' -> mod(a, b);
            default -> -1;
        };
    }
    private static double neg(double a){
        return -a;
    }
    private static double add(double a, double b){
        return a+b;
    }
    private static double sub(double a, double b){
        return a-b;
    }
    private static double mult(double a, double b){
        return a*b;
    }
    private static double div(double a, double b){
        return a/b;
    }
    private static double pow(double a, double b){
        return Math.pow(a,b);
    }
    public static double mod(double a, double b){
        return a%b;
    }
    private static double sqrt(double a){
        return Math.sqrt(a);
    }
    private static int fact(int n){
        if (n == 1)
            return 1;
        return n * fact(n-1);
    }
    private static String deleteSpace(String exp){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < exp.length(); i++) {
            if (exp.charAt(i) != ' '){
                str.append(exp.charAt(i));
            }
        }
        return str.toString();
    }
}
