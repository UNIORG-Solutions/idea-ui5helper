package de.uniorg.ui5helper.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(name = "UI5HelperPluginSettings", storages = {@Storage("ui5helper.xml")})
public class Settings implements PersistentStateComponent<Settings> {

    public String ui5Version = "latest";

    public boolean pluginEnabled = false;

    public boolean injectBindingLanguage = true;

    public boolean foldControllerName = true;

    public boolean xmlDocumentation = true;

    /**
     * show an icon preview in the editor gutter
     */
    public boolean xmlGutterIcon = true;

    public boolean jsFileImportReference = true;

    public boolean notificationDismissed = false;

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
