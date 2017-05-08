package de.uniorg.ui5helper.binding;

import com.intellij.testFramework.ParsingTestCase;

public class ParserTest extends ParsingTestCase {
    public ParserTest() {
        super("", "binding", new BindingParserDefinition());
    }

    public void testSimpleBinding() { doTest(true); }
    public void testSimpleBindingWithoutModel() { doTest(true); }
    public void testI18nBinding() { doTest(true); }
    public void testMixedSimple() { doTest(true); }

    @Override
    protected String getTestDataPath() { return "src/test/resources/parser"; }

}
