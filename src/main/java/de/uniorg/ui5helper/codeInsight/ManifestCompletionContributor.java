package de.uniorg.ui5helper.codeInsight;

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

        if (!matchesJsonPath(psiElement, new String[]{"sap.ui5", "routing", "routes", "target"})
                && !matchesJsonPath(psiElement, new String[]{"sap.ui5", "routing", "config", "bypassed", "target"})) {
            return;
        }

        JsonProperty completedProperty = getParentProperty(psiElement);
        if (!completedProperty.getName().equals("target")) {
            return;
        }

        try {
            JsonObject routingObj = getRoutingObject(completedProperty);
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

    private boolean matchesJsonPath(PsiElement element, String[] path) {
        for (int i = path.length - 1; i >= 0; i--) {
            JsonProperty property = getParentProperty(element);
            if (property == null) {
                return false;
            }
            if (!property.getName().equals(path[i])) {
                return false;
            }

            element = property;
        }

        return true;
    }

    private JsonObject getRoutingObject(PsiElement start) {
        JsonProperty property = getParentProperty(start);
        while (property != null && !property.getName().equals("routing")) {
            property = getParentProperty(property);
        }

        return (JsonObject) property.getValue();
    }

    private JsonProperty getParentProperty(PsiElement start) {
        do {
            start = start.getParent();
        } while (start != null && !(start instanceof JsonProperty));

        if (start == null) {
            return null;
        }

        return (JsonProperty) start;
    }
}
