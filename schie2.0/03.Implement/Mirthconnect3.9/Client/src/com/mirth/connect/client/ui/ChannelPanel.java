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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.core.ForbiddenException;
import com.mirth.connect.client.core.TaskConstants;
import com.mirth.connect.client.ui.Frame.ChannelTask;
import com.mirth.connect.client.ui.Frame.ConflictOption;
import com.mirth.connect.client.ui.codetemplate.CodeTemplateImportDialog;
import com.mirth.connect.client.ui.components.ChannelTableTransferHandler;
import com.mirth.connect.client.ui.components.IconToggleButton;
import com.mirth.connect.client.ui.components.MirthTreeTable;
import com.mirth.connect.client.ui.components.rsta.MirthRTextScrollPane;
import com.mirth.connect.client.ui.components.tag.ChannelNameFilterCompletion;
import com.mirth.connect.client.ui.components.tag.FilterCompletion;
import com.mirth.connect.client.ui.components.tag.MirthTagField;
import com.mirth.connect.client.ui.components.tag.SearchFilterListener;
import com.mirth.connect.client.ui.components.tag.TagFilterCompletion;
import com.mirth.connect.client.ui.dependencies.ChannelDependenciesWarningDialog;
import com.mirth.connect.client.ui.tag.SettingsPanelTags;
import com.mirth.connect.client.ui.util.DisplayUtil;
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.donkey.util.DonkeyElement.DonkeyElementException;
import com.mirth.connect.model.Channel;
import com.mirth.connect.model.ChannelDependency;
import com.mirth.connect.model.ChannelGroup;
import com.mirth.connect.model.ChannelHeader;
import com.mirth.connect.model.ChannelMetadata;
import com.mirth.connect.model.ChannelStatus;
import com.mirth.connect.model.ChannelSummary;
import com.mirth.connect.model.ChannelTag;
import com.mirth.connect.model.DashboardStatus;
import com.mirth.connect.model.InvalidChannel;
import com.mirth.connect.model.codetemplates.CodeTemplate;
import com.mirth.connect.model.codetemplates.CodeTemplateLibrary;
import com.mirth.connect.model.codetemplates.CodeTemplateLibrarySaveResult;
import com.mirth.connect.model.codetemplates.CodeTemplateLibrarySaveResult.CodeTemplateUpdateResult;
import com.mirth.connect.model.converters.ObjectXMLSerializer;
import com.mirth.connect.model.filter.SearchFilter;
import com.mirth.connect.model.filter.SearchFilterParser;
import com.mirth.connect.model.util.ImportConverter3_0_0;
import com.mirth.connect.plugins.ChannelColumnPlugin;
import com.mirth.connect.plugins.ChannelPanelPlugin;
import com.mirth.connect.plugins.TaskPlugin;
import com.mirth.connect.util.ChannelDependencyException;
import com.mirth.connect.util.ChannelDependencyGraph;
import com.mirth.connect.util.DirectedAcyclicGraphNode;

public class ChannelPanel extends AbstractFramePanel {

    public static final String STATUS_COLUMN_NAME = Messages.getString("ChannelPanel.0"); //$NON-NLS-1$
    public static final String DATA_TYPE_COLUMN_NAME = Messages.getString("ChannelPanel.1"); //$NON-NLS-1$
    public static final String NAME_COLUMN_NAME = Messages.getString("ChannelPanel.2"); //$NON-NLS-1$
    public static final String ID_COLUMN_NAME = Messages.getString("ChannelPanel.3"); //$NON-NLS-1$
    public static final String LOCAL_CHANNEL_ID = Messages.getString("ChannelPanel.4"); //$NON-NLS-1$
    public static final String DESCRIPTION_COLUMN_NAME = Messages.getString("ChannelPanel.5"); //$NON-NLS-1$
    public static final String DEPLOYED_REVISION_DELTA_COLUMN_NAME = Messages.getString("ChannelPanel.6"); //$NON-NLS-1$
    public static final String LAST_DEPLOYED_COLUMN_NAME = Messages.getString("ChannelPanel.7"); //$NON-NLS-1$
    public static final String LAST_MODIFIED_COLUMN_NAME = Messages.getString("ChannelPanel.8"); //$NON-NLS-1$

    public static final int STATUS_COLUMN_NUMBER = 0;
    public static final int DATA_TYPE_COLUMN_NUMBER = 1;
    public static final int NAME_COLUMN_NUMBER = 2;
    public static final int ID_COLUMN_NUMBER = 3;
    public static final int LOCAL_CHANNEL_ID_COLUMN_NUMBER = 4;
    public static final int DESCRIPTION_COLUMN_NUMBER = 5;
    public static final int DEPLOYED_REVISION_DELTA_COLUMN_NUMBER = 6;
    public static final int LAST_DEPLOYED_COLUMN_NUMBER = 7;
    public static final int LAST_MODIFIED_COLUMN_NUMBER = 8;

    private final static String[] DEFAULT_COLUMNS = new String[] { STATUS_COLUMN_NAME,
            DATA_TYPE_COLUMN_NAME, NAME_COLUMN_NAME, ID_COLUMN_NAME, LOCAL_CHANNEL_ID,
            DESCRIPTION_COLUMN_NAME, DEPLOYED_REVISION_DELTA_COLUMN_NAME, LAST_DEPLOYED_COLUMN_NAME,
            LAST_MODIFIED_COLUMN_NAME };

    private static final int TASK_CHANNEL_REFRESH = 0;
    private static final int TASK_CHANNEL_REDEPLOY_ALL = 1;
    private static final int TASK_CHANNEL_DEPLOY = 2;
    private static final int TASK_CHANNEL_EDIT_GLOBAL_SCRIPTS = 3;
    private static final int TASK_CHANNEL_EDIT_CODE_TEMPLATES = 4;
    private static final int TASK_CHANNEL_NEW_CHANNEL = 5;
    private static final int TASK_CHANNEL_IMPORT_CHANNEL = 6;
    private static final int TASK_CHANNEL_EXPORT_ALL_CHANNELS = 7;
    private static final int TASK_CHANNEL_EXPORT_CHANNEL = 8;
    private static final int TASK_CHANNEL_DELETE_CHANNEL = 9;
    private static final int TASK_CHANNEL_CLONE = 10;
    private static final int TASK_CHANNEL_EDIT = 11;
    private static final int TASK_CHANNEL_ENABLE = 12;
    private static final int TASK_CHANNEL_DISABLE = 13;
    private static final int TASK_CHANNEL_VIEW_MESSAGES = 14;

    private static final int TASK_GROUP_SAVE = 0;
    private static final int TASK_GROUP_ASSIGN_CHANNEL = 1;
    private static final int TASK_GROUP_NEW_GROUP = 2;
    private static final int TASK_GROUP_EDIT_DETAILS = 3;
    private static final int TASK_GROUP_IMPORT_GROUP = 4;
    private static final int TASK_GROUP_EXPORT_ALL_GROUPS = 5;
    private static final int TASK_GROUP_EXPORT_GROUP = 6;
    private static final int TASK_GROUP_DELETE_GROUP = 7;

    private Frame parent;

    private Map<String, String> channelIdsAndNames = new HashMap<String, String>();
    private Map<String, ChannelStatus> channelStatuses = new LinkedHashMap<String, ChannelStatus>();
    private Map<String, ChannelGroupStatus> groupStatuses = new LinkedHashMap<String, ChannelGroupStatus>();
    private Set<ChannelDependency> channelDependencies = new HashSet<ChannelDependency>();

    private Preferences userPreferences;
    private boolean tagTextModeSelected = false;
    private boolean tagIconModeSelected = false;
    private boolean canViewChannelGroups = AuthorizationControllerFactory.getAuthorizationController().checkTask(TaskConstants.CHANNEL_GROUP_KEY, TaskConstants.CHANNEL_GROUP_EXPORT_GROUP);

    public ChannelPanel() {
        this.parent = PlatformUI.MIRTH_FRAME;
        userPreferences = Preferences.userNodeForPackage(Mirth.class);

        initComponents();
        initLayout();

        channelTasks = new JXTaskPane();
        channelTasks.setTitle(Messages.getString("ChannelPanel.9")); //$NON-NLS-1$
        channelTasks.setName(TaskConstants.CHANNEL_KEY);
        channelTasks.setFocusable(false);

        channelPopupMenu = new JPopupMenu();
        channelTable.setComponentPopupMenu(channelPopupMenu);

        parent.addTask(TaskConstants.CHANNEL_REFRESH, Messages.getString("ChannelPanel.10"), Messages.getString("ChannelPanel.11"), Messages.getString("ChannelPanel.12"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.13"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_REDEPLOY_ALL, Messages.getString("ChannelPanel.14"), Messages.getString("ChannelPanel.15"), Messages.getString("ChannelPanel.16"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.17"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_DEPLOY, Messages.getString("ChannelPanel.18"), Messages.getString("ChannelPanel.19"), Messages.getString("ChannelPanel.20"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.21"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_EDIT_GLOBAL_SCRIPTS, Messages.getString("ChannelPanel.22"), Messages.getString("ChannelPanel.23"), Messages.getString("ChannelPanel.24"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.25"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_EDIT_CODE_TEMPLATES, Messages.getString("ChannelPanel.26"), Messages.getString("ChannelPanel.27"), Messages.getString("ChannelPanel.28"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.29"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_NEW_CHANNEL, Messages.getString("ChannelPanel.30"), Messages.getString("ChannelPanel.31"), Messages.getString("ChannelPanel.32"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.33"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_IMPORT_CHANNEL, Messages.getString("ChannelPanel.34"), Messages.getString("ChannelPanel.35"), Messages.getString("ChannelPanel.36"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.37"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_EXPORT_ALL_CHANNELS, Messages.getString("ChannelPanel.38"), Messages.getString("ChannelPanel.39"), Messages.getString("ChannelPanel.40"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.41"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_EXPORT_CHANNEL, Messages.getString("ChannelPanel.42"), Messages.getString("ChannelPanel.43"), Messages.getString("ChannelPanel.44"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.45"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_DELETE_CHANNEL, Messages.getString("ChannelPanel.46"), Messages.getString("ChannelPanel.47"), Messages.getString("ChannelPanel.48"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.49"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_CLONE, Messages.getString("ChannelPanel.50"), Messages.getString("ChannelPanel.51"), Messages.getString("ChannelPanel.52"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.53"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_EDIT, Messages.getString("ChannelPanel.54"), Messages.getString("ChannelPanel.55"), Messages.getString("ChannelPanel.56"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.57"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_ENABLE, Messages.getString("ChannelPanel.58"), Messages.getString("ChannelPanel.59"), Messages.getString("ChannelPanel.60"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.61"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_DISABLE, Messages.getString("ChannelPanel.62"), Messages.getString("ChannelPanel.63"), Messages.getString("ChannelPanel.64"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.65"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_VIEW_MESSAGES, Messages.getString("ChannelPanel.66"), Messages.getString("ChannelPanel.67"), Messages.getString("ChannelPanel.68"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.69"))), channelTasks, channelPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        parent.setNonFocusable(channelTasks);
        parent.taskPaneContainer.add(channelTasks, parent.taskPaneContainer.getComponentCount() - 1);

        groupTasks = new JXTaskPane();
        groupTasks.setTitle(Messages.getString("ChannelPanel.70")); //$NON-NLS-1$
        groupTasks.setName(TaskConstants.CHANNEL_GROUP_KEY);
        groupTasks.setFocusable(false);

        groupPopupMenu = new JPopupMenu();

        parent.addTask(TaskConstants.CHANNEL_GROUP_SAVE, Messages.getString("ChannelPanel.71"), Messages.getString("ChannelPanel.72"), Messages.getString("ChannelPanel.73"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.74"))), groupTasks, groupPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_GROUP_ASSIGN_CHANNEL, Messages.getString("ChannelPanel.75"), Messages.getString("ChannelPanel.76"), Messages.getString("ChannelPanel.77"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.78"))), groupTasks, groupPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_GROUP_NEW_GROUP, Messages.getString("ChannelPanel.79"), Messages.getString("ChannelPanel.80"), Messages.getString("ChannelPanel.81"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.82"))), groupTasks, groupPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_GROUP_EDIT_DETAILS, Messages.getString("ChannelPanel.83"), Messages.getString("ChannelPanel.84"), Messages.getString("ChannelPanel.85"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.86"))), groupTasks, groupPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_GROUP_IMPORT_GROUP, Messages.getString("ChannelPanel.87"), Messages.getString("ChannelPanel.88"), Messages.getString("ChannelPanel.89"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.90"))), groupTasks, groupPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_GROUP_EXPORT_ALL_GROUPS, Messages.getString("ChannelPanel.91"), Messages.getString("ChannelPanel.92"), Messages.getString("ChannelPanel.93"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.94"))), groupTasks, groupPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_GROUP_EXPORT_GROUP, Messages.getString("ChannelPanel.95"), Messages.getString("ChannelPanel.96"), Messages.getString("ChannelPanel.97"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.98"))), groupTasks, groupPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        parent.addTask(TaskConstants.CHANNEL_GROUP_DELETE_GROUP, Messages.getString("ChannelPanel.99"), Messages.getString("ChannelPanel.100"), Messages.getString("ChannelPanel.101"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("ChannelPanel.102"))), groupTasks, groupPopupMenu, this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        parent.setNonFocusable(groupTasks);
        parent.taskPaneContainer.add(groupTasks, parent.taskPaneContainer.getComponentCount() - 1);

        channelScrollPane.setComponentPopupMenu(channelPopupMenu);

        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();

        if (canViewChannelGroups && userPreferences.getBoolean(Messages.getString("ChannelPanel.103"), true)) { //$NON-NLS-1$
            tableModeGroupsButton.setSelected(true);
            tableModeGroupsButton.setContentFilled(true);
            tableModeChannelsButton.setContentFilled(false);
            model.setGroupModeEnabled(true);
        } else {
            tableModeChannelsButton.setSelected(true);
            tableModeChannelsButton.setContentFilled(true);
            tableModeGroupsButton.setContentFilled(false);
            model.setGroupModeEnabled(false);
        }

        if (!canViewChannelGroups) {
            tableModeGroupsButton.setEnabled(false);
        }

        updateTagButtons(userPreferences.getBoolean(Messages.getString("ChannelPanel.104"), true), userPreferences.getBoolean(Messages.getString("ChannelPanel.105"), false), false); //$NON-NLS-1$ //$NON-NLS-2$

        updateModel(new TableState(new ArrayList<String>(), null));
        updateTasks();
    }

    @Override
    public void switchPanel() {
        boolean groupViewEnabled = canViewChannelGroups && userPreferences.getBoolean(Messages.getString("ChannelPanel.106"), true); //$NON-NLS-1$
        switchTableMode(groupViewEnabled, false);

        if (groupViewEnabled) {
            tableModeGroupsButton.setSelected(true);
            tableModeGroupsButton.setContentFilled(true);
            tableModeChannelsButton.setContentFilled(false);
        } else {
            tableModeChannelsButton.setSelected(true);
            tableModeChannelsButton.setContentFilled(true);
            tableModeGroupsButton.setContentFilled(false);
        }

        updateTagButtons(userPreferences.getBoolean(Messages.getString("ChannelPanel.107"), true), userPreferences.getBoolean(Messages.getString("ChannelPanel.108"), false), false); //$NON-NLS-1$ //$NON-NLS-2$

        List<JXTaskPane> taskPanes = new ArrayList<JXTaskPane>();
        taskPanes.add(channelTasks);

        if (groupViewEnabled) {
            taskPanes.add(groupTasks);
        }

        for (TaskPlugin plugin : LoadedExtensions.getInstance().getTaskPlugins().values()) {
            JXTaskPane taskPane = plugin.getTaskPane();
            if (taskPane != null) {
                taskPanes.add(taskPane);
            }
        }

        tagField.setFocus(true);

        parent.setBold(parent.viewPane, 1);
        parent.setPanelName(Messages.getString("ChannelPanel.109")); //$NON-NLS-1$
        parent.setCurrentContentPage(ChannelPanel.this);
        parent.setFocus(taskPanes.toArray(new JXTaskPane[taskPanes.size()]), true, true);
        parent.setSaveEnabled(false);
        setSaveEnabled(false);

        doRefreshChannels();
    }

    private void updateTagButtons(boolean showTags, boolean textMode, boolean updatePreferences) {
        tagModeTextButton.setSelected(showTags && textMode);
        tagModeIconButton.setSelected(showTags && !textMode);
        tagModeTextButton.setContentFilled(showTags && textMode);
        tagModeIconButton.setContentFilled(showTags && !textMode);
        channelTable.setTreeCellRenderer(new TagTreeCellRenderer(showTags, textMode));
        tagTextModeSelected = showTags && textMode;
        tagIconModeSelected = showTags && !textMode;

        if (updatePreferences) {
            userPreferences.putBoolean(Messages.getString("ChannelPanel.110"), showTags); //$NON-NLS-1$
            userPreferences.putBoolean(Messages.getString("ChannelPanel.111"), textMode); //$NON-NLS-1$
        }
    }

    public void closePopupWindow() {
        tagField.closePopupWindow();
    }

    @Override
    public boolean isSaveEnabled() {
        return groupTasks.getContentPane().getComponent(TASK_GROUP_SAVE).isVisible();
    }

    @Override
    public void setSaveEnabled(boolean enabled) {
        setGroupTaskVisibility(TASK_GROUP_SAVE, enabled);
    }

    @Override
    public boolean changesHaveBeenMade() {
        return isSaveEnabled();
    }

    @Override
    public void doContextSensitiveSave() {
        if (isSaveEnabled()) {
            doSaveGroups();
        }
    }

    @Override
    public boolean confirmLeave() {
        return promptSave(false);
    }

    private boolean promptSave(boolean force) {
        int option;

        if (force) {
            option = JOptionPane.showConfirmDialog(parent, Messages.getString("ChannelPanel.112")); //$NON-NLS-1$
        } else {
            option = JOptionPane.showConfirmDialog(parent, Messages.getString("ChannelPanel.113")); //$NON-NLS-1$
        }

        if (option == JOptionPane.YES_OPTION) {
            return doSaveGroups(false);
        } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION || (option == JOptionPane.NO_OPTION && force)) {
            return false;
        }

        return true;
    }

    @Override
    protected Component addAction(Action action, Set<String> options) {
        Component taskComponent = channelTasks.add(action);
        channelPopupMenu.add(action);
        return taskComponent;
    }

    public Map<String, String> getCachedChannelIdsAndNames() {
        return channelIdsAndNames;
    }

    public Map<String, ChannelStatus> getCachedChannelStatuses() {
        return channelStatuses;
    }

    public Map<String, ChannelGroupStatus> getCachedGroupStatuses() {
        return groupStatuses;
    }

    public Set<ChannelDependency> getCachedChannelDependencies() {
        return channelDependencies;
    }

    public Set<ChannelTag> getCachedChannelTags() {
        return parent.getCachedChannelTags();
    }

    public String getUserTags() {
        return tagField.getTags();
    }

    public void doRefreshChannels() {
        doRefreshChannels(true);
    }

    public void doRefreshChannels(boolean queue) {
        if (isSaveEnabled() && !confirmLeave()) {
            return;
        }

        QueuingSwingWorkerTask<Void, Void> task = new QueuingSwingWorkerTask<Void, Void>(Messages.getString("ChannelPanel.114"), Messages.getString("ChannelPanel.115")) { //$NON-NLS-1$ //$NON-NLS-2$
            @Override
            public Void doInBackground() {
                retrieveChannels();
                return null;
            }

            @Override
            public void done() {
                updateModel(getCurrentTableState());
                updateTasks();
                updateTags(false);
                parent.setSaveEnabled(false);
            }
        };

        new QueuingSwingWorker<Void, Void>(task, queue).executeDelegate();
    }

    private void updateTasks() {
        int[] rows = channelTable.getSelectedModelRows();
        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();
        boolean filterEnabled = tagField.isFilterEnabled();
        boolean saveEnabled = isSaveEnabled();

        setAllTaskVisibility(false);

        setChannelTaskVisible(TASK_CHANNEL_REFRESH);
        setChannelTaskVisible(TASK_CHANNEL_REDEPLOY_ALL);
        setChannelTaskVisible(TASK_CHANNEL_EDIT_GLOBAL_SCRIPTS);
        setChannelTaskVisible(TASK_CHANNEL_EDIT_CODE_TEMPLATES);
        setChannelTaskVisible(TASK_CHANNEL_NEW_CHANNEL);
        setChannelTaskVisible(TASK_CHANNEL_IMPORT_CHANNEL);
        if (model.isGroupModeEnabled()) {
            if (!filterEnabled) {
                setGroupTaskVisible(TASK_GROUP_NEW_GROUP);

                if (!saveEnabled) {
                    setGroupTaskVisible(TASK_GROUP_IMPORT_GROUP);
                }
            }

            if (!saveEnabled) {
                setGroupTaskVisible(TASK_GROUP_EXPORT_ALL_GROUPS);
            }
        } else {
            setChannelTaskVisible(TASK_CHANNEL_EXPORT_ALL_CHANNELS);
        }

        if (rows.length > 0) {
            boolean allGroups = true;
            boolean allChannels = true;
            boolean allEnabled = true;
            boolean allDisabled = true;
            boolean channelNodeFound = false;
            boolean includesDefaultGroup = false;

            for (int row : rows) {
                TreePath path = channelTable.getPathForRow(row);

                if (path != null) {
                    AbstractChannelTableNode node = (AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent();
                    if (node.isGroupNode()) {
                        allChannels = false;

                        for (Enumeration<? extends MutableTreeTableNode> channelNodes = node.children(); channelNodes.hasMoreElements();) {
                            AbstractChannelTableNode channelNode = (AbstractChannelTableNode) channelNodes.nextElement();
                            if (channelNode.getChannelStatus().getChannel().getExportData().getMetadata().isEnabled()) {
                                allDisabled = false;
                            } else {
                                allEnabled = false;
                            }
                            channelNodeFound = true;
                        }

                        if (StringUtils.equals(node.getGroupStatus().getGroup().getId(), ChannelGroup.DEFAULT_ID)) {
                            includesDefaultGroup = true;
                        }
                    } else {
                        allGroups = false;

                        if (node.getChannelStatus().getChannel().getExportData().getMetadata().isEnabled()) {
                            allDisabled = false;
                        } else {
                            allEnabled = false;
                        }
                    }
                }
            }

            if (!allGroups || channelNodeFound) {
                if (!allDisabled) {
                    setChannelTaskVisible(TASK_CHANNEL_DISABLE);
                }
                if (!allEnabled) {
                    setChannelTaskVisible(TASK_CHANNEL_ENABLE);
                }
            }

            if (allGroups) {
                if (rows.length == 1 && !includesDefaultGroup && !filterEnabled) {
                    setGroupTaskVisible(TASK_GROUP_EDIT_DETAILS);
                }

                if (channelNodeFound && !allDisabled) {
                    setChannelTaskVisible(TASK_CHANNEL_DEPLOY);
                }

                if (!saveEnabled) {
                    setGroupTaskVisible(TASK_GROUP_EXPORT_GROUP);
                }

                if (!includesDefaultGroup && !filterEnabled) {
                    setGroupTaskVisible(TASK_GROUP_DELETE_GROUP);
                }
            } else if (allChannels) {
                if (!allDisabled) {
                    setChannelTaskVisible(TASK_CHANNEL_DEPLOY);
                }
                if (!filterEnabled && model.isGroupModeEnabled()) {
                    setGroupTaskVisible(TASK_GROUP_ASSIGN_CHANNEL);
                }
                setChannelTaskVisible(TASK_CHANNEL_EXPORT_CHANNEL);
                setChannelTaskVisible(TASK_CHANNEL_DELETE_CHANNEL);

                if (rows.length == 1) {
                    setChannelTaskVisible(TASK_CHANNEL_CLONE);
                    setChannelTaskVisible(TASK_CHANNEL_EDIT);
                    setChannelTaskVisible(TASK_CHANNEL_VIEW_MESSAGES);
                }
            } else {
                setChannelTaskVisible(TASK_CHANNEL_DEPLOY);
            }
        }
    }

    private void updateTags(boolean updateController) {
        Set<FilterCompletion> tags = new HashSet<FilterCompletion>();
        for (ChannelStatus status : channelStatuses.values()) {
            tags.add(new ChannelNameFilterCompletion(status.getChannel().getName()));
        }

        for (ChannelTag channelTag : getCachedChannelTags()) {
            tags.add(new TagFilterCompletion(channelTag));
        }

        tagField.update(tags, false, true, updateController);
    }

    public void retrieveGroups() {
        try {
            updateChannelGroups(parent.mirthClient.getAllChannelGroups());
        } catch (ClientException e) {
            updateChannelGroups(null);
            if (!(e instanceof ForbiddenException)) {
                SwingUtilities.invokeLater(() -> {
                    parent.alertThrowable(parent, e, false);
                });
            }
        }
    }

    public void retrieveChannelIdsAndNames() {
        try {
            channelIdsAndNames = parent.mirthClient.getChannelIdsAndNames();
        } catch (ClientException e) {
            SwingUtilities.invokeLater(() -> {
                parent.alertThrowable(parent, e, false);
            });
        }
    }

    public void retrieveChannels() {
        retrieveChannels(true);
    }

    public void retrieveChannels(boolean refreshTags) {
        try {
            channelIdsAndNames = parent.mirthClient.getChannelIdsAndNames();
            updateChannelStatuses(parent.mirthClient.getChannelSummary(getChannelHeaders(), false));

            try {
                updateChannelGroups(parent.mirthClient.getAllChannelGroups());
            } catch (ForbiddenException e) {
                // Ignore
                updateChannelGroups(null);
            }

            channelDependencies = parent.mirthClient.getChannelDependencies();
            updateChannelMetadata(parent.mirthClient.getChannelMetadata());

            if (refreshTags) {
                SettingsPanelTags tagsPanel = parent.getTagsPanel();
                if (tagsPanel != null) {
                    tagsPanel.refresh();
                }
            }
        } catch (ClientException e) {
            SwingUtilities.invokeLater(() -> {
                parent.alertThrowable(parent, e);
            });
        }
    }

    public void retrieveDependencies() {
        try {
            channelDependencies = parent.mirthClient.getChannelDependencies();
        } catch (ClientException e) {
            SwingUtilities.invokeLater(() -> {
                parent.alertThrowable(parent, e);
            });
        }
    }

    public Map<String, ChannelHeader> getChannelHeaders() {
        Map<String, ChannelHeader> channelHeaders = new HashMap<String, ChannelHeader>();

        for (ChannelStatus channelStatus : channelStatuses.values()) {
            Channel channel = channelStatus.getChannel();
            channelHeaders.put(channel.getId(), new ChannelHeader(channel.getRevision(), channelStatus.getDeployedDate(), channelStatus.isCodeTemplatesChanged()));
        }

        return channelHeaders;
    }

    public void updateChannelTags(List<ChannelTag> channelTags, String channelId) {
        List<ChannelTag> tagsCopy = new ArrayList<ChannelTag>(channelTags);
        Set<ChannelTag> updateTagList = new HashSet<ChannelTag>();

        for (Iterator<ChannelTag> it = tagsCopy.iterator(); it.hasNext();) {
            ChannelTag tagToUpdate = it.next();

            for (ChannelTag existingTag : getCachedChannelTags()) {
                if (existingTag.getId().equals(tagToUpdate.getId()) || existingTag.getName().equalsIgnoreCase(tagToUpdate.getName())) {
                    existingTag.getChannelIds().add(channelId);
                    it.remove();
                }

                updateTagList.add(existingTag);
            }
        }

        for (ChannelTag newTag : tagsCopy) {
            String tagName = newTag.getName();
            newTag.setName(StringUtils.substring(tagName, 0, 24));
            updateTagList.add(newTag);
        }

        SettingsPanelTags tagsPanel = parent.getTagsPanel();
        if (tagsPanel != null) {
            tagsPanel.updateTagsTable(updateTagList);
        }
    }

    public boolean doSaveGroups() {
        return doSaveGroups(true);
    }

    public boolean doSaveGroups(boolean asynchronous) {
        if (tagField.isFilterEnabled()) {
            return false;
        }

        Set<ChannelGroup> channelGroups = new HashSet<ChannelGroup>();
        Set<String> removedChannelGroupIds = new HashSet<String>(groupStatuses.keySet());
        removedChannelGroupIds.remove(ChannelGroup.DEFAULT_ID);

        MutableTreeTableNode root = (MutableTreeTableNode) channelTable.getTreeTableModel().getRoot();
        if (root == null) {
            return false;
        }

        for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
            ChannelGroup group = ((AbstractChannelTableNode) groupNodes.nextElement()).getGroupStatus().getGroup();
            if (!StringUtils.equals(group.getId(), ChannelGroup.DEFAULT_ID)) {
                channelGroups.add(group);
                removedChannelGroupIds.remove(group.getId());
            }

            if (StringUtils.isBlank(group.getName())) {
                parent.alertError(parent, Messages.getString("ChannelPanel.116")); //$NON-NLS-1$
                return false;
            }
        }

        if (asynchronous) {
            new UpdateSwingWorker(channelGroups, removedChannelGroupIds, false).execute();
        } else {
            return attemptUpdate(channelGroups, removedChannelGroupIds, false);
        }

        tagField.setEnabled(true);
        filterLabel.setEnabled(true);

        return true;
    }

    private boolean attemptUpdate(Set<ChannelGroup> channelGroups, Set<String> removedChannelGroupIds, boolean override) {
        boolean result = false;
        boolean tryAgain = false;

        try {
            result = updateGroups(channelGroups, removedChannelGroupIds, override);

            if (result) {
                afterUpdate();
            } else {
                if (!override) {
                    if (parent.alertOption(parent, Messages.getString("ChannelPanel.117"))) { //$NON-NLS-1$
                        tryAgain = true;
                    }
                } else {
                    parent.alertError(parent, Messages.getString("ChannelPanel.118")); //$NON-NLS-1$
                }
            }
        } catch (Exception e) {
            Throwable cause = e;
            if (cause instanceof ExecutionException) {
                cause = e.getCause();
            }
            parent.alertThrowable(parent, cause, Messages.getString("ChannelPanel.119") + cause.getMessage()); //$NON-NLS-1$
        }

        if (tryAgain && !override) {
            return attemptUpdate(channelGroups, removedChannelGroupIds, true);
        }

        return result;
    }

    private boolean updateGroups(Set<ChannelGroup> channelGroups, Set<String> removedChannelGroupIds, boolean override) throws ClientException {
        return parent.mirthClient.updateChannelGroups(channelGroups, removedChannelGroupIds, override);
    }

    private void afterUpdate() {
        parent.setSaveEnabled(false);
        doRefreshChannels();
    }

    public class UpdateSwingWorker extends SwingWorker<Boolean, Void> {

        private Set<ChannelGroup> channelGroups;
        private Set<String> removedChannelGroupIds;
        private boolean override;
        private String workingId;

        public UpdateSwingWorker(Set<ChannelGroup> channelGroups, Set<String> removedChannelGroupIds, boolean override) {
            this.channelGroups = channelGroups;
            this.removedChannelGroupIds = removedChannelGroupIds;
            this.override = override;
            workingId = parent.startWorking(Messages.getString("ChannelPanel.120")); //$NON-NLS-1$
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            return updateGroups(channelGroups, removedChannelGroupIds, override);
        }

        @Override
        protected void done() {
            boolean tryAgain = false;

            try {
                Boolean result = get();

                if (result) {
                    afterUpdate();
                } else {
                    if (!override) {
                        if (parent.alertOption(parent, Messages.getString("ChannelPanel.121"))) { //$NON-NLS-1$
                            tryAgain = true;
                        }
                    } else {
                        parent.alertError(parent, Messages.getString("ChannelPanel.122")); //$NON-NLS-1$
                    }
                }
            } catch (Exception e) {
                Throwable cause = e;
                if (cause instanceof ExecutionException) {
                    cause = e.getCause();
                }
                parent.alertThrowable(parent, cause, Messages.getString("ChannelPanel.123") + cause.getMessage()); //$NON-NLS-1$
            } finally {
                parent.stopWorking(workingId);

                if (tryAgain && !override) {
                    new UpdateSwingWorker(channelGroups, removedChannelGroupIds, true).execute();
                }
            }
        }
    }

    public void doRedeployAll() {
        if (!parent.alertOption(parent, Messages.getString("ChannelPanel.124"))) { //$NON-NLS-1$
            return;
        }

        final String workingId = parent.startWorking(Messages.getString("ChannelPanel.125")); //$NON-NLS-1$
        parent.dashboardPanel.deselectRows(false);
        parent.doShowDashboard();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                try {
                    parent.mirthClient.redeployAllChannels();
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        parent.alertThrowable(parent, e);
                    });
                }
                return null;
            }

            @Override
            public void done() {
                parent.stopWorking(workingId);
                parent.doRefreshStatuses(true);
            }
        };

        worker.execute();
    }

    public void doDeployChannel() {
        List<Channel> selectedChannels = getSelectedChannels();
        if (selectedChannels.size() == 0) {
            parent.alertWarning(parent, Messages.getString("ChannelPanel.126")); //$NON-NLS-1$
            return;
        }

        // Only deploy enabled channels
        final Set<String> selectedEnabledChannelIds = new LinkedHashSet<String>();
        boolean channelDisabled = false;
        for (Channel channel : selectedChannels) {
            if (channel.getExportData().getMetadata().isEnabled()) {
                selectedEnabledChannelIds.add(channel.getId());
            } else {
                channelDisabled = true;
            }
        }

        if (channelDisabled) {
            parent.alertWarning(parent, Messages.getString("ChannelPanel.127")); //$NON-NLS-1$
        }

        // If there are any channel dependencies, decide if we need to warn the user on deploy.
        try {
            ChannelDependencyGraph channelDependencyGraph = new ChannelDependencyGraph(channelDependencies);

            Set<String> deployedChannelIds = new HashSet<String>();
            if (parent.status != null) {
                for (DashboardStatus dashboardStatus : parent.status) {
                    deployedChannelIds.add(dashboardStatus.getChannelId());
                }
            }

            // For each selected channel, add any dependent/dependency channels as necessary
            Set<String> channelIdsToDeploy = new HashSet<String>();
            for (String channelId : selectedEnabledChannelIds) {
                addChannelToDeploySet(channelId, channelDependencyGraph, deployedChannelIds, channelIdsToDeploy);
            }

            // If additional channels were added to the set, we need to prompt the user
            if (!CollectionUtils.subtract(channelIdsToDeploy, selectedEnabledChannelIds).isEmpty()) {
                ChannelDependenciesWarningDialog dialog = new ChannelDependenciesWarningDialog(ChannelTask.DEPLOY, channelDependencies, selectedEnabledChannelIds, channelIdsToDeploy);
                if (dialog.getResult() == JOptionPane.OK_OPTION) {
                    if (dialog.isIncludeOtherChannels()) {
                        selectedEnabledChannelIds.addAll(channelIdsToDeploy);
                    }
                } else {
                    return;
                }
            }
        } catch (ChannelDependencyException e) {
            // Should never happen
            e.printStackTrace();
        }

        parent.deployChannel(selectedEnabledChannelIds);
    }

