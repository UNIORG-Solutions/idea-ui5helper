package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ClassDocumentation implements ModuleApiSymbol {

    private String className;

    private Map<String, MethodDocumentation> methods = new THashMap<>();

    static ClassDocumentation fromJsonDoc(JsonObject jsonObject) {
        String className = jsonObject.getAsJsonPrimitive("name").getAsString();
        ClassDocumentation symbol = new ClassDocumentation(className);

        for (JsonElement method : jsonObject.getAsJsonArray("methods")) {
            MethodDocumentation m = MethodDocumentation.fromJsonDoc(method.getAsJsonObject());
            symbol.methods.put(m.getName(), m);
        }

        return symbol;
    }

    private ClassDocumentation(String className) {
        this.className = className;
    }

    @NotNull
    public String getName() {
        return className;
    }

    @Override
    @NotNull
    public String getDescription() {
        return "";
    }

    public Map<String, MethodDocumentation> getMethods() {
        return methods;
    }

    @Override
    public String getSourceFile() {
        return null;
    }

    @Override
    public String getModuleName() {
        return null;
    }
}
