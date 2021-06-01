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

            if (!input.equals("")) {
                ArrayList<Expression> exArra = new ArrayList<Expression>();
                int parens = 0;
                int startF;
                boolean func = false;
                int startP;
                int endP = 0;
                ArrayList<String> segment = new ArrayList<>();
                for (int i = 0; i < tokens.size(); i++) {
                    if (tokens.get(i).equals("(")) {
                        parens++;
                        startP = i;
                    } else if (tokens.get(i).equals(")")) {
                        parens--;
                        endP = i;

                    } else if (tokens.get(i).equals("\\")) {
                        func = true;
                    }
                    if (parens == 0) {
                        if (endP != 0) {
                            for (int j = tokens.indexOf("(") + 1; j < endP; j++) {
                                segment.add(tokens.get(j));
                            }
                        }
                        else if (func) {
                            for (int j = i; j < tokens.size(); j++) {
                                segment.add(tokens.get(j));
                                i = tokens.size();
                            }
                        }
                        else if (segment.size() == 0) {
                            segment.add(tokens.get(i));
                        }
                        exArra.add(parse(segment));
                        parens = 0;
                        startF = 0;
                        func = false;
                        startP = 0;
                        endP = 0;
                        segment.clear();

                    }

                }
                if (exArra.size() > 1) {
                    System.out.println(parseEx(exArra));
                }
                else {
                    System.out.println(exArra.get(0));
                }
                //System.out.println(parse(tokens));
            }

            System.out.print("> ");
            input = s.nextLine();
        }
        System.out.printf("Goodbye!");
    }

    public static Expression parseEx(ArrayList<Expression> exArr) {
        ArrayList<Expression> leftovers = new ArrayList<Expression>();
        if (exArr.size() > 2) {
            for (int i = 0; i < exArr.size() - 1; i++) {
                leftovers.add(exArr.get(i));
            }
        }
        if (leftovers.size() > 0) {
            return new Application(parseEx(leftovers), exArr.get(exArr.size() - 1));
        }
        return new Application(exArr.get(0),exArr.get(1));
        }

    public static Expression parse(ArrayList<String> tokens) {
        if (tokens.size() == 1) {
            return new Variable(tokens.get(0));
        }
        else if (tokens.get(0).equals("\\")) {
            ArrayList<String> ex = new ArrayList<String>();
            Function f;
            for(int i = tokens.indexOf(".") + 1; i<tokens.size(); i++){
                ex.add(tokens.get(i));
            }
            if(ex.size()==1){
                f = new Function(new Variable(tokens.get(1)), new Variable(ex.get(0)));
            }
            else{
                f = new Function(new Variable(tokens.get(1)), parse(ex));
            }
            return f;
        }
        else if (tokens.get(0).equals("(")) {
            ArrayList<String> insideParen = new ArrayList<>();
            for (int i = 1; i < tokens.lastIndexOf(")"); i++) {
                insideParen.add(tokens.get(i));
            }
            return parse(insideParen);
        }

        else {
            ArrayList<String> segment = new ArrayList<>();
            for (int i = 0; i < tokens.size() -1; i++) {
                segment.add(tokens.get(i));
            }
            return new Application(parse(segment), new Variable(tokens.get(tokens.size()-1)));
        }
   }
        /*
        for (String x: tokens) {
            System.out.println(x);
        }
        System.out.println();
        if (tokens.size() == 1) {
            Variable v = new Variable(tokens.get(0));
            return v;
        }
        else {
            if (tokens.size() >= 2) {
                if (tokens.get(0).equals("\\") || tokens.get(0).equals("λ")) {
                    ArrayList<String> ex = new ArrayList<String>();
                    Function f;
                    for(int i = tokens.indexOf(".") + 1; i<tokens.size(); i++){
                        ex.add(tokens.get(i));
                        System.out.println(tokens.get(i));
                    }
                    if(ex.size()==1){
                        f = new Function(new Variable(tokens.get(1)), new Variable(ex.get(0)));
                    }
                    else{
                        f = new Function(new Variable(tokens.get(1)), parse(ex));
                    }
                    return f;
                }
                else {
                    Application a = app(tokens);
                    return a;
                }
            }

        }

     return  null;

    }
    public static Application app(ArrayList<String> tokens){
        ArrayList<String> left = new ArrayList<String>();
        ArrayList<String> right = new ArrayList<String>();

        if (tokens.get(0).equals("(")) {
            if(tokens.indexOf(")")!=tokens.size()-1) {
                for (int i = 1; i < tokens.indexOf(")"); i++) {
                    left.add(tokens.get(i));
                }
                if (tokens.lastIndexOf("(") != 0) {
                    for (int j = tokens.lastIndexOf("(") + 1; j < tokens.size() - 1; j++) {
                        right.add(tokens.get(j));
                    }
                }
                else {
                    for (int j = tokens.indexOf(")") + 1; j < tokens.size(); j++) {
                        right.add(tokens.get(j));
                    }
                }
            }


            else{
                tokens.remove(0);
                tokens.remove(tokens.size()-1);
                app(tokens);
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
        else if (tokens.contains("\\")) {
            for (int i = 0; i < tokens.indexOf("\\"); i++) {
                left.add(tokens.get(i));
            }
            for (int j = tokens.indexOf("\\"); j < tokens.size(); j++) {
                right.add(tokens.get(j));
            }
        }

        else  {

            for (int i = 0; i < tokens.size() - 1; i++) {
                left.add(tokens.get(i));
            }
            right.add(tokens.get(tokens.size()-1));
        }
        Application ret;
        if (left.size() >= 2 || right.size() >= 2) {
            ret = new Application(parse(left).toString(), parse(right).toString());
        } else {
            ret = new Application(tokens.get(0), tokens.get(1));
        }
        return ret;
    }

         */

}

