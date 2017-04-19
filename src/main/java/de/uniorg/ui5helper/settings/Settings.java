package de.uniorg.ui5helper.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(
        name = "UI5HelperPluginSettings",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
        }
)
public class Settings implements PersistentStateComponent<Settings> {

    public String ui5Version = "latest";

    public boolean foldControllerName = true;

    public static Settings getInstance(Project project) {
        return ServiceManager.getService(project, Settings.class);
    }

    @Nullable
    @Override
    public Settings getState() {
        return this;
    }

    @Override
    public void loadState(Settings settings) {
        XmlSerializerUtil.copyBean(settings, this);
    }
}
