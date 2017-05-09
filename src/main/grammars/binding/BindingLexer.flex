package de.uniorg.ui5helper.binding;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import de.uniorg.ui5helper.binding.psi.BindingTokenType;
import de.uniorg.ui5helper.binding.psi.BindingTypes;
import java.util.Stack;
%%

%class BindingLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%{
    private enum ContextType {
        UNKNOWN, SIMPLE, COMPLEX, EXPRESSION
    }
    private boolean beforeFirstIdentifier = false;
    private boolean afterFirstIdentifier = false;
    private Stack<ContextType> contexts = new Stack<>();

    private void returnToContext() {
        contexts.pop();
        if (contexts.empty()) {
            yybegin(YYINITIAL);
            return;
        }

        switch(contexts.peek()) {
            case UNKNOWN:
                yybegin(IN_CONTEXT);
                break;
            case SIMPLE:
                yybegin(IN_PATH);
                break;
            case COMPLEX:
                yybegin(IN_COMPLEX);
                break;
            case EXPRESSION:
                yybegin(IN_EXPRESSION);
                break;
        }
    }
%}
%eof{ return;
%eof}

HEX_DIGIT = [a-fA-F0-9]
SINGLE_QUOTE = \x27
COMMON_ESCAPE = ( [nrt0\n\r\\] | "x" {HEX_DIGIT} {2} | "u" {HEX_DIGIT} {4} | "U" {HEX_DIGIT} {8} )
QUOTED_STRING = {SINGLE_QUOTE} ( [^\'\\] | "\\" ( {SINGLE_QUOTE} | {COMMON_ESCAPE}) )* {SINGLE_QUOTE}
WHITE_SPACE = [\ \t\n\r]
PATH_START = [a-zA-Z0-9_.]
PATH_CHAR = [a-zA-Z0-9_.\ -]
IDENTIFIER_CHAR = [a-zA-Z0-9_]
NOT_OPEN = [^{]

EXPONENT      = [eE] [-+]? [0-9_]+

FLT_LITERAL   = ( {DEC_LITERAL} \. {DEC_LITERAL} {EXPONENT}? )
              | ( {DEC_LITERAL} {EXPONENT} )
              | ( {DEC_LITERAL} )

FLT_LITERAL_TDOT = {DEC_LITERAL} \.

INT_LITERAL = ( {DEC_LITERAL}
              | {HEX_LITERAL}
              | {OCT_LITERAL}
              | {BIN_LITERAL} )

DEC_LITERAL = [0-9] [0-9_]*
HEX_LITERAL = "0x" [a-fA-F0-9_]*
OCT_LITERAL = "0o" [0-7_]*
BIN_LITERAL = "0b" [01_]*

%state IN_CONTEXT
%state IN_EXPRESSION
%state IN_PATH
%state NEXT_IS_EMBEDDED
%state IN_COMPLEX

%%

<YYINITIAL> {
    {WHITE_SPACE}+   { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

    "{" {
        yybegin(IN_CONTEXT);
        contexts.push(ContextType.UNKNOWN);
        beforeFirstIdentifier = true;
        afterFirstIdentifier = false;
        return BindingTypes.CURLY_OPEN;
     }

     {NOT_OPEN}+     { yybegin(YYINITIAL); return BindingTypes.STRING; }
}

<IN_CONTEXT> {
    "=" {
        if (contexts.peek() == ContextType.UNKNOWN) {
            contexts.pop();
            contexts.push(ContextType.EXPRESSION);
            yybegin(IN_EXPRESSION);
            return BindingTokenType.T_EXPRESSION_MARKER;
        }

        return TokenType.BAD_CHARACTER;
    }

    ">" {
        if (afterFirstIdentifier) {
            contexts.pop();
            contexts.push(ContextType.SIMPLE);
            yybegin(IN_PATH);
            afterFirstIdentifier = false;
            return BindingTypes.MODEL_SEP;
        }

        return TokenType.BAD_CHARACTER;
    }

    "/" {
        if (beforeFirstIdentifier || afterFirstIdentifier) {
            beforeFirstIdentifier = false;
            afterFirstIdentifier = false;
            contexts.pop();
            contexts.push(ContextType.SIMPLE);
            yybegin(IN_PATH);
            return BindingTypes.PATH_SEP;
        }

        return TokenType.BAD_CHARACTER;
    }

    ":" {
       if (afterFirstIdentifier) {
           contexts.pop();
           contexts.push(ContextType.COMPLEX);
           yybegin(IN_COMPLEX);
           afterFirstIdentifier = false;
           return BindingTypes.COLON;
       }
    }

    {PATH_START}{PATH_CHAR}* {
        yybegin(IN_CONTEXT);
        if (beforeFirstIdentifier && !afterFirstIdentifier) {
            beforeFirstIdentifier = false;
            afterFirstIdentifier = true;
            return BindingTypes.STRING;
        }

        return TokenType.BAD_CHARACTER;
    }
}
<IN_CONTEXT, IN_EXPRESSION, IN_PATH, IN_COMPLEX> {
    {WHITE_SPACE}+           { return TokenType.WHITE_SPACE; }
    "}"                      { returnToContext(); return BindingTypes.CURLY_CLOSE; }
}

<IN_PATH> {
    "/"                      { return BindingTypes.PATH_SEP; }
    {PATH_START}{PATH_CHAR}* { return BindingTypes.STRING; }
    .                        { return TokenType.BAD_CHARACTER; }
}

<IN_EXPRESSION, IN_COMPLEX> {
    "true"                   { return BindingTypes.TRUE; }
    "false"                  { return BindingTypes.FALSE; }
    "["                      { return BindingTypes.BRACKET_OPEN;}
    "]"                      { return BindingTypes.BRACKET_CLOSE;}
    ":"                      { return BindingTypes.COLON; }
    {FLT_LITERAL}            { return BindingTypes.NUMBER; }
    {INT_LITERAL}            { return BindingTypes.NUMBER; }
    {QUOTED_STRING}          { return BindingTypes.QUOTED_STRING; }
}

<IN_COMPLEX> {
    "{"                      { contexts.push(ContextType.COMPLEX); return BindingTypes.CURLY_OPEN; }
    ","                      { return BindingTypes.COMMA; }
    {IDENTIFIER_CHAR}+       { return BindingTypes.STRING; }
    .                       { return TokenType.BAD_CHARACTER; }
}

<IN_EXPRESSION> {
    "$"                      { yybegin(NEXT_IS_EMBEDDED); return BindingTokenType.T_EMBEDDED_MARKER; }
    "&amp;&amp;"             { return BindingTokenType.T_LOGIC_AND; }
    "||"                     { return BindingTokenType.T_LOGIC_OR; }
    "==="                    { return BindingTokenType.T_EQEQEQ; }
    "!=="                    { return BindingTokenType.T_NEEQEQ; }
    "."                      { return BindingTokenType.T_DOT;}
    "?"                      { return BindingTokenType.T_QUESTIONMARK;}
    "!"                      { return BindingTokenType.T_NOT_OPERATOR;}
    "("                      { return BindingTokenType.T_ROUND_OPEN;}
    ")"                      { return BindingTokenType.T_ROUND_CLOSE;}

    {IDENTIFIER_CHAR}+       { return BindingTypes.STRING; }
    .                       { return TokenType.BAD_CHARACTER; }
}

<NEXT_IS_EMBEDDED> {
    "{"                      {
        yybegin(IN_CONTEXT);
        contexts.push(ContextType.UNKNOWN);
        beforeFirstIdentifier = true;
        afterFirstIdentifier = false;
        return BindingTypes.CURLY_OPEN;
     }
    .                       { yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }
}