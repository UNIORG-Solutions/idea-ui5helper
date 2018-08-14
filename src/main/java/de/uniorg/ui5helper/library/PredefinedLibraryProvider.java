package de.uniorg.ui5helper.library;

import com.intellij.lang.javascript.library.JSPredefinedLibraryProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.webcore.libraries.ScriptingLibraryModel;
import de.uniorg.ui5helper.cache.SdkVersionManager;
import de.uniorg.ui5helper.settings.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipFile;

public class PredefinedLibraryProvider extends JSPredefinedLibraryProvider {
    private static final Logger logger = Logger.getInstance("ui5helper");

    @Override
    @NotNull
    public ScriptingLibraryModel[] getPredefinedLibraries(@NotNull Project project) {
        String version = Settings.getInstance(project).ui5Version;
        if (version == null || version.equals("latest")) {
            return ScriptingLibraryModel.EMPTY_ARRAY;
        }

        File srcZip = SdkVersionManager.getInstance().getZipFile(version);

        if (!srcZip.exists()) {
            return ScriptingLibraryModel.EMPTY_ARRAY;
        }

        ArrayList<VirtualFile> libraryPaths = new ArrayList<>();
        try (ZipFile zip = new ZipFile(srcZip)) {
            zip.stream()
                    .filter(
                            entry ->
                                    entry.getName().endsWith(".js")
                                            && !entry.getName().contains("testsuite")
                                            && !entry.getName().contains("demokit")
                                            && !entry.getName().contains("/test/")
                    )
                    .forEach(entry -> {
                        String url = "jar:file://" + srcZip.getAbsolutePath().replace("\\", "/") + "!/" + entry.getName();
                        try {
                            VirtualFile file = VfsUtil.findFileByURL(new URL(url));
                            if (file != null) {
                                libraryPaths.add(file);
                            } else {
                                logger.error("file not found: " + url);
                            }
                        } catch (MalformedURLException e) {
                            logger.error(e);
                        }
                    });
        } catch (IOException e) {
            logger.error(e);
        }

        ScriptingLibraryModel model = ScriptingLibraryModel.createPredefinedLibrary(
                "OpenUI5 v" + version,
                libraryPaths.toArray(new VirtualFile[0]),
                true
        );

        return new ScriptingLibraryModel[]{model};
    }
}
