package de.uniorg.ui5helper.codeInsight.xmlview.attributes;

import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.intellij.psi.meta.PsiPresentableMetaData;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.ArrayUtil;
import com.intellij.xml.impl.BasicXmlAttributeDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ClassAttributeDescriptor extends BasicXmlAttributeDescriptor implements PsiPresentableMetaData {
    private static final String[] predefinedClasses;

    static {
        final String[] sizes = new String[]{"No", "Tiny", "Small", "Medium", "Large"};
        final String[] directions = new String[]{"Top", "Bottom", "Begin", "End", "TopBottom", "BeginEnd"};

        List<String> classes = new ArrayList<>();
        for (String size : sizes) {
            for (String direction : directions) {
                classes.add(String.format("sapUi%sMargin%s", size, direction));
            }
        }

        classes.add("sapUiResponsiveMargin");
        classes.add("sapUiForceWidthAuto");
        classes.add("sapUiNoMargin");
        classes.add("sapUiNoContentPadding");
        classes.add("sapUiContentPadding");
        classes.add("sapUiResponsiveContentPadding");
        classes.add("sapUiSizeCompact");
        classes.add("sapUiSizeCozy");

        predefinedClasses = classes.toArray(new String[0]);
    }


    @Nullable
    @Override
    public String getTypeName() {
        return "String[]";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.Nodes.Property;
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
        return true;
    }

    @Override
    public PsiElement getDeclaration() {
        return null;
    }

    @Override
    public String getName() {
        return "class";
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
        return predefinedClasses;
    }

    @Override
    public PsiElement getValueDeclaration(XmlElement attributeValue, String value) {
        return attributeValue;
    }
}
