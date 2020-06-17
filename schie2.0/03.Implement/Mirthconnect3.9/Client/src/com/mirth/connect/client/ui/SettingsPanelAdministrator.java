/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.core.TaskConstants;
import com.mirth.connect.client.ui.components.KeyStrokeTextField;
import com.mirth.connect.client.ui.components.MirthCheckBox;
import com.mirth.connect.client.ui.components.MirthFieldConstraints;
import com.mirth.connect.client.ui.components.MirthRadioButton;
import com.mirth.connect.client.ui.components.MirthTable;
import com.mirth.connect.client.ui.components.MirthTextField;
import com.mirth.connect.client.ui.components.rsta.AutoCompleteProperties;
import com.mirth.connect.client.ui.components.rsta.MirthRSyntaxTextArea;
import com.mirth.connect.client.ui.components.rsta.RSTAPreferences;
import com.mirth.connect.client.ui.components.rsta.actions.ActionInfo;
import com.mirth.connect.model.User;
import com.mirth.connect.model.converters.ObjectXMLSerializer;

public class SettingsPanelAdministrator extends AbstractSettingsPanel {

    public static final String TAB_NAME = Messages.getString("SettingsPanelAdministrator.0"); //$NON-NLS-1$

    private static final int ACTION_INFO_COLUMN = 0;
    private static final int NAME_COLUMN = 1;
    private static final int DESCRIPTION_COLUMN = 2;
    private static final int KEY_COLUMN = 3;

    private static Preferences userPreferences;

    private User currentUser = getFrame().getCurrentUser(getFrame());
    private List<ActionInfo> shortcutKeyList;

