import java_cup.runtime.*;

%%
/* ----------------- Options and Declarations Section----------------- */

/*
   The name of the class JFlex will create will be Scanner.
   Will write the code to the file Scanner.java.
*/
%class Scanner

/*
  The current line number can be accessed with the variable yyline
  and the current column number with the variable yycolumn.
*/
%line
%column

/*
   Will switch to a CUP compatibility mode to interface with a CUP
   generated parser.
*/
%cup

/*
  Declarations

  Code between %{ and %}, both of which must be at the beginning of a
  line, will be copied letter to letter into the lexer class source.
  Here you declare member variables and functions that are used inside
  scanner actions.
*/

%{
    /**
        The following two methods create java_cup.runtime.Symbol objects
    **/
    StringBuffer stringBuffer = new StringBuffer();
    private Symbol symbol(int type) {
       return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

/*
  Macro Declarations

  These declarations are regular expressions that will be used latter
  in the Lexical Rules Section.
*/

/* A line terminator is a \r (carriage return), \n (line feed), or \r\n.*/
LineTerminator = \r|\n|\r\n

/* White space is a line terminator, space, tab, or line feed. */
WhiteSpace     = {LineTerminator} | [ \t\f]

/* Identifier is a string of character where the first character must be a letter.  */
Identifier = [a-zA-Z_][a-zA-Z0-9_]*

/* In order for us to differentiate a simple function call and a declaration of a function we must create different terminal strings for each case. */
/* First we create a FunctionStart RE to specify the simple function call pattern. */
/* A funtion starts with an Identifier, probably whitespaces and then a left parentesis. */
FunctionStart = {Identifier}{WhiteSpace}*"("

/* And then a RE for the declaration pattern. */
/* A declaration function stars with an Identifier and then a left parenthesis. Then we must include a RE for the parameters of the function, a closing parenthesis and then a left bracket. Between each character we take into account every possible whitespace. */
DeclarationFunctionStart = {Identifier}{WhiteSpace}*"("{WhiteSpace}*({Identifier}{WhiteSpace}*)?(","{WhiteSpace}*{Identifier}{WhiteSpace}*)*")"{WhiteSpace}*"{"

/* If is a regular expression that matches "if (" with probably whitespace characters in between. */
If = "if"{WhiteSpace}*"("
Else = else
Suffix = suffix
Prefix = prefix

%state STRING

%%
/* ------------------------Lexical Rules Section---------------------- */

<YYINITIAL> {
   {If}           { return symbol(sym.IF); }
   {Else}         { return symbol(sym.ELSE); }
   {Suffix}       { return symbol(sym.SUFFIX); }
   {Prefix}       { return symbol(sym.PREFIX); }
   {Identifier}   { return symbol(sym.IDENTIFIER, yytext()); }
   {FunctionStart} { return symbol(sym.FUNCTION_START, yytext()); }
   {DeclarationFunctionStart} { return symbol(sym.DECLARATION_FUNCTION_START, yytext()); }
   /* operators */
   "+"            { return symbol(sym.PLUS); }
   ")"            { return symbol(sym.RPAREN); }
   "}"            { return symbol(sym.RBRACKET); }
   ","            { return symbol(sym.COMMA); }
   \"             { stringBuffer.setLength(0); yybegin(STRING); }
   {WhiteSpace}   { /* do nothing */ }
}

<STRING> {
      \"                             { yybegin(YYINITIAL);
                                       return symbol(sym.STRING_LITERAL, stringBuffer.toString()); }
      [^\n\r\"\\]+                   { stringBuffer.append( yytext() ); }
      \\t                            { stringBuffer.append('\t'); }
      \\n                            { stringBuffer.append('\n'); }
      \\r                            { stringBuffer.append('\r'); }
      \\\"                           { stringBuffer.append('\"'); }
      \\                             { stringBuffer.append('\\'); }
}

/* No token was found for the input so throw an error.  Print out an
   Illegal character message with the illegal character that was found. */
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }
