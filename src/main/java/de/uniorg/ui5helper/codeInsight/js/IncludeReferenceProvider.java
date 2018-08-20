package de.uniorg.ui5helper.codeInsight.js;

import com.intellij.lang.javascript.psi.JSArgumentList;
import com.intellij.lang.javascript.psi.JSArrayLiteralExpression;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import de.uniorg.ui5helper.codeInsight.reference.ImportedFileReference;
import org.jetbrains.annotations.NotNull;

import static de.uniorg.ui5helper.Features.JS_FILE_IMPORT_REFERENCE;
import static de.uniorg.ui5helper.ProjectComponent.isEnabled;


public class IncludeReferenceProvider extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(JSLiteralExpression.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        if (!isEnabled(element.getProject(), JS_FILE_IMPORT_REFERENCE)) {
                            return PsiReference.EMPTY_ARRAY;
                        }

                        if (!(element.getParent() instanceof JSArrayLiteralExpression) || !(element.getParent().getParent() instanceof JSArgumentList)) {
                            return PsiReference.EMPTY_ARRAY;
                        }

                        JSArgumentList argumentList = (JSArgumentList) element.getParent().getParent();

                        if (!(argumentList.getParent() instanceof JSCallExpression)) {
                            return PsiReference.EMPTY_ARRAY;
                        }

                        JSCallExpression call = (JSCallExpression) argumentList.getParent();

                        if (!call.getMethodExpression().getText().equals("sap.ui.define")
                                && !call.getMethodExpression().getText().equals("sap.ui.require")) {
                            return PsiReference.EMPTY_ARRAY;
                        }

                        String filePath = (String) ((JSLiteralExpression) element).getValue();

                        return new PsiReference[]{new ImportedFileReference(element, filePath)};
                    }
                }
        );
    }
}
