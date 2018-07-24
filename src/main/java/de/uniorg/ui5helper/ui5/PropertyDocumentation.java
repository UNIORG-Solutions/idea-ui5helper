package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;

public class PropertyDocumentation extends AbstractApiSymbol {

    private String type;

    private String defaultValue;

    public String getType() {
        return type;
    }

    static PropertyDocumentation fromJsonDoc(JsonObject doc) {
        PropertyDocumentation pdoc = new PropertyDocumentation();
        ParserUtil parser = new ParserUtil(doc);

        pdoc.name = parser.getName();
        pdoc.description = parser.getDescription();
        pdoc.type = parser.getString("type", "");
        pdoc.defaultValue = parser.getString("defaultValue", "");
        pdoc.deprecation = parser.getObject("deprecated", Deprecation::fromJsonDoc);

        return pdoc;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
