package interpreter;

public class Lexer {
    private String text;
    private int pos = 0;
    private Character currentChar;

    public Lexer(String text){
        this.text = text;
        currentChar = text.charAt(pos);
    }

    public Token nextToken() throws Exception {
        while (currentChar != null) {
            if (Character.isSpaceChar(currentChar) || currentChar == '\n') {
                skip();
                continue;
            }
            if (Character.isDigit(currentChar)) {
                return new Token(TokenType.INTEGER, integer());
            }
            if (Character.isLetter(currentChar)){
                String str = row();
                if (str.equals("BEGIN"))
                    return new Token(TokenType.BEGIN, str);
                else if (str.equals("END"))
                    return new Token(TokenType.END, str);
                return new Token(TokenType.ID, str);
            }
            if (currentChar == ';') {
                forward();
                return new Token(TokenType.EOL, ";");
            }
            if (currentChar == '+') {
                forward();
                return new Token(TokenType.PLUS, "+");
            }
            if (currentChar == '=') {
                forward();
                return new Token(TokenType.EQUATING, "=");
            }
            if (currentChar == '-') {
                forward();
                return new Token(TokenType.MINUS, "-");
            }
            if (currentChar == '*') {
                forward();
                return new Token(TokenType.MUL, "*");
            }
            if (currentChar == '/') {
                forward();
                return new Token(TokenType.DIV, "/");
            }
            if (currentChar == '('){
                forward();
                return new Token(TokenType.LPAREN, "(");
            }
            if (currentChar == ')'){
                forward();
                return new Token(TokenType.RPAREN, ")");
            }
            if (currentChar == '.'){
                forward();
                return new Token(TokenType.EOF, ".");
            }
            throw new Exception("Parser error");
        }
       return new Token(TokenType.END, null);
    }

    private void forward(){
        pos += 1;
        if (pos > text.length() - 1){
            currentChar = null;
        }
        else{
            currentChar = text.charAt(pos);
        }
    }

    private void skip(){
        while ((currentChar != null) && (Character.isSpaceChar(currentChar)|| currentChar == '\n')){
            forward();
        }
    }

    private String integer(){
        String result = "";
        while ((currentChar != null) && (Character.isDigit(currentChar))){
            result += currentChar;
            forward();
        }
        return result;
    }

    private String row(){
        String result = "";
        while ((currentChar != null) && (Character.isLetter(currentChar))){
            result += currentChar;
            forward();
        }
        return result;
    }
}
