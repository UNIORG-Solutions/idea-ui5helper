package de.uniorg.ui5helper;

import com.intellij.openapi.project.Project;
import de.uniorg.ui5helper.ui5.ApiService;
import org.jetbrains.annotations.NotNull;

/**
 * Created by masch on 4/5/17.
 */
public class ProjectComponent implements com.intellij.openapi.components.ProjectComponent {

    private final ApiService apiService;

    private Project project;

    public ProjectComponent(Project project) {
        this.project = project;
        this.apiService = new ApiService();
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {
        this.apiService.prefetchDocs("latest");
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Ui5ProjectComponent";
    }

    public ApiService getApiService() {
        return apiService;
    }
}
