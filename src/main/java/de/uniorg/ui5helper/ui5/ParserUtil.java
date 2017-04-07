package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;
import gnu.trove.THashMap;

import java.util.Map;
import java.util.function.Function;

public class ParserUtil {

    private final JsonObject doc;

    public ParserUtil(JsonObject document) {
        this.doc = document;
    }

    <T extends ApiSymbol> Map<String, T> mapArray(String field, Function<JsonObject, T> mapper) {
        Map<String, T> map = new THashMap<>();

        if (doc.has(field) && doc.get(field).isJsonArray()) {
            doc.getAsJsonArray(field).forEach(eventDoc -> {
                T edoc = mapper.apply(eventDoc.getAsJsonObject());
                map.put(edoc.getName(), edoc);
            });
        }


        return map;
    }

    <T> T getObject(String field, Function<JsonObject, T> mapper) {
        if (doc.has(field) && doc.get(field).isJsonObject()) {
            return mapper.apply(doc.getAsJsonObject(field));
        }

        return null;
    }

    String getName() {
        return doc.getAsJsonPrimitive("name").getAsString();
    }

    String getDescription() {
        if (doc.has("description") && doc.get("description").isJsonPrimitive()) {
            return doc.getAsJsonPrimitive("description").getAsString();
        } else {
            return "";
        }
    }

    String getString(String type, String s) {
        if (doc.has(type) && doc.get(type).isJsonPrimitive()) {
            return doc.getAsJsonPrimitive(type).getAsString();
        } else {
            return s;
        }
    }

    boolean getBool(String type, boolean d) {
        if (doc.has(type) && doc.get(type).isJsonPrimitive()) {
            return doc.getAsJsonPrimitive(type).getAsBoolean();
        } else {
            return d;
        }
    }
}
