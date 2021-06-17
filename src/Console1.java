import java.util.ArrayList;
import java.util.Scanner;

public class Console1 {
    public static void main(String[] args) {
        System.out.printf("> ");
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        ArrayList<String> tokens;
        ArrayList<Variable> vars = new ArrayList<Variable>();
        ArrayList<String> varNames = new ArrayList<String>();
        ArrayList<Expression> values = new ArrayList<Expression>();

        while (!input.equals("exit")) {
            if (!input.equals("")) {
                tokens = Lexer.cleanUp(input);


                //running
                if (tokens.size()== 0) {
                    System.out.print("");
                }
                else if (tokens.contains("run") && tokens.contains("=")){
                    ArrayList<String> computable = new ArrayList<>();
                    for (int i = tokens.indexOf("run") + 1; i < tokens.size(); i++) {
                        computable.add(tokens.get(i));
                    }
                    Expression ex = Parser2.parse(Calculate.substituteVar(computable));
                    ex = Calculate.run(ex);
                    ArrayList<String> addable = Lexer.cleanUp(ex.toString());
                    addable.add(0,tokens.get(0));
                    addable.add(1,tokens.get(1));
                    System.out.println(Calculate.add(addable));
                }
                else if (tokens.contains("=")) {
                    System.out.println(Calculate.add(tokens));
                    }
                else if (tokens.contains("run")) {
                    ArrayList<String> computable = new ArrayList<>();
                    for (int i = tokens.indexOf("run") + 1; i < tokens.size(); i++) {
                        computable.add(tokens.get(i));
                    }
                    Expression ex = Parser2.parse(Calculate.substituteVar(computable));
                    System.out.println(Calculate.run(ex));
                }
                else if (Calculate.varNames.contains(tokens.get(0))) {
                    System.out.println(Calculate.values.get(Calculate.varNames.indexOf(tokens.get(0))));
                }
                else {
                    Expression ex = Parser2.parse(tokens);
                    System.out.println(ex);
                }

            }
            System.out.print("> ");
            input = s.nextLine();
        }
        System.out.printf("Goodbye!");

    }
}
