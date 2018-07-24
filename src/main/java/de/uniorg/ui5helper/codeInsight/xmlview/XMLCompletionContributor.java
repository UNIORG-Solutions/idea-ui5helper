package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
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

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

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
                        if (file.getRootTag() == null) {
                            return;
                        }

                        Map<String, String> namespaces = file.getRootTag().getLocalNamespaceDeclarations();

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
                        boolean hasNoFixedAggregation = true;
                        //... tag name starts with a lower-case-character.
                        if (parentTag.getLocalName().startsWith(parentTag.getLocalName().substring(0, 1).toLowerCase())) {
                            // das könnte eine Aggregation sein
                            hasNoFixedAggregation = false;
                            XmlElementDescriptor descriptor = parentTag.getParentTag().getDescriptor();
                            if (descriptor instanceof ControlTag) {
                                ControlTag parentControlDescriptor = (ControlTag) descriptor;
                                ApiSymbol property = ResolverUtil.getMetadataMember(apiIndex, parentControlDescriptor.getName(), parentTag.getName());

                                if (property instanceof AggregationDocumentation) {
                                    aggregation = (AggregationDocumentation) property;
                                }
                            }
                        } else {
                            // default aggregation?
                            XmlElementDescriptor descriptor = parentTag.getDescriptor();
                            if (descriptor instanceof ControlTag) {
                                ControlTag parentControlDescriptor = (ControlTag) descriptor;
                                aggregation = resolver.getDefaultAggregations(parentControlDescriptor.getClassDocumentation());
                            }
                        }

                        if (hasNoFixedAggregation && parentTag.getDescriptor() instanceof ControlTag) {
                            ControlTag parentControlDescriptor = (ControlTag) parentTag.getDescriptor();
                            resolver.getAllAggregations(parentControlDescriptor.getClassDocumentation()).forEach((name, aggregationDocumentation) -> {
                                String lookup = aggregationDocumentation.getName();
                                String nsprefix = !Objects.equals(parentTag.getNamespacePrefix(), "") ? parentTag.getNamespacePrefix() + ":" : "";
                                completionResultSet.addElement(
                                        LookupElementBuilder.create(aggregationDocumentation, nsprefix + lookup)
                                                .withPresentableText(lookup)
                                                .withTypeText(aggregationDocumentation.getType() + (aggregationDocumentation.isMultiple() ? "[]" : ""))
                                                .withIcon(AllIcons.General.Recursive)
                                                .withStrikeoutness(aggregationDocumentation.isDeprecated())
                                );
                            });
                        }

                        for (Map.Entry<String, String> namespace : namespaces.entrySet()) {
                            ClassDocumentation[] symbols = Arrays.stream(apiIndex.findInNamespace(namespace.getValue()))
                                    .filter(apiSymbol -> apiSymbol instanceof ClassDocumentation)
                                    .toArray(ClassDocumentation[]::new);
                            for (ClassDocumentation symbol : symbols) {
                                String name = symbol.getName().replace(namespace.getValue() + ".", "");
                                if (name.contains(".")) { // "<core:layout.form.SimpleForm" does not work
                                    continue;
                                }

                                if (aggregation != null && aggregation.getType() != null && !resolver.isInstanceOf(symbol, aggregation.getType())) {
                                    continue;
                                }

                                String lookup = symbol.getName().replace(namespace.getValue() + ".", namespace.getKey().length() == 0 ? "" : namespace.getKey() + ":");
                                completionResultSet.addElement(
                                        LookupElementBuilder.create(symbol, lookup)
                                                .withPresentableText(name)
                                                .withTypeText(namespace.getValue(), true)
                                                .withStrikeoutness(symbol.isDeprecated())
                                );
                            }
                        }
                    }
                });
    }
}
