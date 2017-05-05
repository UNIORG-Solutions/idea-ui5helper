package de.uniorg.ui5helper.binding.psi;

import com.intellij.psi.tree.IElementType;
import de.uniorg.ui5helper.binding.lang.BindingLanguage;
import org.jetbrains.annotations.NotNull;

public class BindingTokenType extends IElementType {

    public static final BindingTokenType T_CURLY_OPEN = new BindingTokenType("T_CURLY_OPEN");
    public static final BindingTokenType T_CURLY_CLOSE = new BindingTokenType("T_CURLY_CLOSE");
    public static final BindingTokenType T_ROUND_OPEN = new BindingTokenType("T_ROUND_OPEN");
    public static final BindingTokenType T_ROUND_CLOSE = new BindingTokenType("T_ROUND_CLOSE");
    public static final BindingTokenType T_BRACKET_OPEN = new BindingTokenType("T_BRACKET_OPEN");
    public static final BindingTokenType T_BRACKET_CLOSE = new BindingTokenType("T_BRACKET_CLOSE");
    public static final BindingTokenType T_MODEL_SEP = new BindingTokenType("T_MODEL_SEP");
    public static final BindingTokenType T_PATH_SEP = new BindingTokenType("T_PATH_SEP");
    public static final BindingTokenType T_STRING = new BindingTokenType("T_STRING");
    public static final BindingTokenType T_QUOTED_STRING = new BindingTokenType("T_QUOTED_STRING");
    public static final BindingTokenType T_EXPRESSION_MARKER = new BindingTokenType("T_EXPRESSION_MARKER");  // =
    public static final BindingTokenType T_COLON = new BindingTokenType("T_COLON");  // =
    public static final BindingTokenType T_COMMA = new BindingTokenType("T_COMMA");  // =
    public static final BindingTokenType T_EMBEDDED_MARKER = new BindingTokenType("T_EMBEDDED_MARKER");  // =
    public static final BindingTokenType T_LOGIC_AND = new BindingTokenType("T_LOGIC_AND");  // =
    public static final BindingTokenType T_LOGIC_OR = new BindingTokenType("T_LOGIC_OR");  // =
    public static final BindingTokenType T_EQEQEQ = new BindingTokenType("T_EQEQEQ");  // =
    public static final BindingTokenType T_NEEQEQ = new BindingTokenType("T_NEEQEQ");  // =
    public static final BindingTokenType T_TRUE = new BindingTokenType("T_TRUE");  // =
    public static final BindingTokenType T_FALSE = new BindingTokenType("T_FALSE");  // =
    public static final BindingTokenType T_CMP_GTE = new BindingTokenType("T_CMP_GTE");  // =
    public static final BindingTokenType T_DOT = new BindingTokenType("T_DOT");  // .
    public static final BindingTokenType T_QUESTIONMARK = new BindingTokenType("T_QUESTIONMARK");  // ?
    public static final BindingTokenType T_NOT_OPERATOR = new BindingTokenType("T_NOT_OPERATOR");  // !

    public BindingTokenType(@NotNull String debugName) {
        super(debugName, BindingLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "BindingTokenType." + super.toString();
    }
}
