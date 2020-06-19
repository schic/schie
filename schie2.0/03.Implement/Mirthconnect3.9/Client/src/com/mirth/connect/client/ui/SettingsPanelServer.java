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
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.internet.InternetAddress;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.core.TaskConstants;
import com.mirth.connect.client.ui.alert.DefaultAlertPanel;
import com.mirth.connect.client.ui.components.MirthCheckBox;
import com.mirth.connect.client.ui.components.MirthFieldConstraints;
import com.mirth.connect.client.ui.components.MirthPasswordField;
import com.mirth.connect.client.ui.components.MirthRadioButton;
import com.mirth.connect.client.ui.components.MirthTextField;
import com.mirth.connect.client.ui.util.DisplayUtil;
import com.mirth.connect.donkey.model.channel.MetaDataColumn;
import com.mirth.connect.model.Channel;
import com.mirth.connect.model.ServerConfiguration;
import com.mirth.connect.model.ServerSettings;
import com.mirth.connect.model.UpdateSettings;
import com.mirth.connect.model.alert.AlertStatus;
import com.mirth.connect.model.converters.ObjectXMLSerializer;
import com.mirth.connect.model.util.DefaultMetaData;
import com.mirth.connect.util.ConnectionTestResponse;

public class SettingsPanelServer extends AbstractSettingsPanel {

    public static final String TAB_NAME = Messages.getString("SettingsPanelServer.0"); //$NON-NLS-1$

    private List<MetaDataColumn> defaultMetaDataColumns;

