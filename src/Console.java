import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        System.out.printf("> ");
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        ArrayList<String> tokens;
        while (!input.equals("exit")) {
            tokens = Lexer.cleanUp(input);
         /*   for (String x : tokens) {
                System.out.printf(x);
            }
            System.out.println();

          */

            if (!input.equals("")) {
                System.out.println(parse(tokens));
            }

            System.out.print("> ");
            input = s.nextLine();
        }
        System.out.printf("Goodbye!");
        System.out.println ("HI");
    }
    public static String parse(ArrayList<String> tokens) {
        if (tokens.size() == 1) {
            Variable v = new Variable(tokens.get(0));
            return v.toString();
        }
        else {
            if (tokens.size() >= 2) {
                if (tokens.get(0) == "\\\\") {
                    //yah functions: will work on it
                    return null;
                }
                else {
                    ArrayList<String> left = new ArrayList<String>();
                    ArrayList<String> right = new ArrayList<String>();
                    if (tokens.get(0).equals("(")) {
                        for (int i = 1; i < tokens.indexOf(")"); i++) {
                            left.add(tokens.get(i));
                        }
                        if (tokens.lastIndexOf("(") != 0) {
                            for (int j = tokens.lastIndexOf("(") -1; j < tokens.size() -1 ; j++) {
                                right.add(tokens.get(j));
                            }
                        }
                        else {
                            for (int j = tokens.indexOf(")") + 1; j < tokens.size(); j++) {
                                right.add(tokens.get(j));
                            }
                        }
                    }
                    else if (tokens.get(tokens.size()-1).equals(")")) {
                        for (int i = tokens.lastIndexOf("(") + 1; i < tokens.size() -1; i++) {
                            right.add(tokens.get(i));
                        }
                        for (int j = 0; j < tokens.lastIndexOf("("); j++) {
                            left.add(tokens.get(j));
                        }
                    }
                    else {
                        for (int i = 0; i < tokens.size() - 1; i++) {
                            left.add(tokens.get(i));
                        }
                        right.add(tokens.get(tokens.size()-1));
                    }

                    Application ret;
                    if (left.size() >= 2) {
                        ret = new Application(parse(left), parse(right));
                    } else {
                        ret = new Application(tokens.get(0), tokens.get(1));
                    }
                    return ret.toString();
                }
            }

        }

     return  null;

    }
}

