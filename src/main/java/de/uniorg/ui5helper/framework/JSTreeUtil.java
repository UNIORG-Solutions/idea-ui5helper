package de.uniorg.ui5helper.framework;

import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JSTreeUtil {
    @Nullable
    public static JSCallExpression getDefineCall(@NotNull PsiFile psiFile) {
        final List<JSCallExpression> foundExpr = new SmartList<>();
        psiFile.acceptChildren(new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (!(element instanceof JSCallExpression)) {
                    super.visitElement(element);
                    return;
                }

                JSCallExpression expr = (JSCallExpression) element;

                if (expr.getMethodExpression().getText().equals("sap.ui.define")) {
                    foundExpr.add(expr);
                    return;
                }

                super.visitElement(element);
            }
        });

        if (foundExpr.size() == 0) {
            return null;
        }

        return foundExpr.get(0);
    }
}
