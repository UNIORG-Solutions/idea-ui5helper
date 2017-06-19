package de.uniorg.ui5helper.codeInsight.xmlview.tags;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.XmlElementsGroup;
import com.intellij.xml.XmlNSDescriptor;
import com.intellij.xml.impl.schema.AnyXmlAttributeDescriptor;
import com.intellij.xml.impl.schema.AnyXmlElementDescriptor;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.codeInsight.xmlview.attributes.AggregationAttributeDescriptor;
import de.uniorg.ui5helper.codeInsight.xmlview.attributes.EventAttributeDescriptor;
import de.uniorg.ui5helper.codeInsight.xmlview.attributes.PropertyAttributeDescriptor;
import de.uniorg.ui5helper.ui5.*;
import gnu.trove.THashMap;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ControlTag implements XmlElementDescriptor {

    private final ClassDocumentation classDocumentation;
    private XmlTag self;
    private Map<String, XmlAttributeDescriptor> _attributeMap;

    public ControlTag(ClassDocumentation classDocumentation, XmlTag self) {
        this.classDocumentation = classDocumentation;
        this.self = self;
    }

    @Override
    public String getQualifiedName() {
        return classDocumentation.getModuleName() + "." + classDocumentation.getName();
    }

    /**
     * Should return either simple or qualified name depending on the schema/DTD properties.
     * This name should be used in XML documents.
     *
     * @return either simple or qualified name.
     */
    @Override
    public String getDefaultName() {
        return classDocumentation.getName();
    }

    /**
     * Returns an array of child tag descriptors.
     *
     * @param context the parent tag.
     * @return an array of child tag descriptors, or empty array if no child tag allowed.
     */
    @Override
    public XmlElementDescriptor[] getElementsDescriptors(XmlTag context) {
        return new XmlElementDescriptor[]{new AnyXmlElementDescriptor(this, null)};
    }

    @Nullable
    @Override
    public XmlElementDescriptor getElementDescriptor(XmlTag childTag, XmlTag contextTag) {
        return new AnyXmlElementDescriptor(this, null);
    }

    @Override
    public XmlAttributeDescriptor[] getAttributesDescriptors(@Nullable XmlTag context) {
        return this.getAttributeMap().values().toArray(new XmlAttributeDescriptor[0]);
    }

    @Nullable
    @Override
    public XmlAttributeDescriptor getAttributeDescriptor(String attributeName, @Nullable XmlTag context) {
        Map<String, XmlAttributeDescriptor> map = this.getAttributeMap();

        if (map.containsKey(attributeName)) {
            return map.get(attributeName);
        }

        return null;
    }

    @Nullable
    @Override
    public XmlAttributeDescriptor getAttributeDescriptor(XmlAttribute attribute) {
        return this.getAttributeDescriptor(attribute.getName(), attribute.getParent());
    }

    @Nullable
    @Override
    public XmlNSDescriptor getNSDescriptor() {
        return null;
    }

    @Nullable
    @Override
    public XmlElementsGroup getTopGroup() {
        return null;
    }

    @Override
    public int getContentType() {
        return 0;
    }

    @Nullable
    @Override
    public String getDefaultValue() {
        return null;
    }

    @Override
    public PsiElement getDeclaration() {
        return self;
    }

    @Override
    public String getName(PsiElement context) {
        return null;
    }

    @Override
    public String getName() {
        return classDocumentation.getName();
    }

    @Override
    public void init(PsiElement element) {

    }

    /**
     * @return objects this meta data depends on.
     * @see CachedValue
     */
    @Override
    public Object[] getDependences() {
        return new Object[0];
    }

    private ApiIndex getApiIndex(Project project) {
        return project.getComponent(ProjectComponent.class).getApiIndex();
    }

    private Map<String, XmlAttributeDescriptor> getAttributeMap() {
        final ApiIndex apiIndex = this.getApiIndex(self.getProject());
        if (this._attributeMap != null) {
            return this._attributeMap;
        }

        List<XmlAttributeDescriptor> list = ResolverUtil.mapMember(apiIndex, classDocumentation, (ApiSymbol apiSymbol) -> {
            if (apiSymbol instanceof PropertyDocumentation) {
                return new PropertyAttributeDescriptor((PropertyDocumentation) apiSymbol, apiIndex, self);
            }
            if (apiSymbol instanceof EventDocumentation) {
                return new EventAttributeDescriptor((EventDocumentation) apiSymbol, apiIndex, self);
            }
            if (apiSymbol instanceof AggregationDocumentation) {
                return new AggregationAttributeDescriptor((AggregationDocumentation) apiSymbol, apiIndex, self);
            }

            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        // add attributes that exists on any control but aren't part of the api.json
        list.add(new AnyXmlAttributeDescriptor("id"));
        list.add(new AnyXmlAttributeDescriptor("class"));

        // add view specific attributes
        if (this.self.getLocalName().endsWith("View")) {
            //TODO: create a ControllerNameAttributeDescriptor for the reference. makes GotoControllerProvider obsolete.
            list.add(new AnyXmlAttributeDescriptor("controllerName"));
        }

        if (this.self.getLocalName().endsWith("Fragment")) {
            //TODO: add Descriptor for reference
            list.add(new AnyXmlAttributeDescriptor("fragmentName"));
        }

        final Map<String, XmlAttributeDescriptor> result = new THashMap<>();
        list.forEach(desc -> result.put(desc.getName(), desc));

        this._attributeMap = result;

        return result;
    }
}
