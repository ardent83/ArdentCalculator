package controller;

public class Controller {
    static public String replaceX(String exp, double d){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < exp.length(); i++) {
            if (exp.charAt(i) != 'x')
                builder.append(exp.charAt(i));
            else
                builder.append('(').append(d).append(')');
        }
        return builder.toString();
    }
}
