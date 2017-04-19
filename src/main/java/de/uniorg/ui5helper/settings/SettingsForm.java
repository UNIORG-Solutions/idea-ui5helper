package de.uniorg.ui5helper.settings;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.ui5.receive.CacheStorage;
import de.uniorg.ui5helper.ui5.receive.HttpsClient;
import de.uniorg.ui5helper.ui5.receive.Provider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Created by masch on 4/3/17.
 */
public class SettingsForm implements Configurable {
    private final Project project;
    private JComboBox<String> ui5Version;
    private JPanel panel;
    private JButton refreshVersions;

    public SettingsForm(@NotNull final Project project) {
        this.project = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "UI5 Helper";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {

        Provider apiProvider = new Provider(new CacheStorage(PathManager.getSystemPath() + "/caches"), new HttpsClient());

        refreshVersions.setEnabled(false);
        ui5Version.removeAllItems();
        apiProvider.getAvailableVersions(this::onNewVersion);

        refreshVersions.addActionListener(actionEvent -> {
            refreshVersions.setEnabled(false);
            ui5Version.removeAllItems();
            apiProvider.refreshAvailableVersions(this::onNewVersion);
        });

        return panel;
    }

    private void onNewVersion(List<String> versions) {
        String selectedVersion = getSettings().ui5Version;
        versions.forEach(version -> {
            ui5Version.addItem(version);
            if (version.equals(selectedVersion)) {
                ui5Version.setSelectedItem(version);
            }
        });
        ui5Version.setEnabled(true);
        refreshVersions.setEnabled(true);
    }

    @Override
    public boolean isModified() {
        return !getSelectedVersion().equals(getSettings().ui5Version);
    }

    private void updateViewFromSettings() {
        String selectedVersion = getSettings().ui5Version;
        int items = ui5Version.getItemCount();
        for (int i = 0; i < items; i++) {
            if (ui5Version.getItemAt(i).equals(selectedVersion)) {
                ui5Version.setSelectedIndex(i);
                break;
            }
        }
    }

    private String getSelectedVersion() {
        return (String) ui5Version.getSelectedItem();
    }

    private Settings getSettings() {
        return Settings.getInstance(this.project);
    }

    @Override
    public void apply() throws ConfigurationException {
        if (!getSettings().ui5Version.equals(getSelectedVersion())) {
            this.project.getComponent(ProjectComponent.class).changeApiVersion(getSelectedVersion());
        }

        getSettings().ui5Version = getSelectedVersion();

    }
}
