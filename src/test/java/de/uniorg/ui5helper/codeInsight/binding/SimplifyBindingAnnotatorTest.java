package de.uniorg.ui5helper.codeInsight.binding;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

import java.io.File;

public class SimplifyBindingAnnotatorTest extends LightPlatformCodeInsightFixtureTestCase {
    public void testCreatesAnnotation()
    {
        // this test passes every time, whatever we do. this isn't a working test. we should fix it.
        myFixture.configureByText("Binding.view.xml", "<TestTag attr=\"<warning descr='Simplify Binding'>{= ${model>path}}</warning>\" />");
        myFixture.testHighlighting(true, false, true);
    }
}
