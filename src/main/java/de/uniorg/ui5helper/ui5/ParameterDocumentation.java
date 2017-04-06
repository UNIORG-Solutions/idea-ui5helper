package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class ParameterDocumentation implements ApiSymbol {
    private String name;
    private String type;
    private boolean optional;
    private String description;

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

    public String getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    static ParameterDocumentation fromJsonDoc(JsonObject doc) {
        ParameterDocumentation pdoc = new ParameterDocumentation();
        pdoc.name = doc.getAsJsonPrimitive("name").getAsString();
        pdoc.type = doc.getAsJsonPrimitive("type").getAsString();
        pdoc.optional = doc.getAsJsonPrimitive("optional").getAsBoolean();
        pdoc.description = doc.getAsJsonPrimitive("description").getAsString();

        return pdoc;
    }
}
