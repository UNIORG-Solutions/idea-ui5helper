package de.uniorg.ui5helper;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import de.uniorg.ui5helper.cache.CacheStorage;
import de.uniorg.ui5helper.cache.SdkVersionManager;
import de.uniorg.ui5helper.framework.ManifestUtil;
import de.uniorg.ui5helper.index.PathResolver;
import de.uniorg.ui5helper.settings.Settings;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.receive.HttpsClient;
import de.uniorg.ui5helper.ui5.receive.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ProjectComponent implements com.intellij.openapi.components.ProjectComponent {

    private final Provider apiProvider;
    private Project project;
    private ApiIndex apiIndex;
    private PathResolver pathResolver;


    public ProjectComponent(Project project) {
        this.project = project;
        CacheStorage storage = CacheStorage.getInstance();
        storage.checkForInvalidation();
        this.apiProvider = new Provider(storage, new HttpsClient());
        this.pathResolver = new PathResolver(project);
    }

    @Override
    public void projectOpened() {
        this.checkEnabled();

        if (isEnabled()) {
            this.projectEnabled();
        }
    }

    public void projectEnabled() {
        Settings settings = Settings.getInstance(this.project);
        this.changeApiVersion(settings.ui5Version);
        this.pathResolver.updateMap();
    }

    public void enableProject(@Nullable String version) {
        Settings settings = Settings.getInstance(this.project);
        if (version != null) {
            settings.ui5Version = version;
        }
        settings.pluginEnabled = true;
        this.projectEnabled();
    }

    private void checkEnabled() {
        if (!isEnabled() && !Settings.getInstance(project).notificationDismissed) {
            DumbService.getInstance(project).runWhenSmart(() -> {
                Optional<VirtualFile> manifest = VfsUtil.collectChildrenRecursively(this.project.getBaseDir())
                        .stream()
                        .filter(file -> file.getName().contains("manifest.json"))
                        .findFirst();

                if (manifest.isPresent()) {
                    PsiFile manifestPsi = PsiManager.getInstance(project).findFile(manifest.get());
                    if (manifestPsi == null) {
                        System.err.println("PsiFile for manifest not found.");
                        return;
                    }

                    String version = ManifestUtil.getMinimalUi5Version(manifestPsi);

                    String text = "This seems to be a UI5 project. Enable the UI5 Helper plugin?<br />";

                    if (version != null) {
                        if (SdkVersionManager.getInstance().has(version)) {
                            text = text + "<a href='enable'>Enable the plugin</a><br />";
                        } else {
                            text = text + "<a href='enable'>Enable the plugin</a>, but don't download the library source.<br />"
                                    + "<a href='enable_and_download'>Download</a> the library source (v" + version + ") and enable the plugin<br />";
                        }
                    } else {
                        //TODO: open settings dialog
                        text = text + "<a href='enable'>Enable the plugin</a><br />";
                    }

                    Notification notification = new Notification(
                            "UI5 Helper",
                            "UI5 Helper",
                            text + "<a href='dismiss'>Do not ask me again</a>",
                            NotificationType.INFORMATION,
                            (notifi, event) -> {
                                if ("enable".equals(event.getDescription())) {
                                    enableProject(version);
                                } else if ("enable_and_download".equals(event.getDescription()) && version != null) {
                                    enableProject(version);
                                    Task.Backgroundable task = SdkVersionManager.getInstance().download(version, file -> {
                                        Notification restart = new Notification(
                                                "UI5 Helper",
                                                "UI5 Helper",
                                                "New library version downloaded successfully. You may need to <a href='restart'>restart</restart> your IDE.",
                                                NotificationType.INFORMATION,
                                                (restartNotifi, rstevent) -> {
                                                    if ("restart".equals(rstevent.getDescription())) {
                                                        ApplicationManager.getApplication().restart();
                                                    }
                                                }
                                        );

                                        Notifications.Bus.notify(restart);

                                    });
                                    ProgressManager.getInstance().run(task);
                                } else if ("dismiss".equals(event.getDescription())) {
                                    Settings.getInstance(project).notificationDismissed = true;
                                }
                                notifi.expire();
                            }
                    );

                    Notifications.Bus.notify(notification);
                }
            });
        }
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

    public boolean isEnabled() {
        return Settings.getInstance(project).pluginEnabled;
    }

    public static boolean isEnabled(Project project) {
        return project.getComponent(ProjectComponent.class).isEnabled();
    }

    public static boolean isEnabled(Project project, Features feature) {
        ProjectComponent component = project.getComponent(ProjectComponent.class);
        if (component == null || !component.isEnabled()) {
            return false;
        }

        Settings setting = Settings.getInstance(project);

        switch (feature) {
            case JS_FILE_IMPORT_REFERENCE:
                return setting.jsFileImportReference;
            case XML_DOCUMENTATION:
                return setting.xmlDocumentation;
            case XML_COLLAPSE_CONTROLLER_NAME:
                return setting.foldControllerName;
            case XML_BINDING_INJECTION:
                return setting.injectBindingLanguage;
            case XML_GUTTER_ICON:
                return setting.xmlGutterIcon;
            default:
                return false;
        }
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Ui5ProjectComponent";
    }

    public ApiIndex getApiIndex() {
        return apiIndex;
    }

    public PathResolver getPathResolver() {
        return pathResolver;
    }

    public void changeApiVersion(String selectedVersion) {
        this.apiProvider.getApiIndex(selectedVersion, index -> this.apiIndex = index);
    }
}
