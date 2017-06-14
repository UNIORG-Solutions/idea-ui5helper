package de.uniorg.ui5helper.index;

import com.intellij.json.psi.JsonFile;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import de.uniorg.ui5helper.codeInsight.json.ManifestJsonUtil;
import de.uniorg.ui5helper.ui5.general.FileType;
import gnu.trove.THashMap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class PathResolver {
    private Project project;
    private Map<String, VirtualFile> namespaceToPath = new THashMap<>();

    public PathResolver(Project project) {
        this.project = project;
    }

    public void updateMap() {
        DumbService.getInstance(project).smartInvokeLater(() -> {
            PsiFile[] files = FilenameIndex.getFilesByName(project, "manifest.json", GlobalSearchScope.projectScope(project));
            Arrays.stream(files).forEach(this::updateFromManifest);
        });
    }

    public PsiFile[] tryLookupFile(String fullClassName, FileType fileType) {
        Optional<String> longestNamespace = this.namespaceToPath.keySet().stream().filter(fullClassName::startsWith).max(Comparator.comparingInt(String::length));
        if (longestNamespace.isPresent()) {
            String afterNamespace = fullClassName.replaceFirst(longestNamespace.get() + ".", "").replaceAll("\\.", "/");
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

    private void updateFromManifest(PsiFile manifest) {
        if (!(manifest instanceof JsonFile)) {
            return;
        }
        try {
            String namespace = ManifestJsonUtil.getNamespace((JsonFile) manifest);
            if (namespace != null) {
                if (namespaceToPath.containsKey(namespace)) {
                    System.err.println("already have a path for namespace " + namespace + ". Overwriting it.");
                }
                namespaceToPath.put(namespace, manifest.getVirtualFile().getParent());
            }
        } catch (IllegalArgumentException ex) {

        }
    }
}
