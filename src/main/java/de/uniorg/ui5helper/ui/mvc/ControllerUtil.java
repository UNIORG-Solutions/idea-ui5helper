package de.uniorg.ui5helper.ui.mvc;

import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import gnu.trove.THashMap;

import java.util.Map;

public class ControllerUtil {

    public static boolean isControllerDeclaration(PsiElement element) {
        if (!(element instanceof JSCallExpression)) {
            return false;
        }
        JSExpression methodExpression = ((JSCallExpression) element).getMethodExpression();
        if (methodExpression.getText().contains("extend") && !methodExpression.getText().contains("jQuery")) {
            return true;
        }

        return methodExpression.getText().contains("sap.ui.controller");
    }

    public static String getNameFromDeclaration(JSCallExpression call) {
        JSExpression[] arguments = call.getArguments();

        if (arguments[0] instanceof JSLiteralExpression) {
            JSLiteralExpression classNameExpr = (JSLiteralExpression) arguments[0];
            if (classNameExpr.isQuotedLiteral()) {
                return (String) classNameExpr.getValue();
            }
        }

        return null;
    }

    public static Map<String, PsiElement> findDeclarations(PsiElement root) {
        PsiElement[] calls = PsiTreeUtil.collectElements(root, ControllerUtil::isControllerDeclaration);


        Map<String, PsiElement> declarations = new THashMap<>();

        for (PsiElement call : calls) {
            if (!(call instanceof JSCallExpression)) {
                continue;
            }

            String className = getNameFromDeclaration((JSCallExpression) call);
            if (className != null) {
                declarations.put(className, call);
            }
        }

        return declarations;
    }
}
