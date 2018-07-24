package de.uniorg.ui5helper.ui.mvc;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSFunction;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndexImpl;
import de.uniorg.ui5helper.index.mvc.NaiveControllerIndexer;
import gnu.trove.THashMap;
import gnu.trove.THashSet;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ControllerUtil {

    public static PsiElement[] findReferences(Project project, String controllerName) {
        if (controllerName == null) {
            return new PsiElement[0];
        }

        Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance()
                .getContainingFiles(
                        NaiveControllerIndexer.KEY,
                        controllerName,
                        GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.projectScope(project), JavaScriptFileType.INSTANCE)
                );

        return fileCollection.stream()
                .filter(vfile -> !ProjectRootManager.getInstance(project).getFileIndex().isExcluded(vfile))
                .filter(vfile -> PsiManager.getInstance(project).findFile(vfile) != null)
                .map(virtualFile -> {
                    PsiFile targetFile = PsiManager.getInstance(project).findFile(virtualFile);
                    Map<String, PsiElement> declarations = ControllerUtil.findDeclarations(targetFile);

                    PsiElement target = targetFile;
                    if (declarations.containsKey(controllerName)) {
                        target = declarations.get(controllerName);
                    }

                    return target;
                })
                .filter(Objects::nonNull)
                .toArray(PsiElement[]::new);
    }

    public static Set<String> getMethodNames(PsiElement controllerReference) {
        Set<String> targetReferences = new THashSet<>();
        controllerReference.accept(new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element == null) {
                    return;
                }

                if (element instanceof JSFunction) {
                    targetReferences.add(((JSFunction) element).getName());
                } else {
                    super.visitElement(element);
                }
            }
        });

        return targetReferences;
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
        if (arguments.length == 0) {
            return null;
        }

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
