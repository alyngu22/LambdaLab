import java.util.ArrayList;
import java.util.Scanner;

public class Parser1 {
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

    public static Expression substituteBound (Expression ex, ArrayList<Variable> rightList, ArrayList<Variable> leftList, boolean hasAppeared) {
        if (ex instanceof  Application) {
            substituteBound(((Application) ex).getLeft(),rightList, leftList, hasAppeared);
            substituteBound(((Application) ex).getRight(),rightList, leftList, hasAppeared);
        }
        else if (ex instanceof Function) {
            for(Variable v: leftList){
                for (Variable v1: rightList){
                    if(v.toString().equals(v1.toString())){
                        ((Function) ex).getVar().setName(((Function) ex).getVar().toString() + "1");
                    }
                }
                if((((Function) ex).getVar().toString().equals(v.toString())) && hasAppeared){
                    return ex;
                }
                else if(((Function) ex).getVar().toString().equals(v.toString())){
                    hasAppeared = true;
                }
            }
            substituteBound(((Function) ex).getVar(),rightList, leftList, hasAppeared);
            substituteBound(((Function) ex).getEx(),rightList, leftList, hasAppeared);
        }
        else {
            for (Variable v: leftList) {
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
                varList.add(new Variable(((Function)expression).getVar().toString()));

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
                varList.add(new Variable(expression.toString()));
        }
        return varList;
    }
    public static ArrayList<Variable> getVLeft(Expression expression, ArrayList<Variable> varList) {
        if(expression instanceof Function) {
            boolean contains = false;
            for (Variable v : varList) {
                if (v.toString().equals(((Function) expression).getVar().toString()))
                    contains = true;
            }
            if (!contains)
                varList.add(new Variable(((Function) expression).getVar().toString()));
            ArrayList<Variable> varListEx = getVLeft(((Function) expression).getEx(), varList);
            ArrayList<Variable> tempList = getDifference(varList, varListEx);
            varList.addAll(tempList);
            return varList;
        }
        else{
            return varList;
        }
    }
    public static Expression compute (String var, Expression ex, Expression input) {
        if (ex instanceof Application) {
            Application ret = new Application(compute(var, ((Application) ex).getLeft(), input), compute(var, ((Application) ex).getRight(), input));
            return new Application(compute(var, ((Application) ex).getLeft(), input), compute(var, ((Application) ex).getRight(), input));
        }
        else if (ex instanceof Function) {
            Expression ret = compute(var,((Function) ex).getEx(),input);
            return new Function(((Function)ex).getVar(), compute(var,((Function) ex).getEx(),input));
        }
        else {
            if (ex.toString().equals(var)) {
                return input;
            }
            else {
                return new Variable(ex.toString());
            }
        }
    }
//  (\x.(x z))
    /*
          parameters: String var, Expression ex, Expression input
          example, if the whole thing was (\a.\b.(a b)) c d
          var = a
          ex = \b.(a b) c
          input = f
     */

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        ArrayList<String> tokens;
        tokens = Lexer.cleanUp(input);


        Expression ex = parse(tokens);


        ArrayList<Variable> rightVars = getVRight(((Application)ex).getRight(),new ArrayList<>());
        ArrayList<Variable> leftVars = getVLeft(((Application)ex).getLeft(),new ArrayList<>());
        Expression subbed = new Application (substituteBound(((Application) ex).getLeft(),rightVars, leftVars, false),((Application) ex).getRight());
        Expression calculated = compute(((Function)(((Application)subbed).getLeft())).getVar().toString(),((Function)(((Application)subbed).getLeft())).getEx(),((Application)subbed).getRight());
        while (calculated instanceof Application && ((Application) calculated).getLeft() instanceof Function) {
            calculated = compute(((Function)(((Application)calculated).getLeft())).getVar().toString(),((Function)(((Application)calculated).getLeft())).getEx(),((Application)calculated).getRight());
        }
        System.out.println(calculated);
    }
}

