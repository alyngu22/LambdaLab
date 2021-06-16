import java.util.ArrayList;
import java.util.Scanner;

public class Parser2 {
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

    public static Expression compute(Expression ex) {
        if (ex instanceof Application) {
            Expression aLeft = compute(((Application) ex).getLeft());
            Expression aRight = compute(((Application) ex).getRight());
            if (aLeft instanceof Function) {

            }
            return new Application(aLeft,aRight);
        }
        return null;
    }

    public static ArrayList<Variable> getVRight(Expression expression, ArrayList<Variable> varList) {
        if (expression instanceof Application) {
            ArrayList<Variable> varListLeft = getVRight(((Application) expression).getLeft(), varList);
            ArrayList<Variable> varListRight = getVRight(((Application) expression).getRight(), varList);
            ArrayList<Variable> tempList = new ArrayList<>();
            boolean contains = false;
            for (Variable v : varListLeft) {
                for (Variable v1 : varList) {
                    if (!contains & !(v.toString().equals(v1.toString()))) {
                        tempList.add(v);
                        contains = true;
                    }
                }
                contains = false;
            }
            for (Variable v : varListRight) {
                for (Variable v1 : varList) {
                    if (!contains & !(v.toString().equals(v1.toString()))) {
                        tempList.add(v);
                        contains = true;
                    }
                }
                contains = false;
            }
            varList.addAll(tempList);
        }
        else if (expression instanceof Function) {
            boolean contains = false;
            for (Variable v: varList) {
                if (v.toString().equals(((Function) expression).getVar().toString()))
                    contains = true;
            }
            if (!contains)
                varList.add(((Function) expression).getVar());

            ArrayList<Variable> varListRight = getVRight(((Function) expression).getEx(),varList);
            ArrayList<Variable> tempList = new ArrayList<>();
            for (Variable v: varListRight) {
                for (Variable v1 : varList) {
                    if (!contains & !(v.toString().equals(v1.toString()))) {
                        tempList.add(v);
                        contains = true;
                    }
                }
                contains = false;
            }
            varList.addAll(tempList);
        }
        else {
            boolean contains = false;
            for (Variable v1: varList) {
                if (v1.toString().equals(expression.toString())) {
                    contains = true;
                }
            }
            if (!contains)
                varList.add((Variable) expression);
        }
        return varList;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        ArrayList<String> tokens;
        tokens = Lexer.cleanUp(input);
        Expression ex = Parser.parse(tokens);
        ArrayList<Variable> vEmpty = new ArrayList<>();
        ArrayList<Variable> vList = getVRight(ex,vEmpty);
        for (Variable v: vList) {
            System.out.println(v);
        }
    }
}
