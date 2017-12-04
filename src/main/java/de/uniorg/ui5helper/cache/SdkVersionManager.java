package de.uniorg.ui5helper.cache;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.util.io.HttpRequests;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SdkVersionManager {
    private String basePath;

    private static SdkVersionManager INSTANCE = null;

    SdkVersionManager(String path) {
        this.basePath = path;
    }

    public static SdkVersionManager getInstance() {
        if (SdkVersionManager.INSTANCE == null) {
            SdkVersionManager.INSTANCE = new SdkVersionManager(Paths.get(PathManager.getSystemPath(), "extLibs", "openui5").toAbsolutePath().toString());
        }

        return SdkVersionManager.INSTANCE;
    }

    public boolean has(@NotNull String version) {
        return Paths.get(getPath(version).toString(), "release.zip").toFile().exists();
    }

    private Path getPath(String version) {
        return Paths.get(basePath, "ui5_sdk", version);
    }

    public File getZipFile(String version) {
        return Paths.get(getPath(version).toString(), "release.zip").toFile();
    }

    public Task.Backgroundable download(String version) {

        return new Task.Backgroundable(null, "Downloading OpenUI5 v" + version, true) {

            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    HttpRequests
                            .request("https://codeload.github.com/SAP/openui5/zip/" + version)
                            .forceHttps(true)
                            .useProxy(true)
                            .gzip(true)
                            .saveToFile(Paths.get(getPath(version).toString(), "release.zip").toFile(), indicator);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean shouldStartInBackground() {
                return false;
            }
        };
    }

}
