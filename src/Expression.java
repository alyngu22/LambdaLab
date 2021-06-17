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

    public void setEx(Expression ex) {
        this.ex = ex;
    }

    public void setVar(Variable var) {
        this.var = var;
    }

    public Expression getEx(){
        return this.ex;
    }
    public Variable getVar(){
        return this.var;
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

    public void setLeft(Expression left) {
        this.left = left;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    public Expression getLeft(){
        return this.left;
    }

    public Expression getRight(){
        return this.right;
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

    public void setName(String name) {
        this.name = name;
    }
}