    private void addChannelToDeploySet(String channelId, ChannelDependencyGraph channelDependencyGraph, Set<String> deployedChannelIds, Set<String> channelIdsToDeploy) {
        if (!channelIdsToDeploy.add(channelId)) {
            return;
        }

        DirectedAcyclicGraphNode<String> node = channelDependencyGraph.getNode(channelId);

        if (node != null) {
            for (String dependentChannelId : node.getDirectDependentElements()) {
                ChannelStatus channelStatus = channelStatuses.get(dependentChannelId);

                // Only add the dependent channel if it's enabled and currently deployed
                if (channelStatus != null && channelStatus.getChannel().getExportData().getMetadata().isEnabled() && deployedChannelIds.contains(dependentChannelId)) {
                    addChannelToDeploySet(dependentChannelId, channelDependencyGraph, deployedChannelIds, channelIdsToDeploy);
                }
            }

            for (String dependencyChannelId : node.getDirectDependencyElements()) {
                ChannelStatus channelStatus = channelStatuses.get(dependencyChannelId);

                // Only add the dependency channel it it's enabled
                if (channelStatus != null && channelStatus.getChannel().getExportData().getMetadata().isEnabled()) {
                    addChannelToDeploySet(dependencyChannelId, channelDependencyGraph, deployedChannelIds, channelIdsToDeploy);
                }
            }
        }
    }

    public void doEditGlobalScripts() {
        if (isSaveEnabled() && !confirmLeave()) {
            return;
        }

        parent.doEditGlobalScripts();
    }

    public void doEditCodeTemplates() {
        if (isSaveEnabled() && !confirmLeave()) {
            return;
        }

        parent.doEditCodeTemplates();
    }

