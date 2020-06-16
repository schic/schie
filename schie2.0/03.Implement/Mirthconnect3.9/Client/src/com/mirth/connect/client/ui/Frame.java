/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.log4j.Logger;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.action.ActionFactory;
import org.jdesktop.swingx.action.ActionManager;
import org.jdesktop.swingx.action.BoundAction;
import org.jdesktop.swingx.painter.MattePainter;
import org.syntax.jedit.JEditTextArea;

import com.mirth.connect.client.core.Client;
import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.core.ConnectServiceUtil;
import com.mirth.connect.client.core.ForbiddenException;
import com.mirth.connect.client.core.RequestAbortedException;
import com.mirth.connect.client.core.TaskConstants;
import com.mirth.connect.client.core.UnauthorizedException;
import com.mirth.connect.client.core.Version;
import com.mirth.connect.client.core.VersionMismatchException;
import com.mirth.connect.client.ui.DashboardPanel.TableState;
import com.mirth.connect.client.ui.alert.AlertEditPanel;
import com.mirth.connect.client.ui.alert.AlertPanel;
import com.mirth.connect.client.ui.alert.DefaultAlertEditPanel;
import com.mirth.connect.client.ui.alert.DefaultAlertPanel;
import com.mirth.connect.client.ui.browsers.event.EventBrowser;
import com.mirth.connect.client.ui.browsers.message.MessageBrowser;
import com.mirth.connect.client.ui.codetemplate.CodeTemplatePanel;
import com.mirth.connect.client.ui.components.rsta.ac.js.MirthJavaScriptLanguageSupport;
import com.mirth.connect.client.ui.dependencies.ChannelDependenciesWarningDialog;
import com.mirth.connect.client.ui.extensionmanager.ExtensionManagerPanel;
import com.mirth.connect.client.ui.tag.SettingsPanelTags;
import com.mirth.connect.client.ui.util.DisplayUtil;
import com.mirth.connect.donkey.model.channel.DeployedState;
import com.mirth.connect.donkey.model.channel.DestinationConnectorPropertiesInterface;
import com.mirth.connect.donkey.model.channel.MetaDataColumn;
import com.mirth.connect.donkey.model.channel.SourceConnectorPropertiesInterface;
import com.mirth.connect.donkey.model.message.RawMessage;
import com.mirth.connect.model.ApiProvider;
import com.mirth.connect.model.Channel;
import com.mirth.connect.model.ChannelHeader;
import com.mirth.connect.model.ChannelStatus;
import com.mirth.connect.model.ChannelTag;
import com.mirth.connect.model.Connector;
import com.mirth.connect.model.Connector.Mode;
import com.mirth.connect.model.ConnectorMetaData;
import com.mirth.connect.model.DashboardChannelInfo;
import com.mirth.connect.model.DashboardStatus;
import com.mirth.connect.model.DashboardStatus.StatusType;
import com.mirth.connect.model.EncryptionSettings;
import com.mirth.connect.model.InvalidChannel;
import com.mirth.connect.model.MetaData;
import com.mirth.connect.model.PluginMetaData;
import com.mirth.connect.model.ResourceProperties;
import com.mirth.connect.model.ServerSettings;
import com.mirth.connect.model.UpdateSettings;
import com.mirth.connect.model.User;
import com.mirth.connect.model.alert.AlertInfo;
import com.mirth.connect.model.alert.AlertModel;
import com.mirth.connect.model.alert.AlertStatus;
import com.mirth.connect.model.converters.ObjectXMLSerializer;
import com.mirth.connect.model.filters.MessageFilter;
import com.mirth.connect.plugins.DashboardColumnPlugin;
import com.mirth.connect.plugins.DataTypeClientPlugin;
import com.mirth.connect.util.ChannelDependencyException;
import com.mirth.connect.util.ChannelDependencyGraph;
import com.mirth.connect.util.CharsetUtils;
import com.mirth.connect.util.DirectedAcyclicGraphNode;
import com.mirth.connect.util.JavaScriptSharedUtil;
import com.mirth.connect.util.MigrationUtil;

/**
 * The main content frame for the Mirth Client Application. Extends JXFrame and sets up all content.
 */
public class Frame extends JXFrame {

    private Logger logger = Logger.getLogger(this.getClass());
    public Client mirthClient;
    public DashboardPanel dashboardPanel = null;
    public ChannelPanel channelPanel = null;
    public SettingsPane settingsPane = null;
    public UserPanel userPanel = null;
    public ChannelSetup channelEditPanel = null;
    public EventBrowser eventBrowser = null;
    public MessageBrowser messageBrowser = null;
    public AlertPanel alertPanel = null;
    public AlertEditPanel alertEditPanel = null;
    public CodeTemplatePanel codeTemplatePanel = null;
    public GlobalScriptsPanel globalScriptsPanel = null;
    public ExtensionManagerPanel extensionsPanel = null;
    public JXTaskPaneContainer taskPaneContainer;
    public List<DashboardStatus> status = null;
    public List<User> users = null;
    public ActionManager manager = ActionManager.getInstance();
    public JPanel contentPanel;
    public BorderLayout borderLayout1 = new BorderLayout();
    public StatusBar statusBar;
    public JSplitPane splitPane = new JSplitPane();
    public JScrollPane taskPane = new JScrollPane();
    public JScrollPane contentPane = new JScrollPane();
    public Component currentContentPage = null;
    public JXTaskPaneContainer currentTaskPaneContainer = null;
    public JScrollPane container;
    public EditMessageDialog editMessageDialog = null;

    // Task panes and popup menus
    public JXTaskPane viewPane;
    public JXTaskPane otherPane;
    public JXTaskPane dashboardTasks;
    public JPopupMenu dashboardPopupMenu;
    public JXTaskPane eventTasks;
    public JPopupMenu eventPopupMenu;
    public JXTaskPane messageTasks;
    public JPopupMenu messagePopupMenu;
    public JXTaskPane details;
    public JXTaskPane channelEditTasks;
    public JPopupMenu channelEditPopupMenu;
    public JXTaskPane userTasks;
    public JPopupMenu userPopupMenu;
    public JXTaskPane alertTasks;
    public JPopupMenu alertPopupMenu;
    public JXTaskPane alertEditTasks;
    public JPopupMenu alertEditPopupMenu;
    public JXTaskPane globalScriptsTasks;
    public JPopupMenu globalScriptsPopupMenu;
    public JXTaskPane extensionsTasks;
    public JPopupMenu extensionsPopupMenu;

    public JXTitledPanel rightContainer;
    private ExecutorService statusUpdaterExecutor = Executors.newSingleThreadExecutor();
    private Future<?> statusUpdaterJob = null;
    public static Preferences userPreferences;
    private boolean connectionError;
    private ArrayList<CharsetEncodingInformation> availableCharsetEncodings = null;
    private List<String> charsetEncodings = null;
    public boolean isEditingChannel = false;
    private boolean isEditingAlert = false;
    private LinkedHashMap<String, String> workingStatuses = new LinkedHashMap<String, String>();
    public LinkedHashMap<String, String> dataTypeToDisplayName;
    public LinkedHashMap<String, String> displayNameToDataType;
    private Map<String, PluginMetaData> loadedPlugins;
    private Map<String, ConnectorMetaData> loadedConnectors;
    private Map<String, Integer> safeErrorFailCountMap = new HashMap<String, Integer>();
    private Map<Component, String> componentTaskMap = new HashMap<Component, String>();
    private boolean acceleratorKeyPressed = false;
    private boolean canSave = true;
    private RemoveMessagesDialog removeMessagesDialog;
    private MessageExportDialog messageExportDialog;
    private MessageImportDialog messageImportDialog;
    private AttachmentExportDialog attachmentExportDialog;
    private KeyEventDispatcher keyEventDispatcher = null;
    private int deployedChannelCount;

    private static final int REFRESH_BLOCK_SIZE = 100;

    public Frame() {
        Platform.setImplicitExit(false);

        // Load RSyntaxTextArea language support
        LanguageSupportFactory.get().addLanguageSupport(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT, MirthJavaScriptLanguageSupport.class.getName());

        rightContainer = new JXTitledPanel();

        taskPaneContainer = new JXTaskPaneContainer();

        StringBuilder titleText = new StringBuilder();

        if (!StringUtils.isBlank(PlatformUI.ENVIRONMENT_NAME)) {
            titleText.append(PlatformUI.ENVIRONMENT_NAME + Messages.getString("Frame.0")); //$NON-NLS-1$
        }

        if (!StringUtils.isBlank(PlatformUI.SERVER_NAME)) {
            titleText.append(PlatformUI.SERVER_NAME);
        } else {
            titleText.append(PlatformUI.SERVER_URL);
        }

        titleText.append(Messages.getString("Frame.1") + UIConstants.TITLE_TEXT); //$NON-NLS-1$

        setTitle(titleText.toString());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.2"))).getImage()); //$NON-NLS-1$
        makePaneContainer();

        connectionError = false;

        this.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                if (channelEditPanel != null && channelEditPanel.filterPane != null) {
                    channelEditPanel.filterPane.resizePanes();
                }
                if (channelEditPanel != null && channelEditPanel.transformerPane != null) {
                    channelEditPanel.transformerPane.resizePanes();
                }
            }

            public void componentHidden(ComponentEvent e) {}

            public void componentShown(ComponentEvent e) {}

            public void componentMoved(ComponentEvent e) {}
        });

        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (logout(true)) {
                    System.exit(0);
                }
            }
        });

        keyEventDispatcher = new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                // Update the state of the accelerator key (CTRL on Windows)
                updateAcceleratorKeyPressed(e);
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    }

    /**
     * Prepares the list of the encodings. This method is called from the Frame class.
     * 
     */
    public void setCharsetEncodings() {
        if (this.availableCharsetEncodings != null) {
            return;
        }
        try {
            this.charsetEncodings = this.mirthClient.getAvailableCharsetEncodings();
            this.availableCharsetEncodings = new ArrayList<CharsetEncodingInformation>();
            this.availableCharsetEncodings.add(new CharsetEncodingInformation(UIConstants.DEFAULT_ENCODING_OPTION, Messages.getString("Frame.3"))); //$NON-NLS-1$
            for (int i = 0; i < charsetEncodings.size(); i++) {
                String canonical = charsetEncodings.get(i);
                this.availableCharsetEncodings.add(new CharsetEncodingInformation(canonical, canonical));
            }
        } catch (Exception e) {
            alertError(this, Messages.getString("Frame.4") + e); //$NON-NLS-1$
        }
    }

    public void setupCharsetEncodingForConnector(javax.swing.JComboBox charsetEncodingCombobox) {
        setupCharsetEncodingForConnector(charsetEncodingCombobox, false);
    }

    /**
     * Creates all the items in the combo box for the connectors.
     * 
     * This method is called from each connector.
     */
    public void setupCharsetEncodingForConnector(javax.swing.JComboBox charsetEncodingCombobox, boolean allowNone) {
        if (this.availableCharsetEncodings == null) {
            this.setCharsetEncodings();
        }
        if (this.availableCharsetEncodings == null) {
            logger.error(Messages.getString("Frame.5")); //$NON-NLS-1$
            return;
        }
        charsetEncodingCombobox.removeAllItems();
        for (int i = 0; i < this.availableCharsetEncodings.size(); i++) {
            charsetEncodingCombobox.addItem(this.availableCharsetEncodings.get(i));

            // Insert the NONE option after the default option
            if (allowNone && i == 0) {
                charsetEncodingCombobox.addItem(new CharsetEncodingInformation(CharsetUtils.NONE, Messages.getString("Frame.6"))); //$NON-NLS-1$
            }
        }
    }

    public void setPreviousSelectedEncodingForConnector(javax.swing.JComboBox charsetEncodingCombobox, String selectedCharset) {
        setPreviousSelectedEncodingForConnector(charsetEncodingCombobox, selectedCharset, false);
    }

    /**
     * Sets the combobox for the string previously selected. If the server can't support the
     * encoding, the default one is selected. This method is called from each connector.
     */
    public void setPreviousSelectedEncodingForConnector(javax.swing.JComboBox charsetEncodingCombobox, String selectedCharset, boolean allowNone) {
        if (this.availableCharsetEncodings == null) {
            this.setCharsetEncodings();
        }
        if (this.availableCharsetEncodings == null) {
            logger.error(Messages.getString("Frame.7")); //$NON-NLS-1$
            return;
        }
        if ((selectedCharset == null) || (selectedCharset.equalsIgnoreCase(UIConstants.DEFAULT_ENCODING_OPTION))) {
            charsetEncodingCombobox.setSelectedIndex(0);
        } else if (allowNone && selectedCharset.equalsIgnoreCase(CharsetUtils.NONE)) {
            charsetEncodingCombobox.setSelectedIndex(1);
        } else if (this.charsetEncodings.contains(selectedCharset)) {
            int index = this.availableCharsetEncodings.indexOf(new CharsetEncodingInformation(selectedCharset, selectedCharset));
            if (index < 0) {
                logger.error(Messages.getString("Frame.8")); //$NON-NLS-1$
                index = 0;
            }
            if (allowNone && index > 0) { // need to increment since this.availableCharsetEncodings does not include the None option
                index++;
            }
            charsetEncodingCombobox.setSelectedIndex(index);
        } else {
            alertInformation(this, Messages.getString("Frame.9") + selectedCharset + Messages.getString("Frame.10")); //$NON-NLS-1$ //$NON-NLS-2$
            charsetEncodingCombobox.setSelectedIndex(0);
        }
    }

    /**
     * Get the strings which identifies the encoding selected by the user.
     * 
     * This method is called from each connector.
     */
    public String getSelectedEncodingForConnector(javax.swing.JComboBox charsetEncodingCombobox) {
        try {
            return ((CharsetEncodingInformation) charsetEncodingCombobox.getSelectedItem()).getCanonicalName();
        } catch (Throwable t) {
            alertInformation(this, Messages.getString("Frame.11") + t); //$NON-NLS-1$
            return UIConstants.DEFAULT_ENCODING_OPTION;
        }
    }

    /**
     * Called to set up this main window frame.
     */
    public void setupFrame(Client mirthClient) throws ClientException {

        LoginPanel login = LoginPanel.getInstance();

        // Initialize the send message dialog
        editMessageDialog = new EditMessageDialog();

        this.mirthClient = mirthClient;
        login.setStatus(Messages.getString("Frame.12")); //$NON-NLS-1$
        try {
            loadExtensionMetaData();
        } catch (ClientException e) {
            alertError(this, Messages.getString("Frame.13")); //$NON-NLS-1$
            throw e;
        }

        // Re-initialize the controller every time the frame is setup
        AuthorizationControllerFactory.getAuthorizationController().initialize();
        channelPanel = new ChannelPanel();
        channelPanel.retrieveGroups();
        channelPanel.retrieveDependencies();

        // Initialize all of the extensions now that the metadata has been retrieved
        // Make sure to initialize before the code template panel is created because it needs extensions
        LoadedExtensions.getInstance().initialize();

        codeTemplatePanel = new CodeTemplatePanel(this);

        // Now it's okay to start the plugins
        LoadedExtensions.getInstance().startPlugins();

        mirthClient.setRecorder(LoadedExtensions.getInstance().getRecorder());

        statusBar = new StatusBar();
        statusBar.setBorder(BorderFactory.createEmptyBorder());

        channelPanel.initPanelPlugins();

        // Load the data type/display name maps now that the extensions have been loaded.
        dataTypeToDisplayName = new LinkedHashMap<String, String>();
        displayNameToDataType = new LinkedHashMap<String, String>();
        for (Entry<String, DataTypeClientPlugin> entry : LoadedExtensions.getInstance().getDataTypePlugins().entrySet()) {
            dataTypeToDisplayName.put(entry.getKey(), entry.getValue().getDisplayName());
            displayNameToDataType.put(entry.getValue().getDisplayName(), entry.getKey());
        }

        setInitialVisibleTasks();
        login.setStatus(Messages.getString("Frame.14")); //$NON-NLS-1$
        userPreferences = Preferences.userNodeForPackage(Mirth.class);
        userPreferences.put(Messages.getString("Frame.15"), PlatformUI.SERVER_URL); //$NON-NLS-1$
        login.setStatus(Messages.getString("Frame.16")); //$NON-NLS-1$
        splitPane.setDividerSize(0);
        splitPane.setBorder(BorderFactory.createEmptyBorder());

        contentPanel = (JPanel) getContentPane();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder());
        taskPane.setBorder(BorderFactory.createEmptyBorder());

        contentPane.setBorder(BorderFactory.createEmptyBorder());

        buildContentPanel(rightContainer, contentPane, false);

        // Determine background color from user preference if available
        Color backgroundColor = PlatformUI.DEFAULT_BACKGROUND_COLOR;
        try {
            User currentUser = getCurrentUser(this);
            if (currentUser != null) {
                String backgroundColorStr = mirthClient.getUserPreference(currentUser.getId(), UIConstants.USER_PREF_KEY_BACKGROUND_COLOR);
                if (StringUtils.isNotBlank(backgroundColorStr)) {
                    Color backgroundColorPreference = ObjectXMLSerializer.getInstance().deserialize(backgroundColorStr, Color.class);
                    if (backgroundColorPreference != null) {
                        backgroundColor = backgroundColorPreference;
                    }
                }
            }
        } catch (Exception e) {
            alertThrowable(this, e);
        }
        setupBackgroundPainters(backgroundColor);

        splitPane.add(rightContainer, JSplitPane.RIGHT);
        splitPane.add(taskPane, JSplitPane.LEFT);
        taskPane.setMinimumSize(new Dimension(UIConstants.TASK_PANE_WIDTH, 0));
        splitPane.setDividerLocation(UIConstants.TASK_PANE_WIDTH);

        contentPanel.add(statusBar, BorderLayout.SOUTH);
        contentPanel.add(splitPane, java.awt.BorderLayout.CENTER);

        try {
            PlatformUI.SERVER_ID = mirthClient.getServerId();
            PlatformUI.SERVER_VERSION = mirthClient.getVersion();
            PlatformUI.SERVER_TIMEZONE = mirthClient.getServerTimezone();
            PlatformUI.SERVER_TIME = mirthClient.getServerTime();

            setTitle(getTitle() + Messages.getString("Frame.17") + PlatformUI.SERVER_VERSION + Messages.getString("Frame.18")); //$NON-NLS-1$ //$NON-NLS-2$

            PlatformUI.BUILD_DATE = mirthClient.getBuildDate();

            // Initialize ObjectXMLSerializer once we know the server version
            try {
                ObjectXMLSerializer.getInstance().init(PlatformUI.SERVER_VERSION);
            } catch (Exception e) {
            }
        } catch (ClientException e) {
            alertError(this, Messages.getString("Frame.19")); //$NON-NLS-1$
        }

        try {
            JavaScriptSharedUtil.setRhinoLanguageVersion(mirthClient.getRhinoLanguageVersion());
        } catch (ClientException e) {
            alertError(this, Messages.getString("Frame.20")); //$NON-NLS-1$
        }

        // Display the server timezone information
        statusBar.setTimezoneText(PlatformUI.SERVER_TIMEZONE);
        statusBar.setServerTime(PlatformUI.SERVER_TIME);

        // Refresh resources and tags
        if (settingsPane == null) {
            settingsPane = new SettingsPane();
        }

        SettingsPanelResources resourcesPanel = (SettingsPanelResources) settingsPane.getSettingsPanel(SettingsPanelResources.TAB_NAME);
        if (resourcesPanel != null) {
            resourcesPanel.doRefresh();
        }

        SettingsPanelTags tagsPanel = (SettingsPanelTags) settingsPane.getSettingsPanel(SettingsPanelTags.TAB_NAME);
        if (tagsPanel != null) {
            tagsPanel.doRefresh();
        }

        setCurrentTaskPaneContainer(taskPaneContainer);
        login.setStatus(Messages.getString("Frame.21")); //$NON-NLS-1$
        doShowDashboard();
        login.setStatus(Messages.getString("Frame.22")); //$NON-NLS-1$
        channelEditPanel = new ChannelSetup();
        login.setStatus(Messages.getString("Frame.23")); //$NON-NLS-1$
        if (alertEditPanel == null) {
            alertEditPanel = new DefaultAlertEditPanel();
        }
        login.setStatus(Messages.getString("Frame.24")); //$NON-NLS-1$
        messageBrowser = new MessageBrowser();

        // Refresh code templates after extensions have been loaded
        codeTemplatePanel.doRefreshCodeTemplates(false);

        LicenseClient.start();

        // DEBUGGING THE UIDefaults:

