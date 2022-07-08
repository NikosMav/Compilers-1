import java.io.InputStream;
import java.io.IOException;
import java.lang.Math;
import java.util.*;

public class CalcEvaluator {
    private final InputStream in;
    private int lookaheadToken;

    public CalcEvaluator(InputStream in) throws IOException {
        this.in = in;
        lookaheadToken = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {
        if (lookaheadToken == symbol)
            lookaheadToken = in.read();
        else
            throw new ParseError();
    }

    private boolean isDigit() {
        return '0' <= lookaheadToken && lookaheadToken <= '9';
    }

    private boolean isDigitWithout0() {
        return '1' <= lookaheadToken && lookaheadToken <= '9';
    }

    private boolean isValid() {
        return ('0' <= lookaheadToken && lookaheadToken <= '9') || (lookaheadToken == '(' || lookaheadToken == ')' || lookaheadToken == '*');
    }

    private int evalDigit(int c) {
        return c - '0';
    }

    private static int pow(int base, int exponent) {
        if (exponent < 0)
            return 0;
        if (exponent == 0)
            return 1;
        if (exponent == 1)
            return base;    
    
        if (exponent % 2 == 0) //even exp -> b ^ exp = (b^2)^(exp/2)
            return pow(base * base, exponent/2);
        else                   //odd exp -> b ^ exp = b * (b^2)^(exp/2)
            return base * pow(base * base, exponent/2);
    }

    public int eval() throws IOException, ParseError {
        int value = expr();

        if (lookaheadToken != -1 && lookaheadToken != '\n')
            throw new ParseError();

        // Just for showing purposes in case of EOF.
        System.out.println("\n");    
        return value;
    }

    private int expr() throws IOException, ParseError {
        int left, right;
        left = term();
        right = expr2();

        return left + right;        //Always adding the numbers

    }

    private int term() throws IOException, ParseError {
        int left, right;
        left = factor();
        right = term2();

        return pow(left, right);

    }

    private int expr2() throws IOException, ParseError {
        int tempValue1, tempValue2;

        switch(lookaheadToken){
            case '+':
                consume('+');
                tempValue1 = term();
                tempValue2 = expr2();

                return tempValue1 + tempValue2;

            case '-':
                consume('-');
                tempValue1 = term();
                tempValue2 = expr2();

                return (tempValue1 * (-1) + tempValue2);            //Return the number with a minus sign

            case -1:
            case '\n':
            default:
                return 0;
        }
    }

    private int term2() throws IOException, ParseError {
        int tempValue1, tempValue2;

        switch(lookaheadToken){
            case '*':
                consume(lookaheadToken);
                if(lookaheadToken == '*'){
                    consume(lookaheadToken);
                    tempValue1 = factor();
                    tempValue2 = term2();

                    return pow(tempValue1, tempValue2);
                } else {
                    throw new ParseError();
                }
            default: 
                return 1;
        }
    }
    
    private int factor() throws IOException, ParseError {
        int tempValue, number;

        if (!isValid() || lookaheadToken == ')')
            throw new ParseError();

        if (isDigit()) {
            number = num();
            return number;
        }

        consume(lookaheadToken);
        //Inside parenthesis
        tempValue = expr();
        consume(')');

        return tempValue;
    }

    private int num() throws IOException, ParseError {
        int tempDigit, i = 0, actualNumber = 0;
        Stack<Integer> stackOfDigits = new Stack<Integer>();

        if(lookaheadToken == '0'){
            consume(lookaheadToken);
            return 0;

        } else if(isDigitWithout0()) {
            stackOfDigits.push(lookaheadToken);          //Keep digits in list
            consume(lookaheadToken);
            num2(stackOfDigits);

            while(!stackOfDigits.isEmpty()){
                tempDigit = stackOfDigits.pop();
                tempDigit = evalDigit(tempDigit);
                actualNumber += tempDigit * (int)Math.pow(10,i);
                i = i + 1;
            }

            return actualNumber;
        }
        
        throw new ParseError();
        
    }

    private int num2(Stack<Integer> stackOfDigits) throws IOException, ParseError {

        if(isDigit()){
            stackOfDigits.push(lookaheadToken); 
            consume(lookaheadToken);
            num2(stackOfDigits);
        } 

        return 0;
    }

}
