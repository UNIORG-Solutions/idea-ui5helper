package de.uniorg.ui5helper.codeInsight.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.impl.CachingReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileSystemItemUtil;
import com.intellij.refactoring.rename.BindablePsiReference;
import com.intellij.util.IncorrectOperationException;
import de.uniorg.ui5helper.ProjectComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static de.uniorg.ui5helper.ui5.general.FileType.JAVASCRIPT;

public class ImportedFileReference extends PsiReferenceBase<PsiElement> implements PsiFileReference, BindablePsiReference {

    private final String filePath;

    public ImportedFileReference(@NotNull PsiElement element, String filePath) {
        super(element);
        this.filePath = filePath;
    }


    /**
     * Returns the element which is the target of the reference.
     *
     * @return the target element, or null if it was not possible to resolve the reference to a valid target.
     * @see PsiPolyVariantReference#multiResolve(boolean)
     */
    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);

        if (results.length >= 1) {
            return results[0].getElement();
        }

        return null;
    }

    protected PsiElement rename(final String newName) throws IncorrectOperationException {
        final TextRange range = this.getRangeInElement();
        PsiElement element = getElement();
        try {
            return CachingReference.getManipulator(element).handleContentChange(element, range, newName);
        } catch (IncorrectOperationException e) {
            throw e;
        }
    }

    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        //TODO: keep absolute paths absolute.
        String relPath = PsiFileSystemItemUtil.getRelativePath(
                this.getElement().getContainingFile(),
                element.getContainingFile()
        ).replace(".js", "");
        return rename(relPath);
    }

    /**
     * Returns the array of String, {@link PsiElement} and/or {@link LookupElement}
     * instances representing all identifiers that are visible at the location of the reference. The contents
     * of the returned array is used to build the lookup list for basic code completion. (The list
     * of visible identifiers may not be filtered by the completion prefix string - the
     * filtering is performed later by IDEA core.)
     *
     * @return the array of available identifiers.
     */
    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    /**
     * Returns the results of resolving the reference.
     *
     * @param incompleteCode if true, the code in the context of which the reference is
     *                       being resolved is considered incomplete, and the method may return additional
     *                       invalid results.
     * @return the array of results for resolving the reference.
     */
    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        if (isRelativePath()) {
            VirtualFile ref = this.getElement().getContainingFile().getVirtualFile().getParent().findFileByRelativePath(filePath + ".js");
            if (ref == null) {
                return ResolveResult.EMPTY_ARRAY;
            }

            PsiFile pref = PsiManager.getInstance(this.getElement().getProject()).findFile(ref);
            if (pref == null) {
                return ResolveResult.EMPTY_ARRAY;
            }
            return new PsiElementResolveResult[]{new PsiElementResolveResult(pref)};
        }

        PsiFile[] possibleFiles = this.getElement().getProject().getComponent(ProjectComponent.class).getPathResolver().tryLookupFile(filePath, JAVASCRIPT);
        if (possibleFiles.length == 0) {
            return ResolveResult.EMPTY_ARRAY;
        }

        return Arrays.stream(possibleFiles).map(PsiElementResolveResult::new).toArray(PsiElementResolveResult[]::new);
    }

    private boolean isRelativePath() {
        return this.filePath.startsWith(".");
    }
}