    public SettingsPanelAdministrator(String tabName) {
        super(tabName);

        shortcutKeyList = new ArrayList<ActionInfo>();
        ResourceBundle resourceBundle = MirthRSyntaxTextArea.getResourceBundle();
        for (ActionInfo actionInfo : ActionInfo.values()) {
            if (!BooleanUtils.toBoolean(resourceBundle.getString(actionInfo.toString() + Messages.getString("SettingsPanelAdministrator.1")))) { //$NON-NLS-1$
                shortcutKeyList.add(actionInfo);
            }
        }

        initComponents();
        initLayout();

        addTask(TaskConstants.SETTINGS_ADMIN_DEFAULTS, Messages.getString("SettingsPanelAdministrator.2"), Messages.getString("SettingsPanelAdministrator.3"), Messages.getString("SettingsPanelAdministrator.4"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("SettingsPanelAdministrator.5")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    public void doRefresh() {
        if (PlatformUI.MIRTH_FRAME.alertRefresh()) {
            return;
        }

        dashboardRefreshIntervalField.setDocument(new MirthFieldConstraints(3, false, false, true));
        messageBrowserPageSizeField.setDocument(new MirthFieldConstraints(3, false, false, true));
        eventBrowserPageSizeField.setDocument(new MirthFieldConstraints(3, false, false, true));
        userPreferences = Preferences.userNodeForPackage(Mirth.class);
        int interval = userPreferences.getInt(Messages.getString("SettingsPanelAdministrator.6"), 10); //$NON-NLS-1$
        dashboardRefreshIntervalField.setText(interval + Messages.getString("SettingsPanelAdministrator.7")); //$NON-NLS-1$

        int messageBrowserPageSize = userPreferences.getInt(Messages.getString("SettingsPanelAdministrator.8"), 20); //$NON-NLS-1$
        messageBrowserPageSizeField.setText(messageBrowserPageSize + Messages.getString("SettingsPanelAdministrator.9")); //$NON-NLS-1$

        int eventBrowserPageSize = userPreferences.getInt(Messages.getString("SettingsPanelAdministrator.10"), 100); //$NON-NLS-1$
        eventBrowserPageSizeField.setText(eventBrowserPageSize + Messages.getString("SettingsPanelAdministrator.11")); //$NON-NLS-1$

        if (userPreferences.getBoolean(Messages.getString("SettingsPanelAdministrator.12"), true)) { //$NON-NLS-1$
            formatYesRadio.setSelected(true);
        } else {
            formatNoRadio.setSelected(true);
        }

        if (userPreferences.getBoolean(Messages.getString("SettingsPanelAdministrator.13"), true)) { //$NON-NLS-1$
            textSearchWarningYesRadio.setSelected(true);
        } else {
            textSearchWarningNoRadio.setSelected(true);
        }

        if (userPreferences.getBoolean(Messages.getString("SettingsPanelAdministrator.14"), true)) { //$NON-NLS-1$
            filterTransformerShowIteratorYesRadio.setSelected(true);
        } else {
            filterTransformerShowIteratorNoRadio.setSelected(true);
        }

        if (userPreferences.getBoolean(Messages.getString("SettingsPanelAdministrator.15"), true)) { //$NON-NLS-1$
            messageBrowserShowAttachmentTypeDialogYesRadio.setSelected(true);
        } else {
            messageBrowserShowAttachmentTypeDialogNoRadio.setSelected(true);
        }

        if (userPreferences.getBoolean(Messages.getString("SettingsPanelAdministrator.16"), true)) { //$NON-NLS-1$
            reprocessRemoveMessagesWarningYesRadio.setSelected(true);
        } else {
            reprocessRemoveMessagesWarningNoRadio.setSelected(true);
        }

        String importChannelCodeTemplateLibraries = userPreferences.get(Messages.getString("SettingsPanelAdministrator.17"), null); //$NON-NLS-1$
        if (importChannelCodeTemplateLibraries == null) {
            importChannelLibrariesAskRadio.setSelected(true);
        } else if (Boolean.parseBoolean(importChannelCodeTemplateLibraries)) {
            importChannelLibrariesYesRadio.setSelected(true);
        } else {
            importChannelLibrariesNoRadio.setSelected(true);
        }

        String exportChannelCodeTemplateLibraries = userPreferences.get(Messages.getString("SettingsPanelAdministrator.18"), null); //$NON-NLS-1$
        if (exportChannelCodeTemplateLibraries == null) {
            exportChannelLibrariesAskRadio.setSelected(true);
        } else if (Boolean.parseBoolean(exportChannelCodeTemplateLibraries)) {
            exportChannelLibrariesYesRadio.setSelected(true);
        } else {
            exportChannelLibrariesNoRadio.setSelected(true);
        }

        final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelAdministrator.19") + getTabName() + Messages.getString("SettingsPanelAdministrator.20")); //$NON-NLS-1$ //$NON-NLS-2$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            private String checkForNotifications = null;
            private Color backgroundColor = null;

            public Void doInBackground() {
                try {
                    checkForNotifications = getFrame().mirthClient.getUserPreference(currentUser.getId(), Messages.getString("SettingsPanelAdministrator.21")); //$NON-NLS-1$
                } catch (ClientException e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                try {
                    String backgroundColorStr = getFrame().mirthClient.getUserPreference(currentUser.getId(), UIConstants.USER_PREF_KEY_BACKGROUND_COLOR);
                    if (StringUtils.isNotBlank(backgroundColorStr)) {
                        backgroundColor = ObjectXMLSerializer.getInstance().deserialize(backgroundColorStr, Color.class);
                    }
                } catch (Exception e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                return null;
            }

            @Override
            public void done() {
                if (checkForNotifications == null || BooleanUtils.toBoolean(checkForNotifications)) {
                    checkForNotificationsYesRadio.setSelected(true);
                } else {
                    checkForNotificationsNoRadio.setSelected(true);
                }

                if (backgroundColor != null) {
                    backgroundColorCustomRadio.setSelected(true);
                    backgroundColorRadioActionPerformed(false);
                    backgroundColorButton.setBackground(backgroundColor);
                } else {
                    backgroundColorServerDefaultRadio.setSelected(true);
                    backgroundColorRadioActionPerformed(true);
                }

                getFrame().stopWorking(workingId);
            }
        };

        worker.execute();

        RSTAPreferences rstaPreferences = MirthRSyntaxTextArea.getRSTAPreferences();
        updateShortcutKeyTable(rstaPreferences);
        updateRestoreKeyDefaultsButton();

        AutoCompleteProperties autoCompleteProperties = rstaPreferences.getAutoCompleteProperties();
        autoCompleteIncludeLettersCheckBox.setSelected(autoCompleteProperties.isActivateAfterLetters());
        autoCompleteCharactersField.setText(autoCompleteProperties.getActivateAfterOthers());
        autoCompleteDelayField.setText(String.valueOf(autoCompleteProperties.getActivationDelay()));
    }

    public boolean doSave() {
        if (dashboardRefreshIntervalField.getText().length() == 0) {
            getFrame().alertWarning(this, Messages.getString("SettingsPanelAdministrator.22")); //$NON-NLS-1$
            return false;
        }
        if (messageBrowserPageSizeField.getText().length() == 0) {
            getFrame().alertWarning(this, Messages.getString("SettingsPanelAdministrator.23")); //$NON-NLS-1$
            return false;
        }
        if (eventBrowserPageSizeField.getText().length() == 0) {
            getFrame().alertWarning(this, Messages.getString("SettingsPanelAdministrator.24")); //$NON-NLS-1$
            return false;
        }

        if (autoCompleteDelayField.isEnabled() && StringUtils.isBlank(autoCompleteDelayField.getText())) {
            getFrame().alertWarning(this, Messages.getString("SettingsPanelAdministrator.25")); //$NON-NLS-1$
            return false;
        }

        int interval = Integer.parseInt(dashboardRefreshIntervalField.getText());
        int messageBrowserPageSize = Integer.parseInt(messageBrowserPageSizeField.getText());
        int eventBrowserPageSize = Integer.parseInt(eventBrowserPageSizeField.getText());

        if (interval <= 0) {
            getFrame().alertWarning(this, Messages.getString("SettingsPanelAdministrator.26")); //$NON-NLS-1$
        } else if (messageBrowserPageSize <= 0) {
            getFrame().alertWarning(this, Messages.getString("SettingsPanelAdministrator.27")); //$NON-NLS-1$
        } else if (eventBrowserPageSize <= 0) {
            getFrame().alertWarning(this, Messages.getString("SettingsPanelAdministrator.28")); //$NON-NLS-1$
        } else {
            userPreferences.putInt(Messages.getString("SettingsPanelAdministrator.29"), interval); //$NON-NLS-1$
            userPreferences.putInt(Messages.getString("SettingsPanelAdministrator.30"), messageBrowserPageSize); //$NON-NLS-1$
            userPreferences.putInt(Messages.getString("SettingsPanelAdministrator.31"), eventBrowserPageSize); //$NON-NLS-1$
            userPreferences.putBoolean(Messages.getString("SettingsPanelAdministrator.32"), formatYesRadio.isSelected()); //$NON-NLS-1$
            userPreferences.putBoolean(Messages.getString("SettingsPanelAdministrator.33"), textSearchWarningYesRadio.isSelected()); //$NON-NLS-1$
            userPreferences.putBoolean(Messages.getString("SettingsPanelAdministrator.34"), filterTransformerShowIteratorYesRadio.isSelected()); //$NON-NLS-1$
            userPreferences.putBoolean(Messages.getString("SettingsPanelAdministrator.35"), messageBrowserShowAttachmentTypeDialogYesRadio.isSelected()); //$NON-NLS-1$
            userPreferences.putBoolean(Messages.getString("SettingsPanelAdministrator.36"), reprocessRemoveMessagesWarningYesRadio.isSelected()); //$NON-NLS-1$

            if (importChannelLibrariesAskRadio.isSelected()) {
                userPreferences.remove(Messages.getString("SettingsPanelAdministrator.37")); //$NON-NLS-1$
            } else {
                userPreferences.putBoolean(Messages.getString("SettingsPanelAdministrator.38"), importChannelLibrariesYesRadio.isSelected()); //$NON-NLS-1$
            }

            if (exportChannelLibrariesAskRadio.isSelected()) {
                userPreferences.remove(Messages.getString("SettingsPanelAdministrator.39")); //$NON-NLS-1$
            } else {
                userPreferences.putBoolean(Messages.getString("SettingsPanelAdministrator.40"), exportChannelLibrariesYesRadio.isSelected()); //$NON-NLS-1$
            }
        }

        final Color backgroundColor;
        if (backgroundColorServerDefaultRadio.isSelected()) {
            backgroundColor = null;
        } else {
            backgroundColor = backgroundColorButton.getBackground();
        }

        if (backgroundColor != null) {
            getFrame().setupBackgroundPainters(backgroundColor);
        } else {
            getFrame().setupBackgroundPainters(PlatformUI.DEFAULT_BACKGROUND_COLOR);
        }

        final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelAdministrator.41") + getTabName() + Messages.getString("SettingsPanelAdministrator.42")); //$NON-NLS-1$ //$NON-NLS-2$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                try {
                    getFrame().mirthClient.setUserPreference(currentUser.getId(), Messages.getString("SettingsPanelAdministrator.43"), Boolean.toString(checkForNotificationsYesRadio.isSelected())); //$NON-NLS-1$
                } catch (ClientException e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                try {
                    getFrame().mirthClient.setUserPreference(currentUser.getId(), UIConstants.USER_PREF_KEY_BACKGROUND_COLOR, ObjectXMLSerializer.getInstance().serialize(backgroundColor));
                } catch (ClientException e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                return null;
            }

            @Override
            public void done() {
                getFrame().setSaveEnabled(false);
                getFrame().stopWorking(workingId);
            }
        };

        worker.execute();

        RSTAPreferences rstaPreferences = MirthRSyntaxTextArea.getRSTAPreferences();
        for (int row = 0; row < shortcutKeyTable.getRowCount(); row++) {
            ActionInfo actionInfo = (ActionInfo) shortcutKeyTable.getModel().getValueAt(row, ACTION_INFO_COLUMN);
            KeyStroke keyStroke = (KeyStroke) shortcutKeyTable.getModel().getValueAt(row, KEY_COLUMN);
            rstaPreferences.getKeyStrokeMap().put(actionInfo.getActionMapKey(), keyStroke);
        }
        MirthRSyntaxTextArea.updateKeyStrokePreferences(userPreferences);

        AutoCompleteProperties autoCompleteProperties = rstaPreferences.getAutoCompleteProperties();
        autoCompleteProperties.setActivateAfterLetters(autoCompleteIncludeLettersCheckBox.isSelected());
        autoCompleteProperties.setActivateAfterOthers(autoCompleteCharactersField.getText());
        autoCompleteProperties.setActivationDelay(NumberUtils.toInt(autoCompleteDelayField.getText()));
        MirthRSyntaxTextArea.updateAutoCompletePreferences(userPreferences);

        return true;
    }

    public void doSetAdminDefaults() {
        if (!getFrame().alertOkCancel(this, Messages.getString("SettingsPanelAdministrator.44"))) { //$NON-NLS-1$
            return;
        }

        // remove all userPreferences
        try {
            String[] keys = userPreferences.keys();
            for (String key : keys) {
                userPreferences.remove(key);
            }
        } catch (Exception e) {
        }

        RSTAPreferences newRstaPreferences = new RSTAPreferences();
        MirthRSyntaxTextArea.getRSTAPreferences().setAutoCompleteProperties(newRstaPreferences.getAutoCompleteProperties());
        MirthRSyntaxTextArea.getRSTAPreferences().setFindReplaceProperties(newRstaPreferences.getFindReplaceProperties());
        MirthRSyntaxTextArea.getRSTAPreferences().setKeyStrokeMap(newRstaPreferences.getKeyStrokeMap());
        MirthRSyntaxTextArea.getRSTAPreferences().setToggleOptions(newRstaPreferences.getToggleOptions());
        MirthRSyntaxTextArea.updateAutoCompletePreferences(userPreferences);
        MirthRSyntaxTextArea.updateFindReplacePreferences(userPreferences);
        MirthRSyntaxTextArea.updateKeyStrokePreferences(userPreferences);
        MirthRSyntaxTextArea.updateToggleOptionPreferences(userPreferences);

        final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelAdministrator.45") + getTabName() + Messages.getString("SettingsPanelAdministrator.46")); //$NON-NLS-1$ //$NON-NLS-2$
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                try {
                    getFrame().mirthClient.setUserPreference(currentUser.getId(), Messages.getString("SettingsPanelAdministrator.47"), Boolean.toString(true)); //$NON-NLS-1$
                } catch (ClientException e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                try {
                    getFrame().mirthClient.setUserPreference(currentUser.getId(), UIConstants.USER_PREF_KEY_BACKGROUND_COLOR, ObjectXMLSerializer.getInstance().serialize(null));
                } catch (ClientException e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                return null;
            }

            @Override
            public void done() {
                getFrame().setSaveEnabled(false);
                doRefresh();
                getFrame().stopWorking(workingId);
            }
        };
        worker.execute();
    }

    private void updateRestoreKeyDefaultsButton() {
        boolean isDefault = true;
        Map<String, KeyStroke> defaultKeyStrokeMap = new RSTAPreferences().getKeyStrokeMap();

        for (int row = 0; row < shortcutKeyTable.getRowCount(); row++) {
            ActionInfo actionInfo = (ActionInfo) shortcutKeyTable.getModel().getValueAt(row, ACTION_INFO_COLUMN);
            KeyStroke keyStroke = (KeyStroke) shortcutKeyTable.getModel().getValueAt(row, KEY_COLUMN);

            if (!ObjectUtils.equals(keyStroke, defaultKeyStrokeMap.get(actionInfo.getActionMapKey()))) {
                isDefault = false;
                break;
            }
        }

        restoreDefaultsButton.setEnabled(!isDefault);
    }

    private void restoreShortcutKeyDefaults() {
        if (PlatformUI.MIRTH_FRAME.alertOkCancel(PlatformUI.MIRTH_FRAME, Messages.getString("SettingsPanelAdministrator.48"))) { //$NON-NLS-1$
            updateShortcutKeyTable(new RSTAPreferences());
            restoreDefaultsButton.setEnabled(false);
            PlatformUI.MIRTH_FRAME.setSaveEnabled(true);
        }
    }

    private void updateShortcutKeyTable(RSTAPreferences rstaPreferences) {
        ResourceBundle resourceBundle = MirthRSyntaxTextArea.getResourceBundle();
        Object[][] data = new Object[shortcutKeyList.size()][4];
        int i = 0;

        for (ActionInfo actionInfo : shortcutKeyList) {
            data[i][ACTION_INFO_COLUMN] = actionInfo;
            data[i][NAME_COLUMN] = resourceBundle.getString(actionInfo.toString() + Messages.getString("SettingsPanelAdministrator.49")); //$NON-NLS-1$
            data[i][DESCRIPTION_COLUMN] = resourceBundle.getString(actionInfo.toString() + Messages.getString("SettingsPanelAdministrator.50")); //$NON-NLS-1$
            data[i][KEY_COLUMN] = rstaPreferences.getKeyStrokeMap().get(actionInfo.getActionMapKey());
            i++;
        }

        ((RefreshTableModel) shortcutKeyTable.getModel()).refreshDataVector(data);
    }

    private void autoCompleteActionPerformed() {
        boolean enabled = StringUtils.isNotEmpty(autoCompleteCharactersField.getText()) || autoCompleteIncludeLettersCheckBox.isSelected();
        autoCompleteDelayLabel.setEnabled(enabled);
        autoCompleteDelayField.setEnabled(enabled);
    }

    private void initComponents() {
        setBackground(UIConstants.BACKGROUND_COLOR);

        systemSettingsPanel = new JPanel();
        systemSettingsPanel.setBackground(getBackground());
        systemSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(204, 204, 204)), Messages.getString("SettingsPanelAdministrator.51"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(Messages.getString("SettingsPanelAdministrator.52"), 1, 11))); //$NON-NLS-1$ //$NON-NLS-2$

        dashboardRefreshIntervalLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.53")); //$NON-NLS-1$
        dashboardRefreshIntervalField = new MirthTextField();
        dashboardRefreshIntervalField.setToolTipText(Messages.getString("SettingsPanelAdministrator.54")); //$NON-NLS-1$

        String toolTipText = Messages.getString("SettingsPanelAdministrator.55"); //$NON-NLS-1$
        messageBrowserPageSizeLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.56")); //$NON-NLS-1$
        messageBrowserPageSizeField = new MirthTextField();
        messageBrowserPageSizeField.setToolTipText(toolTipText);

        eventBrowserPageSizeLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.57")); //$NON-NLS-1$
        eventBrowserPageSizeField = new MirthTextField();
        eventBrowserPageSizeField.setToolTipText(toolTipText);

        formatLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.58")); //$NON-NLS-1$
        formatButtonGroup = new ButtonGroup();

        toolTipText = Messages.getString("SettingsPanelAdministrator.59"); //$NON-NLS-1$
        formatYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.60")); //$NON-NLS-1$
        formatYesRadio.setBackground(systemSettingsPanel.getBackground());
        formatYesRadio.setToolTipText(toolTipText);
        formatButtonGroup.add(formatYesRadio);

        formatNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.61")); //$NON-NLS-1$
        formatNoRadio.setBackground(systemSettingsPanel.getBackground());
        formatNoRadio.setToolTipText(toolTipText);
        formatButtonGroup.add(formatNoRadio);

        textSearchWarningLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.62")); //$NON-NLS-1$
        textSearchWarningButtonGroup = new ButtonGroup();

        toolTipText = Messages.getString("SettingsPanelAdministrator.63"); //$NON-NLS-1$
        textSearchWarningYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.64")); //$NON-NLS-1$
        textSearchWarningYesRadio.setBackground(systemSettingsPanel.getBackground());
        textSearchWarningYesRadio.setToolTipText(toolTipText);
        textSearchWarningButtonGroup.add(textSearchWarningYesRadio);

        textSearchWarningNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.65")); //$NON-NLS-1$
        textSearchWarningNoRadio.setBackground(systemSettingsPanel.getBackground());
        textSearchWarningNoRadio.setToolTipText(toolTipText);
        textSearchWarningButtonGroup.add(textSearchWarningNoRadio);

        filterTransformerShowIteratorLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.66")); //$NON-NLS-1$
        filterTransformerShowIteratorButtonGroup = new ButtonGroup();

        toolTipText = Messages.getString("SettingsPanelAdministrator.67"); //$NON-NLS-1$
        filterTransformerShowIteratorYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.68")); //$NON-NLS-1$
        filterTransformerShowIteratorYesRadio.setBackground(systemSettingsPanel.getBackground());
        filterTransformerShowIteratorYesRadio.setToolTipText(toolTipText);
        filterTransformerShowIteratorButtonGroup.add(filterTransformerShowIteratorYesRadio);

        filterTransformerShowIteratorNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.69")); //$NON-NLS-1$
        filterTransformerShowIteratorNoRadio.setBackground(systemSettingsPanel.getBackground());
        filterTransformerShowIteratorNoRadio.setToolTipText(toolTipText);
        filterTransformerShowIteratorButtonGroup.add(filterTransformerShowIteratorNoRadio);

        messageBrowserShowAttachmentTypeDialogLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.70")); //$NON-NLS-1$
        messageBrowserShowAttachmentTypeDialogButtonGroup = new ButtonGroup();

        toolTipText = Messages.getString("SettingsPanelAdministrator.71"); //$NON-NLS-1$
        messageBrowserShowAttachmentTypeDialogYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.72")); //$NON-NLS-1$
        messageBrowserShowAttachmentTypeDialogYesRadio.setBackground(systemSettingsPanel.getBackground());
        messageBrowserShowAttachmentTypeDialogYesRadio.setToolTipText(toolTipText);
        messageBrowserShowAttachmentTypeDialogButtonGroup.add(messageBrowserShowAttachmentTypeDialogYesRadio);

        messageBrowserShowAttachmentTypeDialogNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.73")); //$NON-NLS-1$
        messageBrowserShowAttachmentTypeDialogNoRadio.setBackground(systemSettingsPanel.getBackground());
        messageBrowserShowAttachmentTypeDialogNoRadio.setToolTipText(toolTipText);
        messageBrowserShowAttachmentTypeDialogButtonGroup.add(messageBrowserShowAttachmentTypeDialogNoRadio);

        reprocessRemoveMessagesWarningLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.74")); //$NON-NLS-1$
        ButtonGroup reprocessRemoveMessagesWarningButtonGroup = new ButtonGroup();
        toolTipText = Messages.getString("SettingsPanelAdministrator.75"); //$NON-NLS-1$

        reprocessRemoveMessagesWarningYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.76")); //$NON-NLS-1$
        reprocessRemoveMessagesWarningYesRadio.setBackground(systemSettingsPanel.getBackground());
        reprocessRemoveMessagesWarningYesRadio.setToolTipText(toolTipText);
        reprocessRemoveMessagesWarningButtonGroup.add(reprocessRemoveMessagesWarningYesRadio);

        reprocessRemoveMessagesWarningNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.77")); //$NON-NLS-1$
        reprocessRemoveMessagesWarningNoRadio.setBackground(systemSettingsPanel.getBackground());
        reprocessRemoveMessagesWarningNoRadio.setToolTipText(toolTipText);
        reprocessRemoveMessagesWarningButtonGroup.add(reprocessRemoveMessagesWarningNoRadio);

        importChannelLibrariesLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.78")); //$NON-NLS-1$
        importChannelLibrariesButtonGroup = new ButtonGroup();

        toolTipText = Messages.getString("SettingsPanelAdministrator.79"); //$NON-NLS-1$
        importChannelLibrariesYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.80")); //$NON-NLS-1$
        importChannelLibrariesYesRadio.setBackground(systemSettingsPanel.getBackground());
        importChannelLibrariesYesRadio.setToolTipText(toolTipText);
        importChannelLibrariesButtonGroup.add(importChannelLibrariesYesRadio);

        importChannelLibrariesNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.81")); //$NON-NLS-1$
        importChannelLibrariesNoRadio.setBackground(systemSettingsPanel.getBackground());
        importChannelLibrariesNoRadio.setToolTipText(toolTipText);
        importChannelLibrariesButtonGroup.add(importChannelLibrariesNoRadio);

        importChannelLibrariesAskRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.82")); //$NON-NLS-1$
        importChannelLibrariesAskRadio.setBackground(systemSettingsPanel.getBackground());
        importChannelLibrariesAskRadio.setToolTipText(toolTipText);
        importChannelLibrariesButtonGroup.add(importChannelLibrariesAskRadio);

        exportChannelLibrariesLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.83")); //$NON-NLS-1$
        exportChannelLibrariesButtonGroup = new ButtonGroup();

        toolTipText = Messages.getString("SettingsPanelAdministrator.84"); //$NON-NLS-1$
        exportChannelLibrariesYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.85")); //$NON-NLS-1$
        exportChannelLibrariesYesRadio.setBackground(systemSettingsPanel.getBackground());
        exportChannelLibrariesYesRadio.setToolTipText(toolTipText);
        exportChannelLibrariesButtonGroup.add(exportChannelLibrariesYesRadio);

        exportChannelLibrariesNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.86")); //$NON-NLS-1$
        exportChannelLibrariesNoRadio.setBackground(systemSettingsPanel.getBackground());
        exportChannelLibrariesNoRadio.setToolTipText(toolTipText);
        exportChannelLibrariesButtonGroup.add(exportChannelLibrariesNoRadio);

        exportChannelLibrariesAskRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.87")); //$NON-NLS-1$
        exportChannelLibrariesAskRadio.setBackground(systemSettingsPanel.getBackground());
        exportChannelLibrariesAskRadio.setToolTipText(toolTipText);
        exportChannelLibrariesButtonGroup.add(exportChannelLibrariesAskRadio);

        userSettingsPanel = new JPanel();
        userSettingsPanel.setBackground(getBackground());
        userSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(204, 204, 204)), Messages.getString("SettingsPanelAdministrator.88"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(Messages.getString("SettingsPanelAdministrator.89"), 1, 11))); //$NON-NLS-1$ //$NON-NLS-2$

        checkForNotificationsLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.90")); //$NON-NLS-1$
        notificationButtonGroup = new ButtonGroup();

        checkForNotificationsYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.91")); //$NON-NLS-1$
        checkForNotificationsYesRadio.setBackground(userSettingsPanel.getBackground());
        checkForNotificationsYesRadio.setToolTipText(Messages.getString("SettingsPanelAdministrator.92")); //$NON-NLS-1$
        notificationButtonGroup.add(checkForNotificationsYesRadio);

        checkForNotificationsNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.93")); //$NON-NLS-1$
        checkForNotificationsNoRadio.setBackground(userSettingsPanel.getBackground());
        checkForNotificationsNoRadio.setToolTipText(Messages.getString("SettingsPanelAdministrator.94")); //$NON-NLS-1$
        notificationButtonGroup.add(checkForNotificationsNoRadio);

        backgroundColorLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.95")); //$NON-NLS-1$
        backgroundColorButtonGroup = new ButtonGroup();

        backgroundColorServerDefaultRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.96")); //$NON-NLS-1$
        backgroundColorServerDefaultRadio.setSelected(true);
        backgroundColorServerDefaultRadio.setBackground(userSettingsPanel.getBackground());
        backgroundColorServerDefaultRadio.setToolTipText(Messages.getString("SettingsPanelAdministrator.97")); //$NON-NLS-1$
        backgroundColorServerDefaultRadio.addActionListener(e -> backgroundColorRadioActionPerformed(true));
        backgroundColorButtonGroup.add(backgroundColorServerDefaultRadio);

