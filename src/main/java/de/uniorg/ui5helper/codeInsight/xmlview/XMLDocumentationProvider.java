package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlToken;
import de.uniorg.ui5helper.Features;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.ui5.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.uniorg.ui5helper.ProjectComponent.isEnabled;

public class XMLDocumentationProvider implements DocumentationProvider {

    private String getClassName(XmlTag tag) {
        return tag.getNamespace() + '.' + tag.getLocalName();
    }

    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement psiElement, PsiElement psiElement1) {
        if (!isEnabled(psiElement.getProject(), Features.XML_DOCUMENTATION)) {
            return null;
        }

        if (psiElement1 instanceof XmlTag) {
            return getClassName((XmlTag) psiElement1);
        }

        return null;
    }

    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement psiElement, PsiElement psiElement1) {
        if (!isEnabled(psiElement.getProject(), Features.XML_DOCUMENTATION)) {
            return null;
        }

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
        if (!isEnabled(psiElement.getProject(), Features.XML_DOCUMENTATION)) {
            return null;
        }

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
        ApiSymbol member = ResolverUtil.getMetadataMember(apiIndex, className, propertyName);

        if (member == null) {
            return "";
        }

        if (member instanceof EventDocumentation) {
            return this.renderEventDocumentation((EventDocumentation) member);
        }

        if (member instanceof PropertyDocumentation) {
            return this.renderPropertyDocumentation((PropertyDocumentation) member);
        }

        if (member instanceof AggregationDocumentation) {
            return this.renderAggregationDocumentation((AggregationDocumentation) member);
        }

        return member.getDescription();
    }

    private String getDeprecationInfo(DeprecateableInterface symbol) {
        return symbol.isDeprecated()
                ? String.format("<strong>Deprecated since %s:</strong> %s", symbol.getDeprecation().getSince(), symbol.getDeprecation().getMessage())
                : "";
    }

    private String renderAggregationDocumentation(AggregationDocumentation aggregationDocumentation) {
        return String.format(
                "<strong>%s%s %s</strong> (Aggregation)<br />" +
                        "<p>%s</p>",
                aggregationDocumentation.getType(),
                aggregationDocumentation.isMultiple() ? "[]" : "",
                aggregationDocumentation.getName(),
                aggregationDocumentation.getDescription()
        ) + this.getDeprecationInfo(aggregationDocumentation);
    }

    private String renderPropertyDocumentation(PropertyDocumentation propertyDocumentation) {
        return String.format(
                "<strong>%s %s</strong><br /><p>%s</p>",
                propertyDocumentation.getType(),
                propertyDocumentation.getName(),
                propertyDocumentation.getDescription()
        ) + this.getDeprecationInfo(propertyDocumentation);
    }

    private String renderEventDocumentation(EventDocumentation member) {
        return String.format(
                "<strong>%s Event: %s</strong><br />" +
                        "<p>%s</p>",
                member.getVisibility(),
                member.getName(),
                member.getDescription()
        ) + this.getDeprecationInfo(member);
    }

    private String getClassDoc(@NotNull Project project, @NotNull String className) {
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