//         UIDefaults uiDefaults = UIManager.getDefaults(); Enumeration enum1 =
//         uiDefaults.keys(); while (enum1.hasMoreElements()) { Object key =
//         enum1.nextElement(); Object val = uiDefaults.get(key);
////         if(key.toString().indexOf("ComboBox") != -1)
//         System.out.println("UIManager.put(\"" + key.toString() + "\",\"" +
//         (null != val ? val.toString() : "(null)") + "\");"); }

    }

    public void setupBackgroundPainters(Color color) {
        // Set task pane container background painter
        MattePainter taskPanePainter = new MattePainter(new GradientPaint(0f, 0f, color, 0f, 1f, color));
        taskPanePainter.setPaintStretched(true);
        taskPaneContainer.setBackgroundPainter(taskPanePainter);

        // Set main content container title painter
        MattePainter contentTitlePainter = new MattePainter(new GradientPaint(0f, 0f, color, 0f, 1f, color));
        contentTitlePainter.setPaintStretched(true);
        rightContainer.setTitlePainter(contentTitlePainter);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (statusBar != null) {
            statusBar.shutdown();
        }
    }

    private void loadExtensionMetaData() throws ClientException {
        loadedPlugins = mirthClient.getPluginMetaData();
        loadedConnectors = mirthClient.getConnectorMetaData();

        // Register extension JAX-RS providers with the client
        Set<String> apiProviderPackages = new HashSet<String>();
        Set<String> apiProviderClasses = new HashSet<String>();

        for (Object extensionMetaData : CollectionUtils.union(loadedPlugins.values(), loadedConnectors.values())) {
            MetaData metaData = (MetaData) extensionMetaData;
            if (mirthClient.isExtensionEnabled(metaData.getName())) {
                for (ApiProvider provider : metaData.getApiProviders(Version.getLatest())) {
                    switch (provider.getType()) {
                        case SERVLET_INTERFACE_PACKAGE:
                        case CORE_PACKAGE:
                            apiProviderPackages.add(provider.getName());
                            break;
                        case SERVLET_INTERFACE:
                        case CORE_CLASS:
                            apiProviderClasses.add(provider.getName());
                            break;
                        default:
                    }
                }
            }
        }

        mirthClient.registerApiProviders(apiProviderPackages, apiProviderClasses);
    }

    /**
     * Builds the content panel with a title bar and settings.
     */
    private void buildContentPanel(JXTitledPanel container, JScrollPane component, boolean opaque) {
        container.getContentContainer().setLayout(new BorderLayout());
        container.setBorder(null);
//        container.setTitleFont(new Font(Messages.getString("Frame.25"), Font.BOLD, 18)); //$NON-NLS-1$
        container.setTitleFont(new Font("宋体", Font.BOLD, 20));
        container.setTitleForeground(UIConstants.HEADER_TITLE_TEXT_COLOR);
        JLabel mirthConnectImage = new JLabel();
        mirthConnectImage.setIcon(UIConstants.MIRTHCONNECT_LOGO_GRAY);
        mirthConnectImage.setText(Messages.getString("Frame.26")); //$NON-NLS-1$
        mirthConnectImage.setToolTipText(UIConstants.MIRTHCONNECT_TOOLTIP);
        mirthConnectImage.setCursor(new Cursor(Cursor.HAND_CURSOR));

        mirthConnectImage.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BareBonesBrowserLaunch.openURL(UIConstants.MIRTHCONNECT_URL);
            }
        });

        ((JPanel) container.getComponent(0)).add(mirthConnectImage);

        component.setBorder(new LineBorder(Color.GRAY, 1));
        component.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        component.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        container.getContentContainer().add(component);
    }

    /**
     * Set the main content panel title to a String
     */
    public void setPanelName(String name) {
        rightContainer.setTitle(name);
        statusBar.setStatusText(Messages.getString("Frame.27")); //$NON-NLS-1$
    }

    public String startWorking(final String displayText) {
        String id = null;

        synchronized (workingStatuses) {
            if (statusBar != null) {
                id = UUID.randomUUID().toString();
                workingStatuses.put(id, displayText);
                statusBar.setWorking(true);
                statusBar.setText(displayText);
            }
        }

        return id;
    }

    public void stopWorking(final String workingId) {
        synchronized (workingStatuses) {
            if ((statusBar != null) && (workingId != null)) {
                workingStatuses.remove(workingId);

                if (workingStatuses.size() > 0) {
                    statusBar.setWorking(true);
                    statusBar.setText(new LinkedList<String>(workingStatuses.values()).getLast());
                } else {
                    statusBar.setWorking(false);
                    statusBar.setText(Messages.getString("Frame.28")); //$NON-NLS-1$
                }
            }
        }
    }

    /**
     * Changes the current content page to the Channel Editor with the new channel specified as the
     * loaded one.
     */
    public void setupChannel(Channel channel, String groupId) {
        setBold(viewPane, UIConstants.ERROR_CONSTANT);
        setCurrentContentPage(channelEditPanel);
        setFocus(channelEditTasks);
        setVisibleTasks(channelEditTasks, channelEditPopupMenu, 0, 0, false);
        confirmLeave();
        channelEditPanel.addChannel(channel, groupId);
    }

    /**
     * Edits a channel at a specified index, setting that channel as the current channel in the
     * editor.
     */
    public void editChannel(Channel channel) {
        String alertMessage = channelEditPanel.checkInvalidPluginProperties(channel);
        if (StringUtils.isNotBlank(alertMessage)) {
            if (!alertOption(this, alertMessage + Messages.getString("Frame.29"))) { //$NON-NLS-1$
                return;
            }
        }

        confirmLeave();
        setBold(viewPane, UIConstants.ERROR_CONSTANT);
        setCurrentContentPage(channelEditPanel);
        setFocus(channelEditTasks);
        setVisibleTasks(channelEditTasks, channelEditPopupMenu, 0, 4, false);
        channelEditPanel.editChannel(channel);
    }

    /**
     * Changes the current content page to the Alert Editor with the new alert specified as the
     * loaded one.
     */
    public void setupAlert(Map<String, Map<String, String>> protocolOptions) {
        setBold(viewPane, UIConstants.ERROR_CONSTANT);
        setCurrentContentPage(alertEditPanel);
        setFocus(alertEditTasks);
        setVisibleTasks(alertEditTasks, alertEditPopupMenu, 0, 0, false);
        alertEditPanel.addAlert(protocolOptions);
    }

    /**
     * Edits an alert at a specified index, setting that alert as the current alert in the editor.
     */
    public void editAlert(AlertModel alertModel, Map<String, Map<String, String>> protocolOptions) {
        if (alertEditPanel.editAlert(alertModel, protocolOptions)) {
            setBold(viewPane, UIConstants.ERROR_CONSTANT);
            setCurrentContentPage(alertEditPanel);
            setFocus(alertEditTasks);
            setVisibleTasks(alertEditTasks, alertEditPopupMenu, 0, 0, false);
        }
    }

    /**
     * Edit global scripts
     */
    public void editGlobalScripts() {
        setBold(viewPane, UIConstants.ERROR_CONSTANT);
        setCurrentContentPage(globalScriptsPanel);
        setFocus(globalScriptsTasks);
        setVisibleTasks(globalScriptsTasks, globalScriptsPopupMenu, 0, 0, false);
        setPanelName(Messages.getString("Frame.30")); //$NON-NLS-1$
    }

    /**
     * Sets the current content page to the passed in page.
     */
    public void setCurrentContentPage(Component contentPageObject) {
        if (contentPageObject == currentContentPage) {
            return;
        }

        if (currentContentPage != null) {
            contentPane.getViewport().remove(currentContentPage);
        }

        contentPane.getViewport().add(contentPageObject);
        currentContentPage = contentPageObject;

        // Always cancel the current job if it is still running.
        if (statusUpdaterJob != null && !statusUpdaterJob.isDone()) {
            statusUpdaterJob.cancel(true);
        }

        // Start a new status updater job if the current content page is the dashboard
        if (currentContentPage == dashboardPanel || currentContentPage == alertPanel) {
            statusUpdaterJob = statusUpdaterExecutor.submit(new StatusUpdater());
        }
    }

    /**
     * Sets the current task pane container
     */
    private void setCurrentTaskPaneContainer(JXTaskPaneContainer container) {
        if (container == currentTaskPaneContainer) {
            return;
        }

        if (currentTaskPaneContainer != null) {
            taskPane.getViewport().remove(currentTaskPaneContainer);
        }

        taskPane.getViewport().add(container);
        currentTaskPaneContainer = container;
    }

    /**
     * Makes all of the task panes and shows the dashboard panel.
     */
    private void makePaneContainer() {
        createViewPane();
        createChannelEditPane();
        createDashboardPane();
        createEventPane();
        createMessagePane();
        createUserPane();
        createAlertPane();
        createAlertEditPane();
        createGlobalScriptsPane();
        createExtensionsPane();
        createOtherPane();
    }

    private void setInitialVisibleTasks() {
        // View Pane
        setVisibleTasks(viewPane, null, 0, -1, true);

        // Alert Pane
        setVisibleTasks(alertTasks, alertPopupMenu, 0, -1, true);
        setVisibleTasks(alertTasks, alertPopupMenu, 4, -1, false);

        // Alert Edit Pane
        setVisibleTasks(alertEditTasks, alertEditPopupMenu, 0, 0, false);
        setVisibleTasks(alertEditTasks, alertEditPopupMenu, 1, 1, true);

        // Channel Edit Pane
        setVisibleTasks(channelEditTasks, channelEditPopupMenu, 0, 15, false);
        setVisibleTasks(channelEditTasks, channelEditPopupMenu, 14, 14, true);

        // Dashboard Pane
        setVisibleTasks(dashboardTasks, dashboardPopupMenu, 0, 0, true);
        setVisibleTasks(dashboardTasks, dashboardPopupMenu, 1, -1, false);

        // Event Pane
        setVisibleTasks(eventTasks, eventPopupMenu, 0, 2, true);

        // Message Pane
        setVisibleTasks(messageTasks, messagePopupMenu, 0, -1, true);
        setVisibleTasks(messageTasks, messagePopupMenu, 6, -1, false);
        setVisibleTasks(messageTasks, messagePopupMenu, 7, 7, true);

        // User Pane
        setVisibleTasks(userTasks, userPopupMenu, 0, 1, true);
        setVisibleTasks(userTasks, userPopupMenu, 2, -1, false);

        // Global Scripts Pane
        setVisibleTasks(globalScriptsTasks, globalScriptsPopupMenu, 0, 0, false);
        setVisibleTasks(globalScriptsTasks, globalScriptsPopupMenu, 1, -1, true);

        // Extensions Pane
        setVisibleTasks(extensionsTasks, extensionsPopupMenu, 0, 0, true);
        setVisibleTasks(extensionsTasks, extensionsPopupMenu, 1, -1, false);

        // Other Pane
        setVisibleTasks(otherPane, null, 0, -1, true);
    }

    /**
     * Creates the view task pane.
     */
    private void createViewPane() {
        // Create View pane
        viewPane = new JXTaskPane();
        viewPane.setTitle(Messages.getString("Frame.31")); //$NON-NLS-1$
        viewPane.setName(TaskConstants.VIEW_KEY);
        viewPane.setFocusable(false);

        addTask(TaskConstants.VIEW_DASHBOARD, Messages.getString("Frame.32"), Messages.getString("Frame.33"), Messages.getString("Frame.34"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.35"))), viewPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.VIEW_CHANNEL, Messages.getString("Frame.36"), Messages.getString("Frame.37"), Messages.getString("Frame.38"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.39"))), viewPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.VIEW_USERS, Messages.getString("Frame.40"), Messages.getString("Frame.41"), Messages.getString("Frame.42"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.43"))), viewPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.VIEW_SETTINGS, Messages.getString("Frame.44"), Messages.getString("Frame.45"), Messages.getString("Frame.46"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.47"))), viewPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.VIEW_ALERTS, Messages.getString("Frame.48"), Messages.getString("Frame.49"), Messages.getString("Frame.50"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.51"))), viewPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.VIEW_EVENTS, Messages.getString("Frame.52"), Messages.getString("Frame.53"), Messages.getString("Frame.54"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.55"))), viewPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.VIEW_EXTENSIONS, Messages.getString("Frame.56"), Messages.getString("Frame.57"), Messages.getString("Frame.58"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.59"))), viewPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        setNonFocusable(viewPane);
        taskPaneContainer.add(viewPane);
        viewPane.setVisible(true);
    }

    /**
     * Creates the template task pane.
     */
    private void createAlertPane() {
        // Create Alert Edit Tasks Pane
        alertTasks = new JXTaskPane();
        alertPopupMenu = new JPopupMenu();
        alertTasks.setTitle(Messages.getString("Frame.60")); //$NON-NLS-1$
        alertTasks.setName(TaskConstants.ALERT_KEY);
        alertTasks.setFocusable(false);

        addTask(TaskConstants.ALERT_REFRESH, Messages.getString("Frame.61"), Messages.getString("Frame.62"), Messages.getString("Frame.63"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.64"))), alertTasks, alertPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.ALERT_NEW, Messages.getString("Frame.65"), Messages.getString("Frame.66"), Messages.getString("Frame.67"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.68"))), alertTasks, alertPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.ALERT_IMPORT, Messages.getString("Frame.69"), Messages.getString("Frame.70"), Messages.getString("Frame.71"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.72"))), alertTasks, alertPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.ALERT_EXPORT_ALL, Messages.getString("Frame.73"), Messages.getString("Frame.74"), Messages.getString("Frame.75"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.76"))), alertTasks, alertPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.ALERT_EXPORT, Messages.getString("Frame.77"), Messages.getString("Frame.78"), Messages.getString("Frame.79"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.80"))), alertTasks, alertPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.ALERT_DELETE, Messages.getString("Frame.81"), Messages.getString("Frame.82"), Messages.getString("Frame.83"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.84"))), alertTasks, alertPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.ALERT_EDIT, Messages.getString("Frame.85"), Messages.getString("Frame.86"), Messages.getString("Frame.87"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.88"))), alertTasks, alertPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.ALERT_ENABLE, Messages.getString("Frame.89"), Messages.getString("Frame.90"), Messages.getString("Frame.91"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.92"))), alertTasks, alertPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.ALERT_DISABLE, Messages.getString("Frame.93"), Messages.getString("Frame.94"), Messages.getString("Frame.95"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.96"))), alertTasks, alertPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        setNonFocusable(alertTasks);
        taskPaneContainer.add(alertTasks);
    }

    /**
     * Creates the template task pane.
     */
    private void createAlertEditPane() {
        // Create Alert Edit Tasks Pane
        alertEditTasks = new JXTaskPane();
        alertEditPopupMenu = new JPopupMenu();
        alertEditTasks.setTitle(Messages.getString("Frame.97")); //$NON-NLS-1$
        alertEditTasks.setName(TaskConstants.ALERT_EDIT_KEY);
        alertEditTasks.setFocusable(false);

        addTask(TaskConstants.ALERT_EDIT_SAVE, Messages.getString("Frame.98"), Messages.getString("Frame.99"), Messages.getString("Frame.100"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.101"))), alertEditTasks, alertEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.ALERT_EDIT_EXPORT, Messages.getString("Frame.102"), Messages.getString("Frame.103"), Messages.getString("Frame.104"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.105"))), alertEditTasks, alertEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        setNonFocusable(alertEditTasks);
        taskPaneContainer.add(alertEditTasks);
    }

    /**
     * Creates the channel edit task pane.
     */
    private void createChannelEditPane() {
        // Create Channel Edit Tasks Pane
        channelEditTasks = new JXTaskPane();
        channelEditPopupMenu = new JPopupMenu();
        channelEditTasks.setTitle(Messages.getString("Frame.106")); //$NON-NLS-1$
        channelEditTasks.setName(TaskConstants.CHANNEL_EDIT_KEY);
        channelEditTasks.setFocusable(false);

        addTask(TaskConstants.CHANNEL_EDIT_SAVE, Messages.getString("Frame.107"), Messages.getString("Frame.108"), Messages.getString("Frame.109"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.110"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_VALIDATE, Messages.getString("Frame.111"), Messages.getString("Frame.112"), Messages.getString("Frame.113"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.114"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_NEW_DESTINATION, Messages.getString("Frame.115"), Messages.getString("Frame.116"), Messages.getString("Frame.117"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.118"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_DELETE_DESTINATION, Messages.getString("Frame.119"), Messages.getString("Frame.120"), Messages.getString("Frame.121"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.122"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_CLONE_DESTINATION, Messages.getString("Frame.123"), Messages.getString("Frame.124"), Messages.getString("Frame.125"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.126"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_ENABLE_DESTINATION, Messages.getString("Frame.127"), Messages.getString("Frame.128"), Messages.getString("Frame.129"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.130"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_DISABLE_DESTINATION, Messages.getString("Frame.131"), Messages.getString("Frame.132"), Messages.getString("Frame.133"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.134"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_MOVE_DESTINATION_UP, Messages.getString("Frame.135"), Messages.getString("Frame.136"), Messages.getString("Frame.137"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.138"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_MOVE_DESTINATION_DOWN, Messages.getString("Frame.139"), Messages.getString("Frame.140"), Messages.getString("Frame.141"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.142"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_FILTER, UIConstants.EDIT_FILTER, Messages.getString("Frame.143"), Messages.getString("Frame.144"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.145"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        addTask(TaskConstants.CHANNEL_EDIT_TRANSFORMER, UIConstants.EDIT_TRANSFORMER, Messages.getString("Frame.146"), Messages.getString("Frame.147"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.148"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        addTask(TaskConstants.CHANNEL_EDIT_RESPONSE_TRANSFORMER, UIConstants.EDIT_RESPONSE_TRANSFORMER, Messages.getString("Frame.149"), Messages.getString("Frame.150"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.151"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        addTask(TaskConstants.CHANNEL_EDIT_IMPORT_CONNECTOR, Messages.getString("Frame.152"), Messages.getString("Frame.153"), Messages.getString("Frame.154"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.155"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_EXPORT_CONNECTOR, Messages.getString("Frame.156"), Messages.getString("Frame.157"), Messages.getString("Frame.158"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.159"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_EXPORT, Messages.getString("Frame.160"), Messages.getString("Frame.161"), Messages.getString("Frame.162"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.163"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_VALIDATE_SCRIPT, Messages.getString("Frame.164"), Messages.getString("Frame.165"), Messages.getString("Frame.166"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.167"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.CHANNEL_EDIT_DEPLOY, Messages.getString("Frame.168"), Messages.getString("Frame.169"), Messages.getString("Frame.170"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.171"))), channelEditTasks, channelEditPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        setNonFocusable(channelEditTasks);
        taskPaneContainer.add(channelEditTasks);
    }

    /**
     * Creates the status task pane.
     */
    private void createDashboardPane() {
        // Create Status Tasks Pane
        dashboardTasks = new JXTaskPane();
        dashboardPopupMenu = new JPopupMenu();
        dashboardTasks.setTitle(Messages.getString("Frame.172")); //$NON-NLS-1$
        dashboardTasks.setName(TaskConstants.DASHBOARD_KEY);
        dashboardTasks.setFocusable(false);

        addTask(TaskConstants.DASHBOARD_REFRESH, Messages.getString("Frame.173"), Messages.getString("Frame.174"), Messages.getString("Frame.175"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.176"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        addTask(TaskConstants.DASHBOARD_SEND_MESSAGE, Messages.getString("Frame.177"), Messages.getString("Frame.178"), Messages.getString("Frame.179"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.180"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.DASHBOARD_SHOW_MESSAGES, Messages.getString("Frame.181"), Messages.getString("Frame.182"), Messages.getString("Frame.183"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.184"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.DASHBOARD_REMOVE_ALL_MESSAGES, Messages.getString("Frame.185"), Messages.getString("Frame.186"), Messages.getString("Frame.187"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.188"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.DASHBOARD_CLEAR_STATS, Messages.getString("Frame.189"), Messages.getString("Frame.190"), Messages.getString("Frame.191"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.192"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        addTask(TaskConstants.DASHBOARD_START, Messages.getString("Frame.193"), Messages.getString("Frame.194"), Messages.getString("Frame.195"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.196"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.DASHBOARD_PAUSE, Messages.getString("Frame.197"), Messages.getString("Frame.198"), Messages.getString("Frame.199"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.200"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.DASHBOARD_STOP, Messages.getString("Frame.201"), Messages.getString("Frame.202"), Messages.getString("Frame.203"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.204"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.DASHBOARD_HALT, Messages.getString("Frame.205"), Messages.getString("Frame.206"), Messages.getString("Frame.207"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.208"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        addTask(TaskConstants.DASHBOARD_UNDEPLOY, Messages.getString("Frame.209"), Messages.getString("Frame.210"), Messages.getString("Frame.211"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.212"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        addTask(TaskConstants.DASHBOARD_START_CONNECTOR, Messages.getString("Frame.213"), Messages.getString("Frame.214"), Messages.getString("Frame.215"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.216"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.DASHBOARD_STOP_CONNECTOR, Messages.getString("Frame.217"), Messages.getString("Frame.218"), Messages.getString("Frame.219"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.220"))), dashboardTasks, dashboardPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        setNonFocusable(dashboardTasks);
        taskPaneContainer.add(dashboardTasks);
    }

    /**
     * Creates the event task pane.
     */
    private void createEventPane() {
        // Create Event Tasks Pane
        eventTasks = new JXTaskPane();
        eventPopupMenu = new JPopupMenu();
        eventTasks.setTitle(Messages.getString("Frame.221")); //$NON-NLS-1$
        eventTasks.setName(TaskConstants.EVENT_KEY);
        eventTasks.setFocusable(false);

        addTask(TaskConstants.EVENT_REFRESH, Messages.getString("Frame.222"), Messages.getString("Frame.223"), Messages.getString("Frame.224"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.225"))), eventTasks, eventPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.EVENT_EXPORT_ALL, Messages.getString("Frame.226"), Messages.getString("Frame.227"), Messages.getString("Frame.228"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.229"))), eventTasks, eventPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.EVENT_REMOVE_ALL, Messages.getString("Frame.230"), Messages.getString("Frame.231"), Messages.getString("Frame.232"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.233"))), eventTasks, eventPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        setNonFocusable(eventTasks);
        taskPaneContainer.add(eventTasks);
    }

    /**
     * Creates the message task pane.
     */
    private void createMessagePane() {
        // Create Message Tasks Pane
        messageTasks = new JXTaskPane();
        messagePopupMenu = new JPopupMenu();
        messageTasks.setTitle(Messages.getString("Frame.234")); //$NON-NLS-1$
        messageTasks.setName(TaskConstants.MESSAGE_KEY);
        messageTasks.setFocusable(false);

        addTask(TaskConstants.MESSAGE_REFRESH, Messages.getString("Frame.235"), Messages.getString("Frame.236"), Messages.getString("Frame.237"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.238"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.MESSAGE_SEND, Messages.getString("Frame.239"), Messages.getString("Frame.240"), Messages.getString("Frame.241"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.242"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.MESSAGE_IMPORT, Messages.getString("Frame.243"), Messages.getString("Frame.244"), Messages.getString("Frame.245"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.246"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.MESSAGE_EXPORT, Messages.getString("Frame.247"), Messages.getString("Frame.248"), Messages.getString("Frame.249"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.250"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.MESSAGE_REMOVE_ALL, Messages.getString("Frame.251"), Messages.getString("Frame.252"), Messages.getString("Frame.253"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.254"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.MESSAGE_REMOVE_FILTERED, Messages.getString("Frame.255"), Messages.getString("Frame.256"), Messages.getString("Frame.257"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.258"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.MESSAGE_REMOVE, Messages.getString("Frame.259"), Messages.getString("Frame.260"), Messages.getString("Frame.261"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.262"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.MESSAGE_REPROCESS_FILTERED, Messages.getString("Frame.263"), Messages.getString("Frame.264"), Messages.getString("Frame.265"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.266"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.MESSAGE_REPROCESS, Messages.getString("Frame.267"), Messages.getString("Frame.268"), Messages.getString("Frame.269"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.270"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.MESSAGE_VIEW_IMAGE, Messages.getString("Frame.271"), Messages.getString("Frame.272"), Messages.getString("Frame.273"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.274"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.MESSAGE_EXPORT_ATTACHMENT, Messages.getString("Frame.275"), Messages.getString("Frame.276"), Messages.getString("Frame.277"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.278"))), messageTasks, messagePopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        setNonFocusable(messageTasks);
        taskPaneContainer.add(messageTasks);
    }

    /**
     * Creates the users task pane.
     */
    private void createUserPane() {
        // Create User Tasks Pane
        userTasks = new JXTaskPane();
        userPopupMenu = new JPopupMenu();
        userTasks.setTitle(Messages.getString("Frame.279")); //$NON-NLS-1$
        userTasks.setName(TaskConstants.USER_KEY);
        userTasks.setFocusable(false);

        addTask(TaskConstants.USER_REFRESH, Messages.getString("Frame.280"), Messages.getString("Frame.281"), Messages.getString("Frame.282"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.283"))), userTasks, userPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.USER_NEW, Messages.getString("Frame.284"), Messages.getString("Frame.285"), Messages.getString("Frame.286"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.287"))), userTasks, userPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.USER_EDIT, Messages.getString("Frame.288"), Messages.getString("Frame.289"), Messages.getString("Frame.290"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.291"))), userTasks, userPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.USER_DELETE, Messages.getString("Frame.292"), Messages.getString("Frame.293"), Messages.getString("Frame.294"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.295"))), userTasks, userPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        setNonFocusable(userTasks);
        taskPaneContainer.add(userTasks);
    }

    /**
     * Creates the global scripts edit task pane.
     */
    private void createGlobalScriptsPane() {
        globalScriptsTasks = new JXTaskPane();
        globalScriptsPopupMenu = new JPopupMenu();
        globalScriptsTasks.setTitle(Messages.getString("Frame.296")); //$NON-NLS-1$
        globalScriptsTasks.setName(TaskConstants.GLOBAL_SCRIPT_KEY);
        globalScriptsTasks.setFocusable(false);

        addTask(TaskConstants.GLOBAL_SCRIPT_SAVE, Messages.getString("Frame.297"), Messages.getString("Frame.298"), Messages.getString("Frame.299"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.300"))), globalScriptsTasks, globalScriptsPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.GLOBAL_SCRIPT_VALIDATE, Messages.getString("Frame.301"), Messages.getString("Frame.302"), Messages.getString("Frame.303"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.304"))), globalScriptsTasks, globalScriptsPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.GLOBAL_SCRIPT_IMPORT, Messages.getString("Frame.305"), Messages.getString("Frame.306"), Messages.getString("Frame.307"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.308"))), globalScriptsTasks, globalScriptsPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.GLOBAL_SCRIPT_EXPORT, Messages.getString("Frame.309"), Messages.getString("Frame.310"), Messages.getString("Frame.311"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.312"))), globalScriptsTasks, globalScriptsPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        setNonFocusable(globalScriptsTasks);
        taskPaneContainer.add(globalScriptsTasks);
    }

    /**
     * Creates the extensions task pane.
     */
    private void createExtensionsPane() {
        extensionsTasks = new JXTaskPane();
        extensionsPopupMenu = new JPopupMenu();
        extensionsTasks.setTitle(Messages.getString("Frame.313")); //$NON-NLS-1$
        extensionsTasks.setName(TaskConstants.EXTENSIONS_KEY);
        extensionsTasks.setFocusable(false);

        addTask(TaskConstants.EXTENSIONS_REFRESH, Messages.getString("Frame.314"), Messages.getString("Frame.315"), Messages.getString("Frame.316"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.317"))), extensionsTasks, extensionsPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.EXTENSIONS_ENABLE, Messages.getString("Frame.318"), Messages.getString("Frame.319"), Messages.getString("Frame.320"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.321"))), extensionsTasks, extensionsPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.EXTENSIONS_DISABLE, Messages.getString("Frame.322"), Messages.getString("Frame.323"), Messages.getString("Frame.324"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.325"))), extensionsTasks, extensionsPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.EXTENSIONS_SHOW_PROPERTIES, Messages.getString("Frame.326"), Messages.getString("Frame.327"), Messages.getString("Frame.328"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.329"))), extensionsTasks, extensionsPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.EXTENSIONS_UNINSTALL, Messages.getString("Frame.330"), Messages.getString("Frame.331"), Messages.getString("Frame.332"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.333"))), extensionsTasks, extensionsPopupMenu); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        setNonFocusable(extensionsTasks);
        taskPaneContainer.add(extensionsTasks);
    }

    /**
     * Creates the other task pane.
     */
    private void createOtherPane() {
        // Create Other Pane
        otherPane = new JXTaskPane();
        otherPane.setTitle(Messages.getString("Frame.334")); //$NON-NLS-1$
        otherPane.setName(TaskConstants.OTHER_KEY);
        otherPane.setFocusable(false);
        addTask(TaskConstants.OTHER_NOTIFICATIONS, UIConstants.VIEW_NOTIFICATIONS, Messages.getString("Frame.335"), Messages.getString("Frame.336"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.337"))), otherPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        addTask(TaskConstants.OTHER_VIEW_USER_API, Messages.getString("Frame.338"), Messages.getString("Frame.339"), Messages.getString("Frame.340"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.341"))), otherPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.OTHER_VIEW_CLIENT_API, Messages.getString("Frame.342"), Messages.getString("Frame.343"), Messages.getString("Frame.344"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.345"))), otherPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.OTHER_HELP, Messages.getString("Frame.346"), Messages.getString("Frame.347"), Messages.getString("Frame.348"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.349"))), otherPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.OTHER_ABOUT, Messages.getString("Frame.350"), Messages.getString("Frame.351"), Messages.getString("Frame.352"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.353"))), otherPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.OTHER_VISIT_MIRTH, Messages.getString("Frame.354"), Messages.getString("Frame.355"), Messages.getString("Frame.356"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.357"))), otherPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.OTHER_REPORT_ISSUE, Messages.getString("Frame.358"), Messages.getString("Frame.359"), Messages.getString("Frame.360"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.361"))), otherPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.OTHER_LOGOUT, Messages.getString("Frame.362"), Messages.getString("Frame.363"), Messages.getString("Frame.364"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Frame.365"))), otherPane, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        setNonFocusable(otherPane);
        taskPaneContainer.add(otherPane);
        otherPane.setVisible(true);
    }

    public JXTaskPane getOtherPane() {
        return otherPane;
    }

    public void updateNotificationTaskName(int notifications) {
        String taskName = UIConstants.VIEW_NOTIFICATIONS;
        if (notifications > 0) {
            taskName += Messages.getString("Frame.366") + notifications + Messages.getString("Frame.367"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        ((JXHyperlink) otherPane.getContentPane().getComponent(UIConstants.VIEW_NOTIFICATIONS_TASK_NUMBER)).setText(taskName);
    }

    public int addTask(String callbackMethod, String displayName, String toolTip, String shortcutKey, ImageIcon icon, JXTaskPane pane, JPopupMenu menu) {
        return addTask(callbackMethod, displayName, toolTip, shortcutKey, icon, pane, menu, this);
    }

    /**
     * Initializes the bound method call for the task pane actions and adds them to the
     * taskpane/popupmenu.
     */
    public int addTask(String callbackMethod, String displayName, String toolTip, String shortcutKey, ImageIcon icon, JXTaskPane pane, JPopupMenu menu, Object handler) {
        BoundAction boundAction = ActionFactory.createBoundAction(callbackMethod, displayName, shortcutKey);

        if (icon != null) {
            boundAction.putValue(Action.SMALL_ICON, icon);
        }
        boundAction.putValue(Action.SHORT_DESCRIPTION, toolTip);
        boundAction.registerCallback(handler, callbackMethod);

        Component component = pane.add(boundAction);
        getComponentTaskMap().put(component, callbackMethod);

        if (menu != null) {
            menu.add(boundAction);
        }

        return (pane.getContentPane().getComponentCount() - 1);
    }

    public Map<Component, String> getComponentTaskMap() {
        return componentTaskMap;
    }

    /**
     * Alerts the user with a yes/no option with the passed in 'message'
     */
    public boolean alertOption(Component parentComponent, String message) {
        int option = JOptionPane.showConfirmDialog(getVisibleComponent(parentComponent), message, Messages.getString("Frame.368"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
        if (option == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Alerts the user with a Ok/cancel option with the passed in 'message'
     */
    public boolean alertOkCancel(Component parentComponent, String message) {
        int option = JOptionPane.showConfirmDialog(getVisibleComponent(parentComponent), message, Messages.getString("Frame.369"), JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$
        if (option == JOptionPane.OK_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    public enum ConflictOption {
        YES, YES_APPLY_ALL, NO, NO_APPLY_ALL;
    }

    /**
     * Alerts the user with a conflict resolution dialog
     */
    public ConflictOption alertConflict(Component parentComponent, String message, int count) {
        final JCheckBox conflictCheckbox = new JCheckBox(Messages.getString("Frame.370") + String.valueOf(count - 1) + Messages.getString("Frame.371")); //$NON-NLS-1$ //$NON-NLS-2$
        conflictCheckbox.setSelected(false);

        Object[] params = { message, conflictCheckbox };

        int jOption = JOptionPane.showConfirmDialog(getVisibleComponent(parentComponent), params, Messages.getString("Frame.372"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
        boolean isSelected = conflictCheckbox.isSelected();

        ConflictOption conflictOption = null;
        if (jOption == JOptionPane.YES_OPTION) {
            if (isSelected) {
                conflictOption = ConflictOption.YES_APPLY_ALL;
            } else {
                conflictOption = ConflictOption.YES;
            }
        } else {
            if (isSelected || jOption == -1) {
                conflictOption = ConflictOption.NO_APPLY_ALL;
            } else {
                conflictOption = ConflictOption.NO;
            }
        }

        return conflictOption;
    }

    public boolean alertRefresh() {
        boolean cancelRefresh = false;

        if (PlatformUI.MIRTH_FRAME.isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(PlatformUI.MIRTH_FRAME, Messages.getString("Frame.373"), Messages.getString("Frame.374"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$

            if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
                cancelRefresh = true;
            } else {
                setSaveEnabled(false);
            }
        }

        return cancelRefresh;
    }

    /**
     * Alerts the user with an information dialog with the passed in 'message'
     */
    public void alertInformation(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(getVisibleComponent(parentComponent), message, Messages.getString("Frame.375"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
    }

    /**
     * Alerts the user with a warning dialog with the passed in 'message'
     */
    public void alertWarning(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(getVisibleComponent(parentComponent), message, Messages.getString("Frame.376"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
    }

    /**
     * Alerts the user with an error dialog with the passed in 'message'
     */
    public void alertError(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(getVisibleComponent(parentComponent), message, Messages.getString("Frame.377"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
    }

    /**
     * Alerts the user with an error dialog with the passed in 'message' and a 'question'.
     */
    public void alertCustomError(Component parentComponent, String message, String question) {
        parentComponent = getVisibleComponent(parentComponent);

        Window owner = getWindowForComponent(parentComponent);

        if (owner instanceof java.awt.Frame) {
            new CustomErrorDialog((java.awt.Frame) owner, message, question);
        } else { // window instanceof Dialog
            new CustomErrorDialog((java.awt.Dialog) owner, message, question);
        }
    }

    /**
     * Alerts the user with an exception dialog with the passed in stack trace.
     */

    public void alertThrowable(Component parentComponent, Throwable t) {
        alertThrowable(parentComponent, t, null);
    }

    /**
     * Alerts the user with an exception dialog with the passed in stack trace.
     */
    public void alertThrowable(Component parentComponent, Throwable t, String customMessage) {
        alertThrowable(parentComponent, t, customMessage, true);
    }

    /**
     * Alerts the user with an exception dialog with the passed in stack trace.
     */
    public void alertThrowable(Component parentComponent, Throwable t, boolean showMessageOnForbidden) {
        alertThrowable(parentComponent, t, null, showMessageOnForbidden);
    }

    /**
     * Alerts the user with an exception dialog with the passed in stack trace.
     */
    public void alertThrowable(Component parentComponent, Throwable t, String customMessage, boolean showMessageOnForbidden) {
        alertThrowable(parentComponent, t, customMessage, showMessageOnForbidden, null);
    }

    /**
     * Alerts the user with an exception dialog with the passed in stack trace.
     */
    public void alertThrowable(Component parentComponent, Throwable t, String customMessage, String safeErrorKey) {
        alertThrowable(parentComponent, t, customMessage, true, safeErrorKey);
    }

    /**
     * Alerts the user with an exception dialog with the passed in stack trace.
     */
    public void alertThrowable(Component parentComponent, Throwable t, String customMessage, boolean showMessageOnForbidden, String safeErrorKey) {
        if (connectionError) {
            return;
        }

        if (safeErrorKey != null) {
            increaseSafeErrorFailCount(safeErrorKey);

            if (getSafeErrorFailCount(safeErrorKey) < 3) {
                return;
            }
        }

        parentComponent = getVisibleComponent(parentComponent);
        String message = StringUtils.trimToEmpty(customMessage);
        boolean showDialog = true;

        if (t != null) {
            // Always print the stacktrace for troubleshooting purposes
            t.printStackTrace();

            if (t instanceof ExecutionException && t.getCause() != null) {
                t = t.getCause();
            }
            if (t.getCause() != null && t.getCause() instanceof ClientException) {
                t = t.getCause();
            }

            if (StringUtils.isBlank(message) && StringUtils.isNotBlank(t.getMessage())) {
                message = t.getMessage();
            }

            /*
             * Logout if an exception occurs that indicates the server is no longer running or
             * accessible. We only want to do this if a ClientException was passed in, indicating it
             * was actually due to a request to the server. Other places in the application could
             * call this method with an exception that happens to contain the string
             * "Connection reset", for example.
             */
            if (t instanceof ClientException) {
                if (t instanceof ForbiddenException || t.getCause() != null && t.getCause() instanceof ForbiddenException) {
                    message = Messages.getString("Frame.378") + message; //$NON-NLS-1$
                    if (!showMessageOnForbidden) {
                        showDialog = false;
                    }
                } else if (StringUtils.contains(t.getMessage(), Messages.getString("Frame.379"))) { //$NON-NLS-1$
                    return;
                } else if (t.getCause() != null && t.getCause() instanceof IllegalStateException && mirthClient.isClosed()) {
                    return;
                } else if (t instanceof UnauthorizedException || t.getCause() != null && t.getCause() instanceof UnauthorizedException) {
                    connectionError = true;
                    statusUpdaterExecutor.shutdownNow();

                    alertWarning(parentComponent, Messages.getString("Frame.380")); //$NON-NLS-1$
                    if (!exportChannelOnError()) {
                        return;
                    }
                    mirthClient.close();
                    this.dispose();
                    LoginPanel.getInstance().initialize(PlatformUI.SERVER_URL, PlatformUI.CLIENT_VERSION, Messages.getString("Frame.381"), Messages.getString("Frame.382")); //$NON-NLS-1$ //$NON-NLS-2$
                    return;
                } else if (t.getCause() != null && t.getCause() instanceof HttpHostConnectException && (StringUtils.contains(t.getCause().getMessage(), Messages.getString("Frame.383")) || StringUtils.contains(t.getCause().getMessage(), Messages.getString("Frame.384")))) { //$NON-NLS-1$ //$NON-NLS-2$
                    connectionError = true;
                    statusUpdaterExecutor.shutdownNow();

                    String server;
                    if (!StringUtils.isBlank(PlatformUI.SERVER_NAME)) {
                        server = PlatformUI.SERVER_NAME + Messages.getString("Frame.385") + PlatformUI.SERVER_URL + Messages.getString("Frame.386"); //$NON-NLS-1$ //$NON-NLS-2$
                    } else {
                        server = PlatformUI.SERVER_URL;
                    }
                    alertWarning(parentComponent, Messages.getString("Frame.387") + server + Messages.getString("Frame.388")); //$NON-NLS-1$ //$NON-NLS-2$
                    if (!exportChannelOnError()) {
                        return;
                    }
                    mirthClient.close();
                    this.dispose();
                    LoginPanel.getInstance().initialize(PlatformUI.SERVER_URL, PlatformUI.CLIENT_VERSION, Messages.getString("Frame.389"), Messages.getString("Frame.390")); //$NON-NLS-1$ //$NON-NLS-2$
                    return;
                }
            }

            for (String stackFrame : ExceptionUtils.getStackFrames(t)) {
                if (StringUtils.isNotEmpty(message)) {
                    message += '\n';
                }
                message += StringUtils.trim(stackFrame);
            }
        }

        logger.error(message);

        if (showDialog) {
            Window owner = getWindowForComponent(parentComponent);

            if (owner instanceof java.awt.Frame) {
                new ErrorDialog((java.awt.Frame) owner, message);
            } else { // window instanceof Dialog
                new ErrorDialog((java.awt.Dialog) owner, message);
            }
        }
    }

    private Component getVisibleComponent(Component component) {
        if (component != null && component.isVisible()) {
            return component;
        } else if (this.isVisible()) {
            return this;
        } else {
            return null;
        }
    }

    private Window getWindowForComponent(Component parentComponent) {
        Window owner = null;

        if (parentComponent == null) {
            owner = this;
        } else if (parentComponent instanceof java.awt.Frame || parentComponent instanceof java.awt.Dialog) {
            owner = (Window) parentComponent;
        } else {
            owner = SwingUtilities.windowForComponent(parentComponent);

            if (owner == null) {
                owner = this;
            }
        }

        return owner;
    }

    /**
     * Sets the 'index' in 'pane' to be bold
     */
    public void setBold(JXTaskPane pane, int index) {
        for (int i = 0; i < pane.getContentPane().getComponentCount(); i++) {
            pane.getContentPane().getComponent(i).setFont(UIConstants.TEXTFIELD_PLAIN_FONT);
        }

        if (index != UIConstants.ERROR_CONSTANT) {
            pane.getContentPane().getComponent(index).setFont(UIConstants.TEXTFIELD_BOLD_FONT);
        }
    }

    /**
     * Sets the visible task pane to the specified 'pane'
     */
    public void setFocus(JXTaskPane pane) {
        setFocus(new JXTaskPane[] { pane }, true, true);
    }

    /**
     * Sets the visible task panes to the specified 'panes'. Also allows setting the 'Mirth' and
     * 'Other' panes.
     */
    public void setFocus(JXTaskPane[] panes, boolean mirthPane, boolean otherPane) {
        taskPaneContainer.getComponent(0).setVisible(mirthPane);

        // ignore the first and last components
        for (int i = 1; i < taskPaneContainer.getComponentCount() - 1; i++) {
            taskPaneContainer.getComponent(i).setVisible(false);
        }

        taskPaneContainer.getComponent(taskPaneContainer.getComponentCount() - 1).setVisible(otherPane);

        if (panes != null) {
            for (JXTaskPane pane : panes) {
                if (pane != null) {
                    pane.setVisible(true);
                }
            }
        }
    }

    /**
     * Sets all components in pane to be non-focusable.
     */
    public void setNonFocusable(JXTaskPane pane) {
        for (int i = 0; i < pane.getContentPane().getComponentCount(); i++) {
            pane.getContentPane().getComponent(i).setFocusable(false);
        }
    }

    /**
     * Sets the visible tasks in the given 'pane' and 'menu'. The method takes an interval of
     * indices (end index should be -1 to go to the end), as well as a whether they should be set to
     * visible or not-visible.
     */
    public void setVisibleTasks(JXTaskPane pane, JPopupMenu menu, int startIndex, int endIndex, boolean visible) {
        // If the endIndex is -1, disregard it, otherwise stop there.
        for (int i = startIndex; (endIndex == -1 ? true : i <= endIndex) && (i < pane.getContentPane().getComponentCount()); i++) {
            // If the component being set visible is in the security list, don't allow it.

            boolean componentVisible = visible;
            String componentTask = getComponentTaskMap().get(pane.getContentPane().getComponent(i));
            if (componentTask != null) {
                if (!AuthorizationControllerFactory.getAuthorizationController().checkTask(pane.getName(), componentTask)) {
                    componentVisible = false;
                }
            }

            pane.getContentPane().getComponent(i).setVisible(componentVisible);

            if (menu != null) {
                menu.getComponent(i).setVisible(componentVisible);
            }
        }
    }

    /**
     * A prompt to ask the user if he would like to save the changes made before leaving the page.
     */
    public boolean confirmLeave() {
        if (dashboardPanel != null) {
            dashboardPanel.closePopupWindow();
        }

        if (channelPanel != null) {
            channelPanel.closePopupWindow();
        }

        if (channelEditPanel != null) {
            channelEditPanel.closePopupWindow();
        }

        if (currentContentPage == channelPanel && isSaveEnabled()) {
            if (!channelPanel.confirmLeave()) {
                return false;
            }
        } else if ((currentContentPage == channelEditPanel || currentContentPage == channelEditPanel.transformerPane || currentContentPage == channelEditPanel.filterPane) && isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, Messages.getString("Frame.391")); //$NON-NLS-1$
            if (option == JOptionPane.YES_OPTION) {
                if (!channelEditPanel.saveChanges()) {
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        } else if (currentContentPage == settingsPane && isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, Messages.getString("Frame.392") + settingsPane.getCurrentSettingsPanel().getTabName() + Messages.getString("Frame.393")); //$NON-NLS-1$ //$NON-NLS-2$

            if (option == JOptionPane.YES_OPTION) {
                if (!settingsPane.getCurrentSettingsPanel().doSave()) {
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        } else if (currentContentPage == alertEditPanel && isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, Messages.getString("Frame.394")); //$NON-NLS-1$

            if (option == JOptionPane.YES_OPTION) {
                if (!alertEditPanel.saveAlert()) {
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        } else if (currentContentPage == globalScriptsPanel && isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, Messages.getString("Frame.395")); //$NON-NLS-1$

            if (option == JOptionPane.YES_OPTION) {
                if (!doSaveGlobalScripts()) {
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        } else if (currentContentPage == codeTemplatePanel && isSaveEnabled()) {
            if (!codeTemplatePanel.confirmLeave()) {
                return false;
            }
        }

        setSaveEnabled(false);
        return true;
    }

    /**
     * Sends the channel passed in to the server, updating it or adding it.
     * 
     * @throws ClientException
     */
    public boolean updateChannel(Channel curr, boolean overwriting) throws ClientException {
        if (overwriting ? !mirthClient.updateChannel(curr, false) : !mirthClient.createChannel(curr)) {
            if (alertOption(this, Messages.getString("Frame.396"))) { //$NON-NLS-1$
                mirthClient.updateChannel(curr, true);
            } else {
                return false;
            }
        }
        channelPanel.retrieveChannels();

        return true;
    }

    /**
     * Sends the passed in user to the server, updating it or adding it.
     */
    public boolean updateUser(final Component parentComponent, final User updateUser, final String newPassword) {
        final String workingId = startWorking(Messages.getString("Frame.397")); //$NON-NLS-1$

        try {
            if (StringUtils.isNotEmpty(newPassword)) {
                /*
                 * If a new user is being passed in (null user id), the password will only be
                 * checked right now.
                 */
                if (!checkOrUpdateUserPassword(parentComponent, updateUser, newPassword)) {
                    return false;
                }
            }

            try {
                if (updateUser.getId() == null) {
                    mirthClient.createUser(updateUser);
                } else {
                    mirthClient.updateUser(updateUser);
                }
            } catch (ClientException e) {
                if (e.getMessage() != null && e.getMessage().contains(Messages.getString("Frame.398"))) { //$NON-NLS-1$
                    alertWarning(parentComponent, Messages.getString("Frame.399")); //$NON-NLS-1$
                } else {
                    alertThrowable(parentComponent, e);
                }

                return false;
            }

            try {
                retrieveUsers();

                /*
                 * If the user id was null, a new user was being created and the password was only
                 * checked. Get the created user with the id and then update the password.
                 */
                if (updateUser.getId() == null) {
                    User newUser = null;
                    for (User user : users) {
                        if (user.getUsername().equals(updateUser.getUsername())) {
                            newUser = user;
                        }
                    }
                    checkOrUpdateUserPassword(parentComponent, newUser, newPassword);
                }
            } catch (ClientException e) {
                alertThrowable(parentComponent, e);
            } finally {
                // The userPanel will be null if the user panel has not been viewed (i.e. registration).
                if (userPanel != null) {
                    userPanel.updateUserTable();
                }
            }
        } finally {
            stopWorking(workingId);
        }

        return true;
    }

    /**
     * If the current user is being updated, it needs to be done in the main thread so that the
     * username can be changed, re-logged in, and the current user information can be updated.
     * 
     * @param parentComponent
     * @param currentUser
     * @param newPassword
     * @return
     */
    public boolean updateCurrentUser(Component parentComponent, final User currentUser, String newPassword) {
        // Find out if the username is being changed so that we can login again.
        boolean changingUsername = !currentUser.getUsername().equals(PlatformUI.USER_NAME);

        final String workingId = startWorking(Messages.getString("Frame.400")); //$NON-NLS-1$

        try {

            /*
             * If there is a new password, update it. If not, make sure that the username is not
             * being changed, since the password must be updated when the username is changed.
             */
            if (StringUtils.isNotEmpty(newPassword)) {
                if (!checkOrUpdateUserPassword(parentComponent, currentUser, newPassword)) {
                    return false;
                }
            } else if (changingUsername) {
                alertWarning(parentComponent, Messages.getString("Frame.401")); //$NON-NLS-1$
                return false;
            }

            try {
                mirthClient.updateUser(currentUser);
            } catch (ClientException e) {
                if (e.getMessage() != null && e.getMessage().contains(Messages.getString("Frame.402"))) { //$NON-NLS-1$
                    alertWarning(parentComponent, Messages.getString("Frame.403")); //$NON-NLS-1$
                } else {
                    alertThrowable(parentComponent, e);
                }

                return false;
            }

            try {
                retrieveUsers();
            } catch (ClientException e) {
                alertThrowable(parentComponent, e);
            } finally {
                // The userPanel will be null if the user panel has not been viewed (i.e. registration).
                if (userPanel != null) {
                    userPanel.updateUserTable();
                }
            }
        } finally {
            stopWorking(workingId);
        }

        // If the username is being changed, login again.
        if (changingUsername) {
            final String workingId2 = startWorking(Messages.getString("Frame.404")); //$NON-NLS-1$

            try {
                LoadedExtensions.getInstance().resetPlugins();
                mirthClient.logout();
                mirthClient.login(currentUser.getUsername(), newPassword);
                PlatformUI.USER_NAME = currentUser.getUsername();
            } catch (ClientException e) {
                alertThrowable(parentComponent, e);
            } finally {
                stopWorking(workingId2);
            }
        }

        return true;
    }

    public boolean checkOrUpdateUserPassword(Component parentComponent, final User currentUser, String newPassword) {
        try {
            List<String> responses;
            if (currentUser.getId() == null) {
                responses = mirthClient.checkUserPassword(newPassword);
            } else {
                responses = mirthClient.updateUserPassword(currentUser.getId(), newPassword);
            }

            if (CollectionUtils.isNotEmpty(responses)) {
                String responseString = Messages.getString("Frame.405"); //$NON-NLS-1$
                for (String response : responses) {
                    responseString += (Messages.getString("Frame.406") + response + Messages.getString("Frame.407")); //$NON-NLS-1$ //$NON-NLS-2$
                }
                alertError(this, responseString);
                return false;
            }
        } catch (ClientException e) {
            alertThrowable(this, e);
            return false;
        }

        return true;
    }

    public User getCurrentUser(Component parentComponent) {
        User currentUser = null;

        try {
            retrieveUsers();
            for (User user : users) {
                if (user.getUsername().equals(PlatformUI.USER_NAME)) {
                    currentUser = user;
                }
            }
        } catch (ClientException e) {
            alertThrowable(parentComponent, e);
        }

        return currentUser;
    }

    public void registerUser(final User user) {
        final String workingId = startWorking(Messages.getString("Frame.408")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    ConnectServiceUtil.registerUser(PlatformUI.SERVER_ID, PlatformUI.SERVER_VERSION, user, PlatformUI.HTTPS_PROTOCOLS, PlatformUI.HTTPS_CIPHER_SUITES);
                } catch (ClientException e) {
                    // ignore errors connecting to update/stats server
                }

                return null;
            }

            public void done() {
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void sendUsageStatistics() {
        UpdateSettings updateSettings = null;
        try {
            updateSettings = mirthClient.getUpdateSettings();
        } catch (Exception e) {
        }

        if (updateSettings != null && updateSettings.getStatsEnabled()) {
            final String workingId = startWorking(Messages.getString("Frame.409")); //$NON-NLS-1$

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {
                    try {
                        String usageData = mirthClient.getUsageData(getClientStats());
                        if (usageData != null) {
                            boolean isSent = ConnectServiceUtil.sendStatistics(PlatformUI.SERVER_ID, PlatformUI.SERVER_VERSION, false, usageData, PlatformUI.HTTPS_PROTOCOLS, PlatformUI.HTTPS_CIPHER_SUITES);
                            if (isSent) {
                                UpdateSettings settings = new UpdateSettings();
                                settings.setLastStatsTime(System.currentTimeMillis());
                                mirthClient.setUpdateSettings(settings);
                            }
                        }
                    } catch (ClientException e) {
                        // ignore errors connecting to update/stats server
                    }

                    return null;
                }

                public void done() {
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    private Map<String, Object> getClientStats() {
        Map<String, Object> clientStats = new HashMap<String, Object>();
        clientStats.put(Messages.getString("Frame.410"), System.getProperty(Messages.getString("Frame.411"))); //$NON-NLS-1$ //$NON-NLS-2$
        return clientStats;
    }

    /**
     * Enables the save button for needed page.
     */
    public void setSaveEnabled(boolean enabled) {
        if (currentContentPage == channelPanel) {
            channelPanel.setSaveEnabled(enabled);
        } else if (currentContentPage == channelEditPanel) {
            setVisibleTasks(channelEditTasks, channelEditPopupMenu, 0, 0, enabled);
        } else if (currentContentPage == settingsPane) {
            settingsPane.getCurrentSettingsPanel().setSaveEnabled(enabled);
        } else if (alertEditPanel != null && currentContentPage == alertEditPanel) {
            setVisibleTasks(alertEditTasks, alertEditPopupMenu, 0, 0, enabled);
        } else if (globalScriptsPanel != null && currentContentPage == globalScriptsPanel) {
            setVisibleTasks(globalScriptsTasks, globalScriptsPopupMenu, 0, 0, enabled);
        } else if (currentContentPage == codeTemplatePanel) {
            codeTemplatePanel.setSaveEnabled(enabled);
        }
    }

    /**
     * Enables the save button for needed page.
     */
    public boolean isSaveEnabled() {
        boolean enabled = false;

        if (currentContentPage != null) {
            if (currentContentPage == channelPanel) {
                enabled = channelPanel.isSaveEnabled();
            } else if (currentContentPage == channelEditPanel) {
                enabled = channelEditTasks.getContentPane().getComponent(0).isVisible();
            } else if (channelEditPanel != null && currentContentPage == channelEditPanel.transformerPane) {
                enabled = channelEditTasks.getContentPane().getComponent(0).isVisible() || channelEditPanel.transformerPane.isModified();
            } else if (channelEditPanel != null && currentContentPage == channelEditPanel.filterPane) {
                enabled = channelEditTasks.getContentPane().getComponent(0).isVisible() || channelEditPanel.filterPane.isModified();
            } else if (currentContentPage == settingsPane) {
                enabled = settingsPane.getCurrentSettingsPanel().isSaveEnabled();
            } else if (alertEditPanel != null && currentContentPage == alertEditPanel) {
                enabled = alertEditTasks.getContentPane().getComponent(0).isVisible();
            } else if (globalScriptsPanel != null && currentContentPage == globalScriptsPanel) {
                enabled = globalScriptsTasks.getContentPane().getComponent(0).isVisible();
            } else if (currentContentPage == codeTemplatePanel) {
                enabled = codeTemplatePanel.isSaveEnabled();
            }
        }

        return enabled;
    }

    // ////////////////////////////////////////////////////////////
    // --- All bound actions are beneath this point --- //
    // ////////////////////////////////////////////////////////////
    public void goToMirth() {
        BareBonesBrowserLaunch.openURL(Messages.getString("Frame.412")); //$NON-NLS-1$
    }

    public void goToUserAPI() {
        BareBonesBrowserLaunch.openURL(PlatformUI.SERVER_URL + UIConstants.USER_API_LOCATION);
    }

    public void goToClientAPI() {
        BareBonesBrowserLaunch.openURL(PlatformUI.SERVER_URL + UIConstants.CLIENT_API_LOCATION);
    }

    public void goToAbout() {
        new AboutMirth();
    }

    public void doReportIssue() {
        BareBonesBrowserLaunch.openURL(UIConstants.ISSUE_TRACKER_LOCATION);
    }

    public void doShowDashboard() {
        if (dashboardPanel == null) {
            dashboardPanel = new DashboardPanel();
        }

        if (!confirmLeave()) {
            return;
        }

        dashboardPanel.switchPanel();

        setBold(viewPane, 0);
        setPanelName(Messages.getString("Frame.413")); //$NON-NLS-1$
        setCurrentContentPage(dashboardPanel);
        setFocus(dashboardTasks);

        doRefreshStatuses(true);
    }

    public void doShowChannel() {
        if (!confirmLeave()) {
            return;
        }

        channelPanel.switchPanel();
    }

    public void doShowUsers() {
        if (userPanel == null) {
            userPanel = new UserPanel();
        }

        if (!confirmLeave()) {
            return;
        }

        final String workingId = startWorking(Messages.getString("Frame.414")); //$NON-NLS-1$

        setBold(viewPane, 2);
        setPanelName(Messages.getString("Frame.415")); //$NON-NLS-1$
        setCurrentContentPage(userPanel);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                refreshUser();
                return null;
            }

            public void done() {
                setFocus(userTasks);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doShowSettings() {
        if (settingsPane == null) {
            settingsPane = new SettingsPane();
        }

        if (!confirmLeave()) {
            return;
        }

        final String workingId = startWorking(Messages.getString("Frame.416")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                settingsPane.setSelectedSettingsPanel(0);
                return null;
            }

            public void done() {
                setBold(viewPane, 3);
                setPanelName(Messages.getString("Frame.417")); //$NON-NLS-1$
                setCurrentContentPage(settingsPane);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doShowAlerts() {
        if (alertPanel == null) {
            alertPanel = new DefaultAlertPanel();
        }

        if (!confirmLeave()) {
            return;
        }

        setBold(viewPane, 4);
        setPanelName(Messages.getString("Frame.418")); //$NON-NLS-1$
        setCurrentContentPage(alertPanel);
        setFocus(alertTasks);
        setSaveEnabled(false);
        doRefreshAlerts(true);
    }

    public void doShowExtensions() {
        if (extensionsPanel == null) {
            extensionsPanel = new ExtensionManagerPanel();
        }

        final String workingId = startWorking(Messages.getString("Frame.419")); //$NON-NLS-1$
        if (confirmLeave()) {
            setBold(viewPane, 6);
            setPanelName(Messages.getString("Frame.420")); //$NON-NLS-1$
            setCurrentContentPage(extensionsPanel);
            setFocus(extensionsTasks);
            refreshExtensions();
            stopWorking(workingId);
        }
    }

    public void doLogout() {
        logout(false);
    }

    public boolean logout(boolean quit) {
        if (!confirmLeave()) {
            return false;
        }

        LicenseClient.stop();

        // MIRTH-3074 Remove the keyEventDispatcher to prevent memory leak.
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);

        statusUpdaterExecutor.shutdownNow();

        if (currentContentPage == messageBrowser) {
            mirthClient.getServerConnection().abort(messageBrowser.getAbortOperations());
        }

        userPreferences = Preferences.userNodeForPackage(Mirth.class);
        userPreferences.putInt(Messages.getString("Frame.421"), getExtendedState()); //$NON-NLS-1$
        userPreferences.putInt(Messages.getString("Frame.422"), getWidth()); //$NON-NLS-1$
        userPreferences.putInt(Messages.getString("Frame.423"), getHeight()); //$NON-NLS-1$

        LoadedExtensions.getInstance().stopPlugins();

        final Properties tagUserProperties = new Properties();
        tagUserProperties.put(Messages.getString("Frame.424"), dashboardPanel.getUserTags()); //$NON-NLS-1$
        tagUserProperties.put(Messages.getString("Frame.425"), channelPanel.getUserTags()); //$NON-NLS-1$

        try {
            User currentUser = getCurrentUser(this);
            if (currentUser != null) {
                mirthClient.setUserPreferences(currentUser.getId(), tagUserProperties);
            }
        } catch (ClientException e) {
            alertThrowable(this, e);
        }

        try {
            mirthClient.logout();
        } catch (ClientException e) {
            alertThrowable(this, e);
        }

        mirthClient.close();
        this.dispose();

        if (!quit) {
            LoginPanel.getInstance().initialize(PlatformUI.SERVER_URL, PlatformUI.CLIENT_VERSION, Messages.getString("Frame.426"), Messages.getString("Frame.427")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return true;
    }

    public void doMoveDestinationDown() {
        channelEditPanel.moveDestinationDown();
    }

    public void doMoveDestinationUp() {
        channelEditPanel.moveDestinationUp();
    }

    public void doEditGlobalScripts() {
        if (globalScriptsPanel == null) {
            globalScriptsPanel = new GlobalScriptsPanel();
        }

        final String workingId = startWorking(Messages.getString("Frame.428")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                globalScriptsPanel.edit();
                return null;
            }

            public void done() {
                editGlobalScripts();
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doEditCodeTemplates() {
        codeTemplatePanel.switchPanel();
    }

    public void doValidateCurrentGlobalScript() {
        globalScriptsPanel.validateCurrentScript();
    }

    public void doImportGlobalScripts() {
        String content = browseForFileString(Messages.getString("Frame.429")); //$NON-NLS-1$

        if (content != null) {
            try {
                ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
                @SuppressWarnings("unchecked")
                Map<String, String> importScripts = serializer.deserialize(content, Map.class);

                for (Entry<String, String> globalScriptEntry : importScripts.entrySet()) {
                    importScripts.put(globalScriptEntry.getKey(), globalScriptEntry.getValue().replaceAll(Messages.getString("Frame.430"), Messages.getString("Frame.431"))); //$NON-NLS-1$ //$NON-NLS-2$
                }

                if (importScripts.containsKey(Messages.getString("Frame.432")) && !importScripts.containsKey(Messages.getString("Frame.433"))) { //$NON-NLS-1$ //$NON-NLS-2$
                    importScripts.put(Messages.getString("Frame.434"), importScripts.get(Messages.getString("Frame.435"))); //$NON-NLS-1$ //$NON-NLS-2$
                    importScripts.remove(Messages.getString("Frame.436")); //$NON-NLS-1$
                }

                globalScriptsPanel.importAllScripts(importScripts);
            } catch (Exception e) {
                alertThrowable(this, e, Messages.getString("Frame.437") + e.getMessage()); //$NON-NLS-1$
            }
        }
    }

    public void doExportGlobalScripts() {
        if (changesHaveBeenMade()) {
            if (alertOption(this, Messages.getString("Frame.438"))) { //$NON-NLS-1$
                String validationMessage = globalScriptsPanel.validateAllScripts();
                if (validationMessage != null) {
                    alertCustomError(this, validationMessage, CustomErrorDialog.ERROR_VALIDATING_GLOBAL_SCRIPTS);
                    return;
                }

                globalScriptsPanel.save();
                setSaveEnabled(false);
            } else {
                return;
            }
        }

        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        String globalScriptsXML = serializer.serialize(globalScriptsPanel.exportAllScripts());

        exportFile(globalScriptsXML, null, Messages.getString("Frame.439"), Messages.getString("Frame.440")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void doValidateChannelScripts() {
        channelEditPanel.validateScripts();
    }

    public boolean doSaveGlobalScripts() {
        String validationMessage = globalScriptsPanel.validateAllScripts();
        if (validationMessage != null) {
            alertCustomError(this, validationMessage, CustomErrorDialog.ERROR_VALIDATING_GLOBAL_SCRIPTS);
            return false;
        }

        final String workingId = startWorking(Messages.getString("Frame.441")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                globalScriptsPanel.save();
                return null;
            }

            public void done() {
                setSaveEnabled(false);
                stopWorking(workingId);
            }
        };

        worker.execute();

        return true;
    }

    public synchronized void increaseSafeErrorFailCount(String safeErrorKey) {
        int safeErrorFailCount = getSafeErrorFailCount(safeErrorKey) + 1;
        this.safeErrorFailCountMap.put(safeErrorKey, safeErrorFailCount);
    }

    public synchronized void resetSafeErrorFailCount(String safeErrorKey) {
        this.safeErrorFailCountMap.put(safeErrorKey, 0);
    }

    public synchronized int getSafeErrorFailCount(String safeErrorKey) {
        if (safeErrorFailCountMap.containsKey(safeErrorKey)) {
            return safeErrorFailCountMap.get(safeErrorKey);
        } else {
            return 0;
        }
    }

    public void doRefreshStatuses() {
        doRefreshStatuses(true);
    }

    public void doRefreshStatuses(boolean queue) {
        QueuingSwingWorkerTask<Void, DashboardStatus> task = new QueuingSwingWorkerTask<Void, DashboardStatus>(Messages.getString("Frame.442"), Messages.getString("Frame.443")) { //$NON-NLS-1$ //$NON-NLS-2$
            @Override
            public Void doInBackground() {
                try {
                    channelPanel.retrieveGroups();
                    channelPanel.retrieveDependencies();

                    SettingsPanelTags tagsPanel = getTagsPanel();
                    if (tagsPanel != null) {
                        tagsPanel.refresh();
                    }

                    for (DashboardColumnPlugin plugin : LoadedExtensions.getInstance().getDashboardColumnPlugins().values()) {
                        plugin.tableUpdate(status);
                    }

                    String filter = dashboardPanel.getUserTags();
                    DashboardChannelInfo dashboardStatusList = mirthClient.getDashboardChannelInfo(REFRESH_BLOCK_SIZE, filter);
                    status = dashboardStatusList.getDashboardStatuses();
                    Set<String> remainingIds = dashboardStatusList.getRemainingChannelIds();
                    deployedChannelCount = dashboardStatusList.getDeployedChannelCount();

                    if (status != null) {
                        publish(status.toArray(new DashboardStatus[status.size()]));

                        if (CollectionUtils.isNotEmpty(remainingIds)) {
                            Set<String> statusChannelIds = new HashSet<String>(Math.min(remainingIds.size(), REFRESH_BLOCK_SIZE));

                            for (Iterator<String> it = remainingIds.iterator(); it.hasNext();) {
                                statusChannelIds.add(it.next());

                                if (!it.hasNext() || statusChannelIds.size() == REFRESH_BLOCK_SIZE) {
                                    // Processing a new block, retrieve dashboard statuses from server
                                    List<DashboardStatus> intermediateStatusList = mirthClient.getChannelStatusList(statusChannelIds, filter);
                                    // Publish the intermediate statuses
                                    publish(intermediateStatusList.toArray(new DashboardStatus[intermediateStatusList.size()]));
                                    // Add the statuses to the master list
                                    status.addAll(intermediateStatusList);
                                    // Clear the set of channel IDs
                                    statusChannelIds.clear();
                                }
                            }
                        }
                    }
                } catch (ClientException e) {
                    status = null;
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e, e.getMessage(), false, TaskConstants.DASHBOARD_REFRESH);
                    });
                }

                return null;
            }

            @Override
            public void process(List<DashboardStatus> chunks) {
                logger.debug(Messages.getString("Frame.444") + (chunks != null ? chunks.size() : Messages.getString("Frame.445"))); //$NON-NLS-1$ //$NON-NLS-2$
                if (chunks != null) {
                    TableState tableState = dashboardPanel.getCurrentTableState();
                    dashboardPanel.updateTableChannelNodes(chunks);
                    dashboardPanel.updateTableState(tableState);
                }
            }

            @Override
            public void done() {
                if (status != null) {
                    TableState tableState = dashboardPanel.getCurrentTableState();
                    /*
                     * The channel group cache could be out of date, so after we have the completed
                     * list of statuses, make sure any previously unknown channel IDs are added to
                     * the default group.
                     */
                    channelPanel.updateDefaultChannelGroup(status);
                    dashboardPanel.finishUpdatingTable(status, channelPanel.getCachedGroupStatuses().values(), deployedChannelCount);
                    dashboardPanel.updateTableState(tableState);
                }
            }
        };

        new QueuingSwingWorker<Void, DashboardStatus>(task, queue).executeDelegate();
    }

    public int getDeployedChannelCount() {
        return deployedChannelCount;
    }

    public void doStart() {
        final Set<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedChannelStatuses();

        if (selectedStatuses.size() == 0) {
            return;
        }

        if (!getStatusesWithDependencies(selectedStatuses, ChannelTask.START_RESUME)) {
            return;
        }

        final String workingId = startWorking(Messages.getString("Frame.446")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                Set<String> startChannelIds = new HashSet<String>();
                Set<String> resumeChannelIds = new HashSet<String>();

                for (DashboardStatus dashboardStatus : selectedStatuses) {
                    if (dashboardStatus.getState() == DeployedState.PAUSED) {
                        resumeChannelIds.add(dashboardStatus.getChannelId());
                    } else {
                        startChannelIds.add(dashboardStatus.getChannelId());
                    }
                }

                try {
                    if (!startChannelIds.isEmpty()) {
                        mirthClient.startChannels(startChannelIds);
                    }

                    if (!resumeChannelIds.isEmpty()) {
                        mirthClient.resumeChannels(resumeChannelIds);
                    }
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }

                return null;
            }

            public void done() {
                doRefreshStatuses(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doStop() {
        final Set<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedChannelStatuses();

        if (selectedStatuses.size() == 0) {
            return;
        }

        if (!getStatusesWithDependencies(selectedStatuses, ChannelTask.STOP)) {
            return;
        }

        final String workingId = startWorking(Messages.getString("Frame.447")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                Set<String> channelIds = new HashSet<String>();
                for (DashboardStatus dashboardStatus : selectedStatuses) {
                    channelIds.add(dashboardStatus.getChannelId());
                }

                try {
                    if (!channelIds.isEmpty()) {
                        mirthClient.stopChannels(channelIds);
                    }
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }

                return null;
            }

            public void done() {
                doRefreshStatuses(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doHalt() {
        final Set<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedChannelStatuses();

        int size = selectedStatuses.size();
        if (size == 0 || !alertOption(this, Messages.getString("Frame.448") + (size == 1 ? Messages.getString("Frame.449") : Messages.getString("Frame.450")) + Messages.getString("Frame.451"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            return;
        }

        final String workingId = startWorking(Messages.getString("Frame.452")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                Set<String> channelIds = new HashSet<String>();
                for (DashboardStatus dashboardStatus : selectedStatuses) {
                    channelIds.add(dashboardStatus.getChannelId());
                }

                try {
                    if (!channelIds.isEmpty()) {
                        mirthClient.haltChannels(channelIds);
                    }
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }

                return null;
            }

            public void done() {
                doRefreshStatuses(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doPause() {
        final Set<DashboardStatus> selectedChannelStatuses = dashboardPanel.getSelectedChannelStatuses();

        if (selectedChannelStatuses.size() == 0) {
            return;
        }

        if (!getStatusesWithDependencies(selectedChannelStatuses, ChannelTask.PAUSE)) {
            return;
        }

        final String workingId = startWorking(Messages.getString("Frame.453")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                Set<String> channelIds = new HashSet<String>();
                for (DashboardStatus dashboardStatus : selectedChannelStatuses) {
                    channelIds.add(dashboardStatus.getChannelId());
                }

                try {
                    if (!channelIds.isEmpty()) {
                        mirthClient.pauseChannels(channelIds);
                    }
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }
                return null;
            }

            public void done() {
                doRefreshStatuses(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doStartConnector() {
        final List<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedStatuses();

        if (selectedStatuses.size() == 0) {
            return;
        }

        final String workingId = startWorking(Messages.getString("Frame.454")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                Map<String, List<Integer>> connectorInfo = new HashMap<String, List<Integer>>();

                for (DashboardStatus dashboardStatus : selectedStatuses) {
                    String channelId = dashboardStatus.getChannelId();
                    Integer metaDataId = dashboardStatus.getMetaDataId();

                    if (metaDataId != null) {
                        if (!connectorInfo.containsKey(channelId)) {
                            connectorInfo.put(channelId, new ArrayList<Integer>());
                        }
                        connectorInfo.get(channelId).add(metaDataId);
                    }
                }

                try {
                    if (!connectorInfo.isEmpty()) {
                        mirthClient.startConnectors(connectorInfo);
                    }
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }

                return null;
            }

            public void done() {
                doRefreshStatuses(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doStopConnector() {
        final List<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedStatuses();

        if (selectedStatuses.size() == 0) {
            return;
        }

        boolean warnQueueDisabled = false;
        for (Iterator<DashboardStatus> it = selectedStatuses.iterator(); it.hasNext();) {
            DashboardStatus dashboardStatus = it.next();
            if (dashboardStatus.getMetaDataId() != 0 && !dashboardStatus.isQueueEnabled()) {
                warnQueueDisabled = true;
                it.remove();
            }
        }

        final String workingId = startWorking(Messages.getString("Frame.455")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                Map<String, List<Integer>> connectorInfo = new HashMap<String, List<Integer>>();

                for (DashboardStatus dashboardStatus : selectedStatuses) {
                    String channelId = dashboardStatus.getChannelId();
                    Integer metaDataId = dashboardStatus.getMetaDataId();

                    if (metaDataId != null) {
                        if (!connectorInfo.containsKey(channelId)) {
                            connectorInfo.put(channelId, new ArrayList<Integer>());
                        }
                        connectorInfo.get(channelId).add(metaDataId);
                    }
                }

                try {
                    if (!connectorInfo.isEmpty()) {
                        mirthClient.stopConnectors(connectorInfo);
                    }
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }

                return null;
            }

            public void done() {
                doRefreshStatuses(true);
                stopWorking(workingId);
            }
        };

        worker.execute();

        if (warnQueueDisabled) {
            alertWarning(this, Messages.getString("Frame.456")); //$NON-NLS-1$
        }
    }

    public void doNewDestination() {
        channelEditPanel.addNewDestination();
    }

    public void doDeleteDestination() {
        if (!alertOption(this, Messages.getString("Frame.457"))) { //$NON-NLS-1$
            return;
        }

        channelEditPanel.deleteDestination();
    }

    public void doCloneDestination() {
        channelEditPanel.cloneDestination();
    }

    public void doEnableDestination() {
        channelEditPanel.enableDestination();
    }

    public void doDisableDestination() {
        channelEditPanel.disableDestination();
    }

    public void doNewUser() {
        new UserDialog(null);
    }

    public void doEditUser() {
        int index = userPanel.getUserIndex();

        if (index == UIConstants.ERROR_CONSTANT) {
            alertWarning(this, Messages.getString("Frame.458")); //$NON-NLS-1$
        } else {
            new UserDialog(users.get(index));
        }
    }

    public void doDeleteUser() {
        if (!alertOption(this, Messages.getString("Frame.459"))) { //$NON-NLS-1$
            return;
        }

        final String workingId = startWorking(Messages.getString("Frame.460")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                if (users.size() == 1) {
                    alertWarning(PlatformUI.MIRTH_FRAME, Messages.getString("Frame.461")); //$NON-NLS-1$
                    return null;
                }

                int userToDelete = userPanel.getUserIndex();

                try {
                    if (userToDelete != UIConstants.ERROR_CONSTANT) {
                        mirthClient.removeUser(users.get(userToDelete).getId());
                        retrieveUsers();
                    }
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }
                return null;
            }

            public void done() {
                userPanel.updateUserTable();
                userPanel.deselectRows();
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doRefreshUser() {
        final String workingId = startWorking(Messages.getString("Frame.462")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                refreshUser();
                return null;
            }

            public void done() {
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void refreshUser() {
        User user = null;
        String userName = null;
        int index = userPanel.getUserIndex();

        if (index != UIConstants.ERROR_CONSTANT) {
            user = users.get(index);
        }

        try {
            retrieveUsers();
            userPanel.updateUserTable();

            if (user != null) {
                for (int i = 0; i < users.size(); i++) {
                    if (user.equals(users.get(i))) {
                        userName = users.get(i).getUsername();
                    }
                }
            }
        } catch (ClientException e) {
            alertThrowable(this, e);
        }

        // as long as the channel was not deleted
        if (userName != null) {
            userPanel.setSelectedUser(userName);
        }
    }

    public void doDeployFromChannelView() {
        String channelId = channelEditPanel.currentChannel.getId();

        if (isSaveEnabled()) {
            if (alertOption(PlatformUI.MIRTH_FRAME, Messages.getString("Frame.463"))) { //$NON-NLS-1$
                if (channelEditPanel.saveChanges()) {
                    setSaveEnabled(false);
                } else {
                    return;
                }
            } else {
                return;
            }
        } else {
            if (!alertOption(PlatformUI.MIRTH_FRAME, Messages.getString("Frame.464"))) { //$NON-NLS-1$
                return;
            }
        }

        ChannelStatus channelStatus = channelPanel.getCachedChannelStatuses().get(channelId);
        if (channelStatus == null) {
            alertWarning(this, Messages.getString("Frame.465")); //$NON-NLS-1$
            return;
        }

        if (!channelStatus.getChannel().getExportData().getMetadata().isEnabled()) {
            alertWarning(this, Messages.getString("Frame.466")); //$NON-NLS-1$
            return;
        }

        deployChannel(Collections.singleton(channelId));
    }

    public void deployChannel(final Set<String> selectedChannelIds) {
        if (CollectionUtils.isNotEmpty(selectedChannelIds)) {
            String plural = (selectedChannelIds.size() > 1) ? Messages.getString("Frame.467") : Messages.getString("Frame.468"); //$NON-NLS-1$ //$NON-NLS-2$
            final String workingId = startWorking(Messages.getString("Frame.469") + plural + Messages.getString("Frame.470")); //$NON-NLS-1$ //$NON-NLS-2$

            dashboardPanel.deselectRows(false);
            doShowDashboard();

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {
                    try {
                        mirthClient.deployChannels(selectedChannelIds);
                    } catch (ClientException e) {
                        SwingUtilities.invokeLater(() -> {
                            alertThrowable(PlatformUI.MIRTH_FRAME, e);
                        });
                    }
                    return null;
                }

                public void done() {
                    stopWorking(workingId);
                    doRefreshStatuses(true);
                }
            };

            worker.execute();
        }
    }

    public enum ChannelTask {
        DEPLOY(Messages.getString("Frame.471"), Messages.getString("Frame.472"), true), UNDEPLOY(Messages.getString("Frame.473"), Messages.getString("Frame.474"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                false), START_RESUME(Messages.getString("Frame.475"), Messages.getString("Frame.476"), //$NON-NLS-1$ //$NON-NLS-2$
                        true), STOP(Messages.getString("Frame.477"), Messages.getString("Frame.478"), false), PAUSE(Messages.getString("Frame.479"), Messages.getString("Frame.480"), false); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        private String value;
        private String futurePassive;
        private boolean forwardOrder;

        private ChannelTask(String value, String futurePassive, boolean forwardOrder) {
            this.value = value;
            this.futurePassive = futurePassive;
            this.forwardOrder = forwardOrder;
        }

        public String getFuturePassive() {
            return futurePassive;
        }

        public boolean isForwardOrder() {
            return forwardOrder;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Adds dependent/dependency dashboard statuses to the given set, based on the type of task
     * being performed.
     */
    private boolean getStatusesWithDependencies(Set<DashboardStatus> selectedDashboardStatuses, ChannelTask task) {
        try {
            ChannelDependencyGraph channelDependencyGraph = new ChannelDependencyGraph(channelPanel.getCachedChannelDependencies());
            Set<String> selectedChannelIds = new HashSet<String>();
            Set<String> channelIdsToHandle = new HashSet<String>();

            Map<String, DashboardStatus> statusMap = new HashMap<String, DashboardStatus>();
            for (DashboardStatus dashboardStatus : status) {
                statusMap.put(dashboardStatus.getChannelId(), dashboardStatus);
            }

            // For each selected channel, add any dependent/dependency channels as necessary
            for (DashboardStatus dashboardStatus : selectedDashboardStatuses) {
                selectedChannelIds.add(dashboardStatus.getChannelId());
                addChannelToTaskSet(dashboardStatus.getChannelId(), channelDependencyGraph, statusMap, channelIdsToHandle, task);
            }

            // If additional channels were added to the set, we need to prompt the user
            if (!CollectionUtils.subtract(channelIdsToHandle, selectedChannelIds).isEmpty()) {
                ChannelDependenciesWarningDialog dialog = new ChannelDependenciesWarningDialog(task, channelPanel.getCachedChannelDependencies(), selectedChannelIds, channelIdsToHandle);
                if (dialog.getResult() == JOptionPane.OK_OPTION) {
                    if (dialog.isIncludeOtherChannels()) {
                        for (String channelId : channelIdsToHandle) {
                            selectedDashboardStatuses.add(statusMap.get(channelId));
                        }
                    }
                } else {
                    return false;
                }
            }
        } catch (ChannelDependencyException e) {
            // Should never happen
            e.printStackTrace();
        }

        return true;
    }

    private void addChannelToTaskSet(String channelId, ChannelDependencyGraph channelDependencyGraph, Map<String, DashboardStatus> statusMap, Set<String> channelIdsToHandle, ChannelTask task) {
        if (!channelIdsToHandle.add(channelId)) {
            return;
        }

        DirectedAcyclicGraphNode<String> node = channelDependencyGraph.getNode(channelId);

        if (node != null) {
            if (task.isForwardOrder()) {
                // Add dependency channels for the start/resume task.
                for (String dependencyChannelId : node.getDirectDependencyElements()) {
                    // Only add the dependency channel if it's currently deployed and not already started.
                    if (statusMap.containsKey(dependencyChannelId)) {
                        DashboardStatus dashboardStatus = statusMap.get(dependencyChannelId);
                        if (dashboardStatus.getState() != DeployedState.STARTED) {
                            addChannelToTaskSet(dependencyChannelId, channelDependencyGraph, statusMap, channelIdsToHandle, task);
                        }
                    }
                }
            } else {
                // Add dependent channels for the undeploy/stop/pause tasks.
                for (String dependentChannelId : node.getDirectDependentElements()) {
                    /*
                     * Only add the dependent channel if it's currently deployed, and it's not
                     * already stopped/paused (depending on the task being performed).
                     */
                    if (statusMap.containsKey(dependentChannelId)) {
                        DashboardStatus dashboardStatus = statusMap.get(dependentChannelId);
                        if (task == ChannelTask.UNDEPLOY || task == ChannelTask.STOP && dashboardStatus.getState() != DeployedState.STOPPED || task == ChannelTask.PAUSE && dashboardStatus.getState() != DeployedState.PAUSED && dashboardStatus.getState() != DeployedState.STOPPED) {
                            addChannelToTaskSet(dependentChannelId, channelDependencyGraph, statusMap, channelIdsToHandle, task);
                        }
                    }
                }
            }
        }
    }

    public void doUndeployChannel() {
        final Set<DashboardStatus> selectedChannelStatuses = dashboardPanel.getSelectedChannelStatuses();

        if (selectedChannelStatuses.size() == 0) {
            return;
        }

        if (!getStatusesWithDependencies(selectedChannelStatuses, ChannelTask.UNDEPLOY)) {
            return;
        }

        dashboardPanel.deselectRows(false);

        String plural = (selectedChannelStatuses.size() > 1) ? Messages.getString("Frame.481") : Messages.getString("Frame.482"); //$NON-NLS-1$ //$NON-NLS-2$
        final String workingId = startWorking(Messages.getString("Frame.483") + plural + Messages.getString("Frame.484")); //$NON-NLS-1$ //$NON-NLS-2$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    Set<String> channelIds = new LinkedHashSet<String>();

                    for (DashboardStatus channelStatus : selectedChannelStatuses) {
                        channelIds.add(channelStatus.getChannelId());
                    }

                    mirthClient.undeployChannels(channelIds);
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }
                return null;
            }

            public void done() {
                stopWorking(workingId);
                doRefreshStatuses(true);
            }
        };

        worker.execute();
    }

    public void doSaveChannel() {
        final String workingId = startWorking(Messages.getString("Frame.485")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                if (changesHaveBeenMade() || currentContentPage == channelEditPanel.transformerPane || currentContentPage == channelEditPanel.filterPane) {
                    if (channelEditPanel.saveChanges()) {
                        setSaveEnabled(false);
                    }
                    return null;
                } else {
                    return null;
                }
            }

            public void done() {
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public boolean changesHaveBeenMade() {
        if (currentContentPage == channelPanel) {
            return channelPanel.changesHaveBeenMade();
        } else if (channelEditPanel != null && currentContentPage == channelEditPanel) {
            return channelEditTasks.getContentPane().getComponent(0).isVisible();
        } else if (channelEditPanel != null && currentContentPage == channelEditPanel.transformerPane) {
            return channelEditPanel.transformerPane.isModified();
        } else if (channelEditPanel != null && currentContentPage == channelEditPanel.filterPane) {
            return channelEditPanel.filterPane.isModified();
        } else if (settingsPane != null && currentContentPage == settingsPane) {
            return settingsPane.getCurrentSettingsPanel().isSaveEnabled();
        } else if (alertEditPanel != null && currentContentPage == alertEditPanel) {
            return alertEditTasks.getContentPane().getComponent(0).isVisible();
        } else if (globalScriptsPanel != null && currentContentPage == globalScriptsPanel) {
            return globalScriptsTasks.getContentPane().getComponent(0).isVisible();
        } else if (currentContentPage == codeTemplatePanel) {
            return codeTemplatePanel.changesHaveBeenMade();
        } else {
            return false;
        }
    }

    public void doShowMessages() {
        if (messageBrowser == null) {
            messageBrowser = new MessageBrowser();
        }

        String id = Messages.getString("Frame.486"); //$NON-NLS-1$
        String channelName = Messages.getString("Frame.487"); //$NON-NLS-1$
        boolean channelDeployed = true;
        Integer channelRevision = null;

        final List<Integer> metaDataIds = new ArrayList<Integer>();
        if (currentContentPage == dashboardPanel) {
            List<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedStatuses();
            Set<DashboardStatus> selectedChannelStatuses = dashboardPanel.getSelectedChannelStatuses();

            if (selectedStatuses.size() == 0) {
                return;
            }

            if (selectedChannelStatuses.size() > 1) {
                JOptionPane.showMessageDialog(Frame.this, Messages.getString("Frame.488")); //$NON-NLS-1$
                return;
            }

            for (DashboardStatus status : selectedStatuses) {
                metaDataIds.add(status.getMetaDataId());
            }

            id = selectedStatuses.get(0).getChannelId();
            channelName = selectedChannelStatuses.iterator().next().getName();
            channelRevision = 0;
        } else if (currentContentPage == channelPanel) {
            Channel selectedChannel = channelPanel.getSelectedChannels().get(0);

            metaDataIds.add(null);

            id = selectedChannel.getId();
            channelName = selectedChannel.getName();
            channelRevision = selectedChannel.getRevision();

            channelDeployed = false;
            for (DashboardStatus dashStatus : status) {
                if (dashStatus.getChannelId().equals(id)) {
                    channelDeployed = true;
                }
            }
        }

        setBold(viewPane, -1);
        setPanelName(Messages.getString("Frame.489") + channelName); //$NON-NLS-1$
        setCurrentContentPage(messageBrowser);
        setFocus(messageTasks);

        final String channelId = id;
        final boolean isChannelDeployed = channelDeployed;

        final String workingId = startWorking(Messages.getString("Frame.490")); //$NON-NLS-1$
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            Map<Integer, String> connectors;
            List<MetaDataColumn> metaDataColumns;

            public Void doInBackground() {
                try {
                    connectors = mirthClient.getConnectorNames(channelId);
                    metaDataColumns = mirthClient.getMetaDataColumns(channelId);
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }
                return null;
            }

            public void done() {
                stopWorking(workingId);

                if (connectors == null || metaDataColumns == null) {
                    alertError(PlatformUI.MIRTH_FRAME, Messages.getString("Frame.491")); //$NON-NLS-1$
                } else {
                    messageBrowser.loadChannel(channelId, connectors, metaDataColumns, metaDataIds, isChannelDeployed);
                }
            }
        };

        worker.execute();
    }

    public void doShowEvents() {
        doShowEvents(null);
    }

    public void doShowEvents(String eventNameFilter) {
        if (!confirmLeave()) {
            return;
        }

        if (eventBrowser == null) {
            eventBrowser = new EventBrowser();
        }

        setBold(viewPane, 5);
        setPanelName(Messages.getString("Frame.492")); //$NON-NLS-1$
        setCurrentContentPage(eventBrowser);
        setFocus(eventTasks);

        eventBrowser.loadNew(eventNameFilter);
    }

    public void doEditTransformer() {
        channelEditPanel.transformerPane.resizePanes();
        String name = channelEditPanel.editTransformer();
        setPanelName(Messages.getString("Frame.493") + channelEditPanel.currentChannel.getName() + Messages.getString("Frame.494") + name + Messages.getString("Frame.495")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public void doEditResponseTransformer() {
        channelEditPanel.transformerPane.resizePanes();
        String name = channelEditPanel.editResponseTransformer();
        setPanelName(Messages.getString("Frame.496") + channelEditPanel.currentChannel.getName() + Messages.getString("Frame.497") + name + Messages.getString("Frame.498")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public void doEditFilter() {
        channelEditPanel.filterPane.resizePanes();
        String name = channelEditPanel.editFilter();
        setPanelName(Messages.getString("Frame.499") + channelEditPanel.currentChannel.getName() + Messages.getString("Frame.500") + name + Messages.getString("Frame.501")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public void updateFilterTaskName(int rules) {
        updateFilterOrTransformerTaskName(UIConstants.EDIT_FILTER, UIConstants.EDIT_FILTER_TASK_NUMBER, rules, false);
    }

    public void updateTransformerTaskName(int steps, boolean outboundTemplate) {
        updateFilterOrTransformerTaskName(UIConstants.EDIT_TRANSFORMER, UIConstants.EDIT_TRANSFORMER_TASK_NUMBER, steps, outboundTemplate);
    }

    public void updateResponseTransformerTaskName(int steps, boolean outboundTemplate) {
        updateFilterOrTransformerTaskName(UIConstants.EDIT_RESPONSE_TRANSFORMER, UIConstants.EDIT_RESPONSE_TRANSFORMER_TASK_NUMBER, steps, outboundTemplate);
    }

    private void updateFilterOrTransformerTaskName(String taskName, int componentIndex, int rulesOrSteps, boolean outboundTemplate) {
        if (rulesOrSteps > 0) {
            taskName += Messages.getString("Frame.502") + rulesOrSteps + Messages.getString("Frame.503"); //$NON-NLS-1$ //$NON-NLS-2$
        } else if (outboundTemplate) {
            taskName += Messages.getString("Frame.504"); //$NON-NLS-1$
        }

        ((JXHyperlink) channelEditTasks.getContentPane().getComponent(componentIndex)).setText(taskName);
        ((JMenuItem) channelEditPopupMenu.getComponent(componentIndex)).setText(taskName);
    }

    public void doValidate() {
        channelEditPanel.doValidate();
    }

    public boolean doExportChannel() {
        return channelPanel.doExportChannel();
    }

    /**
     * Import a file with the default defined file filter type.
     * 
     * @return
     */
    public String browseForFileString(String fileExtension) {
        File file = browseForFile(fileExtension);

        if (file != null) {
            return readFileToString(file);
        }

        return null;
    }

    /**
     * Read the bytes from a file with the default defined file filter type.
     * 
     * @return
     */
    public byte[] browseForFileBytes(String fileExtension) {
        File file = browseForFile(fileExtension);

        if (file != null) {
            try {
                return FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                alertError(this, Messages.getString("Frame.505")); //$NON-NLS-1$
            }
        }

        return null;
    }

    public String readFileToString(File file) {
        try {
            String content = FileUtils.readFileToString(file, UIConstants.CHARSET);

            if (StringUtils.startsWith(content, EncryptionSettings.ENCRYPTION_PREFIX)) {
                return mirthClient.getEncryptor().decrypt(StringUtils.removeStart(content, EncryptionSettings.ENCRYPTION_PREFIX));
            } else {
                return content;
            }
        } catch (IOException e) {
            alertError(this, Messages.getString("Frame.506")); //$NON-NLS-1$
        }

        return null;
    }

    public File browseForFile(String fileExtension) {
        JFileChooser importFileChooser = new JFileChooser();

        if (fileExtension != null) {
            importFileChooser.setFileFilter(new MirthFileFilter(fileExtension));
        }

        File currentDir = new File(userPreferences.get(Messages.getString("Frame.507"), Messages.getString("Frame.508"))); //$NON-NLS-1$ //$NON-NLS-2$

        if (currentDir.exists()) {
            importFileChooser.setCurrentDirectory(currentDir);
        }

        if (importFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            userPreferences.put(Messages.getString("Frame.509"), importFileChooser.getCurrentDirectory().getPath()); //$NON-NLS-1$
            return importFileChooser.getSelectedFile();
        }

        return null;
    }

    /**
     * Creates a File with the default defined file filter type, but does not yet write to it.
     * 
     * @param defaultFileName
     * @param fileExtension
     * @return
     */
    public File createFileForExport(String defaultFileName, String fileExtension) {
        JFileChooser exportFileChooser = new JFileChooser();

        if (defaultFileName != null) {
            exportFileChooser.setSelectedFile(new File(defaultFileName));
        }

        if (fileExtension != null) {
            exportFileChooser.setFileFilter(new MirthFileFilter(fileExtension));
        }

        File currentDir = new File(userPreferences.get(Messages.getString("Frame.510"), Messages.getString("Frame.511"))); //$NON-NLS-1$ //$NON-NLS-2$

        if (currentDir.exists()) {
            exportFileChooser.setCurrentDirectory(currentDir);
        }

        if (exportFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            userPreferences.put(Messages.getString("Frame.512"), exportFileChooser.getCurrentDirectory().getPath()); //$NON-NLS-1$
            File exportFile = exportFileChooser.getSelectedFile();

            if ((exportFile.getName().length() < 4) || !FilenameUtils.getExtension(exportFile.getName()).equalsIgnoreCase(fileExtension)) {
                exportFile = new File(exportFile.getAbsolutePath() + Messages.getString("Frame.513") + fileExtension.toLowerCase()); //$NON-NLS-1$
            }

            if (exportFile.exists()) {
                if (!alertOption(this, Messages.getString("Frame.514"))) { //$NON-NLS-1$
                    return null;
                }
            }

            return exportFile;
        } else {
            return null;
        }
    }

    /**
     * Export a file with the default defined file filter type.
     * 
     * @param fileContents
     * @param fileName
     * @return
     */
    public boolean exportFile(String fileContents, String defaultFileName, String fileExtension, String name) {
        return exportFile(fileContents, createFileForExport(defaultFileName, fileExtension), name);
    }

    public boolean exportFile(String fileContents, File exportFile, String name) {
        if (exportFile != null) {
            try {
                String contentToWrite = null;

                if (mirthClient.isEncryptExport()) {
                    contentToWrite = EncryptionSettings.ENCRYPTION_PREFIX + mirthClient.getEncryptor().encrypt(fileContents);
                } else {
                    contentToWrite = fileContents;
                }

                FileUtils.writeStringToFile(exportFile, contentToWrite, UIConstants.CHARSET);
                alertInformation(this, name + Messages.getString("Frame.515") + exportFile.getPath() + Messages.getString("Frame.516")); //$NON-NLS-1$ //$NON-NLS-2$
            } catch (IOException ex) {
                alertError(this, Messages.getString("Frame.517")); //$NON-NLS-1$
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    public void doImportConnector() {
        String content = browseForFileString(Messages.getString("Frame.518")); //$NON-NLS-1$

        if (content != null) {
            try {
                channelEditPanel.importConnector(ObjectXMLSerializer.getInstance().deserialize(content, Connector.class));
            } catch (Exception e) {
                alertThrowable(this, e);
            }
        }
    }

    public void doExportConnector() {
        if (changesHaveBeenMade()) {
            if (alertOption(this, Messages.getString("Frame.519"))) { //$NON-NLS-1$
                if (!channelEditPanel.saveChanges()) {
                    return;
                }
            } else {
                return;
            }

            setSaveEnabled(false);
        }

        Connector connector = channelEditPanel.exportSelectedConnector();

        // Update resource names
        updateResourceNames(connector);

        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        String connectorXML = serializer.serialize(connector);

        String fileName = channelEditPanel.currentChannel.getName();
        if (connector.getMode().equals(Mode.SOURCE)) {
            fileName += Messages.getString("Frame.520"); //$NON-NLS-1$
        } else {
            fileName += Messages.getString("Frame.521") + connector.getName(); //$NON-NLS-1$
        }

        exportFile(connectorXML, fileName, Messages.getString("Frame.522"), Messages.getString("Frame.523")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void doRefreshMessages() {
        messageBrowser.refresh(null, true);
    }

    public void doSendMessage() {
        String channelId = null;
        List<Integer> selectedMetaDataIds = null;

        if (currentContentPage == dashboardPanel) {
            List<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedStatuses();
            channelId = selectedStatuses.get(0).getChannelId();
            selectedMetaDataIds = new ArrayList<Integer>();

            for (DashboardStatus status : selectedStatuses) {
                if (status.getChannelId() != channelId) {
                    JOptionPane.showMessageDialog(Frame.this, Messages.getString("Frame.524")); //$NON-NLS-1$
                    return;
                }

                if (status.getStatusType() == StatusType.CHANNEL) {
                    selectedMetaDataIds = null;
                } else if (selectedMetaDataIds != null) {
                    Integer metaDataId = status.getMetaDataId();

                    if (metaDataId != null) {
                        selectedMetaDataIds.add(metaDataId);
                    }
                }
            }
        } else if (currentContentPage == messageBrowser) {
            channelId = messageBrowser.getChannelId();
        }

        if (channelId == null) {
            alertError(this, Messages.getString("Frame.525")); //$NON-NLS-1$
            return;
        }

        String dataType = Messages.getString("Frame.526"); //$NON-NLS-1$

        if (AuthorizationControllerFactory.getAuthorizationController().checkTask(TaskConstants.VIEW_KEY, TaskConstants.VIEW_CHANNEL)) {
            /*
             * If the user has not yet navigated to channels at this point, the cache
             * (channelStatuses object) will return null, and the resulting block will pull down the
             * channelStatus for the given id.
             */
            ChannelStatus channelStatus = channelPanel.getCachedChannelStatuses().get(channelId);

            if (channelStatus == null) {
                try {
                    Map<String, ChannelHeader> channelHeaders = new HashMap<String, ChannelHeader>();
                    channelHeaders.put(channelId, new ChannelHeader(0, null, true));
                    channelPanel.updateChannelStatuses(mirthClient.getChannelSummary(channelHeaders, true));
                    channelStatus = channelPanel.getCachedChannelStatuses().get(channelId);
                } catch (ClientException e) {
                    alertThrowable(PlatformUI.MIRTH_FRAME, e);
                }
            }

            if (channelStatus == null) {
                alertError(this, Messages.getString("Frame.527")); //$NON-NLS-1$
                return;
            } else {
                dataType = channelStatus.getChannel().getSourceConnector().getTransformer().getInboundDataType();
            }
        }

        editMessageDialog.setPropertiesAndShow(Messages.getString("Frame.528"), dataType, channelId, dashboardPanel.getDestinationConnectorNames(channelId), selectedMetaDataIds, new HashMap<String, Object>()); //$NON-NLS-1$
    }

    public void doExportMessages() {
        if (messageExportDialog == null) {
            messageExportDialog = new MessageExportDialog();
        }

        messageExportDialog.setEncryptor(mirthClient.getEncryptor());
        messageExportDialog.setMessageFilter(messageBrowser.getMessageFilter());
        messageExportDialog.setPageSize(messageBrowser.getPageSize());
        messageExportDialog.setChannelId(messageBrowser.getChannelId());
        messageExportDialog.setLocationRelativeTo(this);
        messageExportDialog.setVisible(true);
    }

    public void doImportMessages() {
        if (messageImportDialog == null) {
            messageImportDialog = new MessageImportDialog();
        }

        messageImportDialog.setChannelId(messageBrowser.getChannelId());
        messageImportDialog.setMessageBrowser(messageBrowser);
        messageImportDialog.setLocationRelativeTo(this);
        messageImportDialog.setVisible(true);
    }

    public void doRemoveAllMessages() {
        if (removeMessagesDialog == null) {
            removeMessagesDialog = new RemoveMessagesDialog(this, true);
        }

        Set<String> channelIds = new HashSet<String>();
        boolean restartCheckboxEnabled = false;

        if (currentContentPage instanceof MessageBrowser) {
            String channelId = ((MessageBrowser) currentContentPage).getChannelId();
            channelIds.add(channelId);

            for (DashboardStatus channelStatus : status) {
                if (channelStatus.getChannelId().equals(channelId)) {
                    if (!channelStatus.getState().equals(DeployedState.STOPPED) && !restartCheckboxEnabled) {
                        restartCheckboxEnabled = true;
                    }
                }
            }
        } else {
            for (DashboardStatus channelStatus : dashboardPanel.getSelectedChannelStatuses()) {
                channelIds.add(channelStatus.getChannelId());

                if (!channelStatus.getState().equals(DeployedState.STOPPED) && !restartCheckboxEnabled) {
                    restartCheckboxEnabled = true;
                }
            }
        }

        removeMessagesDialog.init(channelIds, restartCheckboxEnabled);
        removeMessagesDialog.setLocationRelativeTo(this);
        removeMessagesDialog.setVisible(true);
    }

    public void doClearStats() {
        List<DashboardStatus> channelStatuses = dashboardPanel.getSelectedStatusesRecursive();

        if (channelStatuses.size() != 0) {
            new DeleteStatisticsDialog(channelStatuses);
        } else {
            dashboardPanel.deselectRows(false);
        }
    }

    public void clearStats(List<DashboardStatus> statusesToClear, final boolean deleteReceived, final boolean deleteFiltered, final boolean deleteSent, final boolean deleteErrored) {
        final String workingId = startWorking(Messages.getString("Frame.529")); //$NON-NLS-1$
        Map<String, List<Integer>> channelConnectorMap = new HashMap<String, List<Integer>>();

        for (DashboardStatus status : statusesToClear) {
            String channelId = status.getChannelId();
            Integer metaDataId = status.getMetaDataId();

            List<Integer> metaDataIds = channelConnectorMap.get(channelId);

            if (metaDataIds == null) {
                metaDataIds = new ArrayList<Integer>();
                channelConnectorMap.put(channelId, metaDataIds);
            }

            metaDataIds.add(metaDataId);
        }

        final Map<String, List<Integer>> channelConnectorMapFinal = channelConnectorMap;

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    mirthClient.clearStatistics(channelConnectorMapFinal, deleteReceived, deleteFiltered, deleteSent, deleteErrored);
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }
                return null;
            }

            public void done() {
                doRefreshStatuses(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doRemoveFilteredMessages() {
        if (alertOption(this, Messages.getString("Frame.530"))) { //$NON-NLS-1$
            if (userPreferences.getBoolean(Messages.getString("Frame.531"), true)) { //$NON-NLS-1$
                String result = DisplayUtil.showInputDialog(this, Messages.getString("Frame.532"), Messages.getString("Frame.533"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
                if (!StringUtils.equals(result, Messages.getString("Frame.534"))) { //$NON-NLS-1$
                    alertWarning(this, Messages.getString("Frame.535")); //$NON-NLS-1$
                    return;
                }
            }

            final String workingId = startWorking(Messages.getString("Frame.536")); //$NON-NLS-1$

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {
                    try {
                        mirthClient.removeMessages(messageBrowser.getChannelId(), messageBrowser.getMessageFilter());
                    } catch (ClientException e) {
                        if (e instanceof RequestAbortedException) {
                            // The client is no longer waiting for the delete request
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                alertThrowable(PlatformUI.MIRTH_FRAME, e);
                            });
                        }
                    }
                    return null;
                }

                public void done() {
                    if (currentContentPage == dashboardPanel) {
                        doRefreshStatuses(true);
                    } else if (currentContentPage == messageBrowser) {
                        messageBrowser.refresh(1, true);
                    }
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doRemoveMessage() {
        final Integer metaDataId = messageBrowser.getSelectedMetaDataId();
        final Long messageId = messageBrowser.getSelectedMessageId();
        final String channelId = messageBrowser.getChannelId();

        if (alertOption(this, Messages.getString("Frame.537"))) { //$NON-NLS-1$
            final String workingId = startWorking(Messages.getString("Frame.538")); //$NON-NLS-1$

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {
                    try {
                        mirthClient.removeMessage(channelId, messageId, metaDataId);
                    } catch (ClientException e) {
                        SwingUtilities.invokeLater(() -> {
                            alertThrowable(PlatformUI.MIRTH_FRAME, e);
                        });
                    }
                    return null;
                }

                public void done() {
                    if (currentContentPage == dashboardPanel) {
                        doRefreshStatuses(true);
                    } else if (currentContentPage == messageBrowser) {
                        messageBrowser.refresh(null, false);
                    }
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doReprocessFilteredMessages() {
        doReprocess(messageBrowser.getMessageFilter(), null, null, true);
    }

    public void doReprocessMessage() {
        Long messageId = messageBrowser.getSelectedMessageId();

        if (messageBrowser.canReprocessMessage(messageId)) {
            doReprocess(null, messageId, messageBrowser.getSelectedMetaDataId(), false);
        } else {
            alertError(this, Messages.getString("Frame.539") + messageId + Messages.getString("Frame.540")); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private void doReprocess(final MessageFilter filter, final Long messageId, final Integer selectedMetaDataId, final boolean showWarning) {
        final String workingId = startWorking(Messages.getString("Frame.541")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                if (channelPanel.getCachedChannelStatuses() == null || channelPanel.getCachedChannelStatuses().values().size() == 0) {
                    channelPanel.retrieveChannels();
                }

                return null;
            }

            public void done() {
                stopWorking(workingId);
                Map<Integer, String> destinationConnectors = new LinkedHashMap<Integer, String>();
                destinationConnectors.putAll(dashboardPanel.getDestinationConnectorNames(messageBrowser.getChannelId()));
                new ReprocessMessagesDialog(messageBrowser.getChannelId(), filter, messageId, destinationConnectors, selectedMetaDataId, showWarning);
            }
        };

        worker.execute();
    }

    public void reprocessMessage(final String channelId, final MessageFilter filter, final Long messageId, final boolean replace, final Collection<Integer> reprocessMetaDataIds) {
        final String workingId = startWorking(Messages.getString("Frame.542")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                try {
                    if (filter != null) {
                        mirthClient.reprocessMessages(channelId, filter, replace, reprocessMetaDataIds);
                    } else if (messageId != null) {
                        mirthClient.reprocessMessage(channelId, messageId, replace, reprocessMetaDataIds);
                    }
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }
                return null;
            }

            public void done() {
                messageBrowser.updateFilterButtonFont(Font.BOLD);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void viewImage() {
        final String workingId = startWorking(Messages.getString("Frame.543")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                messageBrowser.viewAttachment();
                stopWorking(workingId);
                return null;
            }

            public void done() {
                stopWorking(workingId);
            }
        };
        worker.execute();
    }

    public void doExportAttachment() {
        if (attachmentExportDialog == null) {
            attachmentExportDialog = new AttachmentExportDialog();
        }

        attachmentExportDialog.setLocationRelativeTo(this);
        attachmentExportDialog.setVisible(true);
    }

    public void processMessage(final String channelId, final RawMessage rawMessage) {
        final String workingId = startWorking(Messages.getString("Frame.544")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    mirthClient.processMessage(channelId, rawMessage);
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }
                return null;
            }

            public void done() {
                if (currentContentPage == messageBrowser) {
                    messageBrowser.updateFilterButtonFont(Font.BOLD);
                }

                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doRefreshEvents() {
        eventBrowser.refresh(null);
    }

    public void doRemoveAllEvents() {
        int option = JOptionPane.showConfirmDialog(this, Messages.getString("Frame.545") + Messages.getString("Frame.546")); //$NON-NLS-1$ //$NON-NLS-2$
        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
            return;
        }

        final boolean export = (option == JOptionPane.YES_OPTION);

        final String workingId = startWorking(Messages.getString("Frame.547")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            private String exportPath = null;

            public Void doInBackground() {
                try {
                    if (export) {
                        exportPath = mirthClient.exportAndRemoveAllEvents();
                    } else {
                        mirthClient.removeAllEvents();
                    }
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }
                return null;
            }

            public void done() {
                eventBrowser.runSearch();

                if (exportPath != null) {
                    alertInformation(PlatformUI.MIRTH_FRAME, Messages.getString("Frame.548") + exportPath); //$NON-NLS-1$
                }

                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doExportAllEvents() {
        if (alertOption(this, Messages.getString("Frame.549") + Messages.getString("Frame.550"))) { //$NON-NLS-1$ //$NON-NLS-2$
            final String workingId = startWorking(Messages.getString("Frame.551")); //$NON-NLS-1$

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                private String exportPath = null;

                public Void doInBackground() {
                    try {
                        exportPath = mirthClient.exportAllEvents();
                    } catch (ClientException e) {
                        SwingUtilities.invokeLater(() -> {
                            alertThrowable(PlatformUI.MIRTH_FRAME, e);
                        });
                    }
                    return null;
                }

                public void done() {
                    if (exportPath != null) {
                        alertInformation(PlatformUI.MIRTH_FRAME, Messages.getString("Frame.552") + exportPath); //$NON-NLS-1$
                    }

                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doRefreshAlerts() {
        doRefreshAlerts(true);
    }

    public void doRefreshAlerts(boolean queue) {
        final List<String> selectedAlertIds = alertPanel.getSelectedAlertIds();

        QueuingSwingWorkerTask<Void, Void> task = new QueuingSwingWorkerTask<Void, Void>(Messages.getString("Frame.553"), Messages.getString("Frame.554")) { //$NON-NLS-1$ //$NON-NLS-2$

            private List<AlertStatus> alertStatusList;

            public Void doInBackground() {
                try {
                    alertStatusList = mirthClient.getAlertStatusList();
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }
                return null;
            }

            public void done() {
                alertPanel.updateAlertTable(alertStatusList);
                alertPanel.setSelectedAlertIds(selectedAlertIds);
            }
        };

        new QueuingSwingWorker<Void, Void>(task, queue).executeDelegate();
    }

    public void doSaveAlerts() {
        if (changesHaveBeenMade()) {
            try {
                ServerSettings serverSettings = mirthClient.getServerSettings();
                if (StringUtils.isBlank(serverSettings.getSmtpHost()) || StringUtils.isBlank(serverSettings.getSmtpPort())) {
                    alertWarning(PlatformUI.MIRTH_FRAME, Messages.getString("Frame.555")); //$NON-NLS-1$
                }
            } catch (ClientException e) {
                alertThrowable(PlatformUI.MIRTH_FRAME, e, false);
            }

            final String workingId = startWorking(Messages.getString("Frame.556")); //$NON-NLS-1$

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {
                    if (alertEditPanel.saveAlert()) {
                        setSaveEnabled(false);
                    }

                    return null;
                }

                public void done() {
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doDeleteAlert() {
        if (!alertOption(this, Messages.getString("Frame.557"))) { //$NON-NLS-1$
            return;
        }

        final String workingId = startWorking(Messages.getString("Frame.558")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                List<String> selectedAlertIds = alertPanel.getSelectedAlertIds();

                for (String alertId : selectedAlertIds) {
                    try {
                        mirthClient.removeAlert(alertId);
                    } catch (ClientException e) {
                        SwingUtilities.invokeLater(() -> {
                            alertThrowable(PlatformUI.MIRTH_FRAME, e);
                        });
                        return null;
                    }
                }
                alertPanel.updateAlertDetails(new HashSet<String>(selectedAlertIds));

                return null;
            }

            public void done() {
                doRefreshAlerts(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doNewAlert() throws ClientException {
        AlertInfo alertInfo = mirthClient.getAlertInfo(channelPanel.getChannelHeaders());
        channelPanel.updateChannelStatuses(alertInfo.getChangedChannels());
        setupAlert(alertInfo.getProtocolOptions());
    }

    public void doEditAlert() {
        if (isEditingAlert) {
            return;
        } else {
            isEditingAlert = true;
        }

        List<String> selectedAlertIds = alertPanel.getSelectedAlertIds();
        if (selectedAlertIds.size() > 1) {
            JOptionPane.showMessageDialog(Frame.this, Messages.getString("Frame.559")); //$NON-NLS-1$
        } else if (selectedAlertIds.size() == 0) {
            JOptionPane.showMessageDialog(Frame.this, Messages.getString("Frame.560")); //$NON-NLS-1$
        } else {
            try {
                AlertInfo alertInfo = mirthClient.getAlertInfo(selectedAlertIds.get(0), channelPanel.getChannelHeaders());

                if (alertInfo.getModel() == null) {
                    JOptionPane.showMessageDialog(Frame.this, Messages.getString("Frame.561")); //$NON-NLS-1$
                    doRefreshAlerts(true);
                } else {
                    channelPanel.updateChannelStatuses(alertInfo.getChangedChannels());
                    editAlert(alertInfo.getModel(), alertInfo.getProtocolOptions());
                }
            } catch (ClientException e) {
                alertThrowable(this, e);
            }
        }
        isEditingAlert = false;
    }

    public void doEnableAlert() {
        final String workingId = startWorking(Messages.getString("Frame.562")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                List<String> selectedAlertIds = alertPanel.getSelectedAlertIds();

                for (String alertId : selectedAlertIds) {
                    try {
                        mirthClient.enableAlert(alertId);
                    } catch (ClientException e) {
                        SwingUtilities.invokeLater(() -> {
                            alertThrowable(PlatformUI.MIRTH_FRAME, e);
                        });
                        return null;
                    }
                }

                return null;
            }

            public void done() {
                doRefreshAlerts(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doDisableAlert() {
        final String workingId = startWorking(Messages.getString("Frame.563")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                List<String> selectedAlertIds = alertPanel.getSelectedAlertIds();

                for (String alertId : selectedAlertIds) {
                    try {
                        mirthClient.disableAlert(alertId);
                    } catch (ClientException e) {
                        SwingUtilities.invokeLater(() -> {
                            alertThrowable(PlatformUI.MIRTH_FRAME, e);
                        });
                        return null;
                    }
                }

                return null;
            }

            public void done() {
                doRefreshAlerts(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doExportAlert() {
        if (changesHaveBeenMade()) {
            if (alertOption(this, Messages.getString("Frame.564"))) { //$NON-NLS-1$
                if (!alertEditPanel.saveAlert()) {
                    return;
                }
            } else {
                return;
            }

            setSaveEnabled(false);
        }

        List<String> selectedAlertIds;

        if (currentContentPage == alertEditPanel) {
            selectedAlertIds = new ArrayList<String>();

            String alertId = alertEditPanel.getAlertId();
            if (alertId != null) {
                selectedAlertIds.add(alertId);
            }
        } else {
            selectedAlertIds = alertPanel.getSelectedAlertIds();
        }

        if (CollectionUtils.isEmpty(selectedAlertIds)) {
            return;
        }

        AlertModel alert;
        try {
            alert = mirthClient.getAlert(selectedAlertIds.get(0));
        } catch (ClientException e) {
            alertThrowable(this, e);
            return;
        }

        if (alert == null) {
            JOptionPane.showMessageDialog(Frame.this, Messages.getString("Frame.565")); //$NON-NLS-1$
            doRefreshAlerts(true);
        } else {
            ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
            String alertXML = serializer.serialize(alert);

            exportFile(alertXML, alert.getName(), Messages.getString("Frame.566"), Messages.getString("Frame.567")); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public void doExportAlerts() {
        JFileChooser exportFileChooser = new JFileChooser();
        exportFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        File currentDir = new File(userPreferences.get(Messages.getString("Frame.568"), Messages.getString("Frame.569"))); //$NON-NLS-1$ //$NON-NLS-2$
        if (currentDir.exists()) {
            exportFileChooser.setCurrentDirectory(currentDir);
        }

        int returnVal = exportFileChooser.showSaveDialog(this);
        File exportFile = null;
        File exportDirectory = null;

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            userPreferences.put(Messages.getString("Frame.570"), exportFileChooser.getCurrentDirectory().getPath()); //$NON-NLS-1$
            try {
                exportDirectory = exportFileChooser.getSelectedFile();

                List<AlertModel> alerts;
                try {
                    alerts = mirthClient.getAllAlerts();
                } catch (ClientException e) {
                    alertThrowable(this, e);
                    return;
                }

                for (AlertModel alert : alerts) {
                    ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
                    String channelXML = serializer.serialize(alert);

                    exportFile = new File(exportDirectory.getAbsolutePath() + Messages.getString("Frame.571") + alert.getName() + Messages.getString("Frame.572")); //$NON-NLS-1$ //$NON-NLS-2$

                    if (exportFile.exists()) {
                        if (!alertOption(this, Messages.getString("Frame.573") + alert.getName() + Messages.getString("Frame.574"))) { //$NON-NLS-1$ //$NON-NLS-2$
                            continue;
                        }
                    }

                    FileUtils.writeStringToFile(exportFile, channelXML, UIConstants.CHARSET);
                }
                alertInformation(this, Messages.getString("Frame.575") + exportDirectory.getPath() + Messages.getString("Frame.576")); //$NON-NLS-1$ //$NON-NLS-2$
            } catch (IOException ex) {
                alertError(this, Messages.getString("Frame.577")); //$NON-NLS-1$
            }
        }
    }

    public void doImportAlert() {
        String content = browseForFileString(Messages.getString("Frame.578")); //$NON-NLS-1$

        if (content != null) {
            importAlert(content, true);
        }
    }

    public void importAlert(String alertXML, boolean showAlerts) {
        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        List<AlertModel> alertList;

        try {
            alertList = (List<AlertModel>) serializer.deserializeList(alertXML.replaceAll(Messages.getString("Frame.579"), Messages.getString("Frame.580")).replaceAll(Messages.getString("Frame.581"), Messages.getString("Frame.582")), AlertModel.class); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        } catch (Exception e) {
            if (showAlerts) {
                alertThrowable(this, e, Messages.getString("Frame.583") + e.getMessage()); //$NON-NLS-1$
            }
            return;
        }

        removeInvalidItems(alertList, AlertModel.class);

        for (AlertModel importAlert : alertList) {
            try {
                String alertName = importAlert.getName();
                String tempId = mirthClient.getGuid();

                // Check to see that the alert name doesn't already exist.
                if (!checkAlertName(alertName)) {
                    if (!alertOption(this, Messages.getString("Frame.584"))) { //$NON-NLS-1$
                        do {
                            alertName = DisplayUtil.showInputDialog(this, Messages.getString("Frame.585"), alertName); //$NON-NLS-1$
                            if (alertName == null) {
                                return;
                            }
                        } while (!checkAlertName(alertName));

                        importAlert.setName(alertName);
                        importAlert.setId(tempId);
                    } else {
                        for (Entry<String, String> entry : alertPanel.getAlertNames().entrySet()) {
                            String id = entry.getKey();
                            String name = entry.getValue();
                            if (name.equalsIgnoreCase(alertName)) {
                                // If overwriting, use the old id
                                importAlert.setId(id);
                            }
                        }
                    }
                }

                mirthClient.updateAlert(importAlert);
            } catch (Exception e) {
                alertThrowable(this, e, Messages.getString("Frame.586") + e.getMessage()); //$NON-NLS-1$
            }
        }

        doRefreshAlerts(true);
    }

    /**
     * Checks to see if the passed in channel name already exists
     */
    public boolean checkAlertName(String name) {
        if (name.equals(Messages.getString("Frame.587"))) { //$NON-NLS-1$
            alertWarning(this, Messages.getString("Frame.588")); //$NON-NLS-1$
            return false;
        }

        Pattern alphaNumericPattern = Pattern.compile(Messages.getString("Frame.589")); //$NON-NLS-1$
        Matcher matcher = alphaNumericPattern.matcher(name);

        if (!matcher.find()) {
            alertWarning(this, Messages.getString("Frame.590")); //$NON-NLS-1$
            return false;
        }

        for (String alertName : alertPanel.getAlertNames().values()) {
            if (alertName.equalsIgnoreCase(name)) {
                alertWarning(this, Messages.getString("Frame.591") + name + Messages.getString("Frame.592")); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
        }
        return true;
    }

    ///// Start Extension Tasks /////
    public void doRefreshExtensions() {
        final String workingId = startWorking(Messages.getString("Frame.593")); //$NON-NLS-1$

        if (confirmLeave()) {
            refreshExtensions();
        }

        stopWorking(workingId);
    }

    public void refreshExtensions() {
        extensionsPanel.setPluginData(getPluginMetaData());
        extensionsPanel.setConnectorData(getConnectorMetaData());
    }

    public void doEnableExtension() {
        final String workingId = startWorking(Messages.getString("Frame.594")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private boolean success = true;

            public Void doInBackground() {
                try {
                    mirthClient.setExtensionEnabled(extensionsPanel.getSelectedExtension().getName(), true);
                } catch (ClientException e) {
                    success = false;
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }

                return null;
            }

            public void done() {
                if (success) {
                    extensionsPanel.setSelectedExtensionEnabled(true);
                    extensionsPanel.setRestartRequired(true);
                }
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doDisableExtension() {
        final String workingId = startWorking(Messages.getString("Frame.595")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private boolean success = true;

            public Void doInBackground() {
                try {
                    mirthClient.setExtensionEnabled(extensionsPanel.getSelectedExtension().getName(), false);
                } catch (ClientException e) {
                    success = false;
                    SwingUtilities.invokeLater(() -> {
                        alertThrowable(PlatformUI.MIRTH_FRAME, e);
                    });
                }

                return null;
            }

            public void done() {
                if (success) {
                    extensionsPanel.setSelectedExtensionEnabled(false);
                    extensionsPanel.setRestartRequired(true);
                }
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doShowExtensionProperties() {
        extensionsPanel.showExtensionProperties();
    }

    public void doUninstallExtension() {
        final String workingId = startWorking(Messages.getString("Frame.596")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private boolean success = true;

            public Void doInBackground() {
                String packageName = extensionsPanel.getSelectedExtension().getPath();

                if (alertOkCancel(PlatformUI.MIRTH_FRAME, Messages.getString("Frame.597") + packageName)) { //$NON-NLS-1$
                    try {
                        mirthClient.uninstallExtension(packageName);
                    } catch (ClientException e) {
                        success = false;
                        SwingUtilities.invokeLater(() -> {
                            alertThrowable(PlatformUI.MIRTH_FRAME, e);
                        });
                    }
                }

                return null;
            }

            public void done() {
                if (success) {
                    extensionsPanel.setRestartRequired(true);
                }
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public boolean installExtension(File file) {
        try {
            if (file.exists()) {
                mirthClient.installExtension(file);
            } else {
                alertError(this, Messages.getString("Frame.598")); //$NON-NLS-1$
                return false;
            }
        } catch (ClientException e) {
            if (e.getCause() != null && e.getCause() instanceof VersionMismatchException) {
                alertError(this, e.getCause().getMessage());
            } else {
                alertThrowable(this, e, Messages.getString("Frame.599") + e.getMessage()); //$NON-NLS-1$
            }

            return false;
        }
        return true;
    }

    ///// End Extension Tasks /////

    public boolean exportChannelOnError() {
        if (isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, Messages.getString("Frame.600")); //$NON-NLS-1$
            if (option == JOptionPane.YES_OPTION) {
                if (!channelEditPanel.saveChanges()) {
                    return false;
                }

                boolean enabled = isSaveEnabled();
                setSaveEnabled(false);
                if (!doExportChannel()) {
                    setSaveEnabled(enabled);
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            } else {
                setSaveEnabled(false);
            }
        }
        return true;
    }

    public void setCanSave(boolean canSave) {
        this.canSave = canSave;
    }

    public void doContextSensitiveSave() {
        if (canSave) {
            if (currentContentPage == channelPanel) {
                channelPanel.doContextSensitiveSave();
            } else if (currentContentPage == channelEditPanel) {
                doSaveChannel();
            } else if (currentContentPage == channelEditPanel.filterPane) {
                doSaveChannel();
            } else if (currentContentPage == channelEditPanel.transformerPane) {
                doSaveChannel();
            } else if (currentContentPage == globalScriptsPanel) {
                doSaveGlobalScripts();
            } else if (currentContentPage == codeTemplatePanel) {
                codeTemplatePanel.doContextSensitiveSave();
            } else if (currentContentPage == settingsPane) {
                settingsPane.getCurrentSettingsPanel().doSave();
            } else if (currentContentPage == alertEditPanel) {
                doSaveAlerts();
            }
        }
    }

    public void doFind(JEditTextArea text) {
        FindRplDialog find;
        Window owner = getWindowForComponent(text);

        if (owner instanceof java.awt.Frame) {
            find = new FindRplDialog((java.awt.Frame) owner, true, text);
        } else { // window instanceof Dialog
            find = new FindRplDialog((java.awt.Dialog) owner, true, text);
        }

        find.setVisible(true);
    }

    public void doHelp() {
        BareBonesBrowserLaunch.openURL(UIConstants.HELP_LOCATION);
    }

    public void goToNotifications() {
        new NotificationDialog();
    }

    public Map<String, PluginMetaData> getPluginMetaData() {
        return this.loadedPlugins;
    }

    public Map<String, ConnectorMetaData> getConnectorMetaData() {
        return this.loadedConnectors;
    }

    public String getSelectedChannelIdFromDashboard() {
        return dashboardPanel.getSelectedStatuses().get(0).getChannelId();
    }

    public List<Integer> getSelectedMetaDataIdsFromDashboard(String channelId) {
        List<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedStatuses();
        List<Integer> metaDataIds = new ArrayList<Integer>();

        if (selectedStatuses.size() == 0) {
            return metaDataIds;
        }

        for (DashboardStatus status : selectedStatuses) {
            if (status.getChannelId() == channelId) {
                Integer metaDataId = status.getMetaDataId();

                if (metaDataId != null) {
                    metaDataIds.add(metaDataId);
                }
            }
        }

        return metaDataIds;
    }

    public void retrieveUsers() throws ClientException {
        users = mirthClient.getAllUsers();
    }

    public synchronized void updateAcceleratorKeyPressed(InputEvent e) {
        this.acceleratorKeyPressed = (((e.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) > 0) || ((e.getModifiers() & InputEvent.CTRL_MASK) > 0) || ((e.getModifiers() & InputEvent.ALT_MASK) > 0));
    }

    public synchronized boolean isAcceleratorKeyPressed() {
        return acceleratorKeyPressed;
    }

    /**
     * Checks to see if the passed in channel name already exists
     */
    public boolean checkChannelName(String name, String id) {
        if (name.equals(Messages.getString("Frame.601"))) { //$NON-NLS-1$
            alertWarning(this, Messages.getString("Frame.602")); //$NON-NLS-1$
            return false;
        }

        if (name.length() > 40) {
            alertWarning(this, Messages.getString("Frame.603")); //$NON-NLS-1$
            return false;
        }

        Pattern alphaNumericPattern = Pattern.compile(Messages.getString("Frame.604")); //$NON-NLS-1$
        Matcher matcher = alphaNumericPattern.matcher(name);

        if (!matcher.find()) {
            alertWarning(this, Messages.getString("Frame.605")); //$NON-NLS-1$
            return false;
        }

        for (ChannelStatus channelStatus : channelPanel.getCachedChannelStatuses().values()) {
            if (channelStatus.getChannel().getName().equalsIgnoreCase(name) && !channelStatus.getChannel().getId().equals(id)) {
                alertWarning(this, Messages.getString("Frame.606") + name + Messages.getString("Frame.607")); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
        }
        return true;
    }

    public SettingsPanelTags getTagsPanel() {
        if (settingsPane != null) {
            return (SettingsPanelTags) settingsPane.getSettingsPanel(SettingsPanelTags.TAB_NAME);
        }
        return null;
    }

    public Set<ChannelTag> getCachedChannelTags() {
        SettingsPanelTags tagsPanel = getTagsPanel();
        if (tagsPanel != null) {
            return tagsPanel.getCachedChannelTags();
        }
        return new HashSet<ChannelTag>();
    }

    /**
     * Checks to see if the serialized object version is current, and prompts the user if it is not.
     */
    public boolean promptObjectMigration(String content, String objectName) {
        String version = null;

        try {
            version = MigrationUtil.normalizeVersion(MigrationUtil.getSerializedObjectVersion(content), 3);
        } catch (Exception e) {
            logger.error(Messages.getString("Frame.608"), e); //$NON-NLS-1$
        }

        StringBuilder message = new StringBuilder();

        if (version == null) {
            message.append(Messages.getString("Frame.609") + objectName + Messages.getString("Frame.610")); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            int comparison = MigrationUtil.compareVersions(version, PlatformUI.SERVER_VERSION);

            if (comparison == 0) {
                return true;
            }

            if (comparison > 0) {
                alertInformation(this, Messages.getString("Frame.611") + objectName + Messages.getString("Frame.612") + version + Messages.getString("Frame.613") + PlatformUI.SERVER_VERSION + Messages.getString("Frame.614") + objectName + Messages.getString("Frame.615")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                return false;
            }

            if (comparison < 0) {
                message.append(Messages.getString("Frame.616") + objectName + Messages.getString("Frame.617") + version + Messages.getString("Frame.618")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }

        message.append(Messages.getString("Frame.619") + PlatformUI.SERVER_VERSION + Messages.getString("Frame.620") + objectName + Messages.getString("Frame.621") + PlatformUI.SERVER_VERSION + Messages.getString("Frame.622")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, message.toString(), Messages.getString("Frame.623"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
    }

    /**
     * Removes items from the list that are not of the expected class.
     */
    public void removeInvalidItems(List<?> list, Class<?> expectedClass) {
        if (list == null) {
            return;
        }

        int originalSize = list.size();

        for (int i = 0; i < list.size(); i++) {
            if (!expectedClass.isInstance(list.get(i))) {
                list.remove(i--);
            }
        }

        if (list.size() < originalSize) {
            if (list.size() == 0) {
                alertError(this, Messages.getString("Frame.624") + expectedClass.getSimpleName()); //$NON-NLS-1$
            } else {
                alertError(this, Messages.getString("Frame.625") + expectedClass.getSimpleName()); //$NON-NLS-1$
            }
        }
    }

    public List<ResourceProperties> getResources() {
        if (settingsPane == null) {
            settingsPane = new SettingsPane();
        }

        List<ResourceProperties> resourceProperties = null;

        SettingsPanelResources resourcesPanel = (SettingsPanelResources) settingsPane.getSettingsPanel(SettingsPanelResources.TAB_NAME);
        if (resourcesPanel != null) {
            resourceProperties = resourcesPanel.getCachedResources();
            if (resourceProperties == null) {
                resourcesPanel.refresh();
                resourceProperties = resourcesPanel.getCachedResources();
            }
        }

        return resourceProperties;
    }

    public void updateResourceNames(Channel channel) {
        updateResourceNames(channel, getResources());
    }

    public void updateResourceNames(Channel channel, List<ResourceProperties> resourceProperties) {
        if (!(channel instanceof InvalidChannel)) {
            updateResourceNames(channel.getProperties().getResourceIds(), resourceProperties);
            updateResourceNames(channel.getSourceConnector(), resourceProperties);
            for (Connector destinationConnector : channel.getDestinationConnectors()) {
                updateResourceNames(destinationConnector, resourceProperties);
            }
        }
    }

    public void updateResourceNames(Connector connector) {
        updateResourceNames(connector, getResources());
    }

    private void updateResourceNames(Connector connector, List<ResourceProperties> resourceProperties) {
        if (connector.getProperties() instanceof SourceConnectorPropertiesInterface) {
            updateResourceNames(((SourceConnectorPropertiesInterface) connector.getProperties()).getSourceConnectorProperties().getResourceIds(), resourceProperties);
        } else {
            updateResourceNames(((DestinationConnectorPropertiesInterface) connector.getProperties()).getDestinationConnectorProperties().getResourceIds(), resourceProperties);
        }
    }

    private void updateResourceNames(Map<String, String> resourceIds, List<ResourceProperties> resourceProperties) {
        if (resourceProperties != null) {
            Set<String> invalidIds = new HashSet<String>(resourceIds.keySet());

            // First update the names of all resources currently in the map
            for (ResourceProperties resource : resourceProperties) {
                if (resourceIds.containsKey(resource.getId())) {
                    resourceIds.put(resource.getId(), resource.getName());
                    // If the resource ID was found it's not invalid
                    invalidIds.remove(resource.getId());
                }
            }

            /*
             * Iterate through all resource IDs that weren't found in the current list of resources.
             * If there's a resource with a different ID but the same name as a particular entry,
             * then replace the entry with the correct ID/name.
             */
            for (String invalidId : invalidIds) {
                String resourceName = resourceIds.get(invalidId);
                if (StringUtils.isNotBlank(resourceName)) {
                    for (ResourceProperties resource : resourceProperties) {
                        // Replace if the names are equal and the resource ID isn't already contained in the map
                        if (resource.getName().equals(resourceName) && !resourceIds.containsKey(resource.getId())) {
                            resourceIds.put(resource.getId(), resourceName);
                            resourceIds.remove(invalidId);
                        }
                    }
                }
            }
        }
    }
}