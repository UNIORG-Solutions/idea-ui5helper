package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;
import gnu.trove.THashMap;

import java.util.Map;

public class MethodDocumentation extends AbstractApiSymbol {

    private Map<String, ParameterDocumentation> parameters = new THashMap<>();

    private ReturnValueDocumentation returnValue;

    private boolean isStatic;

    public Map<String, ParameterDocumentation> getParameters() {
        return parameters;
    }

    public ReturnValueDocumentation getReturnValue() {
        return returnValue;
    }

    static MethodDocumentation fromJsonDoc(JsonObject doc) {
        MethodDocumentation mdoc = new MethodDocumentation();
        ParserUtil parser = new ParserUtil(doc);
        mdoc.name = parser.getName();
        mdoc.description = parser.getDescription();
        mdoc.isStatic = parser.getBool("static", false);
        mdoc.parameters = parser.mapArray("parameters", ParameterDocumentation::fromJsonDoc);
        mdoc.returnValue = parser.getObject("returnValue", ReturnValueDocumentation::fromJsonDoc);
        mdoc.deprecation = parser.getObject("deprecated", Deprecation::fromJsonDoc);

        return mdoc;
    }

    public boolean isStatic() {
        return isStatic;
    }

    static MethodDocumentation constructorFromJsonDoc(JsonObject doc) {
        MethodDocumentation mdoc = new MethodDocumentation();
        ParserUtil parser = new ParserUtil(doc);
        mdoc.name = "constructor";
        mdoc.description = parser.getDescription();
        mdoc.parameters = parser.mapArray("parameters", ParameterDocumentation::fromJsonDoc);
        return mdoc;
    }
}
