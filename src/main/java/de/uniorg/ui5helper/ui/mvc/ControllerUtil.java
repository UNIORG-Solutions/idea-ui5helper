package de.uniorg.ui5helper.ui.mvc;

import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import gnu.trove.THashMap;

import java.util.Map;

public class ControllerUtil {
    public static Map<String, PsiElement> findDeclarations(PsiElement root) {
        PsiElement[] calls = PsiTreeUtil.collectElements(root,
                (PsiElement psiElement) -> {
                    if (!(psiElement instanceof JSCallExpression)) {
                        return false;
                    }
                    JSExpression methodExpression = ((JSCallExpression) psiElement).getMethodExpression();
                    if (methodExpression.getText().contains("extend") && !methodExpression.getText().contains("jQuery")) {
                        return true;
                    }

                    if (methodExpression.getText().contains("sap.ui.controller")) {
                        return true;
                    }

                    return false;
                }
        );


        Map<String, PsiElement> declarations = new THashMap<>();

        for (PsiElement call : calls) {
            if (!(call instanceof JSCallExpression)) {
                continue;
            }

            JSExpression[] arguments = ((JSCallExpression) call).getArguments();

            if (arguments[0] instanceof JSLiteralExpression) {
                JSLiteralExpression classNameExpr = (JSLiteralExpression) arguments[0];
                if (classNameExpr.isQuotedLiteral()) {
                    String className = (String) classNameExpr.getValue();
                    declarations.put(className, call);
                }
            }
        }

        return declarations;
    }
}
