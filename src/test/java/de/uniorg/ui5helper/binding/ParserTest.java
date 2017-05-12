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
    public void testComplex() { doTest(true); }
    public void testComplex2() { doTest(true); }
    public void testComplexMultiLine() { doTest(true); }
    public void testExpression() { doTest(true); }

    @Override
    protected String getTestDataPath() { return "src/test/resources/parser"; }

}
