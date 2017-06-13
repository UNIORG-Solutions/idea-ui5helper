package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.ApiSymbol;
import de.uniorg.ui5helper.ui5.ResolverUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AttributeAnnotator implements Annotator {

    private static final String[] ATTR_WHITELIST = new String[]{"id", "class", "controllerName"};

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
        String fileName = psiElement.getContainingFile().getVirtualFile().getName();
        if (psiElement instanceof XmlAttribute && (fileName.endsWith(".view.xml") || fileName.endsWith(".fragment.xml"))) {
            if (Arrays.asList(AttributeAnnotator.ATTR_WHITELIST).contains(((XmlAttribute) psiElement).getName())) {
                return;
            }
            XmlAttribute attr = (XmlAttribute) psiElement;
            XmlTag tag = attr.getParent();
            String className = tag.getNamespace() + '.' + tag.getLocalName();
            ApiIndex apiIndex = psiElement.getProject().getComponent(ProjectComponent.class).getApiIndex();
            ApiSymbol member = ResolverUtil.getMetadataMember(apiIndex, className, attr.getName());
            if (member == null) {
                annotationHolder.createErrorAnnotation(psiElement, "Unknown attribute: " + attr.getName());
            }
        }
    }
}
