package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MethodDocumentation implements ApiSymbol {

    private String name;

    private String description;

    private Map<String, ParameterDocumentation> parameters = new THashMap<>();

    private ReturnValueDocumentation returnValue;

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getDescription() {
        return description;
    }

    public Map<String, ParameterDocumentation> getParameters() {
        return parameters;
    }

    public ReturnValueDocumentation getReturnValue() {
        return returnValue;
    }

    static MethodDocumentation fromJsonDoc(JsonObject doc) {
        MethodDocumentation mdoc = new MethodDocumentation();
        mdoc.name = doc.getAsJsonPrimitive("name").getAsString();
        mdoc.description = doc.getAsJsonPrimitive("description").getAsString();

        for (JsonElement parameter : doc.getAsJsonArray("parameters")) {
            ParameterDocumentation pdoc = ParameterDocumentation.fromJsonDoc(parameter.getAsJsonObject());

            mdoc.parameters.put(pdoc.getName(), pdoc);
        }

        if (doc.has("returnValue") && doc.get("returnValue").isJsonObject()) {
            mdoc.returnValue = ReturnValueDocumentation.fromJsonDoc(doc.getAsJsonObject("returnValue"));
        }

        return mdoc;
    }
}
