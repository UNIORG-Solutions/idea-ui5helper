package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;
import gnu.trove.THashMap;

import java.util.Map;

public class UI5Metadata {
    private String stereotype;

    private Map<String, PropertyDocumentation> properties = new THashMap<>();

    private Map<String, AggregationDocumentation> aggregations = new THashMap<>();

    private Map<String, EventDocumentation> events = new THashMap<>();

    public String getStereotype() {
        return stereotype;
    }

    public Map<String, PropertyDocumentation> getProperties() {
        return properties;
    }

    public Map<String, AggregationDocumentation> getAggregations() {
        return aggregations;
    }

    public Map<String, EventDocumentation> getEvents() {
        return events;
    }

    static UI5Metadata fromJsonDoc(JsonObject doc) {
        UI5Metadata mdata = new UI5Metadata();

        ParserUtil parser = new ParserUtil(doc);

        mdata.stereotype = parser.getString("stereotype", null);

        mdata.events = parser.mapArray("events", EventDocumentation::fromJsonDoc);
        mdata.properties = parser.mapArray("properties", PropertyDocumentation::fromJsonDoc);
        mdata.aggregations = parser.mapArray("aggregations", AggregationDocumentation::fromJsonDoc);

        return mdata;
    }
}
