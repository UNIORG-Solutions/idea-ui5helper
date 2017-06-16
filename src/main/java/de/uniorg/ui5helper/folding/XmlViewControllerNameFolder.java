package de.uniorg.ui5helper.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import de.uniorg.ui5helper.Features;
import de.uniorg.ui5helper.ProjectComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XmlViewControllerNameFolder extends FoldingBuilderEx {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement psiElement, @NotNull Document document, boolean b) {
        if (!ProjectComponent.isEnabled(psiElement.getProject(), Features.XML_COLLAPSE_CONTROLLER_NAME)) {
            return FoldingDescriptor.EMPTY;
        }

        FoldingGroup group = FoldingGroup.newGroup("controllerName");
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        Collection<XmlAttribute> literalExpressions = PsiTreeUtil.findChildrenOfType(psiElement, XmlAttribute.class);
        for (final XmlAttribute attribute : literalExpressions) {
            String name = attribute.getName();
            String value = attribute.getValue();
            XmlAttributeValue valueEl = attribute.getValueElement();

            if (name.equals("controllerName")) {
                FoldingDescriptor desc = new FoldingDescriptor(valueEl.getNode(), valueEl.getValueTextRange(), group) {
                    @Nullable
                    @Override
                    public String getPlaceholderText() {
                        if (value == null) {
                            return "";
                        }

                        return value.substring(value.lastIndexOf("."));
                    }
                };
                descriptors.add(desc);
            }

        }
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode astNode) {
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return true;
    }
}
