package de.uniorg.ui5helper.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import de.uniorg.ui5helper.remote.Ui5InfoService;
import de.uniorg.ui5helper.remote.Ui5Version;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by masch on 4/3/17.
 */
public class SettingsForm implements Configurable {
    private final Project project;
    private JComboBox<Ui5Version> ui5Version;
    private JCheckBox collapsControllerName;
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

        refreshVersions.addActionListener(actionEvent -> {
            String selectedVersion = getSettings().ui5Version;

            Runnable refresh = () -> {
                try {
                    refreshVersions.setEnabled(false);
                    ui5Version.setEnabled(false);
                    ui5Version.removeAllItems();
                    List<Ui5Version> versions = Ui5InfoService.getVersions();
                    versions.forEach(version -> {
                        ui5Version.addItem(version);
                        if (version.getText().equals(selectedVersion)) {
                            ui5Version.setSelectedItem(version);
                        }
                    });

                    ui5Version.setEnabled(true);
                    refreshVersions.setEnabled(true);
                } catch (IOException iox) {
                    System.err.println(iox.getLocalizedMessage());
                    refreshVersions.setEnabled(true);
                }
            };

            new Thread(refresh).start();
        });

        return panel;
    }

    @Override
    public boolean isModified() {
        return !getSelectedVersion().equals(getSettings().ui5Version);
    }

    private void updateViewFromSettings()
    {
        String selectedVersion = getSettings().ui5Version;
        int items = ui5Version.getItemCount();
        for (int i = 0; i < items; i++) {
            if (ui5Version.getItemAt(i).getText().equals(selectedVersion)) {
                ui5Version.setSelectedIndex(i);
                break;
            }
        }
    }

    private String getSelectedVersion() {
        if (ui5Version.getSelectedItem() instanceof Ui5Version) {
            return ((Ui5Version) ui5Version.getSelectedItem()).getText();
        }

        return (String) ui5Version.getSelectedItem();
    }

    private Settings getSettings() {
        return Settings.getInstance(this.project);
    }

    @Override
    public void apply() throws ConfigurationException {
        getSettings().ui5Version = getSelectedVersion();
    }
}
