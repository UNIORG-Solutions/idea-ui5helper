package de.uniorg.ui5helper.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.uniorg.ui5helper.ProjectComponent;
import de.uniorg.ui5helper.cache.CacheStorage;
import de.uniorg.ui5helper.cache.SdkVersionManager;
import de.uniorg.ui5helper.ui5.receive.HttpsClient;
import de.uniorg.ui5helper.ui5.receive.Provider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by masch on 4/3/17.
 */
public class SettingsForm implements Configurable {
    private final Project project;
    private JComboBox<String> ui5Version;
    private JPanel panel;
    private JButton refreshVersions;
    private JCheckBox pluginEnabled;
    private JTabbedPane tabbedPane1;
    private JCheckBox collapseControllerName;
    private JCheckBox bindingSyntaxSupport;
    private JCheckBox xmlDocumentation;
    private JCheckBox checkImportedFileReferencesCheckBox;
    private JButton downloadSources;
    private JCheckBox xmlGutterIconCheckbox;
    private SdkVersionManager versionManager;

    public SettingsForm(@NotNull final Project project) {
        this.project = project;
        versionManager = SdkVersionManager.getInstance();
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "UI5 Helper";
    }

    /**
     * Returns the topic in the help file which is shown when help for the configurable is requested.
     *
     * @return the help topic, or {@code null} if no help is available
     */
    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {

        Provider apiProvider = new Provider(CacheStorage.getInstance(), new HttpsClient());

        refreshVersions.setEnabled(false);
        ui5Version.removeAllItems();
        apiProvider.getAvailableVersions(this::onNewVersion);

        refreshVersions.addActionListener(actionEvent -> {
            refreshVersions.setEnabled(false);
            ui5Version.removeAllItems();
            apiProvider.refreshAvailableVersions(this::onNewVersion);
        });

        ui5Version.addActionListener(actionEvent -> {
            String selectedVersion = getSelectedVersion();
            if (selectedVersion == null) {
                return;
            }
            if (versionManager.has(selectedVersion)) {
                downloadSources.setVisible(false);
                downloadSources.setEnabled(false);
            } else {
                downloadSources.setVisible(true);
                downloadSources.setEnabled(true);
            }
        });

        downloadSources.addActionListener(actionEvent -> {
            Task.Backgroundable dl = versionManager.download(getSelectedVersion(), file -> {
                ProjectComponent.rebuildIndexes();
            });
            ProgressManager.getInstance().run(dl);
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
        String selectedVersion = getSelectedVersion();
        return !(selectedVersion != null && selectedVersion.equals(getSettings().ui5Version))
                || pluginEnabled.isSelected() != getSettings().pluginEnabled
                || collapseControllerName.isSelected() != getSettings().foldControllerName
                || bindingSyntaxSupport.isSelected() != getSettings().injectBindingLanguage
                || xmlDocumentation.isSelected() != getSettings().xmlDocumentation
                || checkImportedFileReferencesCheckBox.isSelected() != getSettings().jsFileImportReference
                || xmlGutterIconCheckbox.isSelected() != getSettings().xmlGutterIcon;
    }

    /**
     * Loads the settings from the configurable component to the Swing form.
     * This method is called on EDT immediately after the form creation or later upon user's request.
     */
    @Override
    public void reset() {
        this.updateViewFromSettings();
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

        pluginEnabled.setSelected(getSettings().pluginEnabled);
        collapseControllerName.setSelected(getSettings().foldControllerName);
        bindingSyntaxSupport.setSelected(getSettings().injectBindingLanguage);
        xmlDocumentation.setSelected(getSettings().xmlDocumentation);
        xmlGutterIconCheckbox.setSelected(getSettings().xmlGutterIcon);
        checkImportedFileReferencesCheckBox.setSelected(getSettings().jsFileImportReference);
    }

    private String getSelectedVersion() {
        return (String) ui5Version.getSelectedItem();
    }

    private Settings getSettings() {
        return Settings.getInstance(this.project);
    }

    @Override
    public void apply() throws ConfigurationException {
        Settings settings = getSettings();
        if (!settings.ui5Version.equals(getSelectedVersion())) {
            this.project.getComponent(ProjectComponent.class).changeApiVersion(getSelectedVersion());
        }

        boolean wasEnabled = settings.pluginEnabled;
        settings.ui5Version = getSelectedVersion();
        settings.pluginEnabled = pluginEnabled.isSelected();
        settings.foldControllerName = collapseControllerName.isSelected();
        settings.injectBindingLanguage = bindingSyntaxSupport.isSelected();
        settings.xmlDocumentation = xmlDocumentation.isSelected();
        settings.xmlGutterIcon = xmlGutterIconCheckbox.isSelected();
        settings.jsFileImportReference = checkImportedFileReferencesCheckBox.isSelected();

        if (pluginEnabled.isSelected() && !wasEnabled) {
            this.project.getComponent(ProjectComponent.class).projectEnabled();
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("UI5 Version");
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ui5Version = new JComboBox();
        ui5Version.setEnabled(false);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("latest");
        ui5Version.setModel(defaultComboBoxModel1);
        panel1.add(ui5Version, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        refreshVersions = new JButton();
        refreshVersions.setIcon(new ImageIcon(getClass().getResource("/actions/refresh.png")));
        refreshVersions.setText("");
        panel1.add(refreshVersions, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pluginEnabled = new JCheckBox();
        pluginEnabled.setSelected(false);
        pluginEnabled.setText("");
        panel1.add(pluginEnabled, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Enabled");
        panel1.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        downloadSources = new JButton();
        downloadSources.setEnabled(false);
        downloadSources.setText("Download sources");
        panel1.add(downloadSources, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabbedPane1 = new JTabbedPane();
        panel.add(tabbedPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("XML Views", panel2);
        collapseControllerName = new JCheckBox();
        collapseControllerName.setText("Collapse controller name");
        panel2.add(collapseControllerName, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        bindingSyntaxSupport = new JCheckBox();
        bindingSyntaxSupport.setText("Enhanced binding syntax support");
        bindingSyntaxSupport.setToolTipText("Use own parser & lexer to read bindings");
        panel2.add(bindingSyntaxSupport, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xmlDocumentation = new JCheckBox();
        xmlDocumentation.setText("Documentation");
        panel2.add(xmlDocumentation, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xmlGutterIconCheckbox = new JCheckBox();
        xmlGutterIconCheckbox.setText("SAP-Icon preview");
        panel2.add(xmlGutterIconCheckbox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("JavaScript", panel3);
        checkImportedFileReferencesCheckBox = new JCheckBox();
        checkImportedFileReferencesCheckBox.setText("Check imported file references (Experimental)");
        panel3.add(checkImportedFileReferencesCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
