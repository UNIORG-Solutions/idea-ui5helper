package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
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
import de.uniorg.ui5helper.ui5.AggregationDocumentation;
import de.uniorg.ui5helper.ui5.ClassDocumentation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

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


                        Collection<LookupElement> elements = XMLViewHelper.getPossibleTags(file, (XmlTag) parentElement).stream()
                                .map(possibleTag -> {
                                    String nsprefix = !possibleTag.prefix.equals("") ? possibleTag.prefix + ":" : "";
                                    if (possibleTag.documentation instanceof AggregationDocumentation) {
                                        AggregationDocumentation aggregationDocumentation = (AggregationDocumentation) possibleTag.documentation;
                                        return LookupElementBuilder.create(aggregationDocumentation, possibleTag.prefix + possibleTag.name)
                                                .withPresentableText(possibleTag.name)
                                                .withTypeText(aggregationDocumentation.getType() + (aggregationDocumentation.isMultiple() ? "[]" : ""))
                                                .withIcon(AllIcons.General.Recursive)
                                                .withStrikeoutness(aggregationDocumentation.isDeprecated());
                                    } else if (possibleTag.documentation instanceof ClassDocumentation) {
                                        ClassDocumentation classDocumentation = (ClassDocumentation) possibleTag.documentation;
                                        return LookupElementBuilder.create(possibleTag.documentation, nsprefix + possibleTag.name)
                                                .withPresentableText(possibleTag.name)
                                                .withTypeText(possibleTag.namespace, true)
                                                .withStrikeoutness(classDocumentation.isDeprecated());
                                    }

                                    return null;
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());

                        completionResultSet.addAllElements(elements);
                    }
                });
    }
}
