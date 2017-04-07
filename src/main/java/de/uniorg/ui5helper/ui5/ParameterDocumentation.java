package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;

public class ParameterDocumentation extends AbstractApiSymbol {
    private String type;
    private boolean optional;

    public String getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    static ParameterDocumentation fromJsonDoc(JsonObject doc) {
        ParameterDocumentation pdoc = new ParameterDocumentation();
        ParserUtil parser = new ParserUtil(doc);
        pdoc.name = parser.getName();
        pdoc.description = parser.getDescription();
        pdoc.type = parser.getString("type", "");
        pdoc.optional = parser.getBool("optional", false);

        return pdoc;
    }
}
