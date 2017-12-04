package de.uniorg.ui5helper.binding;

import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import de.uniorg.ui5helper.Features;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.binding.lang.BindingLanguage;
import de.uniorg.ui5helper.ui.mvc.XmlViewUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class XmlViewInjector implements MultiHostInjector {
    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar multiHostRegistrar, @NotNull PsiElement psiElement) {
        if (!ProjectComponent.isEnabled(psiElement.getProject(), Features.XML_BINDING_INJECTION)) {
            return;
        }

        final PsiFile containingFile = psiElement.getContainingFile();
        if (!(containingFile.getFileType() instanceof XmlFileType
                || XmlViewUtil.isXmlView(containingFile)
                || XmlViewUtil.isXmlFragment(containingFile))) {
            return;
        }

        final XmlAttribute attribute = (XmlAttribute) psiElement;
        XmlAttributeValue value = attribute.getValueElement();

        if (value == null) {
            return;
        }

        if (value.getValue().contains("{") && value.getValue().contains("}")) {
            multiHostRegistrar.startInjecting(BindingLanguage.INSTANCE);
            multiHostRegistrar.addPlace(null, null, (PsiLanguageInjectionHost) value, ElementManipulators.getValueTextRange(value));
            multiHostRegistrar.doneInjecting();
        }

    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return Collections.singletonList(XmlAttribute.class);
    }
}
