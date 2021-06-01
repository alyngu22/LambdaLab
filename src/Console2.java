import java.util.*;
public class Console2 {
    public static void main(String[] args) {
        System.out.printf("> ");
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        ArrayList<String> tokens;
        while (!input.equals("exit")) {
            tokens = Lexer.cleanUp(input);
            System.out.println(parse(tokens));

            System.out.print("> ");
            input = s.nextLine();
        }
        System.out.printf("Goodbye!");

    }

    public static Expression parse (ArrayList<String> tokens) {
        ArrayList<Expression> exArr = new ArrayList<Expression>();
        for (int i = 0; i < tokens.size(); i ++) {
            if (tokens.get(0).equals("\\")) {
                ArrayList<String> ex = new ArrayList<String>();
                Variable v = new Variable(tokens.get(1));
                for (int j = 3; j < tokens.size(); j++) {
                    ex.add(tokens.get(j));
                }
                tokens.clear();
                exArr.add(new Function(v,parse(ex)));

            }
            else if (tokens.get(0).equals("(")) {
                int parens = 1;
                int pIndex = -1;
                ArrayList<String> in = new ArrayList<String>();
                for (int p = 1; p < tokens.size(); p++) {
                    if (tokens.get(p).equals("(")) {
                        parens++;
                    }
                    if (parens != 0 && tokens.get(p).equals(")")) {
                        parens--;
                        if (parens == 0) {
                            pIndex = p;
                            for (int j = 1; j < pIndex; j++) {
                                in.add(tokens.get(j));
                            }
                            for (int j = 1; j < pIndex + 2; j++) {
                                tokens.remove(0);
                            }
                            exArr.add(parse(in));
                        }
                    }
                }
            }
            else {
                exArr.add(new Variable(tokens.get(0)));
                tokens.remove(0);
            }
            i--;
        }
        return parseEx(exArr);
    }

    public static Expression parseEx(ArrayList<Expression> exArr) {
        if (exArr.size() == 1) {
            return exArr.get(0);
        }
        else {
            ArrayList<Expression> front = new ArrayList<Expression>();
            for (int i = 0; i < exArr.size() -1; i++) {
                front.add(exArr.get(i));
            }
            return new Application(parseEx(front), exArr.get(exArr.size()-1));
        }
    }
}
