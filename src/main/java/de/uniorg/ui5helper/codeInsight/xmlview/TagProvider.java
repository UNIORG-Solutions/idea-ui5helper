package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlElementDescriptor;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.codeInsight.xmlview.tags.ControlTag;
import de.uniorg.ui5helper.index.JavascriptClassIndexer;
import de.uniorg.ui5helper.ui.mvc.XmlViewUtil;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.ApiSymbol;
import de.uniorg.ui5helper.ui5.ClassDocumentation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.uniorg.ui5helper.ProjectComponent.isEnabled;

public class TagProvider implements XmlElementDescriptorProvider {
    private static final Logger logger = Logger.getInstance("ui5helper");

    @Nullable
    @Override
    public XmlElementDescriptor getDescriptor(XmlTag context) {
        if (context == null || !isEnabled(context.getProject())) {
            return null;
        }

        if (context.getContainingFile() == null) {
            return null;
        }

        if (!XmlViewUtil.isXmlFragment(context.getContainingFile()) && !XmlViewUtil.isXmlView(context.getContainingFile())) {
            return null;
        }

        ClassDocumentation classDoc = this.getClassDoc(context);
        if (classDoc == null) {
            return null;
        }

        PsiElement decl = JavascriptClassIndexer.lookupDeclaration(context.getProject(), classDoc);

        return new ControlTag(classDoc, context, decl);
    }

    private ClassDocumentation getClassDoc(@NotNull XmlTag tag) {
        ApiIndex index = this.getApiIndex(tag.getProject());
        if (index == null) {
            logger.warn("Cannot find a apiIndex for project " + tag.getProject().getName());
            return null;
        }

        ApiSymbol tagDocs = index.lookup(tag.getNamespace(), tag.getLocalName());
        if (!(tagDocs instanceof ClassDocumentation)) {
            return null;
        }

        return (ClassDocumentation) tagDocs;
    }

    private ApiIndex getApiIndex(Project project) {
        return project.getComponent(ProjectComponent.class).getApiIndex();
    }
}
