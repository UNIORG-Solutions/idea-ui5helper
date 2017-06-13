package de.uniorg.ui5helper.codeInsight.json;

import com.intellij.json.psi.JsonFile;
import com.intellij.json.psi.JsonObject;
import com.intellij.json.psi.JsonProperty;
import com.intellij.json.psi.JsonStringLiteral;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import de.uniorg.ui5helper.codeInsight.reference.ViewReference;
import de.uniorg.ui5helper.ui5.manifest.RoutingConfig;
import org.jetbrains.annotations.NotNull;

public class ManifestReferences extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(JsonStringLiteral.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        if (!(element instanceof JsonStringLiteral)) {
                            return new PsiReference[0];
                        }
                        JsonStringLiteral literal = (JsonStringLiteral) element;

                        if (!ManifestJsonUtil.matchesJsonPath(element, new String[]{"sap.ui5", "routing", "targets", "?", "viewName"})
                                && !ManifestJsonUtil.matchesJsonPath(element, new String[]{"sap.ui5", "rootView"})) {
                            return new PsiReference[0];
                        }

                        if (!(literal.getParent() instanceof JsonProperty) || !((JsonProperty) literal.getParent()).getValue().equals(element)) {
                            return new PsiReference[0];
                        }

                        try {
                            RoutingConfig cfg = ManifestJsonUtil.getRoutingConfig((JsonFile) element.getContainingFile());
                            JsonProperty property = (JsonProperty) literal.getParent();

                            if (property.getName().equals("viewName")) {
                                String fullViewName = null;
                                if (property.getParent() instanceof JsonObject && ((JsonObject) property.getParent()).findProperty("viewPath") != null) {
                                    JsonProperty viewPathProp = ((JsonObject) property.getParent()).findProperty("viewPath");
                                    if (viewPathProp != null && viewPathProp.getValue() instanceof JsonStringLiteral) {
                                        String viewPath = ((JsonStringLiteral) viewPathProp.getValue()).getValue();
                                        fullViewName = viewPath + "." + literal.getValue();
                                    }
                                }

                                if (fullViewName == null) {
                                    fullViewName = cfg.getDefaults().getViewPath() + "." + literal.getValue();
                                    return new PsiReference[]{new ViewReference(element, fullViewName, cfg.getDefaults().getViewType())};
                                }
                            }

                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            return new PsiReference[0];
                        }

                        System.out.println(element.getText());
                        return new PsiReference[0];

                    }
                }
        );
    }
}
