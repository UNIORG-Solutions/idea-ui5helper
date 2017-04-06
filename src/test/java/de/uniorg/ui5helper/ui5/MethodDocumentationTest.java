package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import groovy.util.GroovyTestCase;

/**
 * Created by masch on 4/6/17.
 */
public class MethodDocumentationTest extends GroovyTestCase {
    private final String DOC = "{\"name\": \"extend\", \"visibility\": \"public\", \"static\": true, \"returnValue\": {\"type\": \"function\", \"description\": \"Created class / constructor function\"}, \"parameters\": [{\"name\": \"sClassName\", \"type\": \"string\", \"optional\": false, \"description\": \"Name\"}, {\"name\": \"oClassInfo\", \"type\": \"object\", \"optional\": true, \"description\": \"Object ...\"}], \"description\": \"some text here\"}";

    public void testGetReturnValue() throws Exception {
        JsonObject obj = (new JsonParser()).parse(DOC).getAsJsonObject();
        MethodDocumentation mdoc = MethodDocumentation.fromJsonDoc(obj);

        ReturnValueDocumentation rval = mdoc.getReturnValue();
        assertEquals("function", rval.getType());
        assertEquals("Created class / constructor function", rval.getDescription());
    }

    public void testGetParameters() throws Exception {
        JsonObject obj = (new JsonParser()).parse(DOC).getAsJsonObject();
        MethodDocumentation mdoc = MethodDocumentation.fromJsonDoc(obj);

        assertEquals(2, mdoc.getParameters().size());
        for (ParameterDocumentation parameterDocumentation : mdoc.getParameters().values()) {
            assertNotNull(parameterDocumentation);
        }
    }

    public void testGetName() throws Exception {
        JsonObject obj = (new JsonParser()).parse(DOC).getAsJsonObject();

        MethodDocumentation mdoc = MethodDocumentation.fromJsonDoc(obj);
        assertEquals(mdoc.getName(), "extend");
    }

    public void testGetDescription() throws Exception {
        JsonObject obj = (new JsonParser()).parse(DOC).getAsJsonObject();

        MethodDocumentation mdoc = MethodDocumentation.fromJsonDoc(obj);
        assertEquals(mdoc.getDescription(), "some text here");
    }

    public void testFromJsonDoc() {
        JsonObject obj = (new JsonParser()).parse(DOC).getAsJsonObject();

        MethodDocumentation mdoc = MethodDocumentation.fromJsonDoc(obj);

        assertEquals(mdoc.getName(), "extend");
        assertEquals(mdoc.getDescription(), "some text here");
    }

}
