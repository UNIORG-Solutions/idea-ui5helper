package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.DefaultXmlExtension;
import de.uniorg.ui5helper.ui.mvc.XmlViewUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UI5XmlExtension extends DefaultXmlExtension {
    @Override
    public boolean isAvailable(PsiFile psiFile) {
        return XmlViewUtil.isXmlView(psiFile) || XmlViewUtil.isXmlFragment(psiFile);
    }

    @NotNull
    @Override
    public List<TagInfo> getAvailableTagNames(@NotNull XmlFile xmlFile, @NotNull XmlTag xmlTag) {
        if (xmlTag.getParentTag() == null || xmlFile.getRootTag() == null) {
            return Collections.emptyList();
        }

        return XMLViewHelper.getPossibleTags(xmlFile, xmlTag).stream()
                .map(possibleTag -> new TagInfo(possibleTag.name, possibleTag.namespace))
                .collect(Collectors.toList());
    }
}
