package de.uniorg.ui5helper.folding;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

import java.io.File;

public class XmlViewControllerNameFolderTest extends LightPlatformCodeInsightFixtureTestCase {
    @Override
    public String getTestDataPath() {
        return new File("src/test/resources/folding").getAbsolutePath();
    }

    public void testControllerNameFolding() {
        myFixture.configureByFiles();
        myFixture.testFolding(this.getTestDataPath() + "/ControllerNameFolding.view.xml");
    }
}
