import java.util.ArrayList;
import java.util.Scanner;

public class Console {
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


                Expression ex = Parser2.parse(tokens);
                if (tokens.contains("run") && tokens.contains("=")){
                    System.out.println();
                }

                else if (tokens.contains("=")) {
                    if (varNames.contains(tokens.get(0))) {
                        System.out.println(tokens.get(0) + " is already defined.");
                    }
                    else {
                        ArrayList<String> equalEx = new ArrayList<String>();
                        vars.add(new Variable(tokens.get(0)));
                        varNames.add(tokens.get(0));
                        for (int i = tokens.indexOf("=") + 1; i < tokens.size(); i++) {
                            equalEx.add(tokens.get(i));
                        }
                        for (int i = 0; i < varNames.size(); i++) {
                            String var = varNames.get(i);
                            for (int j = 0; j < equalEx.size(); j++) {
                                if (equalEx.get(j).equals(var)) {
                                    equalEx.set(j, values.get(i).toString());
                                }
                            }
                        }
                        values.add(Parser.parse(equalEx));
                        System.out.println("Added " + values.get(values.size() - 1) + " as " + vars.get(values.size() - 1));
                    }
                } else if (varNames.contains(tokens.get(0))) {
                    System.out.println(values.get(varNames.indexOf(tokens.get(0))));
                } else {
                    System.out.println(Parser.parse(tokens));
                }
                // }
            }
            System.out.print("> ");
            input = s.nextLine();
        }
        System.out.printf("Goodbye!");

    }
}
