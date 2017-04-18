package de.uniorg.ui5helper.codeInsight;

import com.intellij.lang.javascript.psi.JSFunction;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import de.uniorg.ui5helper.ui.mvc.ControllerUtil;
import de.uniorg.ui5helper.ui.mvc.XmlViewUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class EventHandlerReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

    private XmlAttributeValue attrValue;

    public EventHandlerReference(@NotNull PsiElement element) {
        super(element);
        this.attrValue = (XmlAttributeValue) element;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        String handlerName = attrValue.getValue();
        String controllerName = XmlViewUtil.getControllerName(attrValue.getContainingFile());

        if (controllerName == null || handlerName == null) {
            return new ResolveResult[0];
        }

        PsiElement[] controllerReferences = ControllerUtil.findReferences(attrValue.getProject(), controllerName);

        Set<ResolveResult> targetReferences = new THashSet<>();
        for (PsiElement controllerReference : controllerReferences) {
            controllerReference.accept(new PsiRecursiveElementVisitor() {
                @Override
                public void visitElement(PsiElement element) {
                    if (element == null) {
                        return;
                    }

                    if (element instanceof JSFunction && handlerName.equals(((JSFunction) element).getName())) {
                        targetReferences.add(new PsiElementResolveResult(element));
                    } else {
                        super.visitElement(element);
                    }
                }
            });
        }


        return targetReferences.toArray(new ResolveResult[targetReferences.size()]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
