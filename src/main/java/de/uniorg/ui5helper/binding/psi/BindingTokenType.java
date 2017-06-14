package de.uniorg.ui5helper.binding.psi;

import com.intellij.psi.tree.IElementType;
import de.uniorg.ui5helper.binding.lang.BindingLanguage;
import org.jetbrains.annotations.NotNull;

public class BindingTokenType extends IElementType {

    public BindingTokenType(@NotNull String debugName) {
        super(debugName, BindingLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "BindingTokenType." + super.toString();
    }
}
