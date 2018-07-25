package de.uniorg.ui5helper.framework;

import com.intellij.lang.javascript.psi.*;
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

        if (foundExpr.isEmpty()) {
            return null;
        }

        return foundExpr.get(0);
    }

    public static JSCallExpression findClassDeclaration(JSCallExpression defineCall) {
        if (defineCall == null || defineCall.getArguments().length < 2 || !(defineCall.getArguments()[1] instanceof JSFunctionExpression)) {
            return null;
        }

        JSFunctionExpression factory = (JSFunctionExpression) defineCall.getArguments()[1];
        if (factory == null) {
            return null;
        }

        return findClassDeclaration(factory.getBody());
    }

    public static JSCallExpression findClassDeclaration(JSSourceElement[] element) {
        JSReturnStatement ret = findReturn(element);

        if (ret == null) {
            return null;
        }

        if (ret.getExpression() instanceof JSCallExpression && ((JSCallExpression) ret.getExpression()).getMethodExpression().getText().endsWith("extend")) {
            JSCallExpression callExpression = (JSCallExpression) ret.getExpression();
            if (callExpression.getMethodExpression().getText().endsWith("extend")) {
                return callExpression;
            }
        } else if (ret.getExpression() instanceof JSReferenceExpression) {
            JSCallExpression callExpression = findDefinitionOfVariable((JSReferenceExpression) ret.getExpression());
            if (callExpression == null) {
                return null;
            }

            if (callExpression.getMethodExpression().getText().endsWith("extend")) {
                return callExpression;
            }
        }

        return null;
    }

    private static JSCallExpression findDefinitionOfVariable(JSReferenceExpression variable) {
        PsiElement ctx = variable.getContext();
        while (ctx instanceof JSElement) {
            final JSCallExpression[] result = {null};
            ctx.accept(new JSRecursiveElementVisitor() {
                @Override
                public void visitJSVariable(JSVariable node) {
                    PsiElement[] varChildren = node.getChildren();
                    if (varChildren.length >= 1 && result[0] == null && isExtendCall(varChildren[0])) {
                        result[0] = (JSCallExpression) varChildren[0];
                    }
                }
            });

            if (result[0] != null) {
                return result[0];
            }
            ctx = ctx.getContext();
        }

        return null;
    }

    private static boolean isExtendCall(PsiElement expression) {
        return expression instanceof JSCallExpression && ((JSCallExpression) expression).getMethodExpression().getText().endsWith("extend");
    }

    private static JSReturnStatement findReturn(JSSourceElement[] statements) {
        for (JSSourceElement jsSourceElement : statements) {
            if (jsSourceElement instanceof JSBlockStatement) {
                JSReturnStatement tmp = findReturn(((JSBlockStatement) jsSourceElement).getStatements());
                if (tmp != null) {
                    return tmp;
                }
            }
            if (jsSourceElement instanceof JSReturnStatement) {
                return (JSReturnStatement) jsSourceElement;
            }
        }

        return null;
    }

    private static JSReturnStatement findReturn(JSStatement[] statements) {
        for (JSStatement jsSourceElement : statements) {
            if (jsSourceElement instanceof JSBlockStatement) {
                JSReturnStatement tmp = findReturn(((JSBlockStatement) jsSourceElement).getStatements());
                if (tmp != null) {
                    return tmp;
                }
            }
            if (jsSourceElement instanceof JSReturnStatement) {
                return (JSReturnStatement) jsSourceElement;
            }
        }

        return null;
    }
}
