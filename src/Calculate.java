import java.util.ArrayList;

public class Calculate {
    public static ArrayList<Variable> vars = new ArrayList<Variable>();
    public static ArrayList<String> varNames = new ArrayList<String>();
    public static ArrayList<Expression> values = new ArrayList<Expression>();

    public static ArrayList<String> substituteVar(ArrayList<String> tokens) {
        for (int i = 0; i < varNames.size(); i++) {
            String var = varNames.get(i);
            for (int j = 0; j < tokens.size(); j++) {
                if (tokens.get(j).equals(var)) {
                    ArrayList<String> valString = Lexer.cleanUp(values.get(i).toString());
                    tokens.set(j, valString.get(0));
                    for (int k = 1; k < valString.size(); k++) {
                        tokens.add(k + j + 1, valString.get(k));
                    }
                }
            }
        }
      //  tokens = Lexer.cleanUp(tokens.toString());
        return tokens;
    }
    public static String add (ArrayList<String> tokens) {
        if (varNames.contains(tokens.get(0))) {
            return tokens.get(0) + " is already defined.";
        }
        else {
            ArrayList<String> equalEx = new ArrayList<String>();
            vars.add(new Variable(tokens.get(0)));
            varNames.add(tokens.get(0));
            for (int i = tokens.indexOf("=") + 1; i < tokens.size(); i++) {
                equalEx.add(tokens.get(i));
            }
            tokens = substituteVar(equalEx);
            values.add(Parser.parse(tokens));
            return "Added " + values.get(values.size() - 1) + " as " + vars.get(values.size() - 1);
        }
    }
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
    public static Expression run(Expression ex) {
        ArrayList<Variable> rightVars = getVRight(((Application)ex).getRight(),new ArrayList<>());
        Expression subbed = new Application (substituteBound(((Application) ex).getLeft(),rightVars),((Application) ex).getRight());
        Expression calculated = compute(((Function)(((Application)subbed).getLeft())).getVar().toString(),((Function)(((Application)subbed).getLeft())).getEx(),((Application)subbed).getRight());
        while (calculated instanceof Application && ((Application) calculated).getLeft() instanceof Function) {
            calculated = compute(((Function)(((Application)calculated).getLeft())).getVar().toString(),((Function)(((Application)calculated).getLeft())).getEx(),((Application)calculated).getRight());
        }
        return calculated;
    }
}
