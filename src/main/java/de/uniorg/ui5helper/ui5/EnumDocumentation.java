package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EnumDocumentation implements ModuleApiSymbol {
    private String name;

    private String description;

    private String resource;

    private String module;

    private Map<String, PropertyDocumentation> properties;

    public static ApiSymbol fromJsonDoc(JsonObject jsonObject) {
        EnumDocumentation cdoc = new EnumDocumentation();
        ParserUtil parser = new ParserUtil(jsonObject);

        cdoc.name = parser.getName();
        cdoc.description = parser.getString("description", "");
        cdoc.properties = parser.mapArray("properties", PropertyDocumentation::fromJsonDoc);
        cdoc.resource = parser.getString("resource", null);
        cdoc.module = parser.getString("module", null);

        return cdoc;
    }

    @Override
    public String getSourceFile() {
        return resource;
    }

    @Override
    public String getModuleName() {
        return module;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getDescription() {
        return description;
    }

    public Map<String, PropertyDocumentation> getProperties() {
        return properties;
    }
}
