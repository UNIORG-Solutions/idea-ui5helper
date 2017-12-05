package de.uniorg.ui5helper.codeInsight.reference;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndexImpl;
import de.uniorg.ui5helper.index.mvc.NaiveControllerIndexer;
import de.uniorg.ui5helper.ui.mvc.ControllerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ControllerReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

    private final String fullViewName;

    public ControllerReference(@NotNull PsiElement element, String controllerName) {
        super(element);
        this.fullViewName = controllerName;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {

        PsiElement psiElement = this.getElement();

        Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance().getContainingFiles(NaiveControllerIndexer.KEY, this.fullViewName, GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.everythingScope(psiElement.getProject()), JavaScriptFileType.INSTANCE));

        PsiManager manager = PsiManager.getInstance(this.getElement().getProject());
        return fileCollection.stream()
                .map(manager::findFile)
                .filter(Objects::nonNull)
                .map(targetFile -> {
                    Map<String, PsiElement> declarations = ControllerUtil.findDeclarations(targetFile);

                    PsiElement target = targetFile;
                    if (declarations.containsKey(this.fullViewName)) {
                        target = declarations.get(this.fullViewName);
                    }

                    return new PsiElementResolveResult(target);
                })
                .toArray(ResolveResult[]::new);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
