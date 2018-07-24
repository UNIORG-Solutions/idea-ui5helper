package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;

public class Deprecation {
    private String since;
    private String message;

    public Deprecation(String since, String message) {
        this.since = since;
        this.message = message;
    }

    public static Deprecation fromJsonDoc(JsonObject doc) {
        if (doc == null) {
            return null;
        }
        ParserUtil parser = new ParserUtil(doc);
        String since = parser.getString("since", null);
        String message = parser.getString("text", "it is deprecated");

        return new Deprecation(since, message);
    }

    public String getSince() {
        return since;
    }

    public String getMessage() {
        return message;
    }
}
