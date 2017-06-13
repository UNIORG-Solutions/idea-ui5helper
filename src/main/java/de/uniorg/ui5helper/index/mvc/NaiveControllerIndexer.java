package de.uniorg.ui5helper.index.mvc;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.JSFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.indexing.*;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import de.uniorg.ui5helper.ui.mvc.ControllerUtil;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class NaiveControllerIndexer extends ScalarIndexExtension<String> {

    public static final ID<String, Void> KEY = ID.create("de.uniorg.ui5helper.cache.index.mvc.controller");
    private final KeyDescriptor<String> myKeyDescriptor = new EnumeratorStringDescriptor();

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return (file) -> file.getFileType() instanceof JavaScriptFileType && file.getName().contains(".controller.");
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    @NotNull
    @Override
    public ID<String, Void> getName() {
        return KEY;
    }

    @NotNull
    @Override
    public DataIndexer<String, Void, FileContent> getIndexer() {
        return inputData -> {
            Map<String, Void> map = new THashMap<>();
            PsiFile file;
            try {
                file = inputData.getPsiFile();
            } catch (java.lang.IllegalStateException ex) {
                return map;
            }

            if (!(file instanceof JSFile)) {
                return map;
            }

            Map<String, PsiElement> declarations = ControllerUtil.findDeclarations(file);

            declarations.forEach((key, value) -> map.put(key, null));

            return map;
        };
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return this.myKeyDescriptor;
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
