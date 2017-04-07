package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;

/**
 * Created by masch on 4/6/17.
 */
public class ApiSymbolFactory {
    public static ApiSymbol parseSymbol(JsonObject jsonObject) {
        switch (jsonObject.getAsJsonPrimitive("kind").getAsString()) {
            case "class":
                return ClassDocumentation.fromJsonDoc(jsonObject);
        }

        return null;
    }
}
