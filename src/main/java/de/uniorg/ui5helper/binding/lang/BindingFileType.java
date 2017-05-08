package de.uniorg.ui5helper.binding.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import de.uniorg.ui5helper.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class BindingFileType extends LanguageFileType {

    public static final BindingFileType INSTANCE = new BindingFileType();

    protected BindingFileType() {
        super(BindingLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "binding";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "UI5 Binding file Type. Used for Testing";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return ".ui5-binding";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.OPENUI5;
    }
}
