import java.util.ArrayList;
import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        ArrayList<Variable> vars = new ArrayList<Variable>();
        ArrayList<String> varNames = new ArrayList<String>();
        ArrayList<Expression> values = new ArrayList<Expression>();
        System.out.printf("> ");
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        ArrayList<String> tokens;

        while (!input.equals("exit")) {
            if (!input.equals("")) {
                tokens = Lexer.cleanUp(input);
                Parser1.runAndStore(tokens, vars, varNames, values);
            }
            System.out.print("> ");
            input = s.nextLine();
        }
        System.out.printf("Goodbye!");

    }
}
