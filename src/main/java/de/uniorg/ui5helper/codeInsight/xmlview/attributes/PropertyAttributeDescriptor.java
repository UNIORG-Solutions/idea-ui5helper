package de.uniorg.ui5helper.codeInsight.xmlview.attributes;

import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.intellij.psi.meta.PsiPresentableMetaData;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.ArrayUtil;
import com.intellij.xml.impl.BasicXmlAttributeDescriptor;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.ApiSymbol;
import de.uniorg.ui5helper.ui5.EnumDocumentation;
import de.uniorg.ui5helper.ui5.PropertyDocumentation;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Set;

public class PropertyAttributeDescriptor extends BasicXmlAttributeDescriptor implements PsiPresentableMetaData {

    private final String[] BOOL_ENUM = new String[]{"true", "false"};

    private final PropertyDocumentation propertyDocumentation;

    private final ApiIndex apiIndex;

    private final XmlElement self;

    public PropertyAttributeDescriptor(PropertyDocumentation propertyDocumentation, ApiIndex apiIndex, XmlElement self) {
        this.propertyDocumentation = propertyDocumentation;
        this.apiIndex = apiIndex;
        this.self = self;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public boolean hasIdType() {
        return false;
    }

    @Override
    public boolean hasIdRefType() {
        return false;
    }

    @Override
    public boolean isEnumerated() {
        ApiSymbol symbol = this.apiIndex.lookup(this.propertyDocumentation.getType());
        return this.isBool() || symbol instanceof EnumDocumentation;
    }

    private boolean isBool() {
        return this.propertyDocumentation.getType().equalsIgnoreCase("boolean");
    }

    @Override
    public PsiElement getDeclaration() {
        return self;
    }

    @Override
    public String getName() {
        return this.propertyDocumentation.getName();
    }

    @Override
    public void init(PsiElement element) {
    }

    @Override
    public boolean isFixed() {
        return false;
    }

    @Override
    public String getDefaultValue() {
        return this.propertyDocumentation.getDefaultValue();
    }

    @Override
    public String[] getEnumeratedValues() {
        if (isBool()) {
            return BOOL_ENUM;
        }

        ApiSymbol symbol = this.apiIndex.lookup(this.propertyDocumentation.getType());
        if (!(symbol instanceof EnumDocumentation)) {
            return new String[0];
        }

        Set<String> keys = ((EnumDocumentation) symbol).getProperties().keySet();
        return keys.toArray(new String[keys.size()]);
    }

    @Override
    public String getTypeName() {
        return this.propertyDocumentation.getType();
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.Nodes.Property;
    }
}
