# Compilers Assignment - Expression Handling and Program Structure

## Part 1: Grammar for Expression Handling

The grammar used for handling the expressions of the language in part 1 is as follows:

```
expr ::= term expr2
expr2 ::= + term expr2 | - term expr2 | ε
term ::= factor term2
term2 ::= ** factor term2 | ε
factor ::= num | (expr)
num ::= 0 | digit_without_0 num2
num2 ::= digit num2 | ε
digit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
digit_without_0 ::= 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
```

### Program Execution and Usage

The provided folder includes CalcEvaluator, Main, and ParseError files. To compile and execute the program, use the commands javac Main.java and java Main. The program awaits user input and the input can be terminated with either \n\n or EOF.

## Part 2: Grammar for Language Expression Handling

The grammar used for handling the expressions of the language in part 2 is outlined as follows:

```
program ::= declarations main
declarations ::= declaration declarations | ε
declaration ::= DECLARATION_FUNCTION_START expression }
main ::= expression main | ε
expression ::= if_statement | concatenation | function_call | IDENTIFIER | STRING_LITERAL
if_statement ::= IF condition_function ) expression ELSE expression
condition_function ::= expression condition_function_tail
condition_function_tail ::= SUFFIX expression | PREFIX expression
concatenation ::= expression PLUS expression
function_call ::= FUNCTION_START args )
args ::= expression args_tail | ε
args_tail ::= COMMA expression args_tail | ε
```

Two terminal symbols - tokens DECLARATION_FUNCTION_START and FUNCTION_START are used for distinguishing simple function calls from function declarations. The primary distinction is the presence of a brace {. Lexical ambiguity issues are resolved by creating two different symbols in the Lexer using regular expressions.

The precedence of these tokens is defined as follows:

precedence(if) < precedence(prefix/suffix) < precedence(concat) < precedence(function_call).

### Program Execution and Usage
The provided folder contains a makefile identical to the one provided in the laboratory material for compilation and execution of all files. The program reads input from the provided input.txt file. The output, a generated Java file, is stored in the output/ directory where the user can navigate and compile and run the program with the javac Main.java and java Main commands respectively.

## License

This project is for educational use only and is part of the coursework for _Κ31 Compilers_ at _DiT, NKUA_.
