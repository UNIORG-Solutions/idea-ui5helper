package de.uniorg.ui5helper.docs;

import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlToken;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.ApiSymbol;
import de.uniorg.ui5helper.ui5.ClassDocumentation;
import de.uniorg.ui5helper.ui5.UI5Metadata;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masch on 4/5/17.
 */
public class XMLDocumentationProvider implements DocumentationProvider {

    private String getClassName(XmlTag tag) {
        return tag.getNamespace() + '.' + tag.getLocalName();
    }

    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement psiElement, PsiElement psiElement1) {
        if (psiElement1 instanceof XmlTag) {
            return getClassName((XmlTag) psiElement1);
        }

        return null;
    }

    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement psiElement, PsiElement psiElement1) {
        if (psiElement1 instanceof XmlTag) {
            List<String> list = new ArrayList<>();
            list.add("https://openui5.hana.ondemand.com/docs/api/symbols/" + getClassName((XmlTag) psiElement1) + ".html");

            return list;
        }

        if (psiElement1.getParent() instanceof XmlTag) {
            List<String> list = new ArrayList<>();
            list.add("https://openui5.hana.ondemand.com/docs/api/symbols/" + getClassName((XmlTag) psiElement1.getParent()) + ".html");

            return list;
        }

        return null;
    }

    @Nullable
    @Override
    public String generateDoc(PsiElement psiElement, @Nullable PsiElement psiElement1) {
        if (psiElement1 instanceof XmlToken) {
            psiElement1 = psiElement1.getParent();
        }

        if (psiElement1 instanceof XmlTag) {
            String className = getClassName((XmlTag) psiElement1);
            return getClassDoc(psiElement.getProject(), className);
        }

        if (psiElement1 instanceof XmlAttribute) {
            String className = getClassName((XmlTag) psiElement1.getParent());
            String propertyName = ((XmlAttribute) psiElement1).getLocalName();
            return getPropertyDoc(psiElement.getProject(), className, propertyName);
        }

        return null;
    }

    private String getPropertyDoc(Project project, String className, String propertyName) {
        ApiIndex apiIndex = project.getComponent(ProjectComponent.class).getApiIndex();
        ApiSymbol doc = apiIndex.lookup(className);
        if (doc == null || !(doc instanceof ClassDocumentation)) {
            System.out.println("doc for " + className + " is null");
            return null;
        }

        UI5Metadata metadata = ((ClassDocumentation) doc).getUI5Metadata();
        if (metadata == null) {
            return null;
        }

        if (!metadata.getProperties().containsKey(propertyName)) {
            return null;
        }

        return metadata.getProperties().get(propertyName).getDescription();
    }

    private String getClassDoc(Project project, String className) {
        ApiIndex apiIndex = project.getComponent(ProjectComponent.class).getApiIndex();
        ApiSymbol doc = apiIndex.lookup(className);
        if (doc == null) {
            System.out.println("doc for " + className + " is null");
            return null;
        }
        return doc.getDescription();
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object o, PsiElement psiElement) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String s, PsiElement psiElement) {
        return null;
    }
}