    public void doNewGroup() {
        if (tagField.isFilterEnabled()) {
            return;
        }

        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();
        MutableTreeTableNode root = (MutableTreeTableNode) model.getRoot();
        if (root == null) {
            return;
        }

        GroupDetailsDialog dialog = new GroupDetailsDialog(true);

        if (dialog.wasSaved()) {
            AbstractChannelTableNode groupNode = model.addNewGroup(new ChannelGroup(dialog.getGroupName(), dialog.getGroupDescription()));

            parent.setSaveEnabled(true);

            final TreePath path = new TreePath(new Object[] { root, groupNode });
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    channelTable.getTreeSelectionModel().setSelectionPath(path);
                }
            });
        }

        tagField.setEnabled(false);
        filterLabel.setEnabled(false);
    }

    private boolean checkGroupId(String id) {
        MutableTreeTableNode root = (MutableTreeTableNode) channelTable.getTreeTableModel().getRoot();
        if (root == null) {
            return false;
        }

        for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
            if (StringUtils.equals(((AbstractChannelTableNode) groupNodes.nextElement()).getGroupStatus().getGroup().getId(), id)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkGroupName(String name) {
        return checkGroupName(name, true);
    }

    private boolean checkGroupName(String name, boolean includeSelectedRow) {
        MutableTreeTableNode root = (MutableTreeTableNode) channelTable.getTreeTableModel().getRoot();
        if (root == null) {
            return false;
        }

        for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
            AbstractChannelTableNode groupNode = (AbstractChannelTableNode) groupNodes.nextElement();

            if (!includeSelectedRow && channelTable.getSelectedRow() != -1) {
                AbstractChannelTableNode selectedNode = (AbstractChannelTableNode) channelTable.getPathForRow(channelTable.getSelectedRow()).getLastPathComponent();
                if (selectedNode.isGroupNode() && selectedNode.getGroupStatus().getGroup().getId().equals(groupNode.getGroupStatus().getGroup().getId())) {
                    continue;
                }
            }

            if (StringUtils.equals(groupNode.getGroupStatus().getGroup().getName(), name)) {
                return false;
            }
        }

        return true;
    }

    public void doAssignChannelToGroup() {
        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();
        int[] rows = channelTable.getSelectedModelRows();

        if (model.isGroupModeEnabled() && rows.length > 0) {
            for (int row : rows) {
                if (((AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent()).isGroupNode()) {
                    return;
                }
            }

            GroupAssignmentDialog dialog = new GroupAssignmentDialog();

            if (dialog.wasSaved()) {
                AbstractChannelTableNode groupNode = null;
                for (Enumeration<? extends MutableTreeTableNode> groupNodes = ((MutableTreeTableNode) model.getRoot()).children(); groupNodes.hasMoreElements();) {
                    AbstractChannelTableNode node = (AbstractChannelTableNode) groupNodes.nextElement();
                    if (StringUtils.equals(node.getGroupStatus().getGroup().getId(), dialog.getSelectedGroupId())) {
                        groupNode = node;
                        break;
                    }
                }

                if (groupNode != null) {
                    TableState tableState = getCurrentTableState();
                    tableState.getExpandedGroupIds().add(groupNode.getGroupStatus().getGroup().getId());

                    ListSelectionListener[] listeners = ((DefaultListSelectionModel) channelTable.getSelectionModel()).getListSelectionListeners();
                    for (ListSelectionListener listener : listeners) {
                        channelTable.getSelectionModel().removeListSelectionListener(listener);
                    }

                    try {
                        List<AbstractChannelTableNode> channelNodes = new ArrayList<AbstractChannelTableNode>();
                        for (int row : rows) {
                            channelNodes.add((AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent());
                        }
                        for (AbstractChannelTableNode channelNode : channelNodes) {
                            model.addChannelToGroup(groupNode, channelNode.getChannelStatus().getChannel().getId());
                        }
                        channelTable.expandPath(new TreePath(new Object[] {
                                channelTable.getTreeTableModel().getRoot(), groupNode }));

                        parent.setSaveEnabled(true);
                    } finally {
                        for (ListSelectionListener listener : listeners) {
                            channelTable.getSelectionModel().addListSelectionListener(listener);
                        }

                        restoreTableState(tableState);
                    }
                }
            }
        }
    }

    public void doEditGroupDetails() {
        if (tagField.isFilterEnabled()) {
            return;
        }

        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();
        MutableTreeTableNode root = (MutableTreeTableNode) model.getRoot();
        if (root == null) {
            return;
        }

        int[] rows = channelTable.getSelectedModelRows();

        if (rows.length == 1) {
            AbstractChannelTableNode node = (AbstractChannelTableNode) channelTable.getPathForRow(rows[0]).getLastPathComponent();

            if (node.isGroupNode() && !StringUtils.equals(node.getGroupStatus().getGroup().getId(), ChannelGroup.DEFAULT_ID)) {
                GroupDetailsDialog dialog = new GroupDetailsDialog(false);

                if (dialog.wasSaved()) {
                    channelTable.getTreeTableModel().setValueAt(new ChannelTableNameEntry(dialog.getGroupName()), node, NAME_COLUMN_NUMBER);
                    channelTable.getTreeTableModel().setValueAt(dialog.getGroupDescription(), node, DESCRIPTION_COLUMN_NUMBER);

                    parent.setSaveEnabled(true);
                }
            }
        }
    }

    public void doNewChannel() {
        if (isSaveEnabled() && !promptSave(true)) {
            return;
        }

        if (LoadedExtensions.getInstance().getSourceConnectors().size() == 0 || LoadedExtensions.getInstance().getDestinationConnectors().size() == 0) {
            parent.alertError(parent, Messages.getString("ChannelPanel.128")); //$NON-NLS-1$
            return;
        }

        // The channel wizard will call createNewChannel() or create a channel
        // from a wizard.
        new ChannelWizard();
    }

    public void createNewChannel() {
        if (isSaveEnabled() && !promptSave(true)) {
            return;
        }

        Channel channel = new Channel();

        try {
            channel.setId(parent.mirthClient.getGuid());
        } catch (ClientException e) {
            parent.alertThrowable(parent, e);
        }

        channel.setName(Messages.getString("ChannelPanel.129")); //$NON-NLS-1$

        Set<String> selectedGroupIds = new HashSet<String>();

        if (((ChannelTreeTableModel) channelTable.getTreeTableModel()).isGroupModeEnabled()) {
            for (int row : channelTable.getSelectedModelRows()) {
                TreePath path = channelTable.getPathForRow(row);
                if (path != null) {
                    AbstractChannelTableNode node = (AbstractChannelTableNode) path.getLastPathComponent();
                    if (node.isGroupNode()) {
                        selectedGroupIds.add(node.getGroupStatus().getGroup().getId());
                    } else if (node.getParent() instanceof AbstractChannelTableNode) {
                        node = (AbstractChannelTableNode) node.getParent();
                        if (node.isGroupNode()) {
                            selectedGroupIds.add(node.getGroupStatus().getGroup().getId());
                        }
                    }
                }
            }
        }

        parent.setupChannel(channel, selectedGroupIds.size() == 1 ? selectedGroupIds.iterator().next() : null);
    }

    public void addChannelToGroup(String channelId, String groupId) {
        Set<ChannelGroup> channelGroups = new HashSet<ChannelGroup>();

        for (ChannelGroupStatus groupStatus : groupStatuses.values()) {
            ChannelGroup group = groupStatus.getGroup();

            if (!group.getId().equals(ChannelGroup.DEFAULT_ID)) {
                if (group.getId().equals(groupId)) {
                    group.getChannels().add(new Channel(channelId));
                }
                channelGroups.add(group);
            }
        }

        new UpdateSwingWorker(channelGroups, new HashSet<String>(), false).execute();
    }

    public void doImportGroup() {
        if ((isSaveEnabled() && !promptSave(true)) || tagField.isFilterEnabled()) {
            return;
        }

        String content = parent.browseForFileString(Messages.getString("ChannelPanel.130")); //$NON-NLS-1$

        if (content != null) {
            importGroup(content, true);
        }
    }

    public void importGroup(String content, boolean showAlerts) {
        if ((showAlerts && !parent.promptObjectMigration(content, Messages.getString("ChannelPanel.131"))) || tagField.isFilterEnabled()) { //$NON-NLS-1$
            return;
        }

        ChannelGroup importGroup = null;

        try {
            importGroup = ObjectXMLSerializer.getInstance().deserialize(content, ChannelGroup.class);
        } catch (Exception e) {
            if (showAlerts) {
                parent.alertThrowable(parent, e, Messages.getString("ChannelPanel.132") + e.getMessage()); //$NON-NLS-1$
            }
            return;
        }

        importGroup(importGroup, showAlerts);
    }

    public void importGroup(ChannelGroup importGroup, boolean showAlerts) {
        importGroup(importGroup, showAlerts, false);
    }

    public void importGroup(ChannelGroup importGroup, boolean showAlerts, boolean synchronous) {
        // First consolidate and import code template libraries
        Map<String, CodeTemplateLibrary> codeTemplateLibraryMap = new LinkedHashMap<String, CodeTemplateLibrary>();
        Set<String> codeTemplateIds = new HashSet<String>();

        for (Channel channel : importGroup.getChannels()) {
            if (channel.getExportData() != null && channel.getExportData().getCodeTemplateLibraries() != null) {
                for (CodeTemplateLibrary library : channel.getExportData().getCodeTemplateLibraries()) {
                    CodeTemplateLibrary matchingLibrary = codeTemplateLibraryMap.get(library.getId());

                    if (matchingLibrary != null) {
                        for (CodeTemplate codeTemplate : library.getCodeTemplates()) {
                            if (codeTemplateIds.add(codeTemplate.getId())) {
                                matchingLibrary.getCodeTemplates().add(codeTemplate);
                            }
                        }
                    } else {
                        matchingLibrary = library;
                        codeTemplateLibraryMap.put(matchingLibrary.getId(), matchingLibrary);

                        List<CodeTemplate> codeTemplates = new ArrayList<CodeTemplate>();
                        for (CodeTemplate codeTemplate : matchingLibrary.getCodeTemplates()) {
                            if (codeTemplateIds.add(codeTemplate.getId())) {
                                codeTemplates.add(codeTemplate);
                            }
                        }
                        matchingLibrary.setCodeTemplates(codeTemplates);
                    }

                    // Combine the library enabled / disabled channel IDs
                    matchingLibrary.getEnabledChannelIds().addAll(library.getEnabledChannelIds());
                    matchingLibrary.getEnabledChannelIds().add(channel.getId());
                    matchingLibrary.getDisabledChannelIds().addAll(library.getDisabledChannelIds());
                    matchingLibrary.getDisabledChannelIds().removeAll(matchingLibrary.getEnabledChannelIds());
                }

                channel.getExportData().clearCodeTemplateLibraries();
            }
        }

        List<CodeTemplateLibrary> codeTemplateLibraries = new ArrayList<CodeTemplateLibrary>(codeTemplateLibraryMap.values());

        parent.removeInvalidItems(codeTemplateLibraries, CodeTemplateLibrary.class);
        if (CollectionUtils.isNotEmpty(codeTemplateLibraries)) {
            boolean importLibraries;
            String importChannelCodeTemplateLibraries = userPreferences.get(Messages.getString("ChannelPanel.133"), null); //$NON-NLS-1$

            if (importChannelCodeTemplateLibraries == null) {
                JCheckBox alwaysChooseCheckBox = new JCheckBox(Messages.getString("ChannelPanel.134")); //$NON-NLS-1$
                Object[] params = new Object[] {
                        Messages.getString("ChannelPanel.135") + importGroup.getName() + Messages.getString("ChannelPanel.136"), //$NON-NLS-1$ //$NON-NLS-2$
                        alwaysChooseCheckBox };
                int result = JOptionPane.showConfirmDialog(this, params, Messages.getString("ChannelPanel.137"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$

                if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
                    importLibraries = result == JOptionPane.YES_OPTION;
                    if (alwaysChooseCheckBox.isSelected()) {
                        userPreferences.putBoolean(Messages.getString("ChannelPanel.138"), importLibraries); //$NON-NLS-1$
                    }
                } else {
                    return;
                }
            } else {
                importLibraries = Boolean.parseBoolean(importChannelCodeTemplateLibraries);
            }

            if (importLibraries) {
                CodeTemplateImportDialog dialog = new CodeTemplateImportDialog(parent, codeTemplateLibraries, false, true);

                if (dialog.wasSaved()) {
                    CodeTemplateLibrarySaveResult updateSummary = parent.codeTemplatePanel.attemptUpdate(dialog.getUpdatedLibraries(), new HashMap<String, CodeTemplateLibrary>(), dialog.getUpdatedCodeTemplates(), new HashMap<String, CodeTemplate>(), true, null, null);

                    if (updateSummary == null || updateSummary.isOverrideNeeded() || !updateSummary.isLibrariesSuccess()) {
                        return;
                    } else {
                        for (CodeTemplateUpdateResult result : updateSummary.getCodeTemplateResults().values()) {
                            if (!result.isSuccess()) {
                                return;
                            }
                        }
                    }

                    parent.codeTemplatePanel.doRefreshCodeTemplates();
                }
            }
        }

        List<Channel> successfulChannels = new ArrayList<Channel>();
        for (Channel channel : importGroup.getChannels()) {
            Channel importChannel = importChannel(channel, false, false);
            if (importChannel != null) {
                successfulChannels.add(importChannel);
            }
        }

        if (!StringUtils.equals(importGroup.getId(), ChannelGroup.DEFAULT_ID)) {
            ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();
            AbstractChannelTableNode importGroupNode = null;

            String groupName = importGroup.getName();
            String tempId;
            try {
                tempId = parent.mirthClient.getGuid();
            } catch (ClientException e) {
                tempId = UUID.randomUUID().toString();
            }

            // Check to see that the channel name doesn't already exist.
            if (!checkGroupName(groupName)) {
                if (!parent.alertOption(parent, Messages.getString("ChannelPanel.139"))) { //$NON-NLS-1$
                    importGroup.setRevision(0);

                    do {
                        groupName = DisplayUtil.showInputDialog(this, Messages.getString("ChannelPanel.140"), groupName); //$NON-NLS-1$
                        if (groupName == null) {
                            return;
                        }
                    } while (!checkGroupName(groupName));

                    importGroup.setId(tempId);
                    importGroup.setName(groupName);
                } else {
                    MutableTreeTableNode root = (MutableTreeTableNode) model.getRoot();
                    for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
                        AbstractChannelTableNode groupNode = (AbstractChannelTableNode) groupNodes.nextElement();

                        if (StringUtils.equals(groupNode.getGroupStatus().getGroup().getName(), groupName)) {
                            importGroupNode = groupNode;
                        }
                    }
                }
            } else {
                // Start the revision number over for a new channel group
                importGroup.setRevision(0);

                // If the channel name didn't already exist, make sure
                // the id doesn't exist either.
                if (!checkGroupId(importGroup.getId())) {
                    importGroup.setId(tempId);
                }
            }

            Set<ChannelGroup> channelGroups = new HashSet<ChannelGroup>();
            Set<String> removedChannelGroupIds = new HashSet<String>(groupStatuses.keySet());
            removedChannelGroupIds.remove(ChannelGroup.DEFAULT_ID);

            MutableTreeTableNode root = (MutableTreeTableNode) channelTable.getTreeTableModel().getRoot();
            if (root == null) {
                return;
            }

            for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
                ChannelGroup group = ((AbstractChannelTableNode) groupNodes.nextElement()).getGroupStatus().getGroup();

                if (!StringUtils.equals(group.getId(), ChannelGroup.DEFAULT_ID)) {
                    // If the current group is the one we're overwriting, merge the channels
                    if (importGroupNode != null && StringUtils.equals(group.getId(), importGroupNode.getGroupStatus().getGroup().getId())) {
                        group = importGroup;
                        group.setRevision(importGroupNode.getGroupStatus().getGroup().getRevision());

                        Set<String> channelIds = new HashSet<String>();
                        for (Channel channel : group.getChannels()) {
                            channelIds.add(channel.getId());
                        }

                        // Add the imported channels
                        for (Channel channel : successfulChannels) {
                            channelIds.add(channel.getId());
                        }

                        List<Channel> channels = new ArrayList<Channel>();
                        for (String channelId : channelIds) {
                            channels.add(new Channel(channelId));
                        }
                        group.setChannels(channels);
                    }

                    channelGroups.add(group);
                    removedChannelGroupIds.remove(group.getId());
                }
            }

            if (importGroupNode == null) {
                List<Channel> channels = new ArrayList<Channel>();
                for (Channel channel : successfulChannels) {
                    channels.add(new Channel(channel.getId()));
                }
                importGroup.setChannels(channels);

                channelGroups.add(importGroup);
                removedChannelGroupIds.remove(importGroup.getId());
            }

            Set<String> channelIds = new HashSet<String>();
            for (Channel channel : importGroup.getChannels()) {
                channelIds.add(channel.getId());
            }

            for (ChannelGroup group : channelGroups) {
                if (group != importGroup) {
                    for (Iterator<Channel> channels = group.getChannels().iterator(); channels.hasNext();) {
                        if (!channelIds.add(channels.next().getId())) {
                            channels.remove();
                        }
                    }
                }
            }

            attemptUpdate(channelGroups, removedChannelGroupIds, false);
        }

        if (synchronous) {
            retrieveChannels();
            updateModel(getCurrentTableState());
            updateTasks();
            parent.setSaveEnabled(false);
        } else {
            doRefreshChannels();
        }
    }

    public void doImportChannel() {
        if (isSaveEnabled() && !promptSave(true)) {
            return;
        }

        String content = parent.browseForFileString(Messages.getString("ChannelPanel.141")); //$NON-NLS-1$

        if (content != null) {
            importChannel(content, true);
        }
    }

    public void importChannel(String content, boolean showAlerts) {
        if (showAlerts && !parent.promptObjectMigration(content, Messages.getString("ChannelPanel.142"))) { //$NON-NLS-1$
            return;
        }

        Channel importChannel = null;

        try {
            importChannel = ObjectXMLSerializer.getInstance().deserialize(content, Channel.class);
        } catch (Exception e) {
            if (showAlerts) {
                parent.alertThrowable(parent, e, Messages.getString("ChannelPanel.143") + e.getMessage()); //$NON-NLS-1$
            }

            return;
        }

        importChannel(importChannel, showAlerts);
    }

    public Channel importChannel(Channel importChannel, boolean showAlerts) {
        return importChannel(importChannel, showAlerts, true);
    }

    public Channel importChannel(Channel importChannel, boolean showAlerts, boolean refreshStatuses) {
        boolean overwrite = false;

        try {
            String channelName = importChannel.getName();
            String tempId = parent.mirthClient.getGuid();

            // Check to see that the channel name doesn't already exist.
            if (!parent.checkChannelName(channelName, tempId)) {
                if (!parent.alertOption(parent, Messages.getString("ChannelPanel.144"))) { //$NON-NLS-1$
                    importChannel.setRevision(0);

                    do {
                        channelName = DisplayUtil.showInputDialog(this, Messages.getString("ChannelPanel.145"), channelName); //$NON-NLS-1$
                        if (channelName == null) {
                            return null;
                        }
                    } while (!parent.checkChannelName(channelName, tempId));

                    importChannel.setName(channelName);
                    setIdAndUpdateLibraries(importChannel, tempId);
                } else {
                    overwrite = true;

                    for (ChannelStatus channelStatus : channelStatuses.values()) {
                        Channel channel = channelStatus.getChannel();
                        if (channel.getName().equalsIgnoreCase(channelName)) {
                            // If overwriting, use the old revision number and id
                            importChannel.setRevision(channel.getRevision());
                            setIdAndUpdateLibraries(importChannel, channel.getId());
                        }
                    }
                }
            } else {
                // Start the revision number over for a new channel
                importChannel.setRevision(0);

                // If the channel name didn't already exist, make sure
                // the id doesn't exist either.
                if (!checkChannelId(importChannel.getId())) {
                    setIdAndUpdateLibraries(importChannel, tempId);
                }

            }

            channelStatuses.put(importChannel.getId(), new ChannelStatus(importChannel));

            List<ChannelTag> channelTags = (List<ChannelTag>) importChannel.getExportData().getChannelTags();
            if (channelTags != null && !channelTags.isEmpty()) {
                updateChannelTags(channelTags, importChannel.getId());
            }
        } catch (ClientException e) {
            parent.alertThrowable(parent, e);
        }

        if (!(importChannel instanceof InvalidChannel)) {
            // Import code templates / libraries if applicable
            parent.removeInvalidItems(importChannel.getExportData().getCodeTemplateLibraries(), CodeTemplateLibrary.class);
            if (!(importChannel instanceof InvalidChannel) && !importChannel.getExportData().getCodeTemplateLibraries().isEmpty()) {
                boolean importLibraries;
                String importChannelCodeTemplateLibraries = userPreferences.get(Messages.getString("ChannelPanel.146"), null); //$NON-NLS-1$

                if (importChannelCodeTemplateLibraries == null) {
                    JCheckBox alwaysChooseCheckBox = new JCheckBox(Messages.getString("ChannelPanel.147")); //$NON-NLS-1$
                    Object[] params = new Object[] {
                            Messages.getString("ChannelPanel.148") + importChannel.getName() + Messages.getString("ChannelPanel.149"), //$NON-NLS-1$ //$NON-NLS-2$
                            alwaysChooseCheckBox };
                    int result = JOptionPane.showConfirmDialog(this, params, Messages.getString("ChannelPanel.150"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$

                    if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
                        importLibraries = result == JOptionPane.YES_OPTION;
                        if (alwaysChooseCheckBox.isSelected()) {
                            userPreferences.putBoolean(Messages.getString("ChannelPanel.151"), importLibraries); //$NON-NLS-1$
                        }
                    } else {
                        return null;
                    }
                } else {
                    importLibraries = Boolean.parseBoolean(importChannelCodeTemplateLibraries);
                }

                if (importLibraries) {
                    CodeTemplateImportDialog dialog = new CodeTemplateImportDialog(parent, importChannel.getExportData().getCodeTemplateLibraries(), false, true);

                    if (dialog.wasSaved()) {
                        CodeTemplateLibrarySaveResult updateSummary = parent.codeTemplatePanel.attemptUpdate(dialog.getUpdatedLibraries(), new HashMap<String, CodeTemplateLibrary>(), dialog.getUpdatedCodeTemplates(), new HashMap<String, CodeTemplate>(), true, null, null);

                        if (updateSummary == null || updateSummary.isOverrideNeeded() || !updateSummary.isLibrariesSuccess()) {
                            return null;
                        } else {
                            for (CodeTemplateUpdateResult result : updateSummary.getCodeTemplateResults().values()) {
                                if (!result.isSuccess()) {
                                    return null;
                                }
                            }
                        }

                        parent.codeTemplatePanel.doRefreshCodeTemplates();
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(importChannel.getExportData().getDependentIds()) || CollectionUtils.isNotEmpty(importChannel.getExportData().getDependencyIds())) {
                Set<ChannelDependency> channelDependencies = new HashSet<ChannelDependency>(getCachedChannelDependencies());

                if (CollectionUtils.isNotEmpty(importChannel.getExportData().getDependentIds())) {
                    for (String dependentId : importChannel.getExportData().getDependentIds()) {
                        if (StringUtils.isNotBlank(dependentId) && !StringUtils.equals(dependentId, importChannel.getId())) {
                            channelDependencies.add(new ChannelDependency(dependentId, importChannel.getId()));
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(importChannel.getExportData().getDependencyIds())) {
                    for (String dependencyId : importChannel.getExportData().getDependencyIds()) {
                        if (StringUtils.isNotBlank(dependencyId) && !StringUtils.equals(dependencyId, importChannel.getId())) {
                            channelDependencies.add(new ChannelDependency(importChannel.getId(), dependencyId));
                        }
                    }
                }

                if (!channelDependencies.equals(getCachedChannelDependencies())) {
                    try {
                        parent.mirthClient.setChannelDependencies(channelDependencies);
                    } catch (ClientException e) {
                        parent.alertThrowable(parent, e, Messages.getString("ChannelPanel.152")); //$NON-NLS-1$
                    }
                }
            }

            importChannel.getExportData().clearCodeTemplateLibraries();
            importChannel.getExportData().clearDependencies();

            // Update resource names
            parent.updateResourceNames(importChannel);
        }

        /*
         * Update the channel if we're overwriting an imported channel, if we're not showing alerts
         * (dragging/dropping multiple channels), or if we're working with an invalid channel.
         */
        if (overwrite || !showAlerts || importChannel instanceof InvalidChannel) {
            try {
                parent.updateChannel(importChannel, overwrite);

                if (importChannel instanceof InvalidChannel && showAlerts) {
                    InvalidChannel invalidChannel = (InvalidChannel) importChannel;
                    Throwable cause = invalidChannel.getCause();
                    parent.alertThrowable(parent, cause, Messages.getString("ChannelPanel.153") + importChannel.getName() + Messages.getString("ChannelPanel.154") + getMissingExtensions(invalidChannel) + Messages.getString("ChannelPanel.155") + cause.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
            } catch (Exception e) {
                channelStatuses.remove(importChannel.getId());
                parent.alertThrowable(parent, e);
                return null;
            } finally {
                if (refreshStatuses) {
                    doRefreshChannels();
                }
            }
        }

        if (showAlerts) {
            final Channel importChannelFinal = importChannel;
            final boolean overwriteFinal = overwrite;

            /*
             * MIRTH-2048 - This is a hack to fix the memory access error that only occurs on OS X.
             * The block of code that edits the channel needs to be invoked later so that the screen
             * does not change before the drag/drop action of a channel finishes.
             */
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        parent.editChannel(importChannelFinal);
                        parent.setSaveEnabled(!overwriteFinal);
                    } catch (Exception e) {
                        channelStatuses.remove(importChannelFinal.getId());
                        parent.alertError(parent, Messages.getString("ChannelPanel.156")); //$NON-NLS-1$
                        parent.channelEditPanel = new ChannelSetup();
                        parent.doShowChannel();
                    }
                }

            });
        }

        return importChannel;
    }

    private void setIdAndUpdateLibraries(Channel channel, String newChannelId) {
        if (CollectionUtils.isNotEmpty(channel.getExportData().getCodeTemplateLibraries())) {
            for (CodeTemplateLibrary library : channel.getExportData().getCodeTemplateLibraries()) {
                library.getEnabledChannelIds().remove(channel.getId());
                library.getEnabledChannelIds().add(newChannelId);
            }
        }
        channel.setId(newChannelId);
    }

    public void doExportAllChannels() {
        if (isSaveEnabled() && !promptSave(true)) {
            return;
        }

        if (!channelStatuses.isEmpty()) {
            List<Channel> selectedChannels = new ArrayList<Channel>();
            for (ChannelStatus channelStatus : channelStatuses.values()) {
                selectedChannels.add(channelStatus.getChannel());
            }
            exportChannels(selectedChannels);
        }
    }

    public boolean doExportChannel() {
        if (isSaveEnabled() && !promptSave(true)) {
            return false;
        }

        if (isGroupSelected()) {
            JOptionPane.showMessageDialog(parent, Messages.getString("ChannelPanel.157")); //$NON-NLS-1$
            return false;
        }

        if (parent.changesHaveBeenMade()) {
            if (parent.alertOption(this, Messages.getString("ChannelPanel.158"))) { //$NON-NLS-1$
                if (!parent.channelEditPanel.saveChanges()) {
                    return false;
                }
            } else {
                return false;
            }

            parent.setSaveEnabled(false);
        }

        Channel channel;
        if (parent.currentContentPage == parent.channelEditPanel || parent.currentContentPage == parent.channelEditPanel.filterPane || parent.currentContentPage == parent.channelEditPanel.transformerPane) {
            channel = parent.channelEditPanel.currentChannel;
        } else {
            List<Channel> selectedChannels = getSelectedChannels();
            if (selectedChannels.size() > 1) {
                exportChannels(selectedChannels);
                return true;
            }
            channel = selectedChannels.get(0);
        }

        // Add code template libraries if necessary
        if (channelHasLinkedCodeTemplates(channel)) {
            boolean addLibraries = true;
            String exportChannelCodeTemplateLibraries = userPreferences.get(Messages.getString("ChannelPanel.159"), null); //$NON-NLS-1$

            if (exportChannelCodeTemplateLibraries == null) {
                ExportChannelLibrariesDialog dialog = new ExportChannelLibrariesDialog(channel);
                if (dialog.getResult() == JOptionPane.NO_OPTION) {
                    addLibraries = false;
                } else if (dialog.getResult() != JOptionPane.YES_OPTION) {
                    return false;
                }
            } else {
                addLibraries = Boolean.parseBoolean(exportChannelCodeTemplateLibraries);
            }

            if (addLibraries) {
                addCodeTemplateLibrariesToChannel(channel);
            }
        }

        addDependenciesToChannel(channel);
        addTagsToChannel(channel);

        // Update resource names
        parent.updateResourceNames(channel);

        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        String channelXML = serializer.serialize(channel);
        // Reset the libraries on the cached channel
        channel.getExportData().clearAllExceptMetadata();

        return parent.exportFile(channelXML, channel.getName(), Messages.getString("ChannelPanel.160"), Messages.getString("ChannelPanel.161")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private void exportChannels(List<Channel> channelList) {
        if (channelHasLinkedCodeTemplates(channelList)) {
            boolean addLibraries;
            String exportChannelCodeTemplateLibraries = userPreferences.get(Messages.getString("ChannelPanel.162"), null); //$NON-NLS-1$

            if (exportChannelCodeTemplateLibraries == null) {
                JCheckBox alwaysChooseCheckBox = new JCheckBox(Messages.getString("ChannelPanel.163")); //$NON-NLS-1$
                Object[] params = new Object[] {
                        Messages.getString("ChannelPanel.164"), //$NON-NLS-1$
                        alwaysChooseCheckBox };
                int result = JOptionPane.showConfirmDialog(this, params, Messages.getString("ChannelPanel.165"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$

                if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
                    addLibraries = result == JOptionPane.YES_OPTION;
                    if (alwaysChooseCheckBox.isSelected()) {
                        userPreferences.putBoolean(Messages.getString("ChannelPanel.166"), addLibraries); //$NON-NLS-1$
                    }
                } else {
                    return;
                }
            } else {
                addLibraries = Boolean.parseBoolean(exportChannelCodeTemplateLibraries);
            }

            if (addLibraries) {
                for (Channel channel : channelList) {
                    addCodeTemplateLibrariesToChannel(channel);
                }
            }
        }

        for (Channel channel : channelList) {
            addDependenciesToChannel(channel);
            addTagsToChannel(channel);
        }

        JFileChooser exportFileChooser = new JFileChooser();
        exportFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        File currentDir = new File(Frame.userPreferences.get(Messages.getString("ChannelPanel.167"), Messages.getString("ChannelPanel.168"))); //$NON-NLS-1$ //$NON-NLS-2$
        if (currentDir.exists()) {
            exportFileChooser.setCurrentDirectory(currentDir);
        }

        int returnVal = exportFileChooser.showSaveDialog(this);
        File exportFile = null;
        File exportDirectory = null;
        String exportPath = Messages.getString("ChannelPanel.169"); //$NON-NLS-1$

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Frame.userPreferences.put(Messages.getString("ChannelPanel.170"), exportFileChooser.getCurrentDirectory().getPath()); //$NON-NLS-1$

            int exportCollisionCount = 0;
            exportDirectory = exportFileChooser.getSelectedFile();
            exportPath = exportDirectory.getAbsolutePath();

            for (Channel channel : channelList) {
                exportFile = new File(exportPath + Messages.getString("ChannelPanel.171") + channel.getName() + Messages.getString("ChannelPanel.172")); //$NON-NLS-1$ //$NON-NLS-2$

                if (exportFile.exists()) {
                    exportCollisionCount++;
                }

                // Update resource names
                parent.updateResourceNames(channel);
            }

            try {
                int exportCount = 0;

                boolean overwriteAll = false;
                boolean skipAll = false;
                for (int i = 0, size = channelList.size(); i < size; i++) {
                    Channel channel = channelList.get(i);
                    exportFile = new File(exportPath + Messages.getString("ChannelPanel.173") + channel.getName() + Messages.getString("ChannelPanel.174")); //$NON-NLS-1$ //$NON-NLS-2$

                    boolean fileExists = exportFile.exists();
                    if (fileExists) {
                        if (!overwriteAll && !skipAll) {
                            if (exportCollisionCount == 1) {
                                if (!parent.alertOption(parent, Messages.getString("ChannelPanel.175") + channel.getName() + Messages.getString("ChannelPanel.176"))) { //$NON-NLS-1$ //$NON-NLS-2$
                                    continue;
                                }
                            } else {
                                ConflictOption conflictStatus = parent.alertConflict(parent, Messages.getString("ChannelPanel.177") + channel.getName() + Messages.getString("ChannelPanel.178"), exportCollisionCount); //$NON-NLS-1$ //$NON-NLS-2$

                                if (conflictStatus == ConflictOption.YES_APPLY_ALL) {
                                    overwriteAll = true;
                                } else if (conflictStatus == ConflictOption.NO) {
                                    exportCollisionCount--;
                                    continue;
                                } else if (conflictStatus == ConflictOption.NO_APPLY_ALL) {
                                    skipAll = true;
                                    continue;
                                }
                            }
                        }
                        exportCollisionCount--;
                    }

                    if (!fileExists || !skipAll) {
                        String channelXML = ObjectXMLSerializer.getInstance().serialize(channel);
                        FileUtils.writeStringToFile(exportFile, channelXML, UIConstants.CHARSET);
                        exportCount++;
                    }
                }

                if (exportCount > 0) {
                    parent.alertInformation(parent, exportCount + Messages.getString("ChannelPanel.179") + exportPath + Messages.getString("ChannelPanel.180")); //$NON-NLS-1$ //$NON-NLS-2$
                }
            } catch (IOException ex) {
                parent.alertError(parent, Messages.getString("ChannelPanel.181")); //$NON-NLS-1$
            }
        }

        // Reset the libraries on the cached channels
        for (Channel channel : channelList) {
            channel.getExportData().clearAllExceptMetadata();
        }
    }

    private boolean channelHasLinkedCodeTemplates(Channel channel) {
        return channelHasLinkedCodeTemplates(Collections.singletonList(channel));
    }

    private boolean channelHasLinkedCodeTemplates(List<Channel> channels) {
        for (Channel channel : channels) {
            for (CodeTemplateLibrary library : parent.codeTemplatePanel.getCachedCodeTemplateLibraries().values()) {
                if (library.getEnabledChannelIds().contains(channel.getId()) || (library.isIncludeNewChannels() && !library.getDisabledChannelIds().contains(channel.getId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean groupHasLinkedCodeTemplates(List<ChannelGroup> groups) {
        for (ChannelGroup group : groups) {
            if (channelHasLinkedCodeTemplates(group.getChannels())) {
                return true;
            }
        }
        return false;
    }

    private void addCodeTemplateLibrariesToChannel(Channel channel) {
        List<CodeTemplateLibrary> channelLibraries = new ArrayList<CodeTemplateLibrary>();

        for (CodeTemplateLibrary library : parent.codeTemplatePanel.getCachedCodeTemplateLibraries().values()) {
            if (library.getEnabledChannelIds().contains(channel.getId()) || (library.isIncludeNewChannels() && !library.getDisabledChannelIds().contains(channel.getId()))) {
                library = new CodeTemplateLibrary(library);

                List<CodeTemplate> codeTemplates = new ArrayList<CodeTemplate>();
                for (CodeTemplate codeTemplate : library.getCodeTemplates()) {
                    codeTemplate = parent.codeTemplatePanel.getCachedCodeTemplates().get(codeTemplate.getId());
                    if (codeTemplate != null) {
                        codeTemplates.add(codeTemplate);
                    }
                }

                library.setCodeTemplates(codeTemplates);
                channelLibraries.add(library);
            }
        }

        channel.getExportData().setCodeTemplateLibraries(channelLibraries);
    }

    private void addDependenciesToChannel(Channel channel) {
        Set<String> dependentIds = new HashSet<String>();
        Set<String> dependencyIds = new HashSet<String>();
        for (ChannelDependency channelDependency : getCachedChannelDependencies()) {
            if (StringUtils.equals(channelDependency.getDependencyId(), channel.getId())) {
                dependentIds.add(channelDependency.getDependentId());
            } else if (StringUtils.equals(channelDependency.getDependentId(), channel.getId())) {
                dependencyIds.add(channelDependency.getDependencyId());
            }
        }

        if (CollectionUtils.isNotEmpty(dependentIds)) {
            channel.getExportData().setDependentIds(dependentIds);
        }
        if (CollectionUtils.isNotEmpty(dependencyIds)) {
            channel.getExportData().setDependencyIds(dependencyIds);
        }
    }

    private void addTagsToChannel(Channel channel) {
        List<ChannelTag> channelTags = new ArrayList<ChannelTag>();
        for (ChannelTag channelTag : getCachedChannelTags()) {
            if (channelTag.getChannelIds().contains(channel.getId())) {
                channelTags.add(channelTag);
            }
        }

        if (CollectionUtils.isNotEmpty(channelTags)) {
            channel.getExportData().setChannelTags(channelTags);
        }
    }

    public boolean doExportAllGroups() {
        if (isSaveEnabled() && !promptSave(true)) {
            return false;
        }

        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();
        if (!model.isGroupModeEnabled()) {
            return false;
        }

        MutableTreeTableNode root = (MutableTreeTableNode) model.getRoot();
        if (root == null) {
            return false;
        }

        List<ChannelGroup> groups = new ArrayList<ChannelGroup>();

        for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
            AbstractChannelTableNode groupNode = (AbstractChannelTableNode) groupNodes.nextElement();
            if (groupNode.isGroupNode()) {
                groups.add(new ChannelGroup(groupNode.getGroupStatus().getGroup()));
            }
        }

        return handleExportGroups(groups);
    }

    public boolean doExportGroup() {
        if (isSaveEnabled() && !promptSave(true)) {
            return false;
        }

        if (isChannelSelected()) {
            JOptionPane.showMessageDialog(parent, Messages.getString("ChannelPanel.182")); //$NON-NLS-1$
            return false;
        }

        int[] rows = channelTable.getSelectedModelRows();
        if (rows.length > 0) {
            List<ChannelGroup> groups = new ArrayList<ChannelGroup>();

            // Populate list of groups with full channels
            for (int row : rows) {
                AbstractChannelTableNode groupNode = (AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent();
                if (groupNode.isGroupNode()) {
                    groups.add(new ChannelGroup(groupNode.getGroupStatus().getGroup()));
                }
            }

            return handleExportGroups(groups);
        }

        return false;
    }

    private boolean handleExportGroups(List<ChannelGroup> groups) {
        // Populate list of groups with full channels
        for (ChannelGroup group : groups) {
            List<Channel> channels = new ArrayList<Channel>();
            for (Channel channel : group.getChannels()) {
                ChannelStatus channelStatus = this.channelStatuses.get(channel.getId());
                if (channelStatus != null) {
                    channels.add(channelStatus.getChannel());
                }
            }
            group.setChannels(channels);
        }

        try {
            // Add code template libraries to channels if necessary
            if (groupHasLinkedCodeTemplates(groups)) {
                boolean addLibraries;
                String exportChannelCodeTemplateLibraries = userPreferences.get(Messages.getString("ChannelPanel.183"), null); //$NON-NLS-1$

                if (exportChannelCodeTemplateLibraries == null) {
                    JCheckBox alwaysChooseCheckBox = new JCheckBox(Messages.getString("ChannelPanel.184")); //$NON-NLS-1$
                    Object[] params = new Object[] {
                            Messages.getString("ChannelPanel.185"), //$NON-NLS-1$
                            alwaysChooseCheckBox };
                    int result = JOptionPane.showConfirmDialog(this, params, Messages.getString("ChannelPanel.186"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$

                    if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
                        addLibraries = result == JOptionPane.YES_OPTION;
                        if (alwaysChooseCheckBox.isSelected()) {
                            userPreferences.putBoolean(Messages.getString("ChannelPanel.187"), addLibraries); //$NON-NLS-1$
                        }
                    } else {
                        return false;
                    }
                } else {
                    addLibraries = Boolean.parseBoolean(exportChannelCodeTemplateLibraries);
                }

                if (addLibraries) {
                    for (ChannelGroup group : groups) {
                        for (Channel channel : group.getChannels()) {
                            addCodeTemplateLibrariesToChannel(channel);
                        }
                    }
                }
            }

            // Update resource names
            for (ChannelGroup group : groups) {
                for (Channel channel : group.getChannels()) {
                    addDependenciesToChannel(channel);
                    addTagsToChannel(channel);
                    parent.updateResourceNames(channel);
                }
            }

            if (groups.size() == 1) {
                return exportGroup(groups.iterator().next());
            } else {
                return exportGroups(groups);
            }
        } finally {
            // Reset the libraries on the cached channels
            for (ChannelGroup group : groups) {
                for (Channel channel : group.getChannels()) {
                    channel.getExportData().clearAllExceptMetadata();
                }
            }
        }
    }

    private boolean exportGroup(ChannelGroup group) {
        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        String groupXML = serializer.serialize(group);
        return parent.exportFile(groupXML, group.getName().replaceAll(Messages.getString("ChannelPanel.188"), Messages.getString("ChannelPanel.189")), Messages.getString("ChannelPanel.190"), Messages.getString("ChannelPanel.191")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    private boolean exportGroups(List<ChannelGroup> groups) {
        JFileChooser exportFileChooser = new JFileChooser();
        exportFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        File currentDir = new File(Frame.userPreferences.get(Messages.getString("ChannelPanel.192"), Messages.getString("ChannelPanel.193"))); //$NON-NLS-1$ //$NON-NLS-2$
        if (currentDir.exists()) {
            exportFileChooser.setCurrentDirectory(currentDir);
        }

        int returnVal = exportFileChooser.showSaveDialog(this);
        File exportFile = null;
        File exportDirectory = null;
        String exportPath = Messages.getString("ChannelPanel.194"); //$NON-NLS-1$

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Frame.userPreferences.put(Messages.getString("ChannelPanel.195"), exportFileChooser.getCurrentDirectory().getPath()); //$NON-NLS-1$

            int exportCollisionCount = 0;
            exportDirectory = exportFileChooser.getSelectedFile();
            exportPath = exportDirectory.getAbsolutePath();

            for (ChannelGroup group : groups) {
                exportFile = new File(exportPath + Messages.getString("ChannelPanel.196") + group.getName().replaceAll(Messages.getString("ChannelPanel.197"), Messages.getString("ChannelPanel.198")) + Messages.getString("ChannelPanel.199")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

                if (exportFile.exists()) {
                    exportCollisionCount++;
                }
            }

            try {
                int exportCount = 0;

                boolean overwriteAll = false;
                boolean skipAll = false;
                for (int i = 0, size = groups.size(); i < size; i++) {
                    ChannelGroup group = groups.get(i);
                    String groupName = group.getName().replaceAll(Messages.getString("ChannelPanel.200"), Messages.getString("ChannelPanel.201")); //$NON-NLS-1$ //$NON-NLS-2$
                    exportFile = new File(exportPath + Messages.getString("ChannelPanel.202") + groupName + Messages.getString("ChannelPanel.203")); //$NON-NLS-1$ //$NON-NLS-2$

                    boolean fileExists = exportFile.exists();
                    if (fileExists) {
                        if (!overwriteAll && !skipAll) {
                            if (exportCollisionCount == 1) {
                                if (!parent.alertOption(parent, Messages.getString("ChannelPanel.204") + groupName + Messages.getString("ChannelPanel.205"))) { //$NON-NLS-1$ //$NON-NLS-2$
                                    continue;
                                }
                            } else {
                                ConflictOption conflictStatus = parent.alertConflict(parent, Messages.getString("ChannelPanel.206") + groupName + Messages.getString("ChannelPanel.207"), exportCollisionCount); //$NON-NLS-1$ //$NON-NLS-2$

                                if (conflictStatus == ConflictOption.YES_APPLY_ALL) {
                                    overwriteAll = true;
                                } else if (conflictStatus == ConflictOption.NO) {
                                    exportCollisionCount--;
                                    continue;
                                } else if (conflictStatus == ConflictOption.NO_APPLY_ALL) {
                                    skipAll = true;
                                    continue;
                                }
                            }
                        }
                        exportCollisionCount--;
                    }

                    if (!fileExists || !skipAll) {
                        String groupXML = ObjectXMLSerializer.getInstance().serialize(group);
                        FileUtils.writeStringToFile(exportFile, groupXML, UIConstants.CHARSET);
                        exportCount++;
                    }
                }

                if (exportCount > 0) {
                    parent.alertInformation(parent, exportCount + Messages.getString("ChannelPanel.208") + exportPath + Messages.getString("ChannelPanel.209")); //$NON-NLS-1$ //$NON-NLS-2$
                    return true;
                }
            } catch (IOException ex) {
                parent.alertError(parent, Messages.getString("ChannelPanel.210")); //$NON-NLS-1$
            }
        }

        return false;
    }

    public void doDeleteGroup() {
        if (isChannelSelected()) {
            JOptionPane.showMessageDialog(parent, Messages.getString("ChannelPanel.211")); //$NON-NLS-1$
            return;
        }

        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();

        int[] rows = channelTable.getSelectedModelRows();
        if (rows.length >= 0) {
            List<AbstractChannelTableNode> groupNodes = new ArrayList<AbstractChannelTableNode>();

            for (int row : rows) {
                AbstractChannelTableNode groupNode = (AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent();
                if (groupNode.isGroupNode()) {
                    if (!StringUtils.equals(groupNode.getGroupStatus().getGroup().getId(), ChannelGroup.DEFAULT_ID)) {
                        groupNodes.add(groupNode);
                    }
                }
            }

            for (AbstractChannelTableNode groupNode : groupNodes) {
                Set<String> channelIds = new HashSet<String>();
                for (Enumeration<? extends MutableTreeTableNode> channelNodes = groupNode.children(); channelNodes.hasMoreElements();) {
                    channelIds.add(((AbstractChannelTableNode) channelNodes.nextElement()).getChannelStatus().getChannel().getId());
                }
                for (String channelId : channelIds) {
                    model.removeChannelFromGroup(groupNode, channelId);
                }
                model.removeGroup(groupNode);
            }

            parent.setSaveEnabled(true);
        }
    }

    public void doDeleteChannel() {
        if (isSaveEnabled() && !promptSave(true)) {
            return;
        }

        if (isGroupSelected()) {
            JOptionPane.showMessageDialog(parent, Messages.getString("ChannelPanel.212")); //$NON-NLS-1$
            return;
        }

        final List<Channel> selectedChannels = getSelectedChannels();
        if (selectedChannels.size() == 0) {
            return;
        }

        if (!parent.alertOption(parent, Messages.getString("ChannelPanel.213"))) { //$NON-NLS-1$
            return;
        }

        final String workingId = parent.startWorking(Messages.getString("ChannelPanel.214")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                Set<String> channelIds = new HashSet<String>(selectedChannels.size());
                for (Channel channel : selectedChannels) {
                    channelIds.add(channel.getId());
                }

                try {
                    parent.mirthClient.removeChannels(channelIds);
                } catch (ClientException e) {
                    SwingUtilities.invokeLater(() -> {
                        parent.alertThrowable(parent, e);
                    });
                }

                return null;
            }

            public void done() {
                doRefreshChannels();
                parent.stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doCloneChannel() {
        if (isSaveEnabled() && !promptSave(true)) {
            return;
        }

        if (isGroupSelected()) {
            JOptionPane.showMessageDialog(parent, Messages.getString("ChannelPanel.215")); //$NON-NLS-1$
            return;
        }

        List<Channel> selectedChannels = getSelectedChannels();
        if (selectedChannels.size() > 1) {
            JOptionPane.showMessageDialog(parent, Messages.getString("ChannelPanel.216")); //$NON-NLS-1$
            return;
        }

        Channel channel = selectedChannels.get(0);

        if (channel instanceof InvalidChannel) {
            InvalidChannel invalidChannel = (InvalidChannel) channel;
            Throwable cause = invalidChannel.getCause();
            parent.alertThrowable(parent, cause, Messages.getString("ChannelPanel.217") + channel.getName() + Messages.getString("ChannelPanel.218") + getMissingExtensions(invalidChannel) + Messages.getString("ChannelPanel.219") + cause.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return;
        }

        try {
            channel = (Channel) SerializationUtils.clone(channel);
        } catch (SerializationException e) {
            parent.alertThrowable(parent, e);
            return;
        }

        // Before overwriting the ID, get all associated tags 
        List<ChannelTag> channelTags = new ArrayList<ChannelTag>();
        for (ChannelTag tag : getCachedChannelTags()) {
            if (tag.getChannelIds().contains(channel.getId())) {
                channelTags.add(tag);
            }
        }

        try {
            channel.setRevision(0);
            channel.setId(parent.mirthClient.getGuid());
        } catch (ClientException e) {
            parent.alertThrowable(parent, e);
        }

        String channelName = channel.getName();
        do {
            channelName = DisplayUtil.showInputDialog(this, Messages.getString("ChannelPanel.220"), channelName); //$NON-NLS-1$
            if (channelName == null) {
                return;
            }
        } while (!parent.checkChannelName(channelName, channel.getId()));

        channel.setName(channelName);
        channelStatuses.put(channel.getId(), new ChannelStatus(channel));

        // Add the cloned channel's ID to any tags
        for (ChannelTag tag : channelTags) {
            tag.getChannelIds().add(channel.getId());
        }

        parent.editChannel(channel);
        parent.setSaveEnabled(true);
    }

    public void doEditChannel() {
        if (isSaveEnabled() && !confirmLeave()) {
            return;
        }

        if (parent.isEditingChannel) {
            return;
        } else {
            parent.isEditingChannel = true;
        }

        if (isGroupSelected()) {
            JOptionPane.showMessageDialog(parent, Messages.getString("ChannelPanel.221")); //$NON-NLS-1$
            return;
        }

        List<Channel> selectedChannels = getSelectedChannels();
        if (selectedChannels.size() > 1) {
            JOptionPane.showMessageDialog(parent, Messages.getString("ChannelPanel.222")); //$NON-NLS-1$
        } else if (selectedChannels.size() == 0) {
            JOptionPane.showMessageDialog(parent, Messages.getString("ChannelPanel.223")); //$NON-NLS-1$
        } else {
            try {
                Channel channel = selectedChannels.get(0);

                if (channel instanceof InvalidChannel) {
                    InvalidChannel invalidChannel = (InvalidChannel) channel;
                    Throwable cause = invalidChannel.getCause();
                    parent.alertThrowable(parent, cause, Messages.getString("ChannelPanel.224") + channel.getName() + Messages.getString("ChannelPanel.225") + getMissingExtensions(invalidChannel) + Messages.getString("ChannelPanel.226") + cause.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                } else {
                    parent.editChannel((Channel) SerializationUtils.clone(channel));
                }
            } catch (SerializationException e) {
                parent.alertThrowable(parent, e);
            }
        }
        parent.isEditingChannel = false;
    }

    public void doEnableChannel() {
        if (isSaveEnabled() && !promptSave(true)) {
            return;
        }

        final List<Channel> selectedChannels = getSelectedChannels();
        if (selectedChannels.size() == 0) {
            parent.alertWarning(parent, Messages.getString("ChannelPanel.227")); //$NON-NLS-1$
            return;
        }

        final Set<String> channelIds = new HashSet<String>();
        Set<Channel> failedChannels = new HashSet<Channel>();
        String firstValidationMessage = null;

        for (Iterator<Channel> it = selectedChannels.iterator(); it.hasNext();) {
            Channel channel = it.next();
            String validationMessage = null;

            if (channel instanceof InvalidChannel) {
                failedChannels.add(channel);
                it.remove();
            } else if ((validationMessage = parent.channelEditPanel.checkAllForms(channel)) != null) {
                if (firstValidationMessage == null) {
                    firstValidationMessage = validationMessage;
                }

                failedChannels.add(channel);
                it.remove();
            } else {
                channelIds.add(channel.getId());
            }
        }

        if (!channelIds.isEmpty()) {
            final String workingId = parent.startWorking(Messages.getString("ChannelPanel.228")); //$NON-NLS-1$

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                public Void doInBackground() {
                    for (Channel channel : selectedChannels) {
                        channel.getExportData().getMetadata().setEnabled(true);
                    }

                    try {
                        parent.mirthClient.setChannelEnabled(channelIds, true);
                    } catch (ClientException e) {
                        SwingUtilities.invokeLater(() -> {
                            parent.alertThrowable(parent, e);
                        });
                    }
                    return null;
                }

                public void done() {
                    doRefreshChannels();
                    parent.stopWorking(workingId);
                }
            };

            worker.execute();
        }

        if (!failedChannels.isEmpty()) {
            if (failedChannels.size() == 1) {
                Channel channel = failedChannels.iterator().next();

                if (channel instanceof InvalidChannel) {
                    InvalidChannel invalidChannel = (InvalidChannel) channel;
                    Throwable cause = invalidChannel.getCause();
                    parent.alertThrowable(parent, cause, Messages.getString("ChannelPanel.229") + invalidChannel.getName() + Messages.getString("ChannelPanel.230") + getMissingExtensions(invalidChannel) + Messages.getString("ChannelPanel.231") + cause.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                } else {
                    parent.alertCustomError(parent, firstValidationMessage, CustomErrorDialog.ERROR_ENABLING_CHANNEL);
                }
            } else {
                String message = Messages.getString("ChannelPanel.232"); //$NON-NLS-1$
                for (Channel channel : failedChannels) {
                    message += Messages.getString("ChannelPanel.233") + channel.getName() + Messages.getString("ChannelPanel.234") + channel.getId() + Messages.getString("ChannelPanel.235"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
                parent.alertError(parent, message);
            }
        }
    }

    public void doDisableChannel() {
        if (isSaveEnabled() && !promptSave(true)) {
            return;
        }

        final List<Channel> selectedChannels = getSelectedChannels();
        if (selectedChannels.size() == 0) {
            parent.alertWarning(parent, Messages.getString("ChannelPanel.236")); //$NON-NLS-1$
            return;
        }

        final String workingId = parent.startWorking(Messages.getString("ChannelPanel.237")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                Set<String> channelIds = new HashSet<String>();

                for (Channel channel : selectedChannels) {
                    if (!(channel instanceof InvalidChannel)) {
                        channel.getExportData().getMetadata().setEnabled(false);
                        channelIds.add(channel.getId());
                    }
                }

                if (CollectionUtils.isNotEmpty(channelIds)) {
                    try {
                        parent.mirthClient.setChannelEnabled(channelIds, false);
                    } catch (ClientException e) {
                        SwingUtilities.invokeLater(() -> {
                            parent.alertThrowable(parent, e);
                        });
                    }
                }
                return null;
            }

            public void done() {
                doRefreshChannels();
                parent.stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doViewMessages() {
        if (isSaveEnabled() && !confirmLeave()) {
            return;
        }

        parent.doShowMessages();
    }

    public static int getNumberOfDefaultColumns() {
        return DEFAULT_COLUMNS.length;
    }

    private void setChannelTaskVisible(int task) {
        setChannelTaskVisibility(task, true);
    }

    private void setChannelTaskVisibility(int task, boolean visible) {
        parent.setVisibleTasks(channelTasks, channelPopupMenu, task, task, visible);
    }

    private void setGroupTaskVisible(int task) {
        setGroupTaskVisibility(task, true);
    }

    private void setGroupTaskVisibility(int task, boolean visible) {
        parent.setVisibleTasks(groupTasks, groupPopupMenu, task, task, visible);
    }

    private void setAllTaskVisibility(boolean visible) {
        parent.setVisibleTasks(channelTasks, channelPopupMenu, 1, TASK_CHANNEL_VIEW_MESSAGES, visible);
        parent.setVisibleTasks(groupTasks, groupPopupMenu, 1, TASK_GROUP_DELETE_GROUP, visible);
    }

    public void updateChannelStatuses(List<ChannelSummary> changedChannels) {
        for (ChannelSummary channelSummary : changedChannels) {
            String channelId = channelSummary.getChannelId();

            if (channelSummary.isDeleted()) {
                channelStatuses.remove(channelId);
            } else {
                ChannelStatus channelStatus = channelStatuses.get(channelId);
                if (channelStatus == null) {
                    channelStatus = new ChannelStatus();
                    channelStatuses.put(channelId, channelStatus);
                }

                /*
                 * If the status coming back from the server is for an entirely new channel, the
                 * Channel object should never be null.
                 */
                if (channelSummary.getChannelStatus().getChannel() != null) {
                    channelStatus.setChannel(channelSummary.getChannelStatus().getChannel());
                }

                if (channelSummary.isUndeployed()) {
                    channelStatus.setDeployedDate(null);
                    channelStatus.setDeployedRevisionDelta(null);
                    channelStatus.setCodeTemplatesChanged(false);
                } else {
                    if (channelSummary.getChannelStatus().getDeployedDate() != null) {
                        channelStatus.setDeployedDate(channelSummary.getChannelStatus().getDeployedDate());
                        channelStatus.setDeployedRevisionDelta(channelSummary.getChannelStatus().getDeployedRevisionDelta());
                    }

                    channelStatus.setCodeTemplatesChanged(channelSummary.getChannelStatus().isCodeTemplatesChanged());
                }

                channelStatus.setLocalChannelId(channelSummary.getChannelStatus().getLocalChannelId());
            }
        }
    }

    public void updateDefaultChannelGroup(List<DashboardStatus> statuses) {
        if (statuses != null && this.groupStatuses != null) {
            // Build up a map by the channel ID for convenience
            Map<String, DashboardStatus> dashboardStatusMap = new HashMap<String, DashboardStatus>();
            for (DashboardStatus status : statuses) {
                dashboardStatusMap.put(status.getChannelId(), status);
            }

            // Remove any dashboard statuses that are already contained in a non-default channel group
            for (ChannelGroupStatus groupStatus : this.groupStatuses.values()) {
                if (!StringUtils.equals(groupStatus.getGroup().getId(), ChannelGroup.DEFAULT_ID)) {
                    for (Channel channel : groupStatus.getGroup().getChannels()) {
                        dashboardStatusMap.remove(channel.getId());
                    }
                }
            }

            /*
             * The status map should now contain all channels that aren't part of a known real
             * (non-default) group. Build up the default group again using that status map.
             */
            ChannelGroup defaultGroup = ChannelGroup.getDefaultGroup();
            List<ChannelStatus> defaultGroupChannelStatuses = new ArrayList<ChannelStatus>();
            ChannelGroupStatus defaultGroupStatus = new ChannelGroupStatus(defaultGroup, defaultGroupChannelStatuses);

            for (DashboardStatus status : dashboardStatusMap.values()) {
                defaultGroup.getChannels().add(new Channel(status.getChannelId()));

                // Add the channel status if it happens to be cached
                ChannelStatus channelStatus = this.channelStatuses.get(status.getChannelId());
                if (channelStatus != null) {
                    defaultGroupChannelStatuses.add(channelStatus);
                }
            }

            // Update the default group in the cache
            this.groupStatuses.put(defaultGroup.getId(), defaultGroupStatus);
        }
    }

    private void updateChannelGroups(List<ChannelGroup> channelGroups) {
        if (channelGroups == null) {
            channelGroups = new ArrayList<ChannelGroup>();
        }

        this.groupStatuses.clear();

        ChannelGroup defaultGroup = ChannelGroup.getDefaultGroup();
        List<ChannelStatus> defaultGroupChannelStatuses = new ArrayList<ChannelStatus>();
        ChannelGroupStatus defaultGroupStatus = new ChannelGroupStatus(defaultGroup, defaultGroupChannelStatuses);
        this.groupStatuses.put(defaultGroup.getId(), defaultGroupStatus);

        Set<String> visitedChannelIds = new HashSet<String>();
        Set<String> remainingChannelIds = new HashSet<String>(this.channelStatuses.keySet());

        for (ChannelGroup group : channelGroups) {
            List<ChannelStatus> channelStatuses = new ArrayList<ChannelStatus>();

            for (Channel channel : group.getChannels()) {
                if (!visitedChannelIds.contains(channel.getId())) {
                    ChannelStatus channelStatus = this.channelStatuses.get(channel.getId());
                    if (channelStatus != null) {
                        channelStatuses.add(channelStatus);
                    }
                    visitedChannelIds.add(channel.getId());
                    remainingChannelIds.remove(channel.getId());
                }
            }

            this.groupStatuses.put(group.getId(), new ChannelGroupStatus(group, channelStatuses));
        }

        for (String channelId : remainingChannelIds) {
            defaultGroup.getChannels().add(new Channel(channelId));
            defaultGroupChannelStatuses.add(this.channelStatuses.get(channelId));
        }
    }

    private void updateChannelMetadata(Map<String, ChannelMetadata> metadataMap) {
        if (metadataMap != null) {
            for (ChannelStatus status : channelStatuses.values()) {
                Channel channel = status.getChannel();
                channel.getExportData().setMetadata(metadataMap.get(channel.getId()));
                if (channel instanceof InvalidChannel) {
                    channel.getExportData().getMetadata().setEnabled(false);
                }
            }
        }
    }

    public void clearChannelCache() {
        channelStatuses = new LinkedHashMap<String, ChannelStatus>();
        groupStatuses = new LinkedHashMap<String, ChannelGroupStatus>();
    }

    private String getMissingExtensions(InvalidChannel channel) {
        Set<String> missingConnectors = new HashSet<String>();
        Set<String> missingDataTypes = new HashSet<String>();

        try {
            DonkeyElement channelElement = new DonkeyElement(((InvalidChannel) channel).getChannelXml());

            checkConnectorForMissingExtensions(channelElement.getChildElement(Messages.getString("ChannelPanel.238")), true, missingConnectors, missingDataTypes); //$NON-NLS-1$

            DonkeyElement destinationConnectors = channelElement.getChildElement(Messages.getString("ChannelPanel.239")); //$NON-NLS-1$
            if (destinationConnectors != null) {
                for (DonkeyElement destinationConnector : destinationConnectors.getChildElements()) {
                    checkConnectorForMissingExtensions(destinationConnector, false, missingConnectors, missingDataTypes);
                }
            }
        } catch (DonkeyElementException e) {
        }

        StringBuilder builder = new StringBuilder();

        if (!missingConnectors.isEmpty()) {
            builder.append(Messages.getString("ChannelPanel.240")); //$NON-NLS-1$
            builder.append(StringUtils.join(missingConnectors.toArray(), Messages.getString("ChannelPanel.241"))); //$NON-NLS-1$
            builder.append(Messages.getString("ChannelPanel.242")); //$NON-NLS-1$
        }

        if (!missingDataTypes.isEmpty()) {
            if (missingConnectors.isEmpty()) {
                builder.append(Messages.getString("ChannelPanel.243")); //$NON-NLS-1$
            }
            builder.append(Messages.getString("ChannelPanel.244")); //$NON-NLS-1$
            builder.append(StringUtils.join(missingDataTypes.toArray(), Messages.getString("ChannelPanel.245"))); //$NON-NLS-1$
            builder.append(Messages.getString("ChannelPanel.246")); //$NON-NLS-1$
        }

        return builder.toString();
    }

    private void checkConnectorForMissingExtensions(DonkeyElement connector, boolean source, Set<String> missingConnectors, Set<String> missingDataTypes) {
        if (connector != null) {
            DonkeyElement transportName = connector.getChildElement(Messages.getString("ChannelPanel.247")); //$NON-NLS-1$
            // Check for 2.x-specific connectors
            transportName.setTextContent(ImportConverter3_0_0.convertTransportName(transportName.getTextContent()));

            if (transportName != null) {
                if (source && !LoadedExtensions.getInstance().getSourceConnectors().containsKey(transportName.getTextContent())) {
                    missingConnectors.add(transportName.getTextContent());
                } else if (!source && !LoadedExtensions.getInstance().getDestinationConnectors().containsKey(transportName.getTextContent())) {
                    missingConnectors.add(transportName.getTextContent());
                }
            }

            checkTransformerForMissingExtensions(connector.getChildElement(Messages.getString("ChannelPanel.248")), missingDataTypes); //$NON-NLS-1$
            if (!source) {
                checkTransformerForMissingExtensions(connector.getChildElement(Messages.getString("ChannelPanel.249")), missingDataTypes); //$NON-NLS-1$
            }
        }
    }

    private void checkTransformerForMissingExtensions(DonkeyElement transformer, Set<String> missingDataTypes) {
        if (transformer != null) {
            // Check for 2.x-specific data types
            missingDataTypes.addAll(ImportConverter3_0_0.getMissingDataTypes(transformer, LoadedExtensions.getInstance().getDataTypePlugins().keySet()));

            DonkeyElement inboundDataType = transformer.getChildElement(Messages.getString("ChannelPanel.250")); //$NON-NLS-1$

            if (inboundDataType != null && !LoadedExtensions.getInstance().getDataTypePlugins().containsKey(inboundDataType.getTextContent())) {
                missingDataTypes.add(inboundDataType.getTextContent());
            }

            DonkeyElement outboundDataType = transformer.getChildElement(Messages.getString("ChannelPanel.251")); //$NON-NLS-1$

            if (outboundDataType != null && !LoadedExtensions.getInstance().getDataTypePlugins().containsKey(outboundDataType.getTextContent())) {
                missingDataTypes.add(outboundDataType.getTextContent());
            }
        }
    }

    public void initPanelPlugins() {
        loadPanelPlugins();
        switchBottomPane();

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                loadPanelPlugin(sourceTabbedPane.getTitleAt(index));
            }
        };
        tabPane.addChangeListener(changeListener);
    }

    private void switchBottomPane() {
        if (LoadedExtensions.getInstance().getChannelPanelPlugins().size() > 0) {
            splitPane.setBottomComponent(tabPane);
            splitPane.setDividerSize(6);
            splitPane.setDividerLocation(3 * userPreferences.getInt(Messages.getString("ChannelPanel.252"), UIConstants.MIRTH_HEIGHT) / 5); //$NON-NLS-1$
            splitPane.setResizeWeight(0.5);
        } else {
            splitPane.setBottomComponent(null);
            splitPane.setDividerSize(0);
        }
    }

    private void loadPanelPlugins() {
        if (LoadedExtensions.getInstance().getChannelPanelPlugins().size() > 0) {
            for (ChannelPanelPlugin plugin : LoadedExtensions.getInstance().getChannelPanelPlugins().values()) {
                if (plugin.getComponent() != null) {
                    tabPane.addTab(plugin.getPluginPointName(), plugin.getComponent());
                }
            }
        }
    }

    private void loadPanelPlugin(String pluginName) {
        final ChannelPanelPlugin plugin = LoadedExtensions.getInstance().getChannelPanelPlugins().get(pluginName);

        if (plugin != null) {
            final List<Channel> selectedChannels = getSelectedChannels();

            QueuingSwingWorkerTask<Void, Void> task = new QueuingSwingWorkerTask<Void, Void>(pluginName, Messages.getString("ChannelPanel.253") + pluginName + Messages.getString("ChannelPanel.254")) { //$NON-NLS-1$ //$NON-NLS-2$
                @Override
                public Void doInBackground() {
                    try {
                        if (selectedChannels.size() > 0) {
                            plugin.prepareData(selectedChannels);
                        } else {
                            plugin.prepareData();
                        }
                    } catch (ClientException e) {
                        parent.alertThrowable(parent, e);
                    }
                    return null;
                }

                @Override
                public void done() {
                    if (selectedChannels.size() > 0) {
                        plugin.update(selectedChannels);
                    } else {
                        plugin.update();
                    }
                }
            };

            new QueuingSwingWorker<Void, Void>(task, true).executeDelegate();
        }
    }

    private synchronized void updateCurrentPluginPanel() {
        if (LoadedExtensions.getInstance().getChannelPanelPlugins().size() > 0) {
            loadPanelPlugin(tabPane.getTitleAt(tabPane.getSelectedIndex()));
        }
    }

    private synchronized void updateModel(TableState tableState) {
        List<ChannelStatus> filteredChannelStatuses = new ArrayList<ChannelStatus>(channelStatuses.values());

        List<String> activeFilters = new ArrayList<String>();
        String filterText = tagField.getTags();
        if (StringUtils.isNotBlank(filterText)) {
            for (SearchFilter filter : SearchFilterParser.parse(filterText, getCachedChannelTags())) {
                activeFilters.add(filter.toDisplayString());
                filter.filterChannelStatuses(filteredChannelStatuses);
            }
        }

        int totalChannelCount = channelStatuses.size();
        int visibleChannelCount = filteredChannelStatuses.size();

        List<Channel> filteredChannels = new ArrayList<Channel>();
        Set<String> filteredChannelIds = new HashSet<>();
        for (ChannelStatus filteredChannelStatus : filteredChannelStatuses) {
            filteredChannels.add(filteredChannelStatus.getChannel());
            filteredChannelIds.add(filteredChannelStatus.getChannel().getId());
        }

        List<ChannelGroupStatus> filteredGroupStatuses = new ArrayList<ChannelGroupStatus>();
        for (ChannelGroupStatus groupStatus : groupStatuses.values()) {
            filteredGroupStatuses.add(new ChannelGroupStatus(groupStatus));
        }

        int totalGroupCount = filteredGroupStatuses.size();
        int visibleGroupCount = totalGroupCount;

        for (Iterator<ChannelGroupStatus> groupStatusIterator = filteredGroupStatuses.iterator(); groupStatusIterator.hasNext();) {
            ChannelGroupStatus groupStatus = groupStatusIterator.next();

            for (Iterator<ChannelStatus> channelStatusIterator = groupStatus.getChannelStatuses().iterator(); channelStatusIterator.hasNext();) {
                ChannelStatus channelStatus = channelStatusIterator.next();

                boolean found = false;
                for (ChannelStatus filteredChannelStatus : filteredChannelStatuses) {
                    if (filteredChannelStatus.getChannel().getId().equals(channelStatus.getChannel().getId())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    channelStatusIterator.remove();
                }
            }

            if (totalChannelCount != visibleChannelCount && groupStatus.getChannelStatuses().isEmpty()) {
                groupStatusIterator.remove();
                visibleGroupCount--;
            }
        }

        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();

        StringBuilder builder = new StringBuilder();

        if (model.isGroupModeEnabled()) {
            if (totalGroupCount == visibleGroupCount) {
                builder.append(totalGroupCount);
            } else {
                builder.append(visibleGroupCount).append(Messages.getString("ChannelPanel.255")).append(totalGroupCount); //$NON-NLS-1$
            }

            builder.append(Messages.getString("ChannelPanel.256")); //$NON-NLS-1$
            if (totalGroupCount != 1) {
                builder.append('s');
            }
            builder.append(Messages.getString("ChannelPanel.257")); //$NON-NLS-1$
        }

        if (totalChannelCount == visibleChannelCount) {
            builder.append(totalChannelCount);
        } else {
            builder.append(visibleChannelCount).append(Messages.getString("ChannelPanel.258")).append(totalChannelCount); //$NON-NLS-1$
        }

        builder.append(Messages.getString("ChannelPanel.259")); //$NON-NLS-1$
        if (totalChannelCount != 1) {
            builder.append('s');
        }
        
        int totalEnabledChannels = 0;
        int visibleEnabledChannel = 0;
        for (Map.Entry<String, ChannelStatus> entry : channelStatuses.entrySet()) {
            if (entry.getValue().getChannel().getExportData().getMetadata().isEnabled()) {
                if (filteredChannelIds.contains(entry.getKey())) {
                    visibleEnabledChannel++;
                }
                totalEnabledChannels++;
            }
        }
        
        builder.append(Messages.getString("ChannelPanel.260")); //$NON-NLS-1$
        
        if (totalEnabledChannels == visibleEnabledChannel) {
            builder.append(totalEnabledChannels);
        } else {
            builder.append(visibleEnabledChannel).append(Messages.getString("ChannelPanel.261")).append(totalEnabledChannels); //$NON-NLS-1$
        }
        builder.append(Messages.getString("ChannelPanel.262")); //$NON-NLS-1$

        if (tagField.isFilterEnabled()) {
            builder.append(Messages.getString("ChannelPanel.263")); //$NON-NLS-1$
            for (Iterator<String> it = activeFilters.iterator(); it.hasNext();) {
                builder.append(it.next());
                if (it.hasNext()) {
                    builder.append(Messages.getString("ChannelPanel.264")); //$NON-NLS-1$
                }
            }
            builder.append(')');
        }

        tagsLabel.setText(builder.toString());
        tagsLabel.setToolTipText(tagsLabel.getText());

        for (ChannelColumnPlugin plugin : LoadedExtensions.getInstance().getChannelColumnPlugins().values()) {
            plugin.tableUpdate(filteredChannels);
        }

        model.update(filteredGroupStatuses);

        restoreTableState(tableState);
    }

    /**
     * Shows the popup menu when the trigger button (right-click) has been pushed. Deselects the
     * rows if no row was selected.
     */
    private void checkSelectionAndPopupMenu(MouseEvent evt) {
        int row = channelTable.rowAtPoint(new Point(evt.getX(), evt.getY()));
        if (row == -1) {
            deselectRows();
        }

        if (evt.isPopupTrigger()) {
            if (row != -1) {
                if (!channelTable.isRowSelected(row)) {
                    channelTable.setRowSelectionInterval(row, row);
                }

                if (((AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent()).isGroupNode()) {
                    groupPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                } else {
                    channelPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            } else {
                channelPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }

    /** The action called when a Channel is selected. Sets tasks as well. */
    private void channelListSelected(ListSelectionEvent evt) {
        updateTasks();

        int[] rows = channelTable.getSelectedModelRows();

        if (rows.length > 0) {
            for (TaskPlugin plugin : LoadedExtensions.getInstance().getTaskPlugins().values()) {
                plugin.onRowSelected(channelTable);
            }

            updateCurrentPluginPanel();
        }
    }

    public boolean isGroupSelected() {
        for (int row : channelTable.getSelectedModelRows()) {
            AbstractChannelTableNode node = (AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent();
            if (node.isGroupNode()) {
                return true;
            }
        }
        return false;
    }

    public boolean isChannelSelected() {
        for (int row : channelTable.getSelectedModelRows()) {
            AbstractChannelTableNode node = (AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent();
            if (!node.isGroupNode()) {
                return true;
            }
        }
        return false;
    }

    public List<Channel> getSelectedChannels() {
        List<Channel> selectedChannels = new ArrayList<Channel>();

        for (int row : channelTable.getSelectedModelRows()) {
            AbstractChannelTableNode node = (AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent();

            if (node.isGroupNode()) {
                for (Enumeration<? extends MutableTreeTableNode> channelNodes = node.children(); channelNodes.hasMoreElements();) {
                    selectedChannels.add(((AbstractChannelTableNode) channelNodes.nextElement()).getChannelStatus().getChannel());
                }
            } else {
                selectedChannels.add(node.getChannelStatus().getChannel());
            }
        }

        return selectedChannels;
    }

    private void deselectRows() {
        channelTable.clearSelection();
        updateTasks();

        for (TaskPlugin plugin : LoadedExtensions.getInstance().getTaskPlugins().values()) {
            plugin.onRowDeselected();
        }

        updateCurrentPluginPanel();
    }

    /**
     * Checks to see if the passed in channel id already exists
     */
    private boolean checkChannelId(String id) {
        for (ChannelStatus channelStatus : channelStatuses.values()) {
            if (channelStatus.getChannel().getId().equalsIgnoreCase(id)) {
                return false;
            }
        }
        return true;
    }

    private static class DefaultChannelTableNodeFactory implements ChannelTableNodeFactory {
        @Override
        public AbstractChannelTableNode createNode(ChannelGroupStatus groupStatus) {
            return new ChannelTableNode(groupStatus);
        }

        @Override
        public AbstractChannelTableNode createNode(ChannelStatus channelStatus) {
            return new ChannelTableNode(channelStatus);
        }
    }

    private void initComponents() {
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPane.setOneTouchExpandable(true);

        topPanel = new JPanel();

        List<String> columns = new ArrayList<String>();

        for (ChannelColumnPlugin plugin : LoadedExtensions.getInstance().getChannelColumnPlugins().values()) {
            if (plugin.isDisplayFirst()) {
                columns.add(plugin.getColumnHeader());
            }
        }

        columns.addAll(Arrays.asList(DEFAULT_COLUMNS));

        for (ChannelColumnPlugin plugin : LoadedExtensions.getInstance().getChannelColumnPlugins().values()) {
            if (!plugin.isDisplayFirst()) {
                columns.add(plugin.getColumnHeader());
            }
        }

        channelTable = new MirthTreeTable(Messages.getString("ChannelPanel.265"), new LinkedHashSet<String>(columns)); //$NON-NLS-1$

        channelTable.setColumnFactory(new ChannelTableColumnFactory());

        ChannelTreeTableModel model = new ChannelTreeTableModel();
        model.setColumnIdentifiers(columns);
        model.setNodeFactory(new DefaultChannelTableNodeFactory());
        channelTable.setTreeTableModel(model);

        channelTable.setDoubleBuffered(true);
        channelTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        channelTable.getTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        channelTable.setHorizontalScrollEnabled(true);
        channelTable.packTable(UIConstants.COL_MARGIN);
        channelTable.setRowHeight(UIConstants.ROW_HEIGHT);
        channelTable.setOpaque(true);
        channelTable.setRowSelectionAllowed(true);
        channelTable.setSortable(true);
        channelTable.putClientProperty(Messages.getString("ChannelPanel.266"), Messages.getString("ChannelPanel.267")); //$NON-NLS-1$ //$NON-NLS-2$
        channelTable.setAutoCreateColumnsFromModel(false);
        channelTable.setShowGrid(true, true);
        channelTable.restoreColumnPreferences();
        channelTable.setMirthColumnControlEnabled(true);

        channelTable.setDragEnabled(true);
        channelTable.setDropMode(DropMode.ON);
        channelTable.setTransferHandler(new ChannelTableTransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                // Don't allow files to be imported when the save task is enabled 
                if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) && isSaveEnabled()) {
                    return false;
                }
                return super.canImport(support);
            }

            @Override
            public void importFile(final File file, final boolean showAlerts) {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            String fileString = StringUtils.trim(parent.readFileToString(file));

                            try {
                                // If the table is in channel view, or filtering is enabled, don't allow groups to be imported
                                ChannelGroup group = ObjectXMLSerializer.getInstance().deserialize(fileString, ChannelGroup.class);
                                if (group != null && (!((ChannelTreeTableModel) channelTable.getTreeTableModel()).isGroupModeEnabled() || tagField.isFilterEnabled())) {
                                    return;
                                }
                            } catch (Exception e) {
                            }

                            if (showAlerts && !parent.promptObjectMigration(fileString, Messages.getString("ChannelPanel.268"))) { //$NON-NLS-1$
                                return;
                            }

                            try {
                                importChannel(ObjectXMLSerializer.getInstance().deserialize(fileString, Channel.class), showAlerts);
                            } catch (Exception e) {
                                try {
                                    importGroup(ObjectXMLSerializer.getInstance().deserialize(fileString, ChannelGroup.class), showAlerts, !showAlerts);
                                } catch (Exception e2) {
                                    if (showAlerts) {
                                        parent.alertThrowable(parent, e, Messages.getString("ChannelPanel.269") + e.getMessage()); //$NON-NLS-1$
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean canMoveChannels(List<Channel> channels, int row) {
                if (row >= 0) {
                    TreePath path = channelTable.getPathForRow(row);
                    if (path != null) {
                        AbstractChannelTableNode node = (AbstractChannelTableNode) path.getLastPathComponent();

                        if (node.isGroupNode()) {
                            Set<String> currentChannelIds = new HashSet<String>();
                            for (Enumeration<? extends MutableTreeTableNode> channelNodes = node.children(); channelNodes.hasMoreElements();) {
                                currentChannelIds.add(((AbstractChannelTableNode) channelNodes.nextElement()).getChannelStatus().getChannel().getId());
                            }

                            for (Iterator<Channel> it = channels.iterator(); it.hasNext();) {
                                if (currentChannelIds.contains(it.next().getId())) {
                                    it.remove();
                                }
                            }

                            return !channels.isEmpty();
                        }
                    }
                }

                return false;
            }

            @Override
            public boolean moveChannels(List<Channel> channels, int row) {
                if (row >= 0) {
                    TreePath path = channelTable.getPathForRow(row);
                    if (path != null) {
                        AbstractChannelTableNode node = (AbstractChannelTableNode) path.getLastPathComponent();

                        if (node.isGroupNode()) {
                            Set<String> currentChannelIds = new HashSet<String>();
                            for (Enumeration<? extends MutableTreeTableNode> channelNodes = node.children(); channelNodes.hasMoreElements();) {
                                currentChannelIds.add(((AbstractChannelTableNode) channelNodes.nextElement()).getChannelStatus().getChannel().getId());
                            }

                            for (Iterator<Channel> it = channels.iterator(); it.hasNext();) {
                                if (currentChannelIds.contains(it.next().getId())) {
                                    it.remove();
                                }
                            }

                            if (!channels.isEmpty()) {
                                ListSelectionListener[] listeners = ((DefaultListSelectionModel) channelTable.getSelectionModel()).getListSelectionListeners();
                                for (ListSelectionListener listener : listeners) {
                                    channelTable.getSelectionModel().removeListSelectionListener(listener);
                                }

                                try {
                                    ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();
                                    Set<String> channelIds = new HashSet<String>();
                                    for (Channel channel : channels) {
                                        model.addChannelToGroup(node, channel.getId());
                                        channelIds.add(channel.getId());
                                    }

                                    List<TreePath> selectionPaths = new ArrayList<TreePath>();
                                    for (Enumeration<? extends MutableTreeTableNode> channelNodes = node.children(); channelNodes.hasMoreElements();) {
                                        AbstractChannelTableNode channelNode = (AbstractChannelTableNode) channelNodes.nextElement();
                                        if (channelIds.contains(channelNode.getChannelStatus().getChannel().getId())) {
                                            selectionPaths.add(new TreePath(new Object[] {
                                                    model.getRoot(), node, channelNode }));
                                        }
                                    }

                                    parent.setSaveEnabled(true);
                                    channelTable.expandPath(new TreePath(new Object[] {
                                            channelTable.getTreeTableModel().getRoot(), node }));
                                    channelTable.getTreeSelectionModel().setSelectionPaths(selectionPaths.toArray(new TreePath[selectionPaths.size()]));
                                    return true;
                                } finally {
                                    for (ListSelectionListener listener : listeners) {
                                        channelTable.getSelectionModel().addListSelectionListener(listener);
                                    }
                                }
                            }
                        }
                    }
                }

                return false;
            }
        });

        channelTable.setTreeCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                TreePath path = channelTable.getPathForRow(row);
                if (path != null && ((AbstractChannelTableNode) path.getLastPathComponent()).isGroupNode()) {
                    setIcon(UIConstants.ICON_GROUP);
                }

                return label;
            }
        });
        channelTable.setLeafIcon(UIConstants.ICON_CHANNEL);
        channelTable.setOpenIcon(UIConstants.ICON_GROUP);
        channelTable.setClosedIcon(UIConstants.ICON_GROUP);

        channelTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                channelListSelected(evt);
            }
        });

        // listen for trigger button and double click to edit channel.
        channelTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                checkSelectionAndPopupMenu(evt);
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                checkSelectionAndPopupMenu(evt);
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = channelTable.rowAtPoint(new Point(evt.getX(), evt.getY()));
                if (row == -1) {
                    return;
                }

                if (evt.getClickCount() >= 2 && channelTable.getSelectedRowCount() == 1 && channelTable.getSelectedRow() == row) {
                    AbstractChannelTableNode node = (AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent();
                    if (node.isGroupNode()) {
                        doEditGroupDetails();
                    } else {
                        doEditChannel();
                    }
                }
            }
        });

        // Key Listener trigger for DEL
        channelTable.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (channelTable.getSelectedModelRows().length == 0) {
                        return;
                    }

                    boolean allGroups = true;
                    boolean allChannels = true;
                    for (int row : channelTable.getSelectedModelRows()) {
                        AbstractChannelTableNode node = (AbstractChannelTableNode) channelTable.getPathForRow(row).getLastPathComponent();
                        if (node.isGroupNode()) {
                            allChannels = false;
                        } else {
                            allGroups = false;
                        }
                    }

                    if (allChannels) {
                        doDeleteChannel();
                    } else if (allGroups) {
                        doDeleteGroup();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent evt) {}

            @Override
            public void keyTyped(KeyEvent evt) {}
        });

        // MIRTH-2301
        // Since we are using addHighlighter here instead of using setHighlighters, we need to remove the old ones first.
        channelTable.setHighlighters();

        // Set highlighter.
        if (userPreferences.getBoolean(Messages.getString("ChannelPanel.270"), true)) { //$NON-NLS-1$
            Highlighter highlighter = HighlighterFactory.createAlternateStriping(UIConstants.HIGHLIGHTER_COLOR, UIConstants.BACKGROUND_COLOR);
            channelTable.addHighlighter(highlighter);
        }

        HighlightPredicate revisionDeltaHighlighterPredicate = new HighlightPredicate() {
            @Override
            public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                if (adapter.column == channelTable.convertColumnIndexToView(channelTable.getColumnExt(DEPLOYED_REVISION_DELTA_COLUMN_NAME).getModelIndex())) {
                    if (channelTable.getValueAt(adapter.row, adapter.column) != null && ((Integer) channelTable.getValueAt(adapter.row, adapter.column)).intValue() > 0) {
                        return true;
                    }

                    if (channelStatuses != null) {
                        String channelId = (String) channelTable.getModel().getValueAt(channelTable.convertRowIndexToModel(adapter.row), ID_COLUMN_NUMBER);
                        ChannelStatus status = channelStatuses.get(channelId);
                        if (status != null && status.isCodeTemplatesChanged()) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        channelTable.addHighlighter(new ColorHighlighter(revisionDeltaHighlighterPredicate, new Color(255, 204, 0), Color.BLACK, new Color(255, 204, 0), Color.BLACK));

        HighlightPredicate lastDeployedHighlighterPredicate = new HighlightPredicate() {
            @Override
            public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                if (adapter.column == channelTable.convertColumnIndexToView(channelTable.getColumnExt(LAST_DEPLOYED_COLUMN_NAME).getModelIndex())) {
                    Calendar checkAfter = Calendar.getInstance();
                    checkAfter.add(Calendar.MINUTE, -2);

                    if (channelTable.getValueAt(adapter.row, adapter.column) != null && ((Calendar) channelTable.getValueAt(adapter.row, adapter.column)).after(checkAfter)) {
                        return true;
                    }
                }
                return false;
            }
        };
        channelTable.addHighlighter(new ColorHighlighter(lastDeployedHighlighterPredicate, new Color(240, 230, 140), Color.BLACK, new Color(240, 230, 140), Color.BLACK));

        channelScrollPane = new JScrollPane(channelTable);
        channelScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        filterPanel = new JPanel();
        filterPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(164, 164, 164)));

        filterLabel = new JLabel(Messages.getString("ChannelPanel.271")); //$NON-NLS-1$

        Set<FilterCompletion> tags = new HashSet<FilterCompletion>();
        for (ChannelTag tag : getCachedChannelTags()) {
            tags.add(new TagFilterCompletion(tag));
        }

        tagField = new MirthTagField(Messages.getString("ChannelPanel.272"), false, tags); //$NON-NLS-1$
        tagField.addUpdateSearchListener(new SearchFilterListener() {
            @Override
            public void doSearch(String filterString) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        updateModel(getCurrentTableState());
                        updateTasks();
                    }
                });
            }

            @Override
            public void doDelete(String filterString) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        updateModel(getCurrentTableState());
                        updateTasks();
                    }
                });
            }
        });

        tagsLabel = new JLabel();

        tagModeTextButton = new IconToggleButton(UIConstants.ICON_TEXT);
        tagModeTextButton.setToolTipText(Messages.getString("ChannelPanel.273")); //$NON-NLS-1$
        tagModeTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                updateTagButtons(!tagTextModeSelected, true, true);
                channelTable.updateUI();
            }
        });

        tagModeIconButton = new IconToggleButton(UIConstants.ICON_TAG);
        tagModeIconButton.setToolTipText(Messages.getString("ChannelPanel.274")); //$NON-NLS-1$
        tagModeIconButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                updateTagButtons(!tagIconModeSelected, false, true);
                channelTable.updateUI();
            }
        });

        ButtonGroup tableModeButtonGroup = new ButtonGroup();

        tableModeGroupsButton = new IconToggleButton(UIConstants.ICON_GROUP);
        tableModeGroupsButton.setToolTipText(Messages.getString("ChannelPanel.275")); //$NON-NLS-1$
        tableModeGroupsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (!switchTableMode(true)) {
                    tableModeChannelsButton.setSelected(true);
                }
            }
        });
        tableModeButtonGroup.add(tableModeGroupsButton);

        tableModeChannelsButton = new IconToggleButton(UIConstants.ICON_CHANNEL);
        tableModeChannelsButton.setToolTipText(Messages.getString("ChannelPanel.276")); //$NON-NLS-1$
        tableModeChannelsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (!switchTableMode(false)) {
                    tableModeGroupsButton.setSelected(true);
                }
            }
        });
        tableModeButtonGroup.add(tableModeChannelsButton);

        tabPane = new JTabbedPane();

        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(tabPane);
    }

    private void initLayout() {
        setLayout(new MigLayout(Messages.getString("ChannelPanel.277"))); //$NON-NLS-1$

        topPanel.setLayout(new MigLayout(Messages.getString("ChannelPanel.278"))); //$NON-NLS-1$
        topPanel.add(channelScrollPane, Messages.getString("ChannelPanel.279")); //$NON-NLS-1$

        filterPanel.setLayout(new MigLayout(Messages.getString("ChannelPanel.280"), Messages.getString("ChannelPanel.281"))); //$NON-NLS-1$ //$NON-NLS-2$
        filterPanel.add(filterLabel);
        filterPanel.add(tagField, Messages.getString("ChannelPanel.282")); //$NON-NLS-1$
        filterPanel.add(tagsLabel, Messages.getString("ChannelPanel.283")); //$NON-NLS-1$
        filterPanel.add(tagModeTextButton, Messages.getString("ChannelPanel.284")); //$NON-NLS-1$
        filterPanel.add(tagModeIconButton);
        filterPanel.add(new JSeparator(SwingConstants.VERTICAL), Messages.getString("ChannelPanel.285")); //$NON-NLS-1$
        filterPanel.add(tableModeGroupsButton, Messages.getString("ChannelPanel.286")); //$NON-NLS-1$
        filterPanel.add(tableModeChannelsButton);
        topPanel.add(filterPanel, Messages.getString("ChannelPanel.287")); //$NON-NLS-1$

        add(splitPane, Messages.getString("ChannelPanel.288")); //$NON-NLS-1$
    }

    private boolean switchTableMode(boolean groupModeEnabled) {
        return switchTableMode(groupModeEnabled, true);
    }

    private boolean switchTableMode(boolean groupModeEnabled, boolean promptSave) {
        if (!canViewChannelGroups) {
            groupModeEnabled = false;
        }

        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();
        if (model.isGroupModeEnabled() != groupModeEnabled) {
            if (promptSave && isSaveEnabled() && !promptSave(true)) {
                return false;
            }

            userPreferences.putBoolean(Messages.getString("ChannelPanel.289"), groupModeEnabled); //$NON-NLS-1$

            List<JXTaskPane> taskPanes = new ArrayList<JXTaskPane>();
            taskPanes.add(channelTasks);

            if (groupModeEnabled) {
                tableModeChannelsButton.setContentFilled(false);
                taskPanes.add(groupTasks);
            } else {
                tableModeGroupsButton.setContentFilled(false);
            }

            for (TaskPlugin plugin : LoadedExtensions.getInstance().getTaskPlugins().values()) {
                JXTaskPane taskPane = plugin.getTaskPane();
                if (taskPane != null) {
                    taskPanes.add(taskPane);
                }
            }
            parent.setFocus(taskPanes.toArray(new JXTaskPane[taskPanes.size()]), true, true);

            TableState tableState = getCurrentTableState();
            model.setGroupModeEnabled(groupModeEnabled);
            updateModel(tableState);
            updateTasks();
        }

        return true;
    }

    private TableState getCurrentTableState() {
        List<String> selectedIds = new ArrayList<String>();
        List<String> expandedGroupIds = null;

        int[] selectedRows = channelTable.getSelectedModelRows();
        for (int i = 0; i < selectedRows.length; i++) {
            AbstractChannelTableNode node = (AbstractChannelTableNode) channelTable.getPathForRow(selectedRows[i]).getLastPathComponent();
            if (node.isGroupNode()) {
                selectedIds.add(node.getGroupStatus().getGroup().getId());
            } else {
                selectedIds.add(node.getChannelStatus().getChannel().getId());
            }
        }

        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();
        if (model.isGroupModeEnabled()) {
            MutableTreeTableNode root = (MutableTreeTableNode) model.getRoot();
            if (root != null && root.getChildCount() > 0) {
                expandedGroupIds = new ArrayList<String>();

                for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
                    AbstractChannelTableNode groupNode = (AbstractChannelTableNode) groupNodes.nextElement();
                    if (channelTable.isExpanded(new TreePath(new Object[] { root,
                            groupNode })) || groupNode.getChildCount() == 0) {
                        expandedGroupIds.add(groupNode.getGroupStatus().getGroup().getId());
                    }
                }
            }
        }

        return new TableState(selectedIds, expandedGroupIds);
    }

    private void restoreTableState(TableState tableState) {
        ChannelTreeTableModel model = (ChannelTreeTableModel) channelTable.getTreeTableModel();
        MutableTreeTableNode root = (MutableTreeTableNode) model.getRoot();

        if (model.isGroupModeEnabled()) {
            if (tableState.getExpandedGroupIds() != null && root != null) {
                channelTable.collapseAll();

                for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
                    AbstractChannelTableNode groupNode = (AbstractChannelTableNode) groupNodes.nextElement();
                    if (tableState.getExpandedGroupIds().contains(groupNode.getGroupStatus().getGroup().getId())) {
                        channelTable.expandPath(new TreePath(new Object[] { root, groupNode }));
                    }
                }
            } else {
                channelTable.expandAll();
            }
        }

        final List<TreePath> selectionPaths = new ArrayList<TreePath>();

        for (Enumeration<? extends MutableTreeTableNode> children = root.children(); children.hasMoreElements();) {
            AbstractChannelTableNode child = (AbstractChannelTableNode) children.nextElement();
            if (child.isGroupNode() && tableState.getSelectedIds().contains(child.getGroupStatus().getGroup().getId()) || !child.isGroupNode() && tableState.getSelectedIds().contains(child.getChannelStatus().getChannel().getId())) {
                TreePath path = new TreePath(new Object[] { root, child });
                selectionPaths.add(path);
            }

            if (model.isGroupModeEnabled()) {
                for (Enumeration<? extends MutableTreeTableNode> channelNodes = child.children(); channelNodes.hasMoreElements();) {
                    AbstractChannelTableNode channelNode = (AbstractChannelTableNode) channelNodes.nextElement();
                    if (tableState.getSelectedIds().contains(channelNode.getChannelStatus().getChannel().getId())) {
                        TreePath path = new TreePath(new Object[] { root, child, channelNode });
                        selectionPaths.add(path);
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(selectionPaths)) {
            channelTable.getTreeSelectionModel().addSelectionPaths(selectionPaths.toArray(new TreePath[selectionPaths.size()]));
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                channelTable.getTreeSelectionModel().addSelectionPaths(selectionPaths.toArray(new TreePath[selectionPaths.size()]));
            }
        });
    }

    private class TableState {
        private List<String> selectedIds = new ArrayList<String>();
        private List<String> expandedGroupIds = new ArrayList<String>();

        public TableState(List<String> selectedIds, List<String> expandedGroupIds) {
            this.selectedIds = selectedIds;
            this.expandedGroupIds = expandedGroupIds;
        }

        public List<String> getSelectedIds() {
            return selectedIds;
        }

        public List<String> getExpandedGroupIds() {
            return expandedGroupIds;
        }
    }

    private class GroupDetailsDialog extends MirthDialog {

        private boolean saved = false;
        private boolean newGroup;

        public GroupDetailsDialog(boolean newGroup) {
            super(parent, true);
            this.newGroup = newGroup;

            initComponents();
            initLayout();

            if (newGroup) {
                String name;
                int index = 1;
                do {
                    name = Messages.getString("ChannelPanel.290") + index++; //$NON-NLS-1$
                } while (!checkGroupName(name));

                groupNameField.setText(name);
                groupNameField.requestFocus();
                groupNameField.selectAll();
            } else {
                AbstractChannelTableNode selectedNode = (AbstractChannelTableNode) channelTable.getPathForRow(channelTable.getSelectedRow()).getLastPathComponent();
                groupNameField.setText(selectedNode.getGroupStatus().getGroup().getName());
                groupDescriptionScrollPane.setText(selectedNode.getGroupStatus().getGroup().getDescription());

                groupNameField.requestFocus();
                groupNameField.setCaretPosition(groupNameField.getDocument().getLength());
            }

            setPreferredSize(new Dimension(600, 375));
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setTitle(Messages.getString("ChannelPanel.291")); //$NON-NLS-1$
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }

        public boolean wasSaved() {
            return saved;
        }

        public String getGroupName() {
            return groupNameField.getText();
        }

        public String getGroupDescription() {
            return groupDescriptionScrollPane.getText();
        }

        private void initComponents() {
            setBackground(UIConstants.BACKGROUND_COLOR);
            getContentPane().setBackground(getBackground());

            containerPanel = new JPanel();
            containerPanel.setBackground(getBackground());
            containerPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("ChannelPanel.292"))); //$NON-NLS-1$

            groupNameLabel = new JLabel(Messages.getString("ChannelPanel.293")); //$NON-NLS-1$
            groupNameField = new JTextField();

            groupDescriptionLabel = new JLabel(Messages.getString("ChannelPanel.294")); //$NON-NLS-1$
            groupDescriptionScrollPane = new MirthRTextScrollPane(null, false, SyntaxConstants.SYNTAX_STYLE_NONE, false);
            groupDescriptionScrollPane.setSaveEnabled(false);

            separator = new JSeparator(SwingConstants.HORIZONTAL);

            okButton = new JButton(Messages.getString("ChannelPanel.295")); //$NON-NLS-1$
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    String name = groupNameField.getText();

                    if (StringUtils.isBlank(name)) {
                        groupNameField.setBackground(UIConstants.INVALID_COLOR);
                        parent.alertError(GroupDetailsDialog.this, Messages.getString("ChannelPanel.296")); //$NON-NLS-1$
                        return;
                    }

                    if (!checkGroupName(name, newGroup)) {
                        groupNameField.setBackground(UIConstants.INVALID_COLOR);
                        parent.alertError(GroupDetailsDialog.this, Messages.getString("ChannelPanel.297")); //$NON-NLS-1$
                        return;
                    }

                    saved = true;
                    dispose();
                }
            });

            cancelButton = new JButton(Messages.getString("ChannelPanel.298")); //$NON-NLS-1$
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    dispose();
                }
            });
        }

        private void initLayout() {
            setLayout(new MigLayout(Messages.getString("ChannelPanel.299"))); //$NON-NLS-1$

            containerPanel.setLayout(new MigLayout(Messages.getString("ChannelPanel.300"))); //$NON-NLS-1$
            containerPanel.add(groupNameLabel, Messages.getString("ChannelPanel.301")); //$NON-NLS-1$
            containerPanel.add(groupNameField, Messages.getString("ChannelPanel.302")); //$NON-NLS-1$
            containerPanel.add(groupDescriptionLabel, Messages.getString("ChannelPanel.303")); //$NON-NLS-1$
            containerPanel.add(groupDescriptionScrollPane, Messages.getString("ChannelPanel.304")); //$NON-NLS-1$
            add(containerPanel, Messages.getString("ChannelPanel.305")); //$NON-NLS-1$

            add(separator, Messages.getString("ChannelPanel.306")); //$NON-NLS-1$
            add(okButton, Messages.getString("ChannelPanel.307")); //$NON-NLS-1$
            add(cancelButton, Messages.getString("ChannelPanel.308")); //$NON-NLS-1$
        }

        private JPanel containerPanel;
        private JLabel groupNameLabel;
        private JTextField groupNameField;
        private JLabel groupDescriptionLabel;
        private MirthRTextScrollPane groupDescriptionScrollPane;
        private JSeparator separator;
        private JButton okButton;
        private JButton cancelButton;
    }

    private class GroupAssignmentDialog extends MirthDialog {

        private boolean saved = false;

        public GroupAssignmentDialog() {
            super(parent, true);

            initComponents();
            initLayout();

            setPreferredSize(new Dimension(337, 118));
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setTitle(Messages.getString("ChannelPanel.309")); //$NON-NLS-1$
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }

        public boolean wasSaved() {
            return saved;
        }

        public String getSelectedGroupId() {
            return ((Pair<String, String>) groupComboBox.getSelectedItem()).getLeft();
        }

        private void initComponents() {
            setBackground(UIConstants.BACKGROUND_COLOR);
            getContentPane().setBackground(getBackground());

            groupComboBox = new JComboBox<Pair<String, String>>();
            List<Pair<String, String>> groups = new ArrayList<Pair<String, String>>();
            for (Enumeration<? extends MutableTreeTableNode> groupNodes = ((MutableTreeTableNode) channelTable.getTreeTableModel().getRoot()).children(); groupNodes.hasMoreElements();) {
                ChannelGroup group = ((AbstractChannelTableNode) groupNodes.nextElement()).getGroupStatus().getGroup();

                groups.add(new MutablePair<String, String>(group.getId(), group.getName()) {
                    @Override
                    public String toString() {
                        return getRight();
                    }
                });
            }
            groupComboBox.setModel(new DefaultComboBoxModel<Pair<String, String>>(groups.toArray(new Pair[groups.size()])));
            groupComboBox.setSelectedIndex(0);

            separator = new JSeparator(SwingConstants.HORIZONTAL);

            okButton = new JButton(Messages.getString("ChannelPanel.310")); //$NON-NLS-1$
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    saved = true;
                    dispose();
                }
            });

            cancelButton = new JButton(Messages.getString("ChannelPanel.311")); //$NON-NLS-1$
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    dispose();
                }
            });
        }

        private void initLayout() {
            setLayout(new MigLayout(Messages.getString("ChannelPanel.312"))); //$NON-NLS-1$

            add(new JLabel(Messages.getString("ChannelPanel.313"))); //$NON-NLS-1$
            add(groupComboBox, Messages.getString("ChannelPanel.314")); //$NON-NLS-1$
            add(separator, Messages.getString("ChannelPanel.315")); //$NON-NLS-1$
            add(okButton, Messages.getString("ChannelPanel.316")); //$NON-NLS-1$
            add(cancelButton, Messages.getString("ChannelPanel.317")); //$NON-NLS-1$
        }

        private JComboBox<Pair<String, String>> groupComboBox;
        private JSeparator separator;
        private JButton okButton;
        private JButton cancelButton;
    }

    public JXTaskPane channelTasks;
    public JPopupMenu channelPopupMenu;
    public JXTaskPane groupTasks;
    public JPopupMenu groupPopupMenu;

    private JSplitPane splitPane;
    private JPanel topPanel;
    private MirthTreeTable channelTable;
    private JScrollPane channelScrollPane;
    private JPanel filterPanel;
    private JLabel filterLabel;
    private MirthTagField tagField;
    private JLabel tagsLabel;
    private IconToggleButton tagModeTextButton;
    private IconToggleButton tagModeIconButton;
    private IconToggleButton tableModeGroupsButton;
    private IconToggleButton tableModeChannelsButton;

    private JTabbedPane tabPane;
}
