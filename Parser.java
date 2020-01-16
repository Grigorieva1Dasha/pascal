package interpreter;

import java.util.Arrays;
import java.util.List;

public class Parser {
    private Lexer lexer;
    private Token currentToken;
    private Variable curVar;

    public Parser(Lexer lexer) throws Exception {
        this.lexer = lexer;
        currentToken = this.lexer.nextToken();
    }

    private void checkTokenType(TokenType type) throws Exception {
        if (currentToken.getType() == type){
            currentToken = lexer.nextToken();
        }
        else {
            throw new Exception("Parser error");
        }
    }

    private Node factor() throws Exception {
        Token token = currentToken;
        if (token.getType().equals(TokenType.EOL)){
            checkTokenType(TokenType.EOL);
            token = currentToken;
        }
        if (token.getType().equals(TokenType.PLUS)){
            checkTokenType(TokenType.PLUS);
            return new UnaryOp(token, factor());
        }
        else if (token.getType().equals(TokenType.MINUS)){
            checkTokenType(TokenType.MINUS);
            return new UnaryOp(token, factor());
        }
        else if (token.getType().equals(TokenType.INTEGER)) {
            checkTokenType(TokenType.INTEGER);
            return new Number(token);
        }
        else if (token.getType().equals(TokenType.LPAREN)) {
            checkTokenType(TokenType.LPAREN);
            Node node = expr(factor());
            checkTokenType(TokenType.RPAREN);
            return node;
        }
        else if (token.getType().equals(TokenType.BEGIN)) {
            checkTokenType(TokenType.BEGIN);
            Node node = expr(assignment());
            checkTokenType(TokenType.END);
            return node;
        }
        else if (token.getType().equals(TokenType.END)) {
            return new End(token);
        }
        else if (token.getType().equals(TokenType.ID)) {
            checkTokenType(TokenType.ID);
            curVar = new Variable(token);            ;
            return curVar;
        }
        else if (token.getType().equals(TokenType.EQUATING)) {
            checkTokenType(TokenType.EQUATING);
            Node node = expr(factor());
            return new Writer(curVar, node);
        }
        throw new Exception("Factor error");
    }

    private Node term(Node node) throws Exception {
        Node result =  node;
        List<TokenType> ops = Arrays.asList(TokenType.DIV, TokenType.MUL);
        while (ops.contains(currentToken.getType())){
            Token token = currentToken;
            if (token.getType() == TokenType.MUL){
                checkTokenType(TokenType.MUL);
            }
            else if(token.getType() == TokenType.DIV){
                checkTokenType(TokenType.DIV);
            }
            result = new BinOp(result, token, factor());
        }
        return result;
    }

    public Node expr(Node node) throws Exception {
        List<TokenType> ops = Arrays.asList(TokenType.PLUS, TokenType.MINUS);
        Node result = term(node);
        while (ops.contains(currentToken.getType())){
            Token token = currentToken;
            if (token.getType() == TokenType.PLUS){
                checkTokenType(TokenType.PLUS);
            }
            else if (token.getType() == TokenType.MINUS){
                checkTokenType(TokenType.MINUS);
            }
            result = new BinOp(result, token, term(factor()));
        }
        return result;
    }

    public Node equating() throws Exception{
        List<TokenType> ops = Arrays.asList(TokenType.EQUATING, TokenType.ID);
        Node result = factor();
        while (ops.contains(currentToken.getType())){
            Token token = currentToken;
            if (token.getType() == TokenType.ID) {
               checkTokenType(TokenType.ID);
           }
            if (token.getType() == TokenType.EQUATING){
                checkTokenType(TokenType.EQUATING);
                result = new Writer((Variable)curVar, expr(factor()));
                return result;
            }
        }
        return expr(result);
    }

    public Node assignment()throws Exception{
        Body body = new Body();
        do {
            Node result = equating();
            body.addExpression(result);
        } while ( currentToken.getType() == TokenType.EOL);
        return body;
    }

    public Node parse() throws Exception {
        Node result = assignment();
        if (currentToken.getType() != TokenType.EOF){
            throw new Exception("Syntax error");
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer("BEGIN\n END");
        Parser parser = new Parser(lexer);
        System.out.println(parser.parse());
    }
}