    public SettingsPanelServer(String tabName) {
        super(tabName);

        initComponents();
        initLayout();

        addTask(TaskConstants.SETTINGS_SERVER_BACKUP, Messages.getString("SettingsPanelServer.1"), Messages.getString("SettingsPanelServer.2"), Messages.getString("SettingsPanelServer.3"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("SettingsPanelServer.4")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.SETTINGS_SERVER_RESTORE, Messages.getString("SettingsPanelServer.5"), Messages.getString("SettingsPanelServer.6"), Messages.getString("SettingsPanelServer.7"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("SettingsPanelServer.8")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.SETTINGS_CLEAR_ALL_STATS, Messages.getString("SettingsPanelServer.9"), Messages.getString("SettingsPanelServer.10"), Messages.getString("SettingsPanelServer.11"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("SettingsPanelServer.12")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        provideUsageStatsMoreInfoLabel.setToolTipText(UIConstants.PRIVACY_TOOLTIP);
        provideUsageStatsMoreInfoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        queueBufferSizeField.setDocument(new MirthFieldConstraints(8, false, false, true));
        smtpTimeoutField.setDocument(new MirthFieldConstraints(0, false, false, false));

        defaultMetaDataColumns = new ArrayList<MetaDataColumn>();
    }

    public void doRefresh() {
        if (PlatformUI.MIRTH_FRAME.alertRefresh()) {
            return;
        }

        final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelServer.13") + getTabName() + Messages.getString("SettingsPanelServer.14")); //$NON-NLS-1$ //$NON-NLS-2$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            ServerSettings serverSettings = null;
            UpdateSettings updateSettings = null;

            public Void doInBackground() {
                try {
                    serverSettings = getFrame().mirthClient.getServerSettings();
                    updateSettings = getFrame().mirthClient.getUpdateSettings();
                } catch (ClientException e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                return null;
            }

            @Override
            public void done() {
                // null if it failed to get the server/update settings or if confirmLeave returned false
                if (serverSettings != null && updateSettings != null) {
                    setServerSettings(serverSettings);
                    setUpdateSettings(updateSettings);
                }
                getFrame().stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public boolean doSave() {
        final ServerSettings serverSettings = getServerSettings();
        final UpdateSettings updateSettings = getUpdateSettings();

        // Integer queueBufferSize will be null if it was invalid
        queueBufferSizeField.setBackground(null);
        if (serverSettings.getQueueBufferSize() == null) {
            queueBufferSizeField.setBackground(UIConstants.INVALID_COLOR);
            getFrame().alertWarning(this, Messages.getString("SettingsPanelServer.15")); //$NON-NLS-1$
            return false;
        }

        try {
            String emailAddress = serverSettings.getSmtpFrom();
            if (StringUtils.isNotBlank(emailAddress)) {
                new InternetAddress(emailAddress).validate();
            }
        } catch (Exception e) {
            PlatformUI.MIRTH_FRAME.alertWarning(PlatformUI.MIRTH_FRAME, Messages.getString("SettingsPanelServer.16") + e.getMessage()); //$NON-NLS-1$
            return false;
        }

        final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelServer.17") + getTabName() + Messages.getString("SettingsPanelServer.18")); //$NON-NLS-1$ //$NON-NLS-2$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            private boolean usingServerDefaultColor = true;

            public Void doInBackground() {
                try {
                    getFrame().mirthClient.setServerSettings(serverSettings);

                    String environmentName = environmentNameField.getText();
                    String serverName = serverNameField.getText();
                    StringBuilder titleText = new StringBuilder();
                    StringBuilder statusBarText = new StringBuilder();
                    statusBarText.append(Messages.getString("SettingsPanelServer.19")); //$NON-NLS-1$

                    if (!StringUtils.isBlank(environmentName)) {
                        titleText.append(environmentName + Messages.getString("SettingsPanelServer.20")); //$NON-NLS-1$
                        statusBarText.append(environmentName);

                        if (!StringUtils.isBlank(serverName)) {
                            statusBarText.append(Messages.getString("SettingsPanelServer.21")); //$NON-NLS-1$
                        } else {
                            statusBarText.append(Messages.getString("SettingsPanelServer.22")); //$NON-NLS-1$
                        }

                        PlatformUI.ENVIRONMENT_NAME = environmentName;
                    }

                    if (!StringUtils.isBlank(serverName)) {
                        titleText.append(serverName);
                        statusBarText.append(serverName + Messages.getString("SettingsPanelServer.23")); //$NON-NLS-1$
                        PlatformUI.SERVER_NAME = serverName;
                    } else {
                        titleText.append(PlatformUI.SERVER_URL);
                    }
                    titleText.append(Messages.getString("SettingsPanelServer.24") + UIConstants.TITLE_TEXT); //$NON-NLS-1$
                    statusBarText.append(PlatformUI.SERVER_URL);
                    titleText.append(Messages.getString("SettingsPanelServer.25") + PlatformUI.SERVER_VERSION + Messages.getString("SettingsPanelServer.26")); //$NON-NLS-1$ //$NON-NLS-2$
                    getFrame().setTitle(titleText.toString());
                    getFrame().statusBar.setServerText(statusBarText.toString());

                    getFrame().mirthClient.setUpdateSettings(updateSettings);
                } catch (Exception e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                try {
                    Color defaultBackgroundColor = serverSettings.getDefaultAdministratorBackgroundColor();
                    if (defaultBackgroundColor != null) {
                        PlatformUI.DEFAULT_BACKGROUND_COLOR = defaultBackgroundColor;
                    }

                    String backgroundColorStr = getFrame().mirthClient.getUserPreference(getFrame().getCurrentUser(getFrame()).getId(), UIConstants.USER_PREF_KEY_BACKGROUND_COLOR);
                    if (StringUtils.isNotBlank(backgroundColorStr)) {
                        Color backgroundColor = ObjectXMLSerializer.getInstance().deserialize(backgroundColorStr, Color.class);
                        if (backgroundColor != null) {
                            usingServerDefaultColor = false;
                        }
                    }
                } catch (Exception e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                return null;
            }

            @Override
            public void done() {
                if (usingServerDefaultColor) {
                    getFrame().setupBackgroundPainters(PlatformUI.DEFAULT_BACKGROUND_COLOR);
                }
                setSaveEnabled(false);
                getFrame().stopWorking(workingId);
            }
        };

        worker.execute();

        return true;
    }

    /** Loads the current server settings into the Settings form */
    public void setServerSettings(ServerSettings serverSettings) {
        if (serverSettings.getEnvironmentName() != null) {
            environmentNameField.setText(serverSettings.getEnvironmentName());
        } else {
            environmentNameField.setText(Messages.getString("SettingsPanelServer.27")); //$NON-NLS-1$
        }

        if (serverSettings.getServerName() != null) {
            serverNameField.setText(serverSettings.getServerName());
        } else {
            serverNameField.setText(Messages.getString("SettingsPanelServer.28")); //$NON-NLS-1$
        }

        if (serverSettings.getDefaultAdministratorBackgroundColor() != null) {
            defaultAdministratorColorButton.setBackground(serverSettings.getDefaultAdministratorBackgroundColor());
        } else {
            defaultAdministratorColorButton.setBackground(ServerSettings.DEFAULT_COLOR);
        }

        if (serverSettings.getSmtpHost() != null) {
            smtpHostField.setText(serverSettings.getSmtpHost());
        } else {
            smtpHostField.setText(Messages.getString("SettingsPanelServer.29")); //$NON-NLS-1$
        }

        if (serverSettings.getSmtpPort() != null) {
            smtpPortField.setText(serverSettings.getSmtpPort());
        } else {
            smtpPortField.setText(Messages.getString("SettingsPanelServer.30")); //$NON-NLS-1$
        }

        if (serverSettings.getSmtpTimeout() != null) {
            smtpTimeoutField.setText(serverSettings.getSmtpTimeout().toString());
        } else {
            smtpTimeoutField.setText(Messages.getString("SettingsPanelServer.31")); //$NON-NLS-1$
        }

        if (serverSettings.getSmtpFrom() != null) {
            defaultFromAddressField.setText(serverSettings.getSmtpFrom());
        } else {
            defaultFromAddressField.setText(Messages.getString("SettingsPanelServer.32")); //$NON-NLS-1$
        }

        String smtpSecure = serverSettings.getSmtpSecure();
        if (smtpSecure != null && smtpSecure.equalsIgnoreCase(Messages.getString("SettingsPanelServer.33"))) { //$NON-NLS-1$
            secureConnectionTLSRadio.setSelected(true);
        } else if (smtpSecure != null && smtpSecure.equalsIgnoreCase(Messages.getString("SettingsPanelServer.34"))) { //$NON-NLS-1$
            secureConnectionSSLRadio.setSelected(true);
        } else {
            secureConnectionNoneRadio.setSelected(true);
        }

        if (serverSettings.getSmtpAuth() != null && serverSettings.getSmtpAuth()) {
            requireAuthenticationYesRadio.setSelected(true);
            requireAuthenticationYesRadioActionPerformed(null);
        } else {
            requireAuthenticationNoRadio.setSelected(true);
            requireAuthenticationNoRadioActionPerformed(null);
        }

        if (serverSettings.getClearGlobalMap() != null && !serverSettings.getClearGlobalMap()) {
            clearGlobalMapNoRadio.setSelected(true);
        } else {
            clearGlobalMapYesRadio.setSelected(true);
        }

        if (serverSettings.getQueueBufferSize() != null) {
            queueBufferSizeField.setText(serverSettings.getQueueBufferSize().toString());
        } else {
            queueBufferSizeField.setText(Messages.getString("SettingsPanelServer.35")); //$NON-NLS-1$
        }

        // TODO: Change this to use a more complex custom metadata table rather than checkboxes
        List<MetaDataColumn> defaultMetaDataColumns = serverSettings.getDefaultMetaDataColumns();
        if (defaultMetaDataColumns != null) {
            this.defaultMetaDataColumns = new ArrayList<MetaDataColumn>(defaultMetaDataColumns);
        } else {
            this.defaultMetaDataColumns = new ArrayList<MetaDataColumn>(DefaultMetaData.DEFAULT_COLUMNS);
        }
        defaultMetaDataSourceCheckBox.setSelected(this.defaultMetaDataColumns.contains(DefaultMetaData.SOURCE_COLUMN));
        defaultMetaDataTypeCheckBox.setSelected(this.defaultMetaDataColumns.contains(DefaultMetaData.TYPE_COLUMN));
        defaultMetaDataVersionCheckBox.setSelected(this.defaultMetaDataColumns.contains(DefaultMetaData.VERSION_COLUMN));

        if (serverSettings.getSmtpUsername() != null) {
            usernameField.setText(serverSettings.getSmtpUsername());
        } else {
            usernameField.setText(Messages.getString("SettingsPanelServer.36")); //$NON-NLS-1$
        }

        if (serverSettings.getSmtpPassword() != null) {
            passwordField.setText(serverSettings.getSmtpPassword());
        } else {
            passwordField.setText(Messages.getString("SettingsPanelServer.37")); //$NON-NLS-1$
        }
        resetInvalidSettings();
    }

    public void setUpdateSettings(UpdateSettings updateSettings) {
        if (updateSettings.getStatsEnabled() != null && !updateSettings.getStatsEnabled()) {
            provideUsageStatsNoRadio.setSelected(true);
        } else {
            provideUsageStatsYesRadio.setSelected(true);
        }
    }

    /** Saves the current settings from the settings form */
    public ServerSettings getServerSettings() {
        ServerSettings serverSettings = new ServerSettings();

        serverSettings.setEnvironmentName(environmentNameField.getText());

        serverSettings.setServerName(serverNameField.getText());

        serverSettings.setDefaultAdministratorBackgroundColor(defaultAdministratorColorButton.getBackground());

        serverSettings.setClearGlobalMap(clearGlobalMapYesRadio.isSelected());

        // Set the queue buffer size Integer to null if it was invalid
        int queueBufferSize = NumberUtils.toInt(queueBufferSizeField.getText(), 0);
        if (queueBufferSize == 0) {
            serverSettings.setQueueBufferSize(null);
        } else {
            serverSettings.setQueueBufferSize(queueBufferSize);
        }

        // TODO: Change this to use a more complex custom metadata table rather than checkboxes
        // Until this is changed to a table, always add source/type/version in order
        List<MetaDataColumn> defaultMetaDataColumns = new ArrayList<MetaDataColumn>();
        if (defaultMetaDataSourceCheckBox.isSelected()) {
            defaultMetaDataColumns.add(DefaultMetaData.SOURCE_COLUMN);
        } else {
            this.defaultMetaDataColumns.remove(DefaultMetaData.SOURCE_COLUMN);
        }

        if (defaultMetaDataTypeCheckBox.isSelected()) {
            defaultMetaDataColumns.add(DefaultMetaData.TYPE_COLUMN);
        } else {
            this.defaultMetaDataColumns.remove(DefaultMetaData.TYPE_COLUMN);
        }

        if (defaultMetaDataVersionCheckBox.isSelected()) {
            defaultMetaDataColumns.add(DefaultMetaData.VERSION_COLUMN);
        } else {
            this.defaultMetaDataColumns.remove(DefaultMetaData.VERSION_COLUMN);
        }

        for (MetaDataColumn column : this.defaultMetaDataColumns) {
            if (!defaultMetaDataColumns.contains(column)) {
                defaultMetaDataColumns.add(column);
            }
        }
        serverSettings.setDefaultMetaDataColumns(this.defaultMetaDataColumns = defaultMetaDataColumns);

        serverSettings.setSmtpHost(smtpHostField.getText());
        serverSettings.setSmtpPort(smtpPortField.getText());
        serverSettings.setSmtpTimeout(smtpTimeoutField.getText());
        serverSettings.setSmtpFrom(defaultFromAddressField.getText());

        if (secureConnectionTLSRadio.isSelected()) {
            serverSettings.setSmtpSecure(Messages.getString("SettingsPanelServer.38")); //$NON-NLS-1$
        } else if (secureConnectionSSLRadio.isSelected()) {
            serverSettings.setSmtpSecure(Messages.getString("SettingsPanelServer.39")); //$NON-NLS-1$
        } else {
            serverSettings.setSmtpSecure(Messages.getString("SettingsPanelServer.40")); //$NON-NLS-1$
        }

        if (requireAuthenticationYesRadio.isSelected()) {
            serverSettings.setSmtpAuth(true);
            serverSettings.setSmtpUsername(usernameField.getText());
            serverSettings.setSmtpPassword(new String(passwordField.getPassword()));
        } else {
            serverSettings.setSmtpAuth(false);
            serverSettings.setSmtpUsername(Messages.getString("SettingsPanelServer.41")); //$NON-NLS-1$
            serverSettings.setSmtpPassword(Messages.getString("SettingsPanelServer.42")); //$NON-NLS-1$
        }

        return serverSettings;
    }

    public UpdateSettings getUpdateSettings() {
        UpdateSettings updateSettings = new UpdateSettings();

        updateSettings.setStatsEnabled(provideUsageStatsYesRadio.isSelected());

        return updateSettings;
    }

    public void doBackup() {
        if (isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, Messages.getString("SettingsPanelServer.43")); //$NON-NLS-1$

            if (option == JOptionPane.YES_OPTION) {
                if (!doSave()) {
                    return;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }

        final String backupDate = new SimpleDateFormat(Messages.getString("SettingsPanelServer.44")).format(new Date()); //$NON-NLS-1$
        final File exportFile = getFrame().createFileForExport(backupDate.substring(0, 10) + Messages.getString("SettingsPanelServer.45"), Messages.getString("SettingsPanelServer.46")); //$NON-NLS-1$ //$NON-NLS-2$

        if (exportFile != null) {
            final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelServer.47")); //$NON-NLS-1$

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {
                    ServerConfiguration configuration = null;

                    try {
                        configuration = getFrame().mirthClient.getServerConfiguration();
                    } catch (ClientException e) {
                        getFrame().alertThrowable(SettingsPanelServer.this, e);
                        return null;
                    }

                    // Update resource names
                    for (Channel channel : configuration.getChannels()) {
                        getFrame().updateResourceNames(channel, configuration.getResourceProperties().getList());
                    }

                    configuration.setDate(backupDate);
                    String backupXML = ObjectXMLSerializer.getInstance().serialize(configuration);

                    getFrame().exportFile(backupXML, exportFile, Messages.getString("SettingsPanelServer.48")); //$NON-NLS-1$
                    return null;
                }

                public void done() {
                    getFrame().stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doRestore() {
        if (getFrame().isSaveEnabled()) {
            if (!getFrame().alertOkCancel(this, Messages.getString("SettingsPanelServer.49"))) { //$NON-NLS-1$
                return;
            }
            if (!doSave()) {
                return;
            }
        }

        String content = getFrame().browseForFileString(Messages.getString("SettingsPanelServer.50")); //$NON-NLS-1$

        if (content != null) {
            try {
                if (!getFrame().promptObjectMigration(content, Messages.getString("SettingsPanelServer.51"))) { //$NON-NLS-1$
                    return;
                }

                final ServerConfiguration configuration = ObjectXMLSerializer.getInstance().deserialize(content, ServerConfiguration.class);

                final JCheckBox deployChannelsCheckBox = new JCheckBox(Messages.getString("SettingsPanelServer.52")); //$NON-NLS-1$
                deployChannelsCheckBox.setSelected(true);
                final JCheckBox overwriteConfigMap = new JCheckBox(Messages.getString("SettingsPanelServer.53")); //$NON-NLS-1$
                overwriteConfigMap.setSelected(false);
                String warningMessage = Messages.getString("SettingsPanelServer.54") + configuration.getDate() + Messages.getString("SettingsPanelServer.55"); //$NON-NLS-1$ //$NON-NLS-2$
                Object[] params = { warningMessage, new JLabel(Messages.getString("SettingsPanelServer.56")), deployChannelsCheckBox, //$NON-NLS-1$
                        overwriteConfigMap };
                int option = JOptionPane.showConfirmDialog(this, params, Messages.getString("SettingsPanelServer.57"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$

                if (option == JOptionPane.YES_OPTION) {
                    final Set<String> alertIds = new HashSet<String>();
                    for (AlertStatus alertStatus : PlatformUI.MIRTH_FRAME.mirthClient.getAlertStatusList()) {
                        alertIds.add(alertStatus.getId());
                    }

                    final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelServer.58")); //$NON-NLS-1$

                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        private boolean updateAlerts = false;

                        public Void doInBackground() {
                            try {
                                getFrame().mirthClient.setServerConfiguration(configuration, deployChannelsCheckBox.isSelected(), overwriteConfigMap.isSelected());
                                getFrame().channelPanel.clearChannelCache();
                                doRefresh();
                                getFrame().alertInformation(SettingsPanelServer.this, Messages.getString("SettingsPanelServer.59")); //$NON-NLS-1$
                                updateAlerts = true;
                            } catch (ClientException e) {
                                getFrame().alertThrowable(SettingsPanelServer.this, e);
                            }
                            return null;
                        }

                        public void done() {
                            if (getFrame().alertPanel == null) {
                                getFrame().alertPanel = new DefaultAlertPanel();
                            }

                            if (updateAlerts) {
                                getFrame().alertPanel.updateAlertDetails(alertIds);
                            }
                            getFrame().stopWorking(workingId);
                        }
                    };

                    worker.execute();
                }
            } catch (Exception e) {
                getFrame().alertError(this, Messages.getString("SettingsPanelServer.60")); //$NON-NLS-1$
            }
        }
    }

    public void doClearAllStats() {
        String result = DisplayUtil.showInputDialog(this, Messages.getString("SettingsPanelServer.61"), Messages.getString("SettingsPanelServer.62"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$

        if (result != null) {
            if (!result.equals(Messages.getString("SettingsPanelServer.63"))) { //$NON-NLS-1$
                getFrame().alertWarning(SettingsPanelServer.this, Messages.getString("SettingsPanelServer.64")); //$NON-NLS-1$
                return;
            }

            final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelServer.65")); //$NON-NLS-1$

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                private Exception exception = null;

                public Void doInBackground() {
                    try {
                        getFrame().mirthClient.clearAllStatistics();
                    } catch (ClientException e) {
                        exception = e;
                        getFrame().alertThrowable(SettingsPanelServer.this, e);
                    }
                    return null;
                }

                public void done() {
                    getFrame().stopWorking(workingId);

                    if (exception == null) {
                        getFrame().alertInformation(SettingsPanelServer.this, Messages.getString("SettingsPanelServer.66")); //$NON-NLS-1$
                    }
                }
            };

            worker.execute();
        }
    }

    private void resetInvalidSettings() {
        queueBufferSizeField.setBackground(null);
        smtpHostField.setBackground(null);
        smtpPortField.setBackground(null);
        smtpTimeoutField.setBackground(null);
        defaultFromAddressField.setBackground(null);
        usernameField.setBackground(null);
        passwordField.setBackground(null);
    }

    private void initComponents() {
        setBackground(UIConstants.BACKGROUND_COLOR);

        generalPanel = new JPanel();
        generalPanel.setBackground(getBackground());
        generalPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(204, 204, 204)), Messages.getString("SettingsPanelServer.67"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(Messages.getString("SettingsPanelServer.68"), 1, 11))); //$NON-NLS-1$ //$NON-NLS-2$

        environmentNameLabel = new JLabel(Messages.getString("SettingsPanelServer.69")); //$NON-NLS-1$
        environmentNameField = new MirthTextField();
        environmentNameField.setToolTipText(Messages.getString("SettingsPanelServer.70")); //$NON-NLS-1$

        serverNameLabel = new JLabel(Messages.getString("SettingsPanelServer.71")); //$NON-NLS-1$
        serverNameField = new MirthTextField();
        serverNameField.setToolTipText(Messages.getString("SettingsPanelServer.72")); //$NON-NLS-1$

        defaultAdministratorColorLabel = new JLabel(Messages.getString("SettingsPanelServer.73")); //$NON-NLS-1$

        defaultAdministratorColorButton = new JButton();
        defaultAdministratorColorButton.setBackground(ServerSettings.DEFAULT_COLOR);
        defaultAdministratorColorButton.setToolTipText(Messages.getString("SettingsPanelServer.74")); //$NON-NLS-1$
        defaultAdministratorColorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        defaultAdministratorColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Color color = JColorChooser.showDialog(PlatformUI.MIRTH_FRAME, Messages.getString("SettingsPanelServer.75"), defaultAdministratorColorButton.getBackground()); //$NON-NLS-1$
                if (color != null) {
                    defaultAdministratorColorButton.setBackground(color);
                    getFrame().setSaveEnabled(true);
                }
            }
        });

        provideUsageStatsLabel = new JLabel(Messages.getString("SettingsPanelServer.76")); //$NON-NLS-1$
        provideUsageStatsButtonGroup = new ButtonGroup();

        provideUsageStatsYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelServer.77")); //$NON-NLS-1$
        provideUsageStatsYesRadio.setBackground(getBackground());
        provideUsageStatsYesRadio.setToolTipText(Messages.getString("SettingsPanelServer.78")); //$NON-NLS-1$
        provideUsageStatsButtonGroup.add(provideUsageStatsYesRadio);

        provideUsageStatsNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelServer.79")); //$NON-NLS-1$
        provideUsageStatsNoRadio.setBackground(getBackground());
        provideUsageStatsNoRadio.setToolTipText(Messages.getString("SettingsPanelServer.80")); //$NON-NLS-1$
        provideUsageStatsButtonGroup.add(provideUsageStatsNoRadio);

        provideUsageStatsMoreInfoLabel = new JLabel(Messages.getString("SettingsPanelServer.81")); //$NON-NLS-1$
        provideUsageStatsMoreInfoLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                provideUsageStatsMoreInfoLabelMouseClicked(evt);
            }
        });

        channelPanel = new JPanel();
        channelPanel.setBackground(getBackground());
        channelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(204, 204, 204)), Messages.getString("SettingsPanelServer.82"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(Messages.getString("SettingsPanelServer.83"), 1, 11))); //$NON-NLS-1$ //$NON-NLS-2$

        clearGlobalMapLabel = new JLabel(Messages.getString("SettingsPanelServer.84")); //$NON-NLS-1$
        clearGlobalMapButtonGroup = new ButtonGroup();

        clearGlobalMapYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelServer.85")); //$NON-NLS-1$
        clearGlobalMapYesRadio.setBackground(getBackground());
        clearGlobalMapYesRadio.setSelected(true);
        clearGlobalMapYesRadio.setToolTipText(Messages.getString("SettingsPanelServer.86")); //$NON-NLS-1$
        clearGlobalMapButtonGroup.add(clearGlobalMapYesRadio);

        clearGlobalMapNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelServer.87")); //$NON-NLS-1$
        clearGlobalMapNoRadio.setBackground(getBackground());
        clearGlobalMapNoRadio.setToolTipText(Messages.getString("SettingsPanelServer.88")); //$NON-NLS-1$
        clearGlobalMapButtonGroup.add(clearGlobalMapNoRadio);

        queueBufferSizeLabel = new JLabel(Messages.getString("SettingsPanelServer.89")); //$NON-NLS-1$
        queueBufferSizeField = new MirthTextField();
        queueBufferSizeField.setToolTipText(Messages.getString("SettingsPanelServer.90")); //$NON-NLS-1$

        defaultMetaDataLabel = new JLabel(Messages.getString("SettingsPanelServer.91")); //$NON-NLS-1$

        defaultMetaDataSourceCheckBox = new MirthCheckBox(Messages.getString("SettingsPanelServer.92")); //$NON-NLS-1$
        defaultMetaDataSourceCheckBox.setBackground(getBackground());
        defaultMetaDataSourceCheckBox.setToolTipText(Messages.getString("SettingsPanelServer.93")); //$NON-NLS-1$

        defaultMetaDataTypeCheckBox = new MirthCheckBox(Messages.getString("SettingsPanelServer.94")); //$NON-NLS-1$
        defaultMetaDataTypeCheckBox.setBackground(getBackground());
        defaultMetaDataTypeCheckBox.setToolTipText(Messages.getString("SettingsPanelServer.95")); //$NON-NLS-1$

        defaultMetaDataVersionCheckBox = new MirthCheckBox(Messages.getString("SettingsPanelServer.96")); //$NON-NLS-1$
        defaultMetaDataVersionCheckBox.setBackground(getBackground());
        defaultMetaDataVersionCheckBox.setToolTipText(Messages.getString("SettingsPanelServer.97")); //$NON-NLS-1$

        emailPanel = new JPanel();
        emailPanel.setBackground(getBackground());
        emailPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(204, 204, 204)), Messages.getString("SettingsPanelServer.98"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(Messages.getString("SettingsPanelServer.99"), 1, 11))); //$NON-NLS-1$ //$NON-NLS-2$

        smtpHostLabel = new JLabel(Messages.getString("SettingsPanelServer.100")); //$NON-NLS-1$
        smtpHostField = new MirthTextField();
        smtpHostField.setToolTipText(Messages.getString("SettingsPanelServer.101")); //$NON-NLS-1$

        testEmailButton = new JButton(Messages.getString("SettingsPanelServer.102")); //$NON-NLS-1$
        testEmailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                testEmailButtonActionPerformed(evt);
            }
        });

