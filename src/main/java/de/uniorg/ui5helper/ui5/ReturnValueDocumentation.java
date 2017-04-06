package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ReturnValueDocumentation implements ApiSymbol {
    private String type;
    private String description;

    @NotNull
    @Override
    public String getName() {
        return "ReturnValue";
    }

    @NotNull
    @Override
    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    static ReturnValueDocumentation fromJsonDoc(JsonObject doc) {
        ReturnValueDocumentation pdoc = new ReturnValueDocumentation();
        pdoc.type = doc.getAsJsonPrimitive("type").getAsString();
        pdoc.description = doc.getAsJsonPrimitive("description").getAsString();

        return pdoc;
    }
}
