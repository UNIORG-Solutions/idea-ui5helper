package de.uniorg.ui5helper.binding.lang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class BindingFile extends PsiFileBase {
    public BindingFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, BindingLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return BindingFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Binding File";
    }
}
