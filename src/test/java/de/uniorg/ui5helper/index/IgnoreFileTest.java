package de.uniorg.ui5helper.index;

import de.uniorg.ui5helper.index.IgnoreIndexer.IgnoreFile;
import junit.framework.TestCase;

public class IgnoreFileTest extends TestCase {

    public void testMatchesSimpleEntries() {
        String content = "demoFile\nfile/in/directory\n";
        IgnoreFile file = new IgnoreFile("/path/", content.getBytes());

        assertTrue(file.contains("/path/demoFile"));
        assertTrue(file.contains("/path/file/in/directory"));
        assertFalse(file.contains("/path/demoFile/in/directory"));
        assertFalse(file.contains("/path/file/in/demoFile"));
    }

}
