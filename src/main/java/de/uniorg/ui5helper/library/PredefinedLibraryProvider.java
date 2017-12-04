package de.uniorg.ui5helper.library;

import com.intellij.lang.javascript.library.JSPredefinedLibraryProvider;
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
    @NotNull
    public ScriptingLibraryModel[] getPredefinedLibraries(@NotNull Project project) {
        String version = Settings.getInstance(project).ui5Version;
        if (version == null || version.equals("latest")) {
            return ScriptingLibraryModel.EMPTY_ARRAY;
        }

        File srcZip = SdkVersionManager.getInstance().getZipFile(version);

        ArrayList<VirtualFile> libraryPaths = new ArrayList<>();
        try {
            new ZipFile(srcZip).stream().forEach(entry -> {
                if (entry.getName().endsWith(".js")) {
                    String url = "jar:file://" + srcZip.getAbsolutePath() + "!/" + entry.getName();
                    try {
                        VirtualFile file = VfsUtil.findFileByURL(new URL(url));
                        if (file != null) {
                            libraryPaths.add(file);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScriptingLibraryModel model = ScriptingLibraryModel.createPredefinedLibrary(
                "OpenUI5 v" + version,
                libraryPaths.toArray(new VirtualFile[libraryPaths.size()]),
                true
        );

        return new ScriptingLibraryModel[]{model};
    }
}
