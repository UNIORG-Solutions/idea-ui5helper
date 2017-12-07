package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlTokenImpl;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import com.intellij.xml.XmlElementDescriptor;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.codeInsight.xmlview.tags.ControlTag;
import de.uniorg.ui5helper.ui5.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class XMLCompletionContributor extends CompletionContributor {

    public XMLCompletionContributor() {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withParent(XmlPatterns.xmlTag()),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
                        if (!(completionParameters.getOriginalFile() instanceof XmlFile)) {
                            return;
                        }

                        XmlFile file = (XmlFile) completionParameters.getOriginalFile();

                        Map<String, String> namspeaces = file.getRootTag().getLocalNamespaceDeclarations();

                        PsiElement element = completionParameters.getPosition();
                        if (!(element instanceof XmlTokenImpl)) {
                            return;
                        }

                        if (!((XmlTokenImpl) element).getTokenType().equals(XmlTokenType.XML_NAME)) {
                            return;
                        }

                        XmlTokenImpl nameToken = ((XmlTokenImpl) element);

                        PsiElement parentElement = nameToken.getParent().getParent();

                        if (!(parentElement instanceof XmlTag)) {
                            return;
                        }

                        ApiIndex apiIndex = completionParameters.getOriginalFile().getProject().getComponent(ProjectComponent.class).getApiIndex();
                        ResolverUtil resolver = ResolverUtil.getInstanceOf(apiIndex);
                        XmlTag parentTag = (XmlTag) parentElement;

                        AggregationDocumentation aggregation = null;
                        //... tag name starts with a lower-case-character.
                        if (parentTag.getLocalName().startsWith(parentTag.getLocalName().substring(0, 1).toLowerCase())) {
                            // das könnte eine Aggregation sein
                            XmlElementDescriptor descriptor = parentTag.getParentTag().getDescriptor();
                            if (descriptor instanceof ControlTag) {
                                ControlTag parentControlDescriptor = (ControlTag) descriptor;
                                ApiSymbol property = ResolverUtil.getMetadataMember(apiIndex, parentControlDescriptor.getQualifiedName(), parentTag.getName());

                                if (property instanceof AggregationDocumentation) {
                                    aggregation = (AggregationDocumentation) property;
                                }
                            }
                        } else {
                            // default aggregation?
                            XmlElementDescriptor descriptor = parentTag.getDescriptor();
                            if (descriptor instanceof ControlTag) {
                                ControlTag parentControlDescriptor = (ControlTag) descriptor;
                                aggregation = parentControlDescriptor.getClassDocumentation().getUI5Metadata().getDefaultAggregation();
                            }
                        }

                        for (String nsname : namspeaces.keySet()) {
                            ApiSymbol[] symbols = apiIndex.findInNamespace(namspeaces.get(nsname));
                            for (ApiSymbol symbol : symbols) {
                                if (!(symbol instanceof ClassDocumentation)) {
                                    continue;
                                }
                                if (aggregation != null && aggregation.getType() != null && !resolver.isInstanceOf((ClassDocumentation) symbol, aggregation.getType())) {
                                    continue;
                                }
                                String name = symbol.getName().replace(namspeaces.get(nsname) + ".", nsname.length() == 0 ? "" : nsname + ":");
                                completionResultSet.addElement(LookupElementBuilder.create(name));
                            }
                        }
                    }
                });
    }
}
