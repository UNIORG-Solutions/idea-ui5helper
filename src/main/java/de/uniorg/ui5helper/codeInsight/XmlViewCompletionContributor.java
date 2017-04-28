package de.uniorg.ui5helper.codeInsight;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.ui.mvc.ControllerUtil;
import de.uniorg.ui5helper.ui.mvc.XmlViewUtil;
import de.uniorg.ui5helper.ui5.*;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class XmlViewCompletionContributor extends CompletionContributor {
    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        final String fileName = parameters.getOriginalFile().getVirtualFile().getName();
        if (!fileName.endsWith("view.xml") && !fileName.endsWith("fragment.xml")) {
            return;
        }

        final PsiElement position = parameters.getPosition();

        if (position.getParent() instanceof XmlAttribute) {
            final XmlAttribute attribute = (XmlAttribute) position.getParent();
            final XmlTag tag = attribute.getParent();
            final ClassDocumentation classDoc = getClassDoc(tag);
            if (classDoc == null) {
                return;
            }

            this.addMetadataToResultSet(classDoc.getUI5Metadata(), result, name -> tag.getAttribute(name) != null);
        }
        if (position.getParent() instanceof XmlAttributeValue) {
            final XmlAttributeValue value = (XmlAttributeValue) position.getParent();
            final XmlAttribute attribute = (XmlAttribute) value.getParent();
            final XmlTag tag = attribute.getParent();
            final ClassDocumentation classDoc = getClassDoc(tag);
            if (classDoc == null) {
                return;
            }

            final Map<String, EventDocumentation> events = classDoc.getUI5Metadata().getEvents();
            if (events.containsKey(attribute.getLocalName()) && XmlViewUtil.isXmlView(position.getContainingFile())) {
                String controllerName = XmlViewUtil.getControllerName(position.getContainingFile());
                PsiElement[] controllers = ControllerUtil.findReferences(position.getProject(), controllerName);
                Set<String> methodNames = new THashSet<>();
                for (PsiElement controller : controllers) {
                    methodNames.addAll(ControllerUtil.getMethodNames(controller));
                }

                methodNames.forEach(name -> result.addElement(LookupElementBuilder.create(name)));
            }
        }

    }

    private ClassDocumentation getClassDoc(XmlTag tag) {
        ApiSymbol tagDocs = this.getApiIndex(tag.getProject()).lookup(tag.getNamespace(), tag.getName());
        if (tagDocs == null || !(tagDocs instanceof ClassDocumentation)) {
            return null;
        }

        return (ClassDocumentation) tagDocs;
    }

    private void addMetadataToResultSet(@NotNull UI5Metadata metadata, @NotNull CompletionResultSet result, Predicate<String> exists) {
        Set<String> names = new THashSet<>();
        names.addAll(metadata.getProperties().keySet());
        names.addAll(metadata.getEvents().keySet());
        names.addAll(metadata.getAggregations().keySet());

        names.forEach(name -> {
            if (!exists.test(name)) {
                result.addElement(LookupElementBuilder.create(name));
            }
        });
    }

    private ApiIndex getApiIndex(Project project) {
        return project.getComponent(ProjectComponent.class).getApiIndex();
    }
}
