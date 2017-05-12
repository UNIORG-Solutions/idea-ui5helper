package de.uniorg.ui5helper.codeInsight.binding;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import de.uniorg.ui5helper.binding.psi.EmbeddedBinding;
import de.uniorg.ui5helper.binding.psi.ExprExpression;
import de.uniorg.ui5helper.binding.psi.ExprValue;
import de.uniorg.ui5helper.binding.psi.ExpressionBinding;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimplifyBindingAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
        if (!(psiElement instanceof ExpressionBinding)) {
            return;
        }

        ExpressionBinding binding = (ExpressionBinding) psiElement;

        List<ExprExpression> expressions = binding.getExprExpressionList();
        if (expressions.size() != 1) {
            return;
        }

        ExprExpression expr = expressions.get(0);

        if (expr.getFirstChild() instanceof ExprValue && expr.getFirstChild().getFirstChild() instanceof EmbeddedBinding) {
            annotationHolder.createWeakWarningAnnotation(psiElement.getTextRange(), "Binding can be simplified.");
        }
    }
}
