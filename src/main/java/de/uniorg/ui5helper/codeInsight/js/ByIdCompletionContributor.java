package de.uniorg.ui5helper.codeInsight.js;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.lang.javascript.patterns.JSPatterns;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import com.intellij.util.indexing.FileBasedIndexImpl;
import com.intellij.xml.util.XmlPsiUtil;
import de.uniorg.ui5helper.codeInsight.xmlview.tags.ControlTag;
import de.uniorg.ui5helper.index.mvc.NaiveXmlViewIndexer;
import de.uniorg.ui5helper.ui.mvc.ControllerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class ByIdCompletionContributor extends CompletionContributor {
    public ByIdCompletionContributor() {
        extend(CompletionType.BASIC, psiElement().inside(JSPatterns.jsArgument("byId", 0)), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                PsiElement rootNode = parameters.getPosition();
                while (rootNode != null && !(rootNode instanceof PsiFile) && !ControllerUtil.isControllerDeclaration(rootNode)) {
                    rootNode = rootNode.getParent();
                }

                if (!ControllerUtil.isControllerDeclaration(rootNode)) {
                    return;
                }


                Project project = parameters.getPosition().getProject();
                String controllerName = ControllerUtil.getNameFromDeclaration((JSCallExpression) rootNode);
                assert controllerName != null;

                Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance().getContainingFiles(NaiveXmlViewIndexer.KEY, controllerName, GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(project), XmlFileType.INSTANCE));
                PsiManager psiManager = PsiManager.getInstance(project);
                List<PsiFile> targets = fileCollection.stream()
                        .map(psiManager::findFile)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());


                if (targets.isEmpty()) {
                    return;
                }

                Map<String, PsiElement> ids = new HashMap<>();
                PsiFile xmlView = targets.get(0);
                XmlPsiUtil.processXmlElements(((XmlFile) xmlView).getRootTag(), element -> {
                    if (element instanceof XmlTag) {
                        String id = ((XmlTag) element).getAttributeValue("id");
                        if (id != null) {
                            ids.put(id, element);
                        }
                    }

                    return true;
                }, true, true);


                List<LookupElement> elements = ids.entrySet()
                        .stream()
                        .filter(elementEntry -> elementEntry.getValue() instanceof XmlTag && ((XmlTag) elementEntry.getValue()).getDescriptor() instanceof ControlTag)
                        .map(elementEntry -> {
                            String lookupString = String.format("\"%s\"", elementEntry.getKey());
                            ControlTag element = (ControlTag) ((XmlTag) elementEntry.getValue()).getDescriptor();
                            return LookupElementBuilder
                                    .create(lookupString)
                                    .withPresentableText(elementEntry.getKey())
                                    .withPsiElement(elementEntry.getValue())
                                    .withTypeText(element.getName());
                        })
                        .collect(Collectors.toList());

                result.addAllElements(elements);
            }
        });
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        super.fillCompletionVariants(parameters, result);
    }
}
