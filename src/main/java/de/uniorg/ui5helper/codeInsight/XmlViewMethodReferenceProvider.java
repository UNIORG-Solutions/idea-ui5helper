package de.uniorg.ui5helper.codeInsight;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.ui.mvc.XmlViewUtil;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.ApiService;
import de.uniorg.ui5helper.ui5.ClassDocumentation;
import de.uniorg.ui5helper.ui5.EventDocumentation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class XmlViewMethodReferenceProvider extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(XmlAttributeValue.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        if (!(element instanceof XmlAttributeValue)) {
                            return new PsiReference[0];
                        }

                        XmlAttributeValue attrValue = (XmlAttributeValue) element;
                        XmlAttribute attr = (XmlAttribute) attrValue.getParent();
                        XmlTag propTag = (XmlTag) attrValue.getParent().getParent();
                        ApiService api = element.getProject().getComponent(ProjectComponent.class).getApiService();
                        ApiIndex index = api.getIndex("latest");
                        ClassDocumentation doc = (ClassDocumentation) index.lookup(propTag.getNamespace() + "." + propTag.getLocalName());
                        if (doc == null) {
                            return new PsiReference[0];
                        }

                        if (!XmlViewUtil.isXmlView(element.getContainingFile())) {
                            return new PsiReference[0];
                        }

                        Map<String, EventDocumentation> events = doc.getUI5Metadata().getEvents();

                        if (!events.containsKey(attr.getLocalName())) {
                            return new PsiReference[0];
                        }

                        return new PsiReference[] { new EventHandlerReference(attrValue) };
                    }
                }
        );
    }
}
