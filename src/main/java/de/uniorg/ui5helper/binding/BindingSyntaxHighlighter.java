package de.uniorg.ui5helper.binding;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import de.uniorg.ui5helper.binding.psi.BindingTypes;
import org.jetbrains.annotations.NotNull;

public class BindingSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey OPERATORS = TextAttributesKey.createTextAttributesKey("EXP_BINDING_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey("STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey KEY = TextAttributesKey.createTextAttributesKey("KEY", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey PATH = TextAttributesKey.createTextAttributesKey("PATH", DefaultLanguageHighlighterColors.PARAMETER);
    public static final TextAttributesKey COMMA = TextAttributesKey.createTextAttributesKey("COMMA", DefaultLanguageHighlighterColors.COMMA);

    private static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[]{OPERATORS};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEY};
    private static final TextAttributesKey[] PATH_KEYS = new TextAttributesKey[]{PATH};
    private static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{COMMA};

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new LexerAdapter(null);
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType iElementType) {

        if (isOperator(iElementType)) {
            return OPERATOR_KEYS;
        }

        if (iElementType.equals(BindingTypes.COMPLEX_BINDING_KEY)) {
            return KEYWORD_KEYS;
        }

        if (iElementType.equals(BindingTypes.QUOTED_STRING)) {
            return STRING_KEYS;
        }

        if (iElementType.equals(BindingTypes.PATH_SEGMENT) || iElementType.equals(BindingTypes.PATH_SEP)) {
            return PATH_KEYS;
        }

        if (iElementType.equals(BindingTypes.COMMA)) {
            return COMMA_KEYS;
        }

        return new TextAttributesKey[0];
    }

    private boolean isOperator(IElementType elementType) {
        final IElementType[] operatorTypes = new IElementType[]{
                BindingTypes.EQEQEQ,
                BindingTypes.NEEQEQ,
                BindingTypes.NOT_OPERATOR,
                BindingTypes.LOGIC_AND,
                BindingTypes.LOGIC_OR
        };

        for (IElementType operatorType : operatorTypes) {
            if (elementType.equals(operatorType)) {
                return true;
            }
        }

        return false;
    }
}
