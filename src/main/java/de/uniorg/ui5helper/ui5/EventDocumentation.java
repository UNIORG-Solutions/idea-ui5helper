package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;

/**
 * Created by masch on 4/7/17.
 */
public class EventDocumentation extends AbstractApiSymbol {
    private String visibility;

    static EventDocumentation fromJsonDoc(JsonObject doc) {
        EventDocumentation edoc = new EventDocumentation();
        ParserUtil parser = new ParserUtil(doc);
        edoc.name = parser.getName();
        edoc.description = parser.getDescription();
        edoc.visibility = parser.getString("visibility", "public");
        edoc.deprecation = parser.getObject("deprecated", Deprecation::fromJsonDoc);
        return edoc;
    }

    public String getVisibility() {
        return visibility;
    }
}
