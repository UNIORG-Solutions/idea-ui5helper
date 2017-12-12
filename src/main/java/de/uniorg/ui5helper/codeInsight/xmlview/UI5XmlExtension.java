package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.SchemaPrefix;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.XmlExtension;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.codeInsight.xmlview.tags.ControlTag;
import de.uniorg.ui5helper.ui.mvc.XmlViewUtil;
import de.uniorg.ui5helper.ui5.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UI5XmlExtension extends XmlExtension {
    @Override
    public boolean isAvailable(PsiFile psiFile) {
        return XmlViewUtil.isXmlView(psiFile) || XmlViewUtil.isXmlFragment(psiFile);
    }

    @NotNull
    @Override
    public List<TagInfo> getAvailableTagNames(@NotNull XmlFile xmlFile, @NotNull XmlTag xmlTag) {
        List<TagInfo> tags = new ArrayList<>();
        if (xmlTag.getParentTag() == null || xmlFile.getRootTag() == null) {
            return tags;
        }

        ApiIndex apiIndex = xmlFile.getProject().getComponent(ProjectComponent.class).getApiIndex();
        ResolverUtil resolver = ResolverUtil.getInstanceOf(apiIndex);
        Map<String, String> localNamespaces = xmlFile.getRootTag().getLocalNamespaceDeclarations();

        AggregationDocumentation aggregationDocumentation = this.getParentAggregation(xmlTag.getParentTag());
        if (aggregationDocumentation != null) {
            System.out.println("aggType: " + aggregationDocumentation.getType());
        }

        for (String nsname : localNamespaces.keySet()) {
            String namespace = localNamespaces.get(nsname);
            ApiSymbol[] symbols = apiIndex.findInNamespace(namespace);
            for (ApiSymbol symbol : symbols) {
                if (!(symbol instanceof ClassDocumentation)) {
                    continue;
                }
                if (aggregationDocumentation != null && aggregationDocumentation.getType() != null && !resolver.isInstanceOf((ClassDocumentation) symbol, aggregationDocumentation.getType())) {
                    continue;
                }

                tags.add(new TagInfo(symbol.getName().replace(namespace + ".", ""), namespace));
            }
        }

        return tags;
    }

    @Nullable
    @Override
    public SchemaPrefix getPrefixDeclaration(XmlTag xmlTag, String s) {
        return null;
    }

    private AggregationDocumentation getParentAggregation(XmlTag parentTag) {
        ApiIndex apiIndex = parentTag.getProject().getComponent(ProjectComponent.class).getApiIndex();
        //... tag name starts with a lower-case-character.
        if (parentTag.getLocalName().startsWith(parentTag.getLocalName().substring(0, 1).toLowerCase())) {
            // das könnte eine Aggregation sein
            XmlElementDescriptor descriptor = parentTag.getParentTag().getDescriptor();
            if (descriptor instanceof ControlTag) {
                ControlTag parentControlDescriptor = (ControlTag) descriptor;
                ApiSymbol property = ResolverUtil.getMetadataMember(apiIndex, parentControlDescriptor.getQualifiedName(), parentTag.getLocalName());

                if (property instanceof AggregationDocumentation) {
                    System.out.println("aggregation: " + parentTag.getLocalName());
                    return (AggregationDocumentation) property;
                }
            }
        } else {
            // default aggregation?
            XmlElementDescriptor descriptor = parentTag.getDescriptor();
            if (descriptor instanceof ControlTag) {
                ControlTag parentControlDescriptor = (ControlTag) descriptor;
                System.out.println("default aggregation of " + parentControlDescriptor.getQualifiedName());
                return parentControlDescriptor.getClassDocumentation().getUI5Metadata().getDefaultAggregation();
            }
        }

        return null;
    }
}
