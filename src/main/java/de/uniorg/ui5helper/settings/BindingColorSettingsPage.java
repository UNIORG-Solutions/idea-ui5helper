package de.uniorg.ui5helper.settings;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import de.uniorg.ui5helper.Icons;
import de.uniorg.ui5helper.binding.BindingSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class BindingColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("String", BindingSyntaxHighlighter.STRING),
            new AttributesDescriptor("Operator", BindingSyntaxHighlighter.OPERATORS),
            new AttributesDescriptor("Key", BindingSyntaxHighlighter.KEY),
            new AttributesDescriptor("Path", BindingSyntaxHighlighter.PATH),
            new AttributesDescriptor("Comma", BindingSyntaxHighlighter.COMMA),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.OPENUI5;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new BindingSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "{= ${ path: 'model>/path', formatter: '.myCoolFormatter'} === ${otherModel>anotherCoolProperty} ? 1 + 2 : 'test' }";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "UI5 Binding";
    }
}
