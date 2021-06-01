import java.awt.*;
import java.util.ArrayList;

public interface Expression{
    public String toString ();
}

class Function implements Expression{
    private Variable var;
    public Expression ex;
    public Function (Variable v, Expression e) {
        this.var = v;
        this.ex = e;
    }
    @Override
    public String toString () {
        return "(λ" + this.var + "." + this.ex + ")";
    }


//toString, getter, deepcopy


}

class Application implements Expression{
    private Expression left;
    private Expression right;
    public Application(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public String toString() {
        return "(" + this.left.toString() + " " + this.right.toString() + ")";
    }
//has a left and a right


}

class Variable implements Expression {
    private String name;
    public Variable (String s) {
        this.name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Variable deepCopy() {
        Variable cpy = new Variable(this.name);
        return cpy;
    }



}




