package de.uniorg.ui5helper.binding;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import de.uniorg.ui5helper.binding.psi.BindingTokenType;
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
        return BindingTokenType.T_CURLY_OPEN;
     }

     {NOT_OPEN}+     { yybegin(YYINITIAL); return BindingTokenType.T_STRING; }
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
            return BindingTokenType.T_MODEL_SEP;
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
            return BindingTokenType.T_PATH_SEP;
        }

        return TokenType.BAD_CHARACTER;
    }

    ":" {
       if (afterFirstIdentifier) {
           contexts.pop();
           contexts.push(ContextType.COMPLEX);
           yybegin(IN_COMPLEX);
           afterFirstIdentifier = false;
           return BindingTokenType.T_COLON;
       }
    }

    {PATH_START}{PATH_CHAR}* {
        yybegin(IN_CONTEXT);
        if (beforeFirstIdentifier && !afterFirstIdentifier) {
            beforeFirstIdentifier = false;
            afterFirstIdentifier = true;
            return BindingTokenType.T_STRING;
        }

        return TokenType.BAD_CHARACTER;
    }
}

<IN_PATH> {
    "/"                      { return BindingTokenType.T_PATH_SEP; }
    {PATH_START}{PATH_CHAR}* { return BindingTokenType.T_STRING; }
}


<IN_CONTEXT, IN_EXPRESSION, IN_PATH, IN_COMPLEX> {
    {WHITE_SPACE}+           { return TokenType.WHITE_SPACE; }
    "}"                      { returnToContext(); return BindingTokenType.T_CURLY_CLOSE; }
}

<IN_EXPRESSION, IN_COMPLEX> {
    "true"                   { return BindingTokenType.T_TRUE; }
    "false"                  { return BindingTokenType.T_FALSE; }
    "["                      { return BindingTokenType.T_BRACKET_OPEN;}
    "]"                      { return BindingTokenType.T_BRACKET_CLOSE;}
}

<IN_COMPLEX> {
    ":"                      { return BindingTokenType.T_COLON; }
    ","                      { return BindingTokenType.T_COMMA; }
    {QUOTED_STRING}          { return BindingTokenType.T_QUOTED_STRING; }
    {IDENTIFIER_CHAR}+       { yybegin(IN_COMPLEX); return BindingTokenType.T_STRING; }
}

<IN_EXPRESSION> {
    "$"                      { yybegin(NEXT_IS_EMBEDDED); return BindingTokenType.T_EMBEDDED_MARKER; }
    "&amp;&amp;"             { return BindingTokenType.T_LOGIC_AND; }
    "||"                     { return BindingTokenType.T_LOGIC_OR; }
    "==="                    { return BindingTokenType.T_EQEQEQ; }
    "!=="                    { return BindingTokenType.T_NEEQEQ; }
    "."                      { return BindingTokenType.T_DOT;}
    "("                      { return BindingTokenType.T_ROUND_OPEN;}
    ")"                      { return BindingTokenType.T_ROUND_CLOSE;}

    {IDENTIFIER_CHAR}+       { return BindingTokenType.T_STRING; }
}

<NEXT_IS_EMBEDDED> {
    "{"                      {
        yybegin(IN_CONTEXT);
        contexts.push(ContextType.UNKNOWN);
        beforeFirstIdentifier = true;
        afterFirstIdentifier = false;
        return BindingTokenType.T_CURLY_OPEN;
     }
    .                       { yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }
}