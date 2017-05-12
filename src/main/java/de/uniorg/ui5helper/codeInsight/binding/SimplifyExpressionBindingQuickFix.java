package de.uniorg.ui5helper.codeInsight.binding;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.IncorrectOperationException;
import de.uniorg.ui5helper.binding.psi.ExprExpression;
import de.uniorg.ui5helper.binding.psi.ExpressionBinding;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class SimplifyExpressionBindingQuickFix extends PsiElementBaseIntentionAction {
    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "Binding";
    }

    @NotNull
    @Override
    public String getText() {
        return "Replace expression binding with first embedded binding.";
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        ExpressionBinding binding = findExpressionBinding(psiElement);

        ExprExpression exp = binding.getExprExpressionList().get(0);
        if (exp.getExprValue() == null || exp.getExprValue().getEmbeddedBinding() == null) {
            return;
        }

        PsiElement innerBinding = exp.getExprValue().getEmbeddedBinding().getLastChild();
        if (innerBinding != null) {
            binding.replace(innerBinding);
        }
    }

    private ExpressionBinding findExpressionBinding(PsiElement element) throws IndexOutOfBoundsException {
        PsiElement node = element;
        while (node != null && !(node instanceof XmlAttributeValue) && !(node instanceof PsiFile)) {
            if (node instanceof ExpressionBinding) {
                return (ExpressionBinding) node;
            }
            node = node.getParent();
        }

        throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        try {
            return (findExpressionBinding(psiElement) != null);
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }
}
