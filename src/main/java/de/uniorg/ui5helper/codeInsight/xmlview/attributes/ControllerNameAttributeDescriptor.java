package de.uniorg.ui5helper.codeInsight.xmlview.attributes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.ArrayUtil;
import com.intellij.util.indexing.FileBasedIndexImpl;
import com.intellij.xml.impl.BasicXmlAttributeDescriptor;
import de.uniorg.ui5helper.codeInsight.reference.ControllerReference;
import de.uniorg.ui5helper.index.mvc.NaiveControllerIndexer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ControllerNameAttributeDescriptor extends BasicXmlAttributeDescriptor {

    private final PsiElement element;

    public ControllerNameAttributeDescriptor(PsiElement element) {
        this.element = element;
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
        return "controllerName";
    }

    @Override
    public void init(PsiElement psiElement) {

    }

    @Override
    public boolean isFixed() {
        return false;
    }

    @Override
    public String getDefaultValue() {
        return null;
    }

    @Override
    public String[] getEnumeratedValues() {
        List<String> keys = new ArrayList<>();
        FileBasedIndexImpl.getInstance()
                .processAllKeys(
                        NaiveControllerIndexer.KEY,
                        keys::add,
                        GlobalSearchScope.allScope(element.getProject()),
                        null
                );

        return keys.toArray(new String[0]);
    }

    @Override
    public PsiReference[] getValueReferences(XmlElement xmlElement, @NotNull String controllerName) {
        return new PsiReference[]{
                new ControllerReference(xmlElement, controllerName)
        };
    }
}
