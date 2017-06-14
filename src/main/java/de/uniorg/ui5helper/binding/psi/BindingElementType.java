package de.uniorg.ui5helper.binding.psi;

import com.intellij.psi.tree.IElementType;
import de.uniorg.ui5helper.binding.lang.BindingLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class BindingElementType extends IElementType {
    public BindingElementType(@NotNull @NonNls String debugName) {
        super(debugName, BindingLanguage.INSTANCE);
    }
}
