package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;

public class AggregationDocumentation extends AbstractApiSymbol {
    private boolean multiple;
    private String type;

    public boolean isMultiple() {
        return multiple;
    }

    public String getType() {
        return type;
    }

    public static AggregationDocumentation fromJsonDoc(JsonObject doc) {
        AggregationDocumentation adoc = new AggregationDocumentation();
        ParserUtil parser = new ParserUtil(doc);
        adoc.name = parser.getName();
        adoc.description = parser.getDescription();
        adoc.type = parser.getString("type", "any");
        String card = parser.getString("cardinality", null);
        if (card == null) {
            adoc.multiple = parser.getBool("multiple", false);
        } else {
            adoc.multiple = card.equals("0..n") | card.equals("1..n");
        }

        return adoc;
    }
}
