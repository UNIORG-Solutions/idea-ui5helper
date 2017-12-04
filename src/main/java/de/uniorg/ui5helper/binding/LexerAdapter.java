package de.uniorg.ui5helper.binding;

import com.intellij.lexer.FlexAdapter;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;

public class LexerAdapter extends FlexAdapter {
    public LexerAdapter(@Nullable Reader reader) {
        super(new BindingLexer(reader));
    }

    @Override
    public IElementType getTokenType() {
        try {
            return super.getTokenType();
        } catch (java.lang.Error error) {
            error.printStackTrace();
        }

        return null;
    }
}
