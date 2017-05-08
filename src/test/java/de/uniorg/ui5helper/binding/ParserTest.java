package de.uniorg.ui5helper.binding;

import com.intellij.testFramework.ParsingTestCase;

public class ParserTest extends ParsingTestCase {
    public ParserTest() {
        super("", "binding", new BindingParserDefinition());
    }

    public void testSimpleBinding() { doTest(true); }
    public void testSimpleBindingWithoutModel() { doTest(true); }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/parser";
    }

}
