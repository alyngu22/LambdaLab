import java.util.ArrayList;
import java.util.Scanner;

public class Console2 {
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
                if (tokens.contains("run")){
                    Expression ex = Parser2.parse(tokens);
                   // System.out.println(Parser2.compute(ex));

                    /*
                    ArrayList<String> a = new ArrayList<String>();
                    for(int i = tokens.indexOf("run") + 1; i < tokens.size(); i++){
                        a.add(tokens.get(i));
                    }

                    Expression expression = Parser.parse(a);
                    
                    Expression runProduct = null;
                    
                    if(expression.getClass() == Application.class){
                        if(((Application) expression).getLeft() instanceof Function){
                            Expression e = ((Function) ((Application) expression).getLeft()).getEx();
                            Expression v = ((Function) ((Application) expression).getLeft()).getVar();
                            if(e instanceof Variable){
                                if(e.toString().equals(v.toString())){
                                    runProduct = ((Application) expression).getRight();
                                }
                                else{
                                    runProduct = e;
                                }
                            }
                            else if(e instanceof Application){
                                String checkForVar = e.toString();
                                for(int i = 0; i<checkForVar.length(); i++){
                                    //find the variable
                                }
                                System.out.println("heyy");
                            }
                            else{
                                //function
                                System.out.println("function");
                            }
                        }
                        else {
                            runProduct = expression;
                        }
                    }
                    else{
                        runProduct = expression;
                    }
                    if (tokens.contains("=")) {
                        if (varNames.contains(tokens.get(0))) {
                            System.out.println(tokens.get(0) + " is already defined.");
                        }
                        else {
                            vars.add(new Variable(tokens.get(0)));
                            varNames.add(tokens.get(0));
                            values.add(runProduct);
                            System.out.println("Added " + values.get(values.size() - 1) + " as " + vars.get(values.size() - 1));
                        }
                    }
                    else if (varNames.contains(tokens.get(0))) {
                        System.out.println(values.get(varNames.indexOf(tokens.get(0))));
                    }
                    else {
                        System.out.println(runProduct);
                    }

                     */
                }
                
                //adding only
           //    else {
                    if (tokens.contains("=")) {
                        if (varNames.contains(tokens.get(0))) {
                            System.out.println(tokens.get(0) + " is already defined.");
                        } else {
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
                            values.add(Parser2.parse(equalEx));
                            System.out.println("Added " + values.get(values.size() - 1) + " as " + vars.get(values.size() - 1));
                        }
                    } else if (varNames.contains(tokens.get(0))) {
                        System.out.println(values.get(varNames.indexOf(tokens.get(0))));
                    } else {
                        System.out.println(Parser2.parse(tokens));
                    }
               // }
            }
            System.out.print("> ");
            input = s.nextLine();
        }
        System.out.printf("Goodbye!");

    }
}
