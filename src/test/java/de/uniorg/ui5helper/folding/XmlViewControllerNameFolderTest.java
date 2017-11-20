package de.uniorg.ui5helper.folding;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import de.uniorg.ui5helper.Features;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.settings.Settings;

import java.io.File;

public class XmlViewControllerNameFolderTest extends LightPlatformCodeInsightFixtureTestCase {
    @Override
    public String getTestDataPath() {
        return new File("src/test/resources/folding").getAbsolutePath();
    }

    public void testControllerNameFolding() {
        myFixture.configureByFiles();
        Settings.getInstance(myFixture.getProject()).pluginEnabled = true;
        Settings.getInstance(myFixture.getProject()).foldControllerName = true;
        assertTrue(ProjectComponent.isEnabled(myFixture.getProject(), Features.XML_COLLAPSE_CONTROLLER_NAME));
        myFixture.testFolding(this.getTestDataPath() + "/ControllerNameFolding.view.xml");
    }

    public void testControllerNameFoldingDisabled() {
        myFixture.configureByFiles();
        Settings.getInstance(myFixture.getProject()).pluginEnabled = true;
        Settings.getInstance(myFixture.getProject()).foldControllerName = false;
        assertFalse(ProjectComponent.isEnabled(myFixture.getProject(), Features.XML_COLLAPSE_CONTROLLER_NAME));
        myFixture.testFolding(this.getTestDataPath() + "/ControllerNameFoldingDisabled.view.xml");
    }

    public void testControllerNameFoldingPluginDisabled() {
        myFixture.configureByFiles();
        Settings.getInstance(myFixture.getProject()).pluginEnabled = false;
        Settings.getInstance(myFixture.getProject()).foldControllerName = true;
        assertFalse(ProjectComponent.isEnabled(myFixture.getProject(), Features.XML_COLLAPSE_CONTROLLER_NAME));
        myFixture.testFolding(this.getTestDataPath() + "/ControllerNameFoldingDisabled.view.xml");
    }
}
