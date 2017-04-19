package de.uniorg.ui5helper.ui.mvc;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndexImpl;
import de.uniorg.ui5helper.index.mvc.NaiveControllerIndexer;
import gnu.trove.THashMap;
import gnu.trove.THashSet;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ControllerUtil {

    public static PsiElement[] findReferences(Project project, String controllerName) {
        Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance()
                .getContainingFiles(
                        NaiveControllerIndexer.KEY,
                        controllerName,
                        GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(project), JavaScriptFileType.INSTANCE)
                );
        Set<PsiElement> results = new THashSet<>();
        fileCollection.iterator().forEachRemaining(
                virtualFile -> {
                    PsiFile targetFile = PsiManager.getInstance(project).findFile(virtualFile);
                    if (targetFile == null) {
                        return;
                    }

                    Map<String, PsiElement> declarations = ControllerUtil.findDeclarations(targetFile);

                    PsiElement target = targetFile;
                    if (declarations.containsKey(controllerName)) {
                        target = declarations.get(controllerName);
                    }

                    results.add(target);
                }
        );

        return results.toArray(new PsiElement[results.size()]);
    }

    public static boolean isControllerDeclaration(PsiElement element) {
        if (!(element instanceof JSCallExpression)) {
            return false;
        }
        JSExpression methodExpression = ((JSCallExpression) element).getMethodExpression();
        if (methodExpression == null) {
            return false;
        }

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
