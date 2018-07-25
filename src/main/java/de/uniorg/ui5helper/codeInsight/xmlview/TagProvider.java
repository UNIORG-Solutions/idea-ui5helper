package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.indexing.FileBasedIndexImpl;
import com.intellij.xml.XmlElementDescriptor;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.codeInsight.xmlview.tags.ControlTag;
import de.uniorg.ui5helper.framework.JSTreeUtil;
import de.uniorg.ui5helper.index.JavascriptClassIndexer;
import de.uniorg.ui5helper.ui.mvc.XmlViewUtil;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.ApiSymbol;
import de.uniorg.ui5helper.ui5.ClassDocumentation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static de.uniorg.ui5helper.ProjectComponent.isEnabled;

public class TagProvider implements XmlElementDescriptorProvider {
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

        PsiElement decl = this.getDeclaration(classDoc, context.getProject());

        return new ControlTag(classDoc, context, decl);
    }

    private PsiElement getDeclaration(ClassDocumentation classDoc, Project project) {
        Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance().getContainingFiles(JavascriptClassIndexer.KEY, classDoc.getName(), GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(project), JavaScriptFileType.INSTANCE));

        PsiManager psiManager = PsiManager.getInstance(project);

        List<JSCallExpression> classDefinitions = fileCollection.stream()
                .map(psiManager::findFile)
                .filter(Objects::nonNull)
                .filter(psiFile -> psiFile.getFileType().equals(JavaScriptFileType.INSTANCE))
                .map(JSTreeUtil::getDefineCall)
                .filter(Objects::nonNull)
                .map(JSTreeUtil::findClassDeclaration)
                .collect(Collectors.toList());

        if (!classDefinitions.isEmpty()) {
            return classDefinitions.get(0);
        }

        return null;
    }

    private ClassDocumentation getClassDoc(@NotNull XmlTag tag) {
        ApiIndex index = this.getApiIndex(tag.getProject());
        if (index == null) {
            System.err.println("Cannot find a apiIndex for project " + tag.getProject().getName());
            return null;
        }
        ApiSymbol tagDocs = index.lookup(tag.getNamespace(), tag.getLocalName());
        if (tagDocs == null || !(tagDocs instanceof ClassDocumentation)) {
            return null;
        }

        return (ClassDocumentation) tagDocs;
    }

    private ApiIndex getApiIndex(Project project) {
        return project.getComponent(ProjectComponent.class).getApiIndex();
    }
}