        smtpPortLabel = new JLabel(Messages.getString("SettingsPanelServer.103")); //$NON-NLS-1$
        smtpPortField = new MirthTextField();
        smtpPortField.setToolTipText(Messages.getString("SettingsPanelServer.104")); //$NON-NLS-1$

        smtpTimeoutLabel = new JLabel(Messages.getString("SettingsPanelServer.105")); //$NON-NLS-1$
        smtpTimeoutField = new MirthTextField();
        smtpTimeoutField.setToolTipText(Messages.getString("SettingsPanelServer.106")); //$NON-NLS-1$

        defaultFromAddressLabel = new JLabel(Messages.getString("SettingsPanelServer.107")); //$NON-NLS-1$
        defaultFromAddressField = new MirthTextField();
        defaultFromAddressField.setToolTipText(Messages.getString("SettingsPanelServer.108")); //$NON-NLS-1$

        secureConnectionLabel = new JLabel(Messages.getString("SettingsPanelServer.109")); //$NON-NLS-1$
        secureConnectionButtonGroup = new ButtonGroup();

        secureConnectionNoneRadio = new MirthRadioButton(Messages.getString("SettingsPanelServer.110")); //$NON-NLS-1$
        secureConnectionNoneRadio.setBackground(getBackground());
        secureConnectionNoneRadio.setSelected(true);
        secureConnectionNoneRadio.setToolTipText(Messages.getString("SettingsPanelServer.111")); //$NON-NLS-1$
        secureConnectionButtonGroup.add(secureConnectionNoneRadio);

