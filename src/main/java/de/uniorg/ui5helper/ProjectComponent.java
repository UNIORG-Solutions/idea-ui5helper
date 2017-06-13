package de.uniorg.ui5helper;

import com.intellij.json.psi.JsonFile;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import de.uniorg.ui5helper.cache.CacheStorage;
import de.uniorg.ui5helper.codeInsight.json.ManifestJsonUtil;
import de.uniorg.ui5helper.settings.Settings;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.general.FileType;
import de.uniorg.ui5helper.ui5.receive.HttpsClient;
import de.uniorg.ui5helper.ui5.receive.Provider;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class ProjectComponent implements com.intellij.openapi.components.ProjectComponent {

    private final Provider apiProvider;
    private Project project;
    private ApiIndex apiIndex;

    private Map<String, VirtualFile> namespaceToPath = new THashMap<>();

    public ProjectComponent(Project project) {
        this.project = project;
        CacheStorage storage = CacheStorage.getInstance();
        storage.checkForInvalidation();
        this.apiProvider = new Provider(storage, new HttpsClient());
    }

    @Override
    public void projectOpened() {
        Settings settings = Settings.getInstance(this.project);
        this.changeApiVersion(settings.ui5Version);

        DumbService.getInstance(project).smartInvokeLater(() -> {
            PsiFile[] manifests = FilenameIndex.getFilesByName(project, "manifest.json", GlobalSearchScope.projectScope(project));
            for (PsiFile manifest : manifests) {
                if (manifest instanceof JsonFile) {
                    String namespace = ManifestJsonUtil.getNamespace((JsonFile) manifest);
                    if (namespace != null) {
                        if (namespaceToPath.containsKey(namespace)) {
                            System.err.println("already have a path for namespace " + namespace + ". Overwriting it.");
                        }
                        namespaceToPath.put(namespace, manifest.getVirtualFile().getParent());
                    }
                }
            }
        });
    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Ui5ProjectComponent";
    }

    public ApiIndex getApiIndex() {
        return apiIndex;
    }

    public void changeApiVersion(String selectedVersion) {
        this.apiProvider.getApiIndex(selectedVersion, index -> this.apiIndex = index);
    }

    public PsiFile[] tryLookupFile(String fullViewName, FileType fileType) {
        Optional<String> longestNamespace = this.namespaceToPath.keySet().stream().filter(fullViewName::startsWith).max(Comparator.comparingInt(String::length));
        if (longestNamespace.isPresent()) {
            String afterNamespace = fullViewName.replaceFirst(longestNamespace.get() + ".", "").replaceAll("\\.", "/");
            VirtualFile fullPath = this.namespaceToPath.get(longestNamespace.get()).findFileByRelativePath("./" + afterNamespace + guessExtension(fileType));
            if (fullPath == null) {
                return new PsiFile[0];

            }
            return new PsiFile[]{PsiManager.getInstance(project).findFile(fullPath)};
        }

        return new PsiFile[0];
    }

    private String guessExtension(FileType type) {
        switch (type) {
            case XML_VIEW:
                return ".view.xml";
            case JS_VIEW:
                return ".view.js";
            case JSON_VIEW:
                return ".view.json";
            case CONTROLLER:
                return ".controller.js";
            case MANIFEST_JSON:
                return ".json";
            case PROPERTIES:
                return ".properties";
            default:
                return "";
        }
    }
}
