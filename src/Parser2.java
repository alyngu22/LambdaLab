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

   /* public static Expression compute(Expression ex) {
        if (ex instanceof Application) {
            Expression aLeft = compute(((Application) ex).getLeft());
            Expression aRight = compute(((Application) ex).getRight());
            if (aLeft instanceof Function) {

            }
            return new Application(aLeft,aRight);
        }
        return null;
    }

    */

    public static Expression substituteBound (Expression ex, ArrayList<Variable> list) {
        if (ex instanceof  Application) {
            substituteBound(((Application) ex).getLeft(),list);
            substituteBound(((Application) ex).getRight(),list);
        }
        else if (ex instanceof Function) {
            substituteBound(((Function) ex).getVar(),list);
            substituteBound(((Function) ex).getEx(),list);
        }
        else {
            for (Variable v: list) {
                if (v.toString().equals(ex.toString())) {
                    ((Variable)ex).setName(ex.toString() + "1");
                }
            }
        }
        return ex;
    }
    public static ArrayList<Variable> getDifference (ArrayList<Variable> oldList, ArrayList<Variable> newList) {
        ArrayList<Variable> ret = new ArrayList<>();
        boolean contains = false;
        for (Variable v: newList) {
            for (Variable v1: oldList) {
                if (v1.toString().equals(v.toString())) {
                    contains = true;
                }
            }
            if (!contains) {
                ret.add(v);
            }
            contains = false;
        }
        return ret;
    }

    public static ArrayList<Variable> getVRight(Expression expression, ArrayList<Variable> varList) {
        if (expression instanceof Application) {
            ArrayList<Variable> varListLeft = getVRight(((Application) expression).getLeft(), varList);
            ArrayList<Variable> varListRight = getVRight(((Application) expression).getRight(), varList);
            ArrayList<Variable> tempListLeft = getDifference(varList,varListLeft);
            ArrayList<Variable> tempListRight = getDifference(varList,varListRight);

            varList.addAll(tempListLeft);
            varList.addAll(tempListRight);
        }
        else if (expression instanceof Function) {
            boolean contains = false;
            for (Variable v: varList) {
                if (v.toString().equals(((Function) expression).getVar().toString()))
                    contains = true;
            }
            if (!contains)
                varList.add(((Function) expression).getVar());

            ArrayList<Variable> varListEx = getVRight(((Function) expression).getEx(),varList);
            ArrayList<Variable> tempList = getDifference(varList,varListEx);
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


//    \a.\b.\c.(a b c) e f
  //var = a
    //ex = \b.\c.( a b c) e
    //input = f
    public static Expression compute (String var, Expression ex, Expression input) {
        if (ex instanceof Application) {
            compute(var, ((Application) ex).getLeft(), input);
            compute(var, ((Application) ex).getRight(), input);
        }
        else if (ex instanceof Function) {
            compute(var,((Function) ex).getEx(),input);
        }
        else {
            if (ex.)
        }

    }


            (\a.\b.(a b)) (\x.x)


    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        ArrayList<String> tokens;
        tokens = Lexer.cleanUp(input);
        Expression ex = Parser.parse(tokens);
        ArrayList<Variable> rightVars = getVRight(((Application)ex).getRight(),new ArrayList<>());
        System.out.println(substituteBound(((Application) ex).getLeft(),rightVars));

        }
    }