        secureConnectionTLSRadio = new MirthRadioButton(Messages.getString("SettingsPanelServer.112")); //$NON-NLS-1$
        secureConnectionTLSRadio.setBackground(getBackground());
        secureConnectionTLSRadio.setToolTipText(Messages.getString("SettingsPanelServer.113")); //$NON-NLS-1$
        secureConnectionButtonGroup.add(secureConnectionTLSRadio);

        secureConnectionSSLRadio = new MirthRadioButton(Messages.getString("SettingsPanelServer.114")); //$NON-NLS-1$
        secureConnectionSSLRadio.setBackground(getBackground());
        secureConnectionSSLRadio.setToolTipText(Messages.getString("SettingsPanelServer.115")); //$NON-NLS-1$
        secureConnectionButtonGroup.add(secureConnectionSSLRadio);

        requireAuthenticationLabel = new JLabel(Messages.getString("SettingsPanelServer.116")); //$NON-NLS-1$
        requireAuthenticationButtonGroup = new ButtonGroup();

        requireAuthenticationYesRadio = new MirthRadioButton(Messages.getString("SettingsPanelServer.117")); //$NON-NLS-1$
        requireAuthenticationYesRadio.setBackground(getBackground());
        requireAuthenticationYesRadio.setSelected(true);
        requireAuthenticationYesRadio.setToolTipText(Messages.getString("SettingsPanelServer.118")); //$NON-NLS-1$
        requireAuthenticationYesRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                requireAuthenticationYesRadioActionPerformed(evt);
            }
        });
        requireAuthenticationButtonGroup.add(requireAuthenticationYesRadio);

        requireAuthenticationNoRadio = new MirthRadioButton(Messages.getString("SettingsPanelServer.119")); //$NON-NLS-1$
        requireAuthenticationNoRadio.setBackground(getBackground());
        requireAuthenticationNoRadio.setToolTipText(Messages.getString("SettingsPanelServer.120")); //$NON-NLS-1$
        requireAuthenticationNoRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                requireAuthenticationNoRadioActionPerformed(evt);
            }
        });
        requireAuthenticationButtonGroup.add(requireAuthenticationNoRadio);

        usernameLabel = new JLabel(Messages.getString("SettingsPanelServer.121")); //$NON-NLS-1$
        usernameField = new MirthTextField();
        usernameField.setToolTipText(Messages.getString("SettingsPanelServer.122")); //$NON-NLS-1$

        passwordLabel = new JLabel(Messages.getString("SettingsPanelServer.123")); //$NON-NLS-1$
        passwordField = new MirthPasswordField();
        passwordField.setToolTipText(Messages.getString("SettingsPanelServer.124")); //$NON-NLS-1$
    }

    private void initLayout() {
        setLayout(new MigLayout(Messages.getString("SettingsPanelServer.125"), Messages.getString("SettingsPanelServer.126"), Messages.getString("SettingsPanelServer.127"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        generalPanel.setLayout(new MigLayout(Messages.getString("SettingsPanelServer.128"), Messages.getString("SettingsPanelServer.129"), Messages.getString("SettingsPanelServer.130"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        generalPanel.add(environmentNameLabel, Messages.getString("SettingsPanelServer.131")); //$NON-NLS-1$
        generalPanel.add(environmentNameField, Messages.getString("SettingsPanelServer.132")); //$NON-NLS-1$
        generalPanel.add(serverNameLabel, Messages.getString("SettingsPanelServer.133")); //$NON-NLS-1$
        generalPanel.add(serverNameField, Messages.getString("SettingsPanelServer.134")); //$NON-NLS-1$
        generalPanel.add(defaultAdministratorColorLabel, Messages.getString("SettingsPanelServer.135")); //$NON-NLS-1$
        generalPanel.add(defaultAdministratorColorButton, Messages.getString("SettingsPanelServer.136")); //$NON-NLS-1$
        generalPanel.add(provideUsageStatsLabel, Messages.getString("SettingsPanelServer.137")); //$NON-NLS-1$
        generalPanel.add(provideUsageStatsYesRadio, Messages.getString("SettingsPanelServer.138")); //$NON-NLS-1$
        generalPanel.add(provideUsageStatsNoRadio);
        generalPanel.add(provideUsageStatsMoreInfoLabel, Messages.getString("SettingsPanelServer.139")); //$NON-NLS-1$
        add(generalPanel, Messages.getString("SettingsPanelServer.140")); //$NON-NLS-1$

        channelPanel.setLayout(new MigLayout(Messages.getString("SettingsPanelServer.141"), Messages.getString("SettingsPanelServer.142"), Messages.getString("SettingsPanelServer.143"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        channelPanel.add(clearGlobalMapLabel, Messages.getString("SettingsPanelServer.144")); //$NON-NLS-1$
        channelPanel.add(clearGlobalMapYesRadio, Messages.getString("SettingsPanelServer.145")); //$NON-NLS-1$
        channelPanel.add(clearGlobalMapNoRadio);
        channelPanel.add(queueBufferSizeLabel, Messages.getString("SettingsPanelServer.146")); //$NON-NLS-1$
        channelPanel.add(queueBufferSizeField, Messages.getString("SettingsPanelServer.147")); //$NON-NLS-1$
        channelPanel.add(defaultMetaDataLabel, Messages.getString("SettingsPanelServer.148")); //$NON-NLS-1$
        channelPanel.add(defaultMetaDataSourceCheckBox, Messages.getString("SettingsPanelServer.149")); //$NON-NLS-1$
        channelPanel.add(defaultMetaDataTypeCheckBox);
        channelPanel.add(defaultMetaDataVersionCheckBox);
        add(channelPanel, Messages.getString("SettingsPanelServer.150")); //$NON-NLS-1$

        emailPanel.setLayout(new MigLayout(Messages.getString("SettingsPanelServer.151"), Messages.getString("SettingsPanelServer.152"), Messages.getString("SettingsPanelServer.153"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        emailPanel.add(smtpHostLabel, Messages.getString("SettingsPanelServer.154")); //$NON-NLS-1$
        emailPanel.add(smtpHostField, Messages.getString("SettingsPanelServer.155")); //$NON-NLS-1$
        emailPanel.add(testEmailButton);
        emailPanel.add(smtpPortLabel, Messages.getString("SettingsPanelServer.156")); //$NON-NLS-1$
        emailPanel.add(smtpPortField, Messages.getString("SettingsPanelServer.157")); //$NON-NLS-1$
        emailPanel.add(smtpTimeoutLabel, Messages.getString("SettingsPanelServer.158")); //$NON-NLS-1$
        emailPanel.add(smtpTimeoutField, Messages.getString("SettingsPanelServer.159")); //$NON-NLS-1$
        emailPanel.add(defaultFromAddressLabel, Messages.getString("SettingsPanelServer.160")); //$NON-NLS-1$
        emailPanel.add(defaultFromAddressField, Messages.getString("SettingsPanelServer.161")); //$NON-NLS-1$
        emailPanel.add(secureConnectionLabel, Messages.getString("SettingsPanelServer.162")); //$NON-NLS-1$
        emailPanel.add(secureConnectionNoneRadio, Messages.getString("SettingsPanelServer.163")); //$NON-NLS-1$
        emailPanel.add(secureConnectionTLSRadio);
        emailPanel.add(secureConnectionSSLRadio);
        emailPanel.add(requireAuthenticationLabel, Messages.getString("SettingsPanelServer.164")); //$NON-NLS-1$
        emailPanel.add(requireAuthenticationYesRadio, Messages.getString("SettingsPanelServer.165")); //$NON-NLS-1$
        emailPanel.add(requireAuthenticationNoRadio);
        emailPanel.add(usernameLabel, Messages.getString("SettingsPanelServer.166")); //$NON-NLS-1$
        emailPanel.add(usernameField, Messages.getString("SettingsPanelServer.167")); //$NON-NLS-1$
        emailPanel.add(passwordLabel, Messages.getString("SettingsPanelServer.168")); //$NON-NLS-1$
        emailPanel.add(passwordField, Messages.getString("SettingsPanelServer.169")); //$NON-NLS-1$
        add(emailPanel, Messages.getString("SettingsPanelServer.170")); //$NON-NLS-1$
    }

    private void provideUsageStatsMoreInfoLabelMouseClicked(MouseEvent evt) {
        BareBonesBrowserLaunch.openURL(UIConstants.PRIVACY_URL);
    }

    private void requireAuthenticationNoRadioActionPerformed(ActionEvent evt) {
        usernameField.setEnabled(false);
        passwordField.setEnabled(false);
        usernameLabel.setEnabled(false);
        passwordLabel.setEnabled(false);
    }

    private void requireAuthenticationYesRadioActionPerformed(ActionEvent evt) {
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);
        usernameLabel.setEnabled(true);
        passwordLabel.setEnabled(true);
    }

    private void testEmailButtonActionPerformed(ActionEvent evt) {
        resetInvalidSettings();
        ServerSettings serverSettings = getServerSettings();
        StringBuilder invalidFields = new StringBuilder();

        if (StringUtils.isBlank(serverSettings.getSmtpHost())) {
            smtpHostField.setBackground(UIConstants.INVALID_COLOR);
            invalidFields.append(Messages.getString("SettingsPanelServer.171")); //$NON-NLS-1$
        }

        if (StringUtils.isBlank(serverSettings.getSmtpPort())) {
            smtpPortField.setBackground(UIConstants.INVALID_COLOR);
            invalidFields.append(Messages.getString("SettingsPanelServer.172")); //$NON-NLS-1$
        }

        if (StringUtils.isBlank(serverSettings.getSmtpTimeout())) {
            smtpTimeoutField.setBackground(UIConstants.INVALID_COLOR);
            invalidFields.append(Messages.getString("SettingsPanelServer.173")); //$NON-NLS-1$
        }

        if (StringUtils.isBlank(serverSettings.getSmtpFrom())) {
            defaultFromAddressField.setBackground(UIConstants.INVALID_COLOR);
            invalidFields.append(Messages.getString("SettingsPanelServer.174")); //$NON-NLS-1$
        }

        if (serverSettings.getSmtpAuth()) {
            if (StringUtils.isBlank(serverSettings.getSmtpUsername())) {
                usernameField.setBackground(UIConstants.INVALID_COLOR);
                invalidFields.append(Messages.getString("SettingsPanelServer.175")); //$NON-NLS-1$
            }

            if (StringUtils.isBlank(serverSettings.getSmtpPassword())) {
                passwordField.setBackground(UIConstants.INVALID_COLOR);
                invalidFields.append(Messages.getString("SettingsPanelServer.176")); //$NON-NLS-1$
            }
        }

        String errors = invalidFields.toString();
        if (StringUtils.isNotBlank(errors)) {
            PlatformUI.MIRTH_FRAME.alertCustomError(PlatformUI.MIRTH_FRAME, errors, Messages.getString("SettingsPanelServer.177")); //$NON-NLS-1$
            return;
        }

        String sendToEmail = (String) DisplayUtil.showInputDialog(PlatformUI.MIRTH_FRAME, Messages.getString("SettingsPanelServer.178"), Messages.getString("SettingsPanelServer.179"), JOptionPane.INFORMATION_MESSAGE, null, null, serverSettings.getSmtpFrom()); //$NON-NLS-1$ //$NON-NLS-2$

        if (StringUtils.isNotBlank(sendToEmail)) {
            try {
                new InternetAddress(sendToEmail).validate();
            } catch (Exception error) {
                PlatformUI.MIRTH_FRAME.alertWarning(PlatformUI.MIRTH_FRAME, Messages.getString("SettingsPanelServer.180") + error.getMessage()); //$NON-NLS-1$
                return;
            }

            final Properties properties = new Properties();
            properties.put(Messages.getString("SettingsPanelServer.181"), serverSettings.getSmtpPort()); //$NON-NLS-1$
            properties.put(Messages.getString("SettingsPanelServer.182"), serverSettings.getSmtpSecure()); //$NON-NLS-1$
            properties.put(Messages.getString("SettingsPanelServer.183"), serverSettings.getSmtpHost()); //$NON-NLS-1$
            properties.put(Messages.getString("SettingsPanelServer.184"), serverSettings.getSmtpTimeout()); //$NON-NLS-1$
            properties.put(Messages.getString("SettingsPanelServer.185"), String.valueOf(serverSettings.getSmtpAuth())); //$NON-NLS-1$
            properties.put(Messages.getString("SettingsPanelServer.186"), serverSettings.getSmtpUsername()); //$NON-NLS-1$
            properties.put(Messages.getString("SettingsPanelServer.187"), serverSettings.getSmtpPassword()); //$NON-NLS-1$
            properties.put(Messages.getString("SettingsPanelServer.188"), sendToEmail); //$NON-NLS-1$
            properties.put(Messages.getString("SettingsPanelServer.189"), serverSettings.getSmtpFrom()); //$NON-NLS-1$

            final String workingId = PlatformUI.MIRTH_FRAME.startWorking(Messages.getString("SettingsPanelServer.190")); //$NON-NLS-1$

            SwingWorker worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {

                    try {
                        ConnectionTestResponse response = (ConnectionTestResponse) PlatformUI.MIRTH_FRAME.mirthClient.sendTestEmail(properties);

                        if (response == null) {
                            PlatformUI.MIRTH_FRAME.alertError(PlatformUI.MIRTH_FRAME, Messages.getString("SettingsPanelServer.191")); //$NON-NLS-1$
                        } else if (response.getType().equals(ConnectionTestResponse.Type.SUCCESS)) {
                            PlatformUI.MIRTH_FRAME.alertInformation(PlatformUI.MIRTH_FRAME, response.getMessage());
                        } else {
                            PlatformUI.MIRTH_FRAME.alertWarning(PlatformUI.MIRTH_FRAME, response.getMessage());
                        }

                        return null;
                    } catch (Exception e) {
                        PlatformUI.MIRTH_FRAME.alertThrowable(PlatformUI.MIRTH_FRAME, e);
                        return null;
                    }
                }

                public void done() {
                    PlatformUI.MIRTH_FRAME.stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    private JPanel generalPanel;
    private JLabel environmentNameLabel;
    private MirthTextField environmentNameField;
    private JLabel serverNameLabel;
    private MirthTextField serverNameField;
    private JLabel defaultAdministratorColorLabel;
    private JButton defaultAdministratorColorButton;

    private JLabel provideUsageStatsLabel;
    private ButtonGroup provideUsageStatsButtonGroup;
    private MirthRadioButton provideUsageStatsYesRadio;
    private MirthRadioButton provideUsageStatsNoRadio;
    private JLabel provideUsageStatsMoreInfoLabel;

    private JPanel channelPanel;
    private JLabel clearGlobalMapLabel;
    private ButtonGroup clearGlobalMapButtonGroup;
    private MirthRadioButton clearGlobalMapYesRadio;
    private MirthRadioButton clearGlobalMapNoRadio;
    private JLabel queueBufferSizeLabel;
    private MirthTextField queueBufferSizeField;
    private JLabel defaultMetaDataLabel;
    private MirthCheckBox defaultMetaDataSourceCheckBox;
    private MirthCheckBox defaultMetaDataTypeCheckBox;
    private MirthCheckBox defaultMetaDataVersionCheckBox;

    private JPanel emailPanel;
    private JLabel smtpHostLabel;
    private MirthTextField smtpHostField;
    private JButton testEmailButton;
    private JLabel smtpPortLabel;
    private MirthTextField smtpPortField;
    private JLabel smtpTimeoutLabel;
    private MirthTextField smtpTimeoutField;
    private JLabel defaultFromAddressLabel;
    private MirthTextField defaultFromAddressField;
    private JLabel secureConnectionLabel;
    private ButtonGroup secureConnectionButtonGroup;
    private MirthRadioButton secureConnectionNoneRadio;
    private MirthRadioButton secureConnectionSSLRadio;
    private MirthRadioButton secureConnectionTLSRadio;
    private JLabel requireAuthenticationLabel;
    private ButtonGroup requireAuthenticationButtonGroup;
    private MirthRadioButton requireAuthenticationYesRadio;
    private MirthRadioButton requireAuthenticationNoRadio;
    private JLabel usernameLabel;
    private MirthTextField usernameField;
    private JLabel passwordLabel;
    private MirthPasswordField passwordField;
}