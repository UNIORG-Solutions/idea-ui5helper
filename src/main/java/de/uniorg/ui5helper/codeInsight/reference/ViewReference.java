package de.uniorg.ui5helper.codeInsight.reference;

import com.intellij.psi.*;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.ui5.general.FileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ViewReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

    private final String fullViewName;
    private final String viewType;

    public ViewReference(@NotNull PsiElement element, String fullViewName, String viewType) {
        super(element);
        this.fullViewName = fullViewName;
        this.viewType = viewType;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        System.out.println(this.fullViewName);
        FileType ft = FileType.XML_VIEW;
        switch (this.viewType.toUpperCase()) {
            case "JSON":
                ft = FileType.JSON_VIEW;
                break;
            case "JAVASCRIPT":
            case "JS":
                ft = FileType.JS_VIEW;
                break;
            case "XML":
            default:
                ft = FileType.XML_VIEW;
        }
        PsiFile[] files = this.getElement().getProject().getComponent(ProjectComponent.class).getPathResolver().tryLookupFile(this.fullViewName, ft);

        return Arrays.stream(files).map(PsiElementResolveResult::new).toArray(ResolveResult[]::new);
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
