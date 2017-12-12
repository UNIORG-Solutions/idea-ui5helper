package de.uniorg.ui5helper.framework;

import com.intellij.json.psi.*;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManifestUtil {

    @Nullable
    public static String getMinimalUi5Version(@NotNull PsiFile manifestFile) {
        if (!(manifestFile instanceof JsonFile)) {
            return null;
        }

        JsonValue manifest = ((JsonFile) manifestFile).getTopLevelValue();

        if (!(manifest instanceof JsonObject)) {
            return null;
        }

        JsonProperty ui5Property = ((JsonObject) manifest).findProperty("sap.ui5");
        if (ui5Property == null || !(ui5Property.getValue() instanceof JsonObject)) {
            return null;
        }

        JsonProperty dependenciesProp = ((JsonObject) ui5Property.getValue()).findProperty("dependencies");
        if (dependenciesProp == null || !(dependenciesProp.getValue() instanceof JsonObject)) {
            return null;
        }

        JsonProperty minUi5Version = ((JsonObject) dependenciesProp.getValue()).findProperty("minUI5Version");
        if (minUi5Version == null || !(minUi5Version.getValue() instanceof JsonStringLiteral)) {
            return null;
        }

        return ((JsonStringLiteral) minUi5Version.getValue()).getValue();
    }
}
