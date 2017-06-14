package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.openapi.project.Project;
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlElementDescriptor;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.codeInsight.xmlview.tags.ControlTag;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.ApiSymbol;
import de.uniorg.ui5helper.ui5.ClassDocumentation;
import org.jetbrains.annotations.Nullable;

public class TagProvider implements XmlElementDescriptorProvider {
    @Nullable
    @Override
    public XmlElementDescriptor getDescriptor(XmlTag context) {
        if (context == null || context.getContainingFile() == null) {
            return null;
        }

        final String fileName = context.getContainingFile().getName();
        if (!fileName.endsWith("view.xml") && !fileName.endsWith("fragment.xml")) {
            return null;
        }
        ClassDocumentation classDoc = this.getClassDoc(context);
        if (classDoc == null) {
            return null;
        }

        return new ControlTag(classDoc, context);
    }


    private ClassDocumentation getClassDoc(XmlTag tag) {
        ApiSymbol tagDocs = this.getApiIndex(tag.getProject()).lookup(tag.getNamespace(), tag.getLocalName());
        if (tagDocs == null || !(tagDocs instanceof ClassDocumentation)) {
            return null;
        }

        return (ClassDocumentation) tagDocs;
    }

    private ApiIndex getApiIndex(Project project) {
        return project.getComponent(ProjectComponent.class).getApiIndex();
    }
}
