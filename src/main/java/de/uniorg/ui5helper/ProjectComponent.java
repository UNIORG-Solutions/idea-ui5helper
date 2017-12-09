package de.uniorg.ui5helper;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import de.uniorg.ui5helper.cache.CacheStorage;
import de.uniorg.ui5helper.index.PathResolver;
import de.uniorg.ui5helper.settings.Settings;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.receive.HttpsClient;
import de.uniorg.ui5helper.ui5.receive.Provider;
import org.jetbrains.annotations.NotNull;

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

    public void enableProject() {
        Settings settings = Settings.getInstance(this.project);
        settings.pluginEnabled = true;
        this.projectEnabled();
    }

    private void checkEnabled() {
        if (!isEnabled() && !Settings.getInstance(project).notificationDismissed) {
            DumbService.getInstance(project).runWhenSmart(() -> {
                long foundFiles = VfsUtil.collectChildrenRecursively(this.project.getBaseDir())
                        .stream()
                        .filter(file -> file.getName().contains("manifest.json") || file.getName().contains("Component.js"))
                        .count();

                if (foundFiles > 0) {
                    Notification notification = new Notification(
                            "UI5 Helper",
                            "UI5 Helper",
                            "This seems to be a UI5 project. <a href='enable'>Enable</a> the UI5 Helper plugin?<br />" +
                                    "<a href='dismiss'>Do not ask me again</a>",
                            NotificationType.INFORMATION,
                            (notifi, event) -> {
                                if ("enable".equals(event.getDescription())) {
                                    enableProject();
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
