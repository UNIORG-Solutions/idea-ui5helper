package de.uniorg.ui5helper;

import com.intellij.openapi.project.Project;
import de.uniorg.ui5helper.cache.CacheStorage;
import de.uniorg.ui5helper.settings.Settings;
import de.uniorg.ui5helper.ui5.ApiIndex;
import de.uniorg.ui5helper.ui5.receive.HttpsClient;
import de.uniorg.ui5helper.ui5.receive.Provider;
import org.jetbrains.annotations.NotNull;

public class ProjectComponent implements com.intellij.openapi.components.ProjectComponent {

    private final Provider apiProvider;
    private Project project;
    private ApiIndex apiIndex;

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
}
