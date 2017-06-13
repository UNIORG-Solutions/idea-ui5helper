package de.uniorg.ui5helper.codeInsight.json;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.json.psi.JsonObject;
import com.intellij.json.psi.JsonProperty;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManifestCompletionContributor extends CompletionContributor {

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        if (!parameters.getOriginalFile().getVirtualFile().getName().endsWith("manifest.json")) {
            return;
        }

        PsiElement psiElement = parameters.getPosition();

        if (!ManifestJsonUtil.matchesJsonPath(psiElement, new String[]{"sap.ui5", "routing", "routes", "target"})
                && !ManifestJsonUtil.matchesJsonPath(psiElement, new String[]{"sap.ui5", "routing", "config", "bypassed", "target"})) {
            return;
        }

        JsonProperty completedProperty = ManifestJsonUtil.getParentProperty(psiElement);
        if (!completedProperty.getName().equals("target")) {
            return;
        }

        try {
            JsonObject routingObj = ManifestJsonUtil.getRoutingObject(completedProperty);
            getTargetList(routingObj).forEach(target -> result.addElement(LookupElementBuilder.create(target)));
        } catch (NullPointerException npe) {
            return;
        }
    }

    private List<String> getTargetList(JsonObject routing) {
        JsonProperty targetProp = routing.findProperty("targets");

        if (targetProp == null) {
            return new ArrayList<>();
        }

        JsonObject targets = (JsonObject) targetProp.getValue();

        if (targets == null) {
            return new ArrayList<>();
        }

        return targets.getPropertyList().stream().map(JsonProperty::getName).collect(Collectors.toList());
    }
}
