package de.uniorg.ui5helper.remote;

import com.intellij.util.text.SemVer;

/**
 * Created by masch on 4/3/17.
 */
public class Ui5Version {

    public static final String LATEST = "latest";

    private final String text;
    private final String path;
    private final SemVer version;

    public Ui5Version(String text, String path, SemVer version) {
        this.text = text;
        this.path = path;
        this.version = version;
    }

    public String getText() {
        return text;
    }

    public String getPath() {
        return path;
    }

    public SemVer getVersion() {
        return version;
    }

    public String toString() {
        return text;
    }
}
