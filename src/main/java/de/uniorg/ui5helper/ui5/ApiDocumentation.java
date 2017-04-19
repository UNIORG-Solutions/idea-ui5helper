package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.util.text.SemVer;
import gnu.trove.THashMap;

import java.util.Map;

public class ApiDocumentation {

    /**
     *
     */
    private final Map<String, ApiSymbol> symbols;

    /**
     * ui5 library version
     */
    private SemVer version;

    /**
     * namespace of the ui5 library
     */
    private String library;

    public ApiDocumentation(JsonObject apijson) {
        this.version = SemVer.parseFromText(apijson.getAsJsonPrimitive("version").getAsString());
        this.library = apijson.getAsJsonPrimitive("library").getAsString();
        this.symbols = new THashMap<>();
        for (JsonElement jsonElement : apijson.getAsJsonArray("symbols")) {
            if (jsonElement.isJsonObject()) {
                ApiSymbol symbol = ApiSymbolFactory.parseSymbol(jsonElement.getAsJsonObject());
                if (symbol != null) {
                    this.symbols.put(symbol.getName(), symbol);
                }
            }
        }
    }

    public boolean has(String name) {
        return this.symbols.containsKey(name);
    }

    public ApiSymbol get(String name) {
        return this.symbols.get(name);
    }

    public String getLibrary() {
        return library;
    }

    public SemVer getVersion() {
        return version;
    }

    public void createIndex(ApiIndex index) {
        this.symbols.keySet().forEach(key -> index.register(key, this));
    }
}
