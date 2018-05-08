package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlAttributeDescriptorsProvider;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.codeInsight.xmlview.attributes.PropertyAttributeDescriptor;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.ApiSymbol;
import de.uniorg.ui5helper.ui5.ClassDocumentation;
import org.jetbrains.annotations.Nullable;

public class AttributesProvider implements XmlAttributeDescriptorsProvider {

    @Override
    public XmlAttributeDescriptor[] getAttributeDescriptors(XmlTag context) {
        if (context.getContainingFile() == null || context.getContainingFile().getVirtualFile() == null) {
            return XmlAttributeDescriptor.EMPTY;
        }
        final String fileName = context.getContainingFile().getVirtualFile().getName();
        if (!fileName.endsWith("view.xml") && !fileName.endsWith("fragment.xml")) {
            return XmlAttributeDescriptor.EMPTY;
        }
        ClassDocumentation classDoc = this.getClassDoc(context);
        if (classDoc == null) {
            return XmlAttributeDescriptor.EMPTY;
        }

        final ApiIndex apiIndex = this.getApiIndex(context.getProject());
        return classDoc.getUI5Metadata()
                .getProperties()
                .values().stream()
                .map(propertyDocumentation -> new PropertyAttributeDescriptor(propertyDocumentation, apiIndex, context))
                .toArray(XmlAttributeDescriptor[]::new);
    }

    @Nullable
    @Override
    public XmlAttributeDescriptor getAttributeDescriptor(String attributeName, XmlTag context) {
        return null;
    }


    private ClassDocumentation getClassDoc(XmlTag tag) {
        ApiSymbol tagDocs = this.getApiIndex(tag.getProject()).lookup(tag.getNamespace(), tag.getName());
        if (tagDocs == null || !(tagDocs instanceof ClassDocumentation)) {
            return null;
        }

        return (ClassDocumentation) tagDocs;
    }

    private ApiIndex getApiIndex(Project project) {
        return project.getComponent(ProjectComponent.class).getApiIndex();
    }

}
