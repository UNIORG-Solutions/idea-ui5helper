package de.uniorg.ui5helper.codeInsight.reference;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndexImpl;
import de.uniorg.ui5helper.framework.JSTreeUtil;
import de.uniorg.ui5helper.index.mvc.NaiveControllerIndexer;
import de.uniorg.ui5helper.ui.mvc.ControllerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ControllerReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

    private final String fullControllerName;

    public ControllerReference(@NotNull PsiElement element, String controllerName) {
        super(element);
        this.fullControllerName = controllerName;
    }


    private static Integer calcPathProp(String path) {
        int prop = 0;

        // files in src/ and/or webapp/ should be fine
        if (path.contains("src")) {
            prop += 1;
        }
        if (path.contains("webapp")) {
            prop += 1;
        }

        // files in dist/ and/or node_modules/ should be used only if they there are no files in src etc
        if (path.contains("dist") || path.contains("build")) {
            prop -= 2;
        }
        if (path.contains("node_modules")) {
            prop -= 2;
        }

        // prefer dbg files over minified alternatives
        if (path.contains("-dbg.js")) {
            prop += 1;
        }

        return prop;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {

        PsiElement psiElement = this.getElement();

        Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance().getContainingFiles(NaiveControllerIndexer.KEY, this.fullControllerName, GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.everythingScope(psiElement.getProject()), JavaScriptFileType.INSTANCE));

        PsiManager manager = PsiManager.getInstance(this.getElement().getProject());
        return fileCollection.stream()
                .map(manager::findFile)
                .filter(Objects::nonNull)
                .sorted((file1, file2) -> {
                    String path1 = file1.getVirtualFile().getPath();
                    String path2 = file2.getVirtualFile().getPath();

                    return calcPathProp(path2).compareTo(calcPathProp(path1));

                })
                .map(targetFile -> {

                    JSCallExpression expr = JSTreeUtil.getDefineCall(targetFile);
                    if (expr == null) {
                        return null;
                    }

                    PsiElement target = JSTreeUtil.findClassDeclaration(expr);

                    if (target != null) {
                        return new PsiElementResolveResult(target);
                    }

                    Map<String, PsiElement> declarations = ControllerUtil.findDeclarations(targetFile);
                    target = targetFile;
                    if (declarations.containsKey(this.fullControllerName)) {
                        target = declarations.get(this.fullControllerName);
                    }

                    return new PsiElementResolveResult(target);
                })
                .filter(Objects::nonNull)
                .toArray(ResolveResult[]::new);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length >= 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
