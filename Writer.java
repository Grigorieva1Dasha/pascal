package interpreter;

public class Writer extends Node{
    Variable ID;
    Node expr;

    public Writer(Variable ID, Node node) {
        this.ID = ID;
        this.expr = node;
    }

    public Variable getID(){
        return this.ID;
    }

    public Node getExpr(){
        return this.expr;
    }
}
