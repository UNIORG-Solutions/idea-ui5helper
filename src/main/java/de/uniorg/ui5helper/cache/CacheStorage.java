package de.uniorg.ui5helper.cache;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.util.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CacheStorage {
    private String basePath;

    private static CacheStorage INSTANCE = null;

    private CacheStorage(String path) {
        this.basePath = path;
    }

    public static CacheStorage getInstance() {
        if (CacheStorage.INSTANCE == null) {
            CacheStorage.INSTANCE = new CacheStorage(Paths.get(PathManager.getSystemPath(), "caches").toAbsolutePath().toString());
        }

        return CacheStorage.INSTANCE;
    }

    public void store(String version, String file, String content) throws IOException {
        Path path = getPath(version, file);
        File parent = path.getParent().toFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public String get(String version, String file) throws IOException {
        return new String(Files.readAllBytes(getPath(version, file)));
    }

    public boolean has(String version, String file) {
        return getPath(version, file).toFile().exists();
    }

    private Path getPath(String version, String file) {
        return Paths.get(basePath, "ui5_api", version, file);
    }

    public void checkForInvalidation() {
        if (this.getInvalidationMarker().exists()) {
            FileUtil.delete(Paths.get(this.basePath).toFile());
        }
    }

    public void invalidate() {
        FileUtil.createIfDoesntExist(this.getInvalidationMarker());
        System.out.println("invalidated caches");
    }

    private File getInvalidationMarker() {
        return Paths.get(this.basePath, "ui5_api", "invalidate.marker").toFile();
    }
}
