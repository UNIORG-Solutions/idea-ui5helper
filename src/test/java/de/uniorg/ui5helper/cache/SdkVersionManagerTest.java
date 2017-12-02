package de.uniorg.ui5helper.cache;

import com.intellij.mock.MockProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.testFramework.LightPlatformTestCase;

import static com.intellij.testFramework.TemporaryDirectoryKt.generateTemporaryPath;

public class SdkVersionManagerTest extends LightPlatformTestCase {

    public void testHas() {
        SdkVersionManager man = new SdkVersionManager(generateTemporaryPath("ui5_sdk_has_test").toString());
        assertFalse(man.has("1.52.1"));
    }

    public void testDownload() {
        SdkVersionManager man = new SdkVersionManager(generateTemporaryPath("ui5_sdk_download_test").toString());
        assertFalse(man.has("1.52.1"));
        Task dl = man.download("1.52.1");
        dl.run(new MockProgressIndicator());
        assertTrue(man.has("1.52.1"));
    }
}