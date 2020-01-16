package interpreter;

public interface NodeVisitor {
    void visit(Node node) throws Exception;
}
