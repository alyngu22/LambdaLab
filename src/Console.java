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
    }

    public static String parse(ArrayList<String> tokens) {
        if (tokens.size() == 1) {
            Variable v = new Variable(tokens.get(0));
            System.out.print(v);
        }
        else {
            if (tokens.size() >= 2) {
                if (tokens.get(0) == "\\\\") {
                    //yah functions: will work on it
                    return null;
                }
                else {
                    ArrayList<String> left = new ArrayList<String>();
                    for (int i = 0; i < tokens.size() - 1; i++) {
                        left.add(tokens.get(i));
                    }
                    Application ret;
                    if (left.size() >= 2) {
                        ret = new Application(parse(left), tokens.get(tokens.size() - 1));
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

