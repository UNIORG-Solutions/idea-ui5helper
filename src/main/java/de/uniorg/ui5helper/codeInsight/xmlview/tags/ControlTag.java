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
import com.intellij.xml.impl.schema.AnyXmlElementDescriptor;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.codeInsight.xmlview.attributes.PropertyAttributeDescriptor;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.ClassDocumentation;
import de.uniorg.ui5helper.ui5.PropertyDocumentation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ControlTag implements XmlElementDescriptor {

    private final ClassDocumentation classDocumentation;
    private XmlTag self;

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
        final ApiIndex apiIndex = this.getApiIndex(context.getProject());
        return classDocumentation.getUI5Metadata()
                .getProperties()
                .values().stream()
                .map(propertyDocumentation -> new PropertyAttributeDescriptor(propertyDocumentation, apiIndex, context))
                .toArray(XmlAttributeDescriptor[]::new);
    }

    @Nullable
    @Override
    public XmlAttributeDescriptor getAttributeDescriptor(String attributeName, @Nullable XmlTag context) {
        final ApiIndex apiIndex = this.getApiIndex(context.getProject());
        Map<String, PropertyDocumentation> props = classDocumentation.getUI5Metadata().getProperties();
        if (props.containsKey(attributeName)) {
            return new PropertyAttributeDescriptor(props.get(attributeName), apiIndex, context.getAttribute(attributeName));
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
}
