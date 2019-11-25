package de.uniorg.ui5helper.codeInsight.binding;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import de.uniorg.ui5helper.ProjectComponent;

public class SimplifyBindingAnnotatorTest extends BasePlatformTestCase {
    public void testCreatesAnnotation() {
        myFixture.getProject().getComponent(ProjectComponent.class).enableProject(null);

        // this test passes every time, whatever we do. this isn't a working test. we should fix it.
        myFixture.configureByText("Binding.view.xml", "<TestTag id=\"<warning descr='Simplify Binding'><warning descr=\"Binding can be simplified.\">{= ${model>path}}</warning></warning>\" />");
        myFixture.testHighlighting(true, false, true);
    }
}
