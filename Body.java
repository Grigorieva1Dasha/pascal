package interpreter;

import java.util.ArrayList;

public class Body extends Node {

    ArrayList <Node> expressions;
    int currentExprId;

    public Body(){
        expressions = new ArrayList<>();
        currentExprId = 0;
    }

    public void addExpression(Node node){
        expressions.add(node);
    }

    public Node getNext(){
        currentExprId += 1;
        if (currentExprId != expressions.size() + 1)
             return expressions.get(currentExprId - 1);
        return null;
    }
}
