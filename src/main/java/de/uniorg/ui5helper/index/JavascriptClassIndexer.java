package de.uniorg.ui5helper.index;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.*;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import de.uniorg.ui5helper.framework.JSTreeUtil;
import de.uniorg.ui5helper.ui5.ClassDocumentation;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class JavascriptClassIndexer extends ScalarIndexExtension<String> {

    public static final ID<String, Void> KEY = ID.create("ui5.helper.js.classes.index");
    private final KeyDescriptor<String> myKeyDescriptor = new EnumeratorStringDescriptor();

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    @NotNull
    @Override
    public ID<String, Void> getName() {
        return KEY;
    }

    public static PsiElement lookupDeclaration(@NotNull Project project, @NotNull String className) {
        Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance().getContainingFiles(JavascriptClassIndexer.KEY, className, GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.everythingScope(project), JavaScriptFileType.INSTANCE));

        PsiManager psiManager = PsiManager.getInstance(project);

        List<JSCallExpression> classDefinitions = fileCollection.stream()
                .map(psiManager::findFile)
                .filter(Objects::nonNull)
                .filter(psiFile -> psiFile.getFileType().equals(JavaScriptFileType.INSTANCE))
                .map(JSTreeUtil::getDefineCall)
                .filter(Objects::nonNull)
                .map(JSTreeUtil::findClassDeclaration)
                .collect(Collectors.toList());

        if (!classDefinitions.isEmpty()) {
            return classDefinitions.get(0);
        }

        return null;
    }

    public static PsiElement lookupDeclaration(@NotNull Project project, @NotNull ClassDocumentation classDoc) {
        return lookupDeclaration(project, classDoc.getName());
    }

    private String getClassName(JSCallExpression extendCall) {
        if (extendCall.getArguments().length == 0) {
            return null;
        }

        JSExpression firstArgument = extendCall.getArguments()[0];
        if (!(firstArgument instanceof JSLiteralExpression)) {
            return null;
        }

        if (((JSLiteralExpression) firstArgument).getValue() instanceof String) {
            return (String) ((JSLiteralExpression) firstArgument).getValue();
        }

        return null;
    }

    @NotNull
    @Override
    public DataIndexer<String, Void, FileContent> getIndexer() {
        return inputData -> {
            Map<String, Void> map = new THashMap<>();
            JSCallExpression defineCall = JSTreeUtil.getDefineCall(inputData.getPsiFile());
            if (defineCall == null) {
                return map;
            }

            JSCallExpression classDecl = JSTreeUtil.findClassDeclaration(defineCall);
            if (classDecl != null) {
                String className = getClassName(classDecl);
                if (className == null) {
                    return map;
                }
                map.put(className, null);
            }

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
        return 3;
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return file -> file.getFileType() instanceof JavaScriptFileType;
    }

}
