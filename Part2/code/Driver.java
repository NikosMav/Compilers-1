import java_cup.runtime.*;
import java.io.*;

class Driver {
    public static void main(String[] argv) throws Exception{
        System.err.println("Expression(s) should be semantically correct.");
        // Read from file.
        InputStream inputStream = new FileInputStream("input.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        Parser p = new Parser(new Scanner(inputStreamReader));
        p.parse();
    }
}