package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ClassDocumentation implements ModuleApiSymbol, DeprecateableInterface {

    private String className;

    private Map<String, MethodDocumentation> methods = new THashMap<>();

    private MethodDocumentation constructor;

    private String description;

    private String resource;

    private String module;

    private String inherits;

    private UI5Metadata ui5Metadata;

    private Deprecation deprecation = null;


    static ClassDocumentation fromJsonDoc(JsonObject doc) {
        ClassDocumentation cdoc = new ClassDocumentation();
        ParserUtil parser = new ParserUtil(doc);

        cdoc.className = parser.getName();
        cdoc.methods = parser.mapArray("methods", MethodDocumentation::fromJsonDoc);
        cdoc.description = parser.getString("description", "");
        cdoc.constructor = parser.getObject("constructor", MethodDocumentation::constructorFromJsonDoc);
        cdoc.resource = parser.getString("resource", null);
        cdoc.module = parser.getString("module", null);
        cdoc.ui5Metadata = parser.getObject("ui5-metadata", UI5Metadata::fromJsonDoc);
        cdoc.inherits = parser.getString("extends", null);
        cdoc.deprecation = parser.getObject("deprecated", Deprecation::fromJsonDoc);

        return cdoc;
    }


    public boolean isDeprecated() {
        return deprecation != null;
    }

    public Deprecation getDeprecation() {
        return deprecation;
    }

    @NotNull
    public String getName() {
        return className;
    }

    @Override
    @NotNull
    public String getDescription() {
        return description;
    }

    public Map<String, MethodDocumentation> getMethods() {
        return methods;
    }

    @Override
    public String getSourceFile() {
        return resource;
    }

    @Override
    public String getModuleName() {
        return module;
    }

    public String getInherits() {
        return inherits;
    }

    public MethodDocumentation getConstructor() {
        return constructor;
    }

    public UI5Metadata getUI5Metadata() {
        return ui5Metadata;
    }
}