        backgroundColorCustomRadio = new MirthRadioButton(Messages.getString("SettingsPanelAdministrator.98")); //$NON-NLS-1$
        backgroundColorCustomRadio.setBackground(userSettingsPanel.getBackground());
        backgroundColorCustomRadio.setToolTipText(Messages.getString("SettingsPanelAdministrator.99")); //$NON-NLS-1$
        backgroundColorCustomRadio.addActionListener(e -> backgroundColorRadioActionPerformed(false));
        backgroundColorButtonGroup.add(backgroundColorCustomRadio);

        backgroundColorButton = new JButton();
        backgroundColorButton.setEnabled(false);
        backgroundColorButton.setBackground(PlatformUI.DEFAULT_BACKGROUND_COLOR);
        backgroundColorButton.setToolTipText(Messages.getString("SettingsPanelAdministrator.100")); //$NON-NLS-1$
        backgroundColorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backgroundColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Color color = JColorChooser.showDialog(PlatformUI.MIRTH_FRAME, Messages.getString("SettingsPanelAdministrator.101"), backgroundColorButton.getBackground()); //$NON-NLS-1$
                if (color != null) {
                    backgroundColorButton.setBackground(color);
                    getFrame().setSaveEnabled(true);
                }
            }
        });

        codeEditorSettingsPanel = new JPanel();
        codeEditorSettingsPanel.setBackground(getBackground());
        codeEditorSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(204, 204, 204)), Messages.getString("SettingsPanelAdministrator.102"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(Messages.getString("SettingsPanelAdministrator.103"), 1, 11))); //$NON-NLS-1$ //$NON-NLS-2$

        toolTipText = Messages.getString("SettingsPanelAdministrator.104"); //$NON-NLS-1$
        autoCompleteCharactersLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.105")); //$NON-NLS-1$
        autoCompleteCharactersField = new MirthTextField();
        autoCompleteCharactersField.setToolTipText(toolTipText);
        autoCompleteCharactersField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent evt) {
                autoCompleteActionPerformed();
            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                autoCompleteActionPerformed();
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                autoCompleteActionPerformed();
            }
        });

        toolTipText = Messages.getString("SettingsPanelAdministrator.106"); //$NON-NLS-1$
        autoCompleteIncludeLettersCheckBox = new MirthCheckBox(Messages.getString("SettingsPanelAdministrator.107")); //$NON-NLS-1$
        autoCompleteIncludeLettersCheckBox.setBackground(codeEditorSettingsPanel.getBackground());
        autoCompleteIncludeLettersCheckBox.setToolTipText(toolTipText);
        autoCompleteIncludeLettersCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                autoCompleteActionPerformed();
            }
        });

        toolTipText = Messages.getString("SettingsPanelAdministrator.108"); //$NON-NLS-1$
        autoCompleteDelayLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.109")); //$NON-NLS-1$
        autoCompleteDelayField = new MirthTextField();
        autoCompleteDelayField.setToolTipText(toolTipText);
        autoCompleteDelayField.setDocument(new MirthFieldConstraints(9, false, false, true));

        shortcutKeyLabel = new JLabel(Messages.getString("SettingsPanelAdministrator.110")); //$NON-NLS-1$

        shortcutKeyTable = new MirthTable();
        shortcutKeyTable.setModel(new RefreshTableModel(new Object[] { Messages.getString("SettingsPanelAdministrator.111"), Messages.getString("SettingsPanelAdministrator.112"), //$NON-NLS-1$ //$NON-NLS-2$
                Messages.getString("SettingsPanelAdministrator.113"), Messages.getString("SettingsPanelAdministrator.114") }, 0) { //$NON-NLS-1$ //$NON-NLS-2$
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == KEY_COLUMN;
            }
        });

        shortcutKeyTable.setDragEnabled(false);
        shortcutKeyTable.setRowSelectionAllowed(false);
        shortcutKeyTable.setRowHeight(UIConstants.ROW_HEIGHT);
        shortcutKeyTable.setFocusable(false);
        shortcutKeyTable.setOpaque(true);
        shortcutKeyTable.setSortable(true);

        if (Preferences.userNodeForPackage(Mirth.class).getBoolean(Messages.getString("SettingsPanelAdministrator.115"), true)) { //$NON-NLS-1$
            shortcutKeyTable.setHighlighters(HighlighterFactory.createAlternateStriping(UIConstants.HIGHLIGHTER_COLOR, UIConstants.BACKGROUND_COLOR));
        }

        shortcutKeyTable.getColumnModel().getColumn(NAME_COLUMN).setMinWidth(145);
        shortcutKeyTable.getColumnModel().getColumn(NAME_COLUMN).setPreferredWidth(145);

        shortcutKeyTable.getColumnModel().getColumn(DESCRIPTION_COLUMN).setPreferredWidth(600);

        shortcutKeyTable.getColumnModel().getColumn(KEY_COLUMN).setMinWidth(120);
        shortcutKeyTable.getColumnModel().getColumn(KEY_COLUMN).setPreferredWidth(150);
        shortcutKeyTable.getColumnModel().getColumn(KEY_COLUMN).setCellRenderer(new KeyStrokeCellRenderer());
        shortcutKeyTable.getColumnModel().getColumn(KEY_COLUMN).setCellEditor(new KeyStrokeCellEditor());

        shortcutKeyTable.removeColumn(shortcutKeyTable.getColumnModel().getColumn(ACTION_INFO_COLUMN));

        shortcutKeyTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent evt) {
                updateRestoreKeyDefaultsButton();
            }
        });

        shortcutKeyScrollPane = new JScrollPane(shortcutKeyTable);

        restoreDefaultsButton = new JButton(Messages.getString("SettingsPanelAdministrator.116")); //$NON-NLS-1$
        restoreDefaultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                restoreShortcutKeyDefaults();
            }
        });
    }

    private void initLayout() {
        setLayout(new MigLayout(Messages.getString("SettingsPanelAdministrator.117"), Messages.getString("SettingsPanelAdministrator.118"), Messages.getString("SettingsPanelAdministrator.119"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        systemSettingsPanel.setLayout(new MigLayout(Messages.getString("SettingsPanelAdministrator.120"), Messages.getString("SettingsPanelAdministrator.121"), Messages.getString("SettingsPanelAdministrator.122"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        systemSettingsPanel.add(dashboardRefreshIntervalLabel, Messages.getString("SettingsPanelAdministrator.123")); //$NON-NLS-1$
        systemSettingsPanel.add(dashboardRefreshIntervalField, Messages.getString("SettingsPanelAdministrator.124")); //$NON-NLS-1$
        systemSettingsPanel.add(messageBrowserPageSizeLabel, Messages.getString("SettingsPanelAdministrator.125")); //$NON-NLS-1$
        systemSettingsPanel.add(messageBrowserPageSizeField, Messages.getString("SettingsPanelAdministrator.126")); //$NON-NLS-1$
        systemSettingsPanel.add(eventBrowserPageSizeLabel, Messages.getString("SettingsPanelAdministrator.127")); //$NON-NLS-1$
        systemSettingsPanel.add(eventBrowserPageSizeField, Messages.getString("SettingsPanelAdministrator.128")); //$NON-NLS-1$
        systemSettingsPanel.add(formatLabel, Messages.getString("SettingsPanelAdministrator.129")); //$NON-NLS-1$
        systemSettingsPanel.add(formatYesRadio, Messages.getString("SettingsPanelAdministrator.130")); //$NON-NLS-1$
        systemSettingsPanel.add(formatNoRadio);
        systemSettingsPanel.add(textSearchWarningLabel, Messages.getString("SettingsPanelAdministrator.131")); //$NON-NLS-1$
        systemSettingsPanel.add(textSearchWarningYesRadio, Messages.getString("SettingsPanelAdministrator.132")); //$NON-NLS-1$
        systemSettingsPanel.add(textSearchWarningNoRadio);
        systemSettingsPanel.add(filterTransformerShowIteratorLabel, Messages.getString("SettingsPanelAdministrator.133")); //$NON-NLS-1$
        systemSettingsPanel.add(filterTransformerShowIteratorYesRadio, Messages.getString("SettingsPanelAdministrator.134")); //$NON-NLS-1$
        systemSettingsPanel.add(filterTransformerShowIteratorNoRadio);
        systemSettingsPanel.add(messageBrowserShowAttachmentTypeDialogLabel, Messages.getString("SettingsPanelAdministrator.135")); //$NON-NLS-1$
        systemSettingsPanel.add(messageBrowserShowAttachmentTypeDialogYesRadio, Messages.getString("SettingsPanelAdministrator.136")); //$NON-NLS-1$
        systemSettingsPanel.add(messageBrowserShowAttachmentTypeDialogNoRadio);
        systemSettingsPanel.add(reprocessRemoveMessagesWarningLabel, Messages.getString("SettingsPanelAdministrator.137")); //$NON-NLS-1$
        systemSettingsPanel.add(reprocessRemoveMessagesWarningYesRadio, Messages.getString("SettingsPanelAdministrator.138")); //$NON-NLS-1$
        systemSettingsPanel.add(reprocessRemoveMessagesWarningNoRadio);
        systemSettingsPanel.add(importChannelLibrariesLabel, Messages.getString("SettingsPanelAdministrator.139")); //$NON-NLS-1$
        systemSettingsPanel.add(importChannelLibrariesYesRadio, Messages.getString("SettingsPanelAdministrator.140")); //$NON-NLS-1$
        systemSettingsPanel.add(importChannelLibrariesNoRadio);
        systemSettingsPanel.add(importChannelLibrariesAskRadio);
        systemSettingsPanel.add(exportChannelLibrariesLabel, Messages.getString("SettingsPanelAdministrator.141")); //$NON-NLS-1$
        systemSettingsPanel.add(exportChannelLibrariesYesRadio, Messages.getString("SettingsPanelAdministrator.142")); //$NON-NLS-1$
        systemSettingsPanel.add(exportChannelLibrariesNoRadio);
        systemSettingsPanel.add(exportChannelLibrariesAskRadio);
        add(systemSettingsPanel, Messages.getString("SettingsPanelAdministrator.143")); //$NON-NLS-1$

        userSettingsPanel.setLayout(new MigLayout(Messages.getString("SettingsPanelAdministrator.144"), Messages.getString("SettingsPanelAdministrator.145"), Messages.getString("SettingsPanelAdministrator.146"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        userSettingsPanel.add(checkForNotificationsLabel, Messages.getString("SettingsPanelAdministrator.147")); //$NON-NLS-1$
        userSettingsPanel.add(checkForNotificationsYesRadio, Messages.getString("SettingsPanelAdministrator.148")); //$NON-NLS-1$
        userSettingsPanel.add(checkForNotificationsNoRadio);
        userSettingsPanel.add(backgroundColorLabel, Messages.getString("SettingsPanelAdministrator.149")); //$NON-NLS-1$
        userSettingsPanel.add(backgroundColorServerDefaultRadio, Messages.getString("SettingsPanelAdministrator.150")); //$NON-NLS-1$
        userSettingsPanel.add(backgroundColorCustomRadio);
        userSettingsPanel.add(backgroundColorButton, Messages.getString("SettingsPanelAdministrator.151")); //$NON-NLS-1$
        add(userSettingsPanel, Messages.getString("SettingsPanelAdministrator.152")); //$NON-NLS-1$

        codeEditorSettingsPanel.setLayout(new MigLayout(Messages.getString("SettingsPanelAdministrator.153"), Messages.getString("SettingsPanelAdministrator.154"), Messages.getString("SettingsPanelAdministrator.155"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        codeEditorSettingsPanel.add(autoCompleteCharactersLabel, Messages.getString("SettingsPanelAdministrator.156")); //$NON-NLS-1$
        codeEditorSettingsPanel.add(autoCompleteCharactersField, Messages.getString("SettingsPanelAdministrator.157")); //$NON-NLS-1$
        codeEditorSettingsPanel.add(autoCompleteIncludeLettersCheckBox, Messages.getString("SettingsPanelAdministrator.158")); //$NON-NLS-1$
        codeEditorSettingsPanel.add(autoCompleteDelayLabel, Messages.getString("SettingsPanelAdministrator.159")); //$NON-NLS-1$
        codeEditorSettingsPanel.add(autoCompleteDelayField, Messages.getString("SettingsPanelAdministrator.160")); //$NON-NLS-1$
        codeEditorSettingsPanel.add(shortcutKeyLabel, Messages.getString("SettingsPanelAdministrator.161")); //$NON-NLS-1$
        codeEditorSettingsPanel.add(shortcutKeyScrollPane, Messages.getString("SettingsPanelAdministrator.162")); //$NON-NLS-1$
        codeEditorSettingsPanel.add(restoreDefaultsButton, Messages.getString("SettingsPanelAdministrator.163")); //$NON-NLS-1$
        add(codeEditorSettingsPanel, Messages.getString("SettingsPanelAdministrator.164")); //$NON-NLS-1$
    }

    private void backgroundColorRadioActionPerformed(boolean serverDefault) {
        if (serverDefault) {
            backgroundColorButton.setBackground(PlatformUI.DEFAULT_BACKGROUND_COLOR);
            backgroundColorButton.setEnabled(false);
        } else {
            backgroundColorButton.setEnabled(true);
        }
    }

    private class KeyStrokeCellRenderer extends KeyStrokeTextField implements TableCellRenderer {

        public KeyStrokeCellRenderer() {
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setKeyStroke((KeyStroke) value);
            return this;
        }
    }

    private class KeyStrokeCellEditor extends DefaultCellEditor {

        private KeyStrokeTextField textField;

        public KeyStrokeCellEditor() {
            super(new KeyStrokeTextField());
            textField = (KeyStrokeTextField) getComponent();
        }

        @Override
        public Object getCellEditorValue() {
            return textField.getKeyStroke();
        }

        @Override
        public boolean isCellEditable(EventObject evt) {
            if (evt != null && evt instanceof MouseEvent) {
                return ((MouseEvent) evt).getClickCount() >= 2;
            }
            return false;
        }

        @Override
        public boolean stopCellEditing() {
            KeyStroke keyStroke = (KeyStroke) getCellEditorValue();
            if (keyStroke != null) {
                // Don't allow alphabetic key strokes without modifiers (or with only shift).
                if (keyStroke.getKeyCode() >= KeyEvent.VK_A && keyStroke.getKeyCode() <= KeyEvent.VK_Z && (keyStroke.getModifiers() == 0 || (keyStroke.getModifiers() & InputEvent.SHIFT_MASK) > 0)) {
                    cancelCellEditing();
                } else {
                    // Don't allow key strokes already mapped to something else in the table
                    for (int row = 0; row < shortcutKeyTable.getRowCount(); row++) {
                        if (keyStroke.equals((KeyStroke) shortcutKeyTable.getModel().getValueAt(row, KEY_COLUMN))) {
                            cancelCellEditing();
                            break;
                        }
                    }
                }
            }

            return super.stopCellEditing();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            textField.setKeyStroke((KeyStroke) value);
            return textField;
        }
    }

    private JPanel systemSettingsPanel;
    private JLabel dashboardRefreshIntervalLabel;
    private JTextField dashboardRefreshIntervalField;
    private JLabel messageBrowserPageSizeLabel;
    private JTextField messageBrowserPageSizeField;
    private JLabel eventBrowserPageSizeLabel;
    private JTextField eventBrowserPageSizeField;
    private JLabel formatLabel;
    private ButtonGroup formatButtonGroup;
    private JRadioButton formatYesRadio;
    private JRadioButton formatNoRadio;
    private JLabel textSearchWarningLabel;
    private ButtonGroup textSearchWarningButtonGroup;
    private JRadioButton textSearchWarningYesRadio;
    private JRadioButton textSearchWarningNoRadio;
    private JLabel filterTransformerShowIteratorLabel;
    private ButtonGroup filterTransformerShowIteratorButtonGroup;
    private JRadioButton filterTransformerShowIteratorYesRadio;
    private JRadioButton filterTransformerShowIteratorNoRadio;
    private JLabel messageBrowserShowAttachmentTypeDialogLabel;
    private ButtonGroup messageBrowserShowAttachmentTypeDialogButtonGroup;
    private JRadioButton messageBrowserShowAttachmentTypeDialogYesRadio;
    private JRadioButton messageBrowserShowAttachmentTypeDialogNoRadio;
    private JLabel reprocessRemoveMessagesWarningLabel;
    private JRadioButton reprocessRemoveMessagesWarningYesRadio;
    private JRadioButton reprocessRemoveMessagesWarningNoRadio;
    private JLabel importChannelLibrariesLabel;
    private ButtonGroup importChannelLibrariesButtonGroup;
    private JRadioButton importChannelLibrariesYesRadio;
    private JRadioButton importChannelLibrariesNoRadio;
    private JRadioButton importChannelLibrariesAskRadio;
    private JLabel exportChannelLibrariesLabel;
    private ButtonGroup exportChannelLibrariesButtonGroup;
    private JRadioButton exportChannelLibrariesYesRadio;
    private JRadioButton exportChannelLibrariesNoRadio;
    private JRadioButton exportChannelLibrariesAskRadio;
    private JPanel userSettingsPanel;
    private JLabel checkForNotificationsLabel;
    private ButtonGroup notificationButtonGroup;
    private JRadioButton checkForNotificationsYesRadio;
    private JRadioButton checkForNotificationsNoRadio;
    private JLabel backgroundColorLabel;
    private ButtonGroup backgroundColorButtonGroup;
    private JRadioButton backgroundColorServerDefaultRadio;
    private JRadioButton backgroundColorCustomRadio;
    private JButton backgroundColorButton;
    private JPanel codeEditorSettingsPanel;
    private JLabel autoCompleteCharactersLabel;
    private JTextField autoCompleteCharactersField;
    private JCheckBox autoCompleteIncludeLettersCheckBox;
    private JLabel autoCompleteDelayLabel;
    private JTextField autoCompleteDelayField;
    private JLabel shortcutKeyLabel;
    private JScrollPane shortcutKeyScrollPane;
    private MirthTable shortcutKeyTable;
    private JButton restoreDefaultsButton;
}