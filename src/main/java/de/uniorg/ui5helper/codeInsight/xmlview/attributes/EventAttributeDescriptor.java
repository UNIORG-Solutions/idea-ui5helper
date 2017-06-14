package de.uniorg.ui5helper.codeInsight.xmlview.attributes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.ArrayUtil;
import com.intellij.xml.impl.BasicXmlAttributeDescriptor;
import de.uniorg.ui5helper.codeInsight.EventHandlerReference;
import de.uniorg.ui5helper.ui.mvc.ControllerUtil;
import de.uniorg.ui5helper.ui.mvc.XmlViewUtil;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.EventDocumentation;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Set;


public class EventAttributeDescriptor extends BasicXmlAttributeDescriptor {

    private final EventDocumentation eventDocumentation;

    private final ApiIndex apiIndex;

    private final XmlElement self;

    public EventAttributeDescriptor(EventDocumentation eventDocumentation, ApiIndex apiIndex, XmlElement self) {
        this.eventDocumentation = eventDocumentation;
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
        return true;
    }

    @Override
    public PsiElement getDeclaration() {
        return self;
    }

    @Override
    public String getName() {
        return this.eventDocumentation.getName();
    }

    @Override
    public void init(PsiElement element) {

    }

    @Override
    public Object[] getDependences() {
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
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
        String controllerName = XmlViewUtil.getControllerName(self.getContainingFile());
        PsiElement[] controllers = ControllerUtil.findReferences(self.getProject(), controllerName);
        Set<String> methodNames = new THashSet<>();
        for (PsiElement controller : controllers) {
            methodNames.addAll(ControllerUtil.getMethodNames(controller));
        }

        return methodNames.toArray(new String[0]);
    }

    @Override
    public PsiReference[] getValueReferences(XmlElement element, @NotNull String text) {

        return new PsiReference[]{new EventHandlerReference(element)};
    }
}
