package de.uniorg.ui5helper.codeInsight.js;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSNewExpression;
import com.intellij.lang.javascript.psi.JSReferenceExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.codeInsight.intention.ImportClassIntention;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.ApiSymbol;
import de.uniorg.ui5helper.ui5.ModuleApiSymbol;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ProblemAnnotator implements Annotator {
    /**
     * Annotates the specified PSI element.
     * It is guaranteed to be executed in non-reentrant fashion.
     * I.e there will be no call of this method for this instance before previous call get completed.
     * Multiple instances of the annotator might exist simultaneously, though.
     *
     * @param element to annotate.
     * @param holder  the container which receives annotations created by the plugin.
     */
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof JSCallExpression) {
            this.annotateCallExpression((JSCallExpression) element, holder);
        }
    }

    private void annotateCallExpression(JSCallExpression callExpression, AnnotationHolder holder) {
        if (callExpression instanceof JSNewExpression && callExpression.getMethodExpression() instanceof JSReferenceExpression) {
            JSReferenceExpression method = (JSReferenceExpression) callExpression.getMethodExpression();
            ResolveResult[] ref = method.multiResolve(true);
            if (Arrays.stream(ref).anyMatch(ResolveResult::isValidResult)) {
                return;
            }

            ApiIndex index = callExpression.getProject().getComponent(ProjectComponent.class).getApiIndex();
            ApiSymbol[] found;
            try {
                found = index.findClass(method.getText());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
            for (ApiSymbol apiSymbol : found) {
                if (!(apiSymbol instanceof ModuleApiSymbol)) {
                    continue;
                }
                ModuleApiSymbol classDoc = (ModuleApiSymbol) apiSymbol;
                holder.createErrorAnnotation(method, "unresolved class").registerFix(new ImportClassIntention(method.getText(), classDoc));
            }

        }
    }
}
