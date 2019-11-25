package de.uniorg.ui5helper.codeInsight.xmlview.attributes;


import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.intellij.psi.meta.PsiPresentableMetaData;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.ArrayUtil;
import com.intellij.xml.impl.BasicXmlAttributeDescriptor;
import de.uniorg.ui5helper.ui5.AggregationDocumentation;
import de.uniorg.ui5helper.ui5.ApiIndex;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class AggregationAttributeDescriptor extends BasicXmlAttributeDescriptor implements PsiPresentableMetaData {

    private final AggregationDocumentation aggregationDocumentation;

    private final ApiIndex apiIndex;

    private final XmlElement self;

    public AggregationAttributeDescriptor(AggregationDocumentation aggregationDocumentation, ApiIndex apiIndex, XmlElement self) {
        this.aggregationDocumentation = aggregationDocumentation;
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
        return false;
    }

    @Override
    public PsiElement getDeclaration() {
        return self;
    }

    @Override
    public String getName() {
        return this.aggregationDocumentation.getName();
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
        return "";
    }

    @Override
    public String[] getEnumeratedValues() {
        return new String[0];
    }

    @Override
    public String getTypeName() {
        return null;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.General.Recursive;
    }
}
