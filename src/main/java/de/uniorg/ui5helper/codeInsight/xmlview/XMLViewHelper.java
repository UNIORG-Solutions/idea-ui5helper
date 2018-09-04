package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.SmartList;
import com.intellij.xml.XmlElementDescriptor;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.codeInsight.xmlview.tags.ControlTag;
import de.uniorg.ui5helper.ui5.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

class XMLViewHelper {
    static Collection<PossibleTag> getPossibleTags(@NotNull XmlFile file, @NotNull XmlTag tag) {
        if (file.getRootTag() == null) {
            return Collections.emptyList();
        }

        final Map<String, String> namespaces = file.getRootTag().getLocalNamespaceDeclarations();
        List<PossibleTag> tags = new SmartList<>();

        final ApiIndex apiIndex = file.getProject().getComponent(ProjectComponent.class).getApiIndex();
        if (apiIndex == null) {
            return Collections.emptyList();
        }

        final ResolverUtil resolver = ResolverUtil.getInstanceOf(apiIndex);

        AggregationDocumentation aggregation = null;
        boolean hasNoFixedAggregation = true;
        //... tag name starts with a lower-case-character.
        if (tag.getLocalName().startsWith(tag.getLocalName().substring(0, 1).toLowerCase()) && tag.getParentTag() != null) {
            // das könnte eine Aggregation sein
            hasNoFixedAggregation = false;
            XmlElementDescriptor descriptor = tag.getParentTag().getDescriptor();
            if (descriptor instanceof ControlTag) {
                ControlTag parentControlDescriptor = (ControlTag) descriptor;
                ApiSymbol property = ResolverUtil.getMetadataMember(apiIndex, parentControlDescriptor.getName(), tag.getName());

                if (property instanceof AggregationDocumentation) {
                    aggregation = (AggregationDocumentation) property;
                }
            }
        } else {
            // default aggregation?
            XmlElementDescriptor descriptor = tag.getDescriptor();
            if (descriptor instanceof ControlTag) {
                ControlTag parentControlDescriptor = (ControlTag) descriptor;
                aggregation = resolver.getDefaultAggregations(parentControlDescriptor.getClassDocumentation());
            }
        }

        if (hasNoFixedAggregation && tag.getDescriptor() instanceof ControlTag) {
            ControlTag parentControlDescriptor = (ControlTag) tag.getDescriptor();
            tags.addAll(getPossibleAggregations(tag, resolver, parentControlDescriptor));
        }

        tags.addAll(getPossibleChildTags(namespaces, apiIndex, resolver, aggregation));

        return tags;
    }

    private static Collection<PossibleTag> getPossibleAggregations(@NotNull XmlTag tag, @NotNull ResolverUtil resolver, @NotNull ControlTag parentControlDescriptor) {
        return resolver.getAllAggregations(parentControlDescriptor.getClassDocumentation()).values().stream()
                .map(aggregationDocumentation -> {
                    String nsprefix = !Objects.equals(tag.getNamespacePrefix(), "") ? tag.getNamespacePrefix() : "";
                    PossibleTag ptag = new PossibleTag();
                    ptag.name = aggregationDocumentation.getName();
                    ptag.prefix = nsprefix;
                    ptag.documentation = aggregationDocumentation;
                    ptag.namespace = tag.getNamespace();
                    return ptag;
                }).collect(Collectors.toSet());
    }

    private static Collection<PossibleTag> getPossibleChildTags(@NotNull Map<String, String> namespaces, @NotNull ApiIndex apiIndex, @NotNull ResolverUtil resolver, AggregationDocumentation aggregation) {
        List<PossibleTag> tags = new SmartList<>();
        for (Map.Entry<String, String> namespace : namespaces.entrySet()) {
            apiIndex.findDirectInNamespace(namespace.getValue(), ClassDocumentation.class)
                    .filter(apiSymbol -> isInAggregation(resolver, aggregation, apiSymbol))
                    .map(apiSymbol -> PossibleTag.fromClassDocumentation(apiSymbol, namespace.getValue(), namespace.getKey()))
                    .filter(Objects::nonNull)
                    .forEach(tags::add);

        }
        return tags;
    }

    private static boolean isInAggregation(ResolverUtil resolver, AggregationDocumentation aggregation, ClassDocumentation classDoc) {
        return aggregation == null || aggregation.getType() == null || resolver.isInstanceOf(classDoc, aggregation.getType());
    }

    static class PossibleTag {
        String namespace;
        String prefix;
        String name;
        ApiSymbol documentation;

        static PossibleTag fromClassDocumentation(ClassDocumentation classDoc, String namespace, String namespacePrefix) {
            String[] nameParts = classDoc.getName().split("\\.");
            String className = nameParts[nameParts.length - 1];

            PossibleTag tag = new PossibleTag();
            tag.namespace = namespace;
            tag.name = className;
            tag.prefix = namespacePrefix;
            tag.documentation = classDoc;

            return tag;
        }
    }
}
