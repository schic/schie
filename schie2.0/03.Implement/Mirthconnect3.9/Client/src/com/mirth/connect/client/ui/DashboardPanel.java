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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.core.TaskConstants;
import com.mirth.connect.client.ui.components.IconToggleButton;
import com.mirth.connect.client.ui.components.MirthTreeTable;
import com.mirth.connect.client.ui.components.tag.ChannelNameFilterCompletion;
import com.mirth.connect.client.ui.components.tag.FilterCompletion;
import com.mirth.connect.client.ui.components.tag.MirthTagField;
import com.mirth.connect.client.ui.components.tag.SearchFilterListener;
import com.mirth.connect.client.ui.components.tag.TagFilterCompletion;
import com.mirth.connect.donkey.model.channel.DeployedState;
import com.mirth.connect.model.Channel;
import com.mirth.connect.model.ChannelGroup;
import com.mirth.connect.model.ChannelTag;
import com.mirth.connect.model.DashboardStatus;
import com.mirth.connect.model.DashboardStatus.StatusType;
import com.mirth.connect.model.filter.SearchFilter;
import com.mirth.connect.model.filter.SearchFilterParser;
import com.mirth.connect.plugins.DashboardColumnPlugin;
import com.mirth.connect.plugins.DashboardPanelPlugin;
import com.mirth.connect.plugins.DashboardTabPlugin;
import com.mirth.connect.plugins.DashboardTablePlugin;

public class DashboardPanel extends JPanel {

    private static final String STATUS_COLUMN_NAME = Messages.getString("DashboardPanel.0"); //$NON-NLS-1$
    private static final String NAME_COLUMN_NAME = Messages.getString("DashboardPanel.1"); //$NON-NLS-1$
    private static final String RECEIVED_COLUMN_NAME = Messages.getString("DashboardPanel.2"); //$NON-NLS-1$
    private static final String QUEUED_COLUMN_NAME = Messages.getString("DashboardPanel.3"); //$NON-NLS-1$
    private static final String SENT_COLUMN_NAME = Messages.getString("DashboardPanel.4"); //$NON-NLS-1$
    private static final String ERROR_COLUMN_NAME = Messages.getString("DashboardPanel.5"); //$NON-NLS-1$
    private static final String FILTERED_COLUMN_NAME = Messages.getString("DashboardPanel.6"); //$NON-NLS-1$
    private static final String LAST_DEPLOYED_COLUMN_NAME = Messages.getString("DashboardPanel.7"); //$NON-NLS-1$
    private static final String DEPLOYED_REVISION_DELTA_COLUMN_NAME = Messages.getString("DashboardPanel.8"); //$NON-NLS-1$
    private static final String[] defaultColumns = new String[] { STATUS_COLUMN_NAME,
            NAME_COLUMN_NAME, DEPLOYED_REVISION_DELTA_COLUMN_NAME, LAST_DEPLOYED_COLUMN_NAME,
            RECEIVED_COLUMN_NAME, FILTERED_COLUMN_NAME, QUEUED_COLUMN_NAME, SENT_COLUMN_NAME,
            ERROR_COLUMN_NAME };

    private Frame parent;
    private boolean showLifetimeStats = false;
    private Preferences userPreferences;
    private boolean tagTextModeSelected = false;
    private boolean tagIconModeSelected = false;
    private boolean canViewChannelGroups = AuthorizationControllerFactory.getAuthorizationController().checkTask(TaskConstants.CHANNEL_GROUP_KEY, TaskConstants.CHANNEL_GROUP_EXPORT_GROUP);

    private Set<String> defaultVisibleColumns;
    private Set<DeployedState> haltableStates = new HashSet<DeployedState>();
    private ListSelectionListener listSelectionListener;

    public DashboardPanel() {
        this.parent = PlatformUI.MIRTH_FRAME;
        userPreferences = Preferences.userNodeForPackage(Mirth.class);

        haltableStates.add(DeployedState.DEPLOYING);
        haltableStates.add(DeployedState.UNDEPLOYING);
        haltableStates.add(DeployedState.STARTING);
        haltableStates.add(DeployedState.STOPPING);
        haltableStates.add(DeployedState.PAUSING);
        haltableStates.add(DeployedState.SYNCING);
        haltableStates.add(DeployedState.UNKNOWN);

        initComponents();
        initLayout();

        loadTabPlugins();
        ChangeListener changeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();

                if (LoadedExtensions.getInstance().getDashboardTabPlugins().size() > 0) {
                    loadPanelPlugin(LoadedExtensions.getInstance().getDashboardTabPlugins().get(sourceTabbedPane.getTitleAt(index)));
                }
            }
        };
        tabPane.addChangeListener(changeListener);

        defaultVisibleColumns = new LinkedHashSet<String>();

        makeStatusTable();
        loadTablePlugins();

        this.setDoubleBuffered(true);

        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();

        if (canViewChannelGroups && userPreferences.getBoolean(Messages.getString("DashboardPanel.9"), true)) { //$NON-NLS-1$
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

        updateTagButtons(userPreferences.getBoolean(Messages.getString("DashboardPanel.10"), true), userPreferences.getBoolean(Messages.getString("DashboardPanel.11"), false), false); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void loadTabPlugins() {
        if (LoadedExtensions.getInstance().getDashboardTabPlugins().size() > 0) {
            for (DashboardTabPlugin plugin : LoadedExtensions.getInstance().getDashboardTabPlugins().values()) {
                if (plugin.getTabComponent() != null) {
                    tabPane.addTab(plugin.getPluginPointName(), plugin.getTabComponent());
                }
            }

            splitPane.setBottomComponent(tabPane);
            splitPane.setDividerSize(6);
            splitPane.setDividerLocation(3 * userPreferences.getInt(Messages.getString("DashboardPanel.12"), UIConstants.MIRTH_HEIGHT) / 5); //$NON-NLS-1$
            splitPane.setResizeWeight(0.5);
        }
    }

    public void closePopupWindow() {
        tagField.closePopupWindow();
    }

    public String getUserTags() {
        return tagField.getTags();
    }

    public void updateTags(Set<String> channelNames, boolean updateUserTags) {
        Set<FilterCompletion> tags = new HashSet<FilterCompletion>();
        for (String name : channelNames) {
            tags.add(new ChannelNameFilterCompletion(name));
        }

        for (ChannelTag channelTag : parent.getCachedChannelTags()) {
            tags.add(new TagFilterCompletion(channelTag));
        }

        tagField.update(tags, false, updateUserTags, true);
    }

    private void loadTablePlugins() {
        pluginContainerPanel.setLayout(new MigLayout(Messages.getString("DashboardPanel.13"), Messages.getString("DashboardPanel.14"), Messages.getString("DashboardPanel.15"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        for (DashboardTablePlugin plugin : LoadedExtensions.getInstance().getDashboardTablePlugins().values()) {
            for (JComponent component : plugin.getToolbarComponents(dashboardTable)) {
                pluginContainerPanel.add(component, Messages.getString("DashboardPanel.16")); //$NON-NLS-1$
            }
        }

        for (DashboardTablePlugin plugin : LoadedExtensions.getInstance().getDashboardTablePlugins().values()) {
            plugin.onDashboardInit(dashboardTable);
        }

        repaint();
    }

    public void loadPanelPlugin(final DashboardPanelPlugin plugin) {
        final List<DashboardStatus> selectedStatuses = getSelectedStatuses();

        QueuingSwingWorkerTask<Void, Void> task = new QueuingSwingWorkerTask<Void, Void>(plugin.getPluginPointName(), Messages.getString("DashboardPanel.17") + plugin.getPluginPointName() + Messages.getString("DashboardPanel.18")) { //$NON-NLS-1$ //$NON-NLS-2$
            @Override
            public Void doInBackground() {
                try {
                    if (selectedStatuses.size() != 0) {
                        plugin.prepareData(selectedStatuses);
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
                if (selectedStatuses.size() != 0) {
                    plugin.update(selectedStatuses);
                } else {
                    plugin.update();
                }
            }
        };

        new QueuingSwingWorker<Void, Void>(task, true).executeDelegate();
    }

    public DashboardTabPlugin getCurrentTabPlugin() {
        return LoadedExtensions.getInstance().getDashboardTabPlugins().get(tabPane.getTitleAt(tabPane.getSelectedIndex()));
    }

    public void switchPanel() {
        boolean groupViewEnabled = canViewChannelGroups && userPreferences.getBoolean(Messages.getString("DashboardPanel.19"), true); //$NON-NLS-1$
        switchTableMode(groupViewEnabled);

        if (groupViewEnabled) {
            tableModeGroupsButton.setSelected(true);
            tableModeGroupsButton.setContentFilled(true);
            tableModeChannelsButton.setContentFilled(false);
        } else {
            tableModeChannelsButton.setSelected(true);
            tableModeChannelsButton.setContentFilled(true);
            tableModeGroupsButton.setContentFilled(false);
        }

        updateTagButtons(userPreferences.getBoolean(Messages.getString("DashboardPanel.20"), true), userPreferences.getBoolean(Messages.getString("DashboardPanel.21"), false), false); //$NON-NLS-1$ //$NON-NLS-2$

        updateTags(new HashSet<String>(parent.channelPanel.getCachedChannelIdsAndNames().values()), true);
        tagField.setUserPreferenceTags();
    }

    private void updateTagButtons(boolean showTags, boolean textMode, boolean updatePreferences) {
        tagModeTextButton.setSelected(showTags && textMode);
        tagModeIconButton.setSelected(showTags && !textMode);
        tagModeTextButton.setContentFilled(showTags && textMode);
        tagModeIconButton.setContentFilled(showTags && !textMode);
        dashboardTable.setTreeCellRenderer(new TagTreeCellRenderer(showTags, textMode));
        tagTextModeSelected = showTags && textMode;
        tagIconModeSelected = showTags && !textMode;

        if (updatePreferences) {
            userPreferences.putBoolean(Messages.getString("DashboardPanel.22"), showTags); //$NON-NLS-1$
            userPreferences.putBoolean(Messages.getString("DashboardPanel.23"), textMode); //$NON-NLS-1$
        }
    }

    /**
     * Makes the status table with all current server information.
     */
    public void makeStatusTable() {
        List<String> columns = new ArrayList<String>();

        for (DashboardColumnPlugin plugin : LoadedExtensions.getInstance().getDashboardColumnPlugins().values()) {
            if (plugin.isDisplayFirst()) {
                columns.add(plugin.getColumnHeader());
            }
        }

        columns.addAll(Arrays.asList(defaultColumns));

        for (DashboardColumnPlugin plugin : LoadedExtensions.getInstance().getDashboardColumnPlugins().values()) {
            if (!plugin.isDisplayFirst()) {
                columns.add(plugin.getColumnHeader());
            }
        }

        DashboardTreeTableModel model = new DashboardTreeTableModel();
        model.setColumnIdentifiers(columns);
        model.setNodeFactory(new DefaultDashboardTableNodeFactory());

        for (DashboardTablePlugin plugin : LoadedExtensions.getInstance().getDashboardTablePlugins().values()) {
            dashboardTable = plugin.getTable();

            if (dashboardTable != null) {
                break;
            }
        }

        defaultVisibleColumns.addAll(columns);

        if (dashboardTable == null) {
            dashboardTable = new MirthTreeTable(Messages.getString("DashboardPanel.24"), defaultVisibleColumns); //$NON-NLS-1$
        }

        dashboardTable.setColumnFactory(new DashboardTableColumnFactory());
        dashboardTable.setTreeTableModel(model);
        dashboardTable.setDoubleBuffered(true);
        dashboardTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        dashboardTable.setHorizontalScrollEnabled(true);
        dashboardTable.packTable(UIConstants.COL_MARGIN);
        dashboardTable.setRowHeight(UIConstants.ROW_HEIGHT);
        dashboardTable.setOpaque(true);
        dashboardTable.setRowSelectionAllowed(true);
        dashboardTable.setSortable(true);
        dashboardTable.putClientProperty(Messages.getString("DashboardPanel.25"), Messages.getString("DashboardPanel.26")); //$NON-NLS-1$ //$NON-NLS-2$
        dashboardTable.setAutoCreateColumnsFromModel(false);
        dashboardTable.setShowGrid(true, true);
        dashboardTable.restoreColumnPreferences();
        dashboardTable.setMirthColumnControlEnabled(true);

        dashboardTable.setTreeCellRenderer(new TagTreeCellRenderer(true, false));

        dashboardTable.setLeafIcon(UIConstants.ICON_CONNECTOR);
        dashboardTable.setOpenIcon(UIConstants.ICON_CHANNEL);
        dashboardTable.setClosedIcon(UIConstants.ICON_CHANNEL);

        dashboardTableScrollPane.setViewportView(dashboardTable);

        dashboardTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                checkSelectionAndPopupMenu(event);
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                checkSelectionAndPopupMenu(event);
            }

            @Override
            public void mouseClicked(MouseEvent event) {
                int clickedRow = dashboardTable.rowAtPoint(new Point(event.getX(), event.getY()));
                if (clickedRow == -1) {
                    return;
                }

                TreePath path = dashboardTable.getPathForRow(clickedRow);
                if (path != null && ((AbstractDashboardTableNode) path.getLastPathComponent()).isGroupNode()) {
                    return;
                }

                if (event.getClickCount() >= 2 && dashboardTable.getSelectedRowCount() == 1 && dashboardTable.getSelectedRow() == clickedRow) {
                    parent.doShowMessages();
                }
            }
        });

        listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                /*
                 * MIRTH-3199: Only update the panel plugin if the selection is finished. This does
                 * mean that the logs aren't updated live when adding to or removing from a
                 * currently adjusting selection, but it's much more efficient when it comes to the
                 * number of requests being done from the client. Plus, it actually will still
                 * update while a selection is adjusting if the refresh interval on the dashboard
                 * elapses. We can change this so that plugins are updated during a selection
                 * adjustment, but it would first require a major rewrite of the connection log /
                 * status column plugin.
                 */
                updatePopupMenu(!event.getValueIsAdjusting());
            }
        };
        dashboardTable.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    /**
     * Shows the popup menu when the trigger button (right-click) has been pushed. Deselects the
     * rows if no row was selected.
     */
    private void checkSelectionAndPopupMenu(MouseEvent event) {
        TreePath path = dashboardTable.getPathForLocation(event.getX(), event.getY());

        /*
         * On mouse events we don't need to update the dashboard panel plugins. They will already
         * have been updated because of the ListSelectionEvent, and multiple mouse events will enter
         * here (as many as three, one pressed and two released) so we would basically be doing four
         * times the work.
         */
        if (path == null) {
            deselectRows(false);
        } else {
            updatePopupMenu(false);
        }

        if (event.isPopupTrigger()) {
            TreeSelectionModel selectionModel = dashboardTable.getTreeSelectionModel();

            if (!selectionModel.isPathSelected(path)) {
                deselectRows(false);
                selectionModel.addSelectionPath(path);
            }

            parent.dashboardPopupMenu.show(event.getComponent(), event.getX(), event.getY());
        }
    }

    /*
     * Action when something on the status list has been selected. Sets all appropriate tasks
     * visible.
     */
    private void updatePopupMenu(boolean loadPanelPlugin) {
        // @formatter:off
        /*
         * 0 - Refresh
         * 1 - Send Message
         * 2 - View Messages
         * 3 - Remove All Messages
         * 4 - Clear Statistics
         * 5 - Start
         * 6 - Pause
         * 7 - Stop
         * 8 - Halt
         * 9 - Undeploy Channel
         * 10 - Start Connector
         * 11 - Stop Connector
         */
        // @formatter:on

        parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 1, -1, false); // hide all
        parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 2, 2, true); // show "View Messages"
        parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 4, 4, true); // show "Clear Statistics"

        // Indicates if any channel nodes are selected.
        boolean useChannelOptions = true;
        // Indicates if only nodes for a single channel is selected. This is true even if only a connector node is selected.
        boolean singleChannel = true;
        // Indicates whether any haltable channel states are selected, ignoring syncing
        boolean containsHaltableNonSyncingState = false;
        // Stores all channel ids that are selected, even if only a channel's connector is selected
        Set<String> selectedChannelIds = new HashSet<String>();
        // Stores all channel ids that have their channel node selected.
        Set<String> selectedChannelNodes = new HashSet<String>();

        List<AbstractDashboardTableNode> selectedNodes = dashboardTable.getSelectedNodes();

        if (selectedNodes.size() > 0) {
            for (AbstractDashboardTableNode node : selectedNodes) {
                if (!node.isGroupNode()) {
                    selectedChannelIds.add(node.getChannelId());
                    if (node.getDashboardStatus().getStatusType() == StatusType.CHANNEL) {
                        if (haltableStates.contains(node.getDashboardStatus().getState()) && node.getDashboardStatus().getState() != DeployedState.SYNCING) {
                            containsHaltableNonSyncingState = true;
                        }
                        selectedChannelNodes.add(node.getChannelId());
                    } else if (!selectedChannelNodes.contains(node.getChannelId())) {
                        useChannelOptions = false;
                    }
                }

                if (selectedChannelIds.size() > 1) {
                    singleChannel = false;
                }
            }
        } else {
            parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 2, 2, false);
            parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 4, 4, false);
        }

        for (AbstractDashboardTableNode node : selectedNodes) {
            if (!node.isGroupNode()) {
                DashboardStatus status = node.getDashboardStatus();
                StatusType statusType = status.getStatusType();

                if (useChannelOptions) {
                    if (statusType == StatusType.CHANNEL) {

                        switch (status.getState()) {
                            case STARTED:
                                parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 6, 7, true);
                                break;
                            case PAUSED:
                                parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 5, 5, true);
                                parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 7, 7, true);
                                break;
                            case STOPPED:
                                parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 5, 5, true);
                                break;
                            default:
                                break;
                        }

                        parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 1, 1, true);
                        parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 3, 3, true);
                        parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 9, 9, true);

                        if (isHaltable(node)) {
                            // Hide tasks that can not be performed on a channel that is haltable
                            if (status.getState() == DeployedState.DEPLOYING || status.getState() == DeployedState.UNDEPLOYING) {
                                // If the channel is still deploying or undeploying, don't show anything except refresh
                                parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 1, 8, false);
                            } else {
                                parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 3, 3, false);
                                parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 5, 8, false);
                            }

                            if (containsHaltableNonSyncingState) {
                                // Disable the undeploy task if there are any haltable states (ignoring syncing)
                                parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 9, 9, false);
                            }

                            if (singleChannel) {
                                // Show the halt task only if a single channel is selected
                                parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 8, 8, true);
                            } else {
                                break;
                            }
                        }
                    }
                } else if (selectedChannelNodes.size() == 0) {
                    AbstractDashboardTableNode channelNode = (AbstractDashboardTableNode) node.getParent();
                    if (channelNode.getDashboardStatus().getState() != DeployedState.STARTED && channelNode.getDashboardStatus().getState() != DeployedState.PAUSED) {
                        parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 10, 11, false);
                        break;
                    }

                    switch (status.getState()) {
                        case STARTED:
                            parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 11, 11, true);
                            break;
                        case STOPPED:
                            parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 10, 10, true);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        if (showLifetimeStats) {
            parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 4, 4, false);
        }

        if (loadPanelPlugin) {
            for (DashboardPanelPlugin plugin : LoadedExtensions.getInstance().getDashboardTablePlugins().values()) {
                loadPanelPlugin(plugin);
            }

            loadPanelPlugin(getCurrentTabPlugin());
        }
    }

    /**
     * Checks if this node or any of its children are in a haltable state
     * 
     * @param node
     * @return
     */
    private boolean isHaltable(AbstractDashboardTableNode node) {
        DeployedState nodeState = node.getDashboardStatus().getState();

        if (haltableStates.contains(nodeState)) {
            return true;
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (isHaltable((AbstractDashboardTableNode) node.getChildAt(i))) {
                    return true;
                }
            }
        }

        return false;
    }

    public synchronized void updateTableChannelNodes(List<DashboardStatus> intermediateStatuses) {
        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();
        model.setStatuses(intermediateStatuses);
        model.setShowLifetimeStats(showLifetimeStatsButton.isSelected());

        updateTableHighlighting();
    }

    public synchronized void finishUpdatingTable(List<DashboardStatus> finishedStatuses, Collection<ChannelGroupStatus> channelGroupStatuses, int totalDeployedCount) {
        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();

        model.finishStatuses(finishedStatuses);
        model.setShowLifetimeStats(showLifetimeStatsButton.isSelected());

        // The ListSelectionListener is not notified that the tree table model has changed so we must update the menu items manually.
        // If we switch everything to use a TreeSelectionListener then we should remove this.
        if (dashboardTable.getSelectedRowCount() == 0) {
            deselectRows(true);
        } else {
            updatePopupMenu(true);
        }
        updateTableHighlighting();

        Map<String, DashboardStatus> statusMap = new HashMap<String, DashboardStatus>();
        for (DashboardStatus status : parent.status) {
            statusMap.put(status.getChannelId(), status);
        }

        List<ChannelGroupStatus> filteredGroupStatuses = new ArrayList<ChannelGroupStatus>(channelGroupStatuses);

        for (Iterator<ChannelGroupStatus> it = filteredGroupStatuses.iterator(); it.hasNext();) {
            ChannelGroup group = it.next().getGroup();
            boolean found = false;

            for (Channel channel : group.getChannels()) {
                DashboardStatus dashboardStatus = statusMap.get(channel.getId());
                if (dashboardStatus != null) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                it.remove();
            }
        }

        model.setGroupStatuses(filteredGroupStatuses);

        Map<String, String> channelNameMap = new HashMap<String, String>();
        for (DashboardStatus status : finishedStatuses) {
            channelNameMap.put(status.getChannelId(), status.getName());
        }

        updateTags(new HashSet<String>(channelNameMap.values()), false);

        updateTagsLabel(channelGroupStatuses.size(), filteredGroupStatuses.size(), totalDeployedCount, finishedStatuses.size());
    }

    private void updateTagsLabel(int totalGroupCount, int visibleGroupCount, int totalChannelCount, int visibleChannelCount) {
        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();

        if (tagField.isFilterEnabled()) {
            StringBuilder builder = new StringBuilder();

            if (model.isGroupModeEnabled()) {
                if (totalGroupCount == visibleGroupCount) {
                    builder.append(totalGroupCount);
                } else {
                    builder.append(visibleGroupCount).append(Messages.getString("DashboardPanel.27")).append(totalGroupCount); //$NON-NLS-1$
                }

                builder.append(Messages.getString("DashboardPanel.28")); //$NON-NLS-1$
                if (totalGroupCount != 1) {
                    builder.append('s');
                }

                if (totalGroupCount != visibleGroupCount) {
                    builder.append(Messages.getString("DashboardPanel.29")).append(totalGroupCount - visibleGroupCount).append(Messages.getString("DashboardPanel.30")); //$NON-NLS-1$ //$NON-NLS-2$
                }
                builder.append(Messages.getString("DashboardPanel.31")); //$NON-NLS-1$
            }

            if (totalChannelCount == visibleChannelCount) {
                builder.append(totalChannelCount);
            } else {
                builder.append(visibleChannelCount).append(Messages.getString("DashboardPanel.32")).append(totalChannelCount); //$NON-NLS-1$
            }

            builder.append(Messages.getString("DashboardPanel.33")); //$NON-NLS-1$
            if (totalChannelCount != 1) {
                builder.append('s');
            }

            if (totalChannelCount != visibleChannelCount) {
                builder.append(Messages.getString("DashboardPanel.34")).append(totalChannelCount - visibleChannelCount).append(Messages.getString("DashboardPanel.35")); //$NON-NLS-1$ //$NON-NLS-2$
            }

            List<String> activeFilters = new ArrayList<String>();
            for (SearchFilter filter : SearchFilterParser.parse(tagField.getTags(), parent.getCachedChannelTags())) {
                activeFilters.add(filter.toDisplayString());
            }

            builder.append(Messages.getString("DashboardPanel.36")); //$NON-NLS-1$
            for (Iterator<String> it = activeFilters.iterator(); it.hasNext();) {
                builder.append(it.next());
                if (it.hasNext()) {
                    builder.append(Messages.getString("DashboardPanel.37")); //$NON-NLS-1$
                }
            }
            builder.append(')');

            tagsLabel.setText(builder.toString());
        } else if (model.isGroupModeEnabled()) {
            tagsLabel.setText(totalGroupCount + Messages.getString("DashboardPanel.38") + totalChannelCount + Messages.getString("DashboardPanel.39")); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            tagsLabel.setText(totalChannelCount + Messages.getString("DashboardPanel.40")); //$NON-NLS-1$
        }
    }

    public void updateTableState(TableState tableState) {
        restoreTableState(tableState);
        updatePopupMenu(false);
    }

    public synchronized void updateTableHighlighting() {
        // MIRTH-2301
        // Since we are using addHighlighter here instead of using setHighlighters, we need to remove the old ones first.
        dashboardTable.setHighlighters();

        // Add the highlighters. Always add the error highlighter.
        if (userPreferences.getBoolean(Messages.getString("DashboardPanel.41"), true)) { //$NON-NLS-1$
            Highlighter highlighter = HighlighterFactory.createAlternateStriping(UIConstants.HIGHLIGHTER_COLOR, UIConstants.BACKGROUND_COLOR);
            dashboardTable.addHighlighter(highlighter);
        }

        HighlightPredicate queuedHighlighterPredicate = new HighlightPredicate() {
            public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                if (adapter.column == dashboardTable.getColumnViewIndex(QUEUED_COLUMN_NAME)) {
                    Long value = (Long) dashboardTable.getValueAt(adapter.row, adapter.column);

                    if (value != null && value.longValue() > 0) {
                        return true;
                    }
                }
                return false;
            }
        };

        dashboardTable.addHighlighter(new ColorHighlighter(queuedHighlighterPredicate, new Color(240, 230, 140), Color.BLACK, new Color(240, 230, 140), Color.BLACK));

        HighlightPredicate errorHighlighterPredicate = new HighlightPredicate() {

            public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                if (adapter.column == dashboardTable.getColumnViewIndex(ERROR_COLUMN_NAME)) {
                    Long value = (Long) dashboardTable.getValueAt(adapter.row, adapter.column);

                    if (value != null && value.longValue() > 0) {
                        return true;
                    }
                }
                return false;
            }
        };

        Highlighter errorHighlighter = new ColorHighlighter(errorHighlighterPredicate, Color.PINK, Color.BLACK, Color.PINK, Color.BLACK);
        dashboardTable.addHighlighter(errorHighlighter);

        HighlightPredicate revisionDeltaHighlighterPredicate = new HighlightPredicate() {
            public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                if (adapter.column == dashboardTable.getColumnViewIndex(DEPLOYED_REVISION_DELTA_COLUMN_NAME)) {
                    Integer value = (Integer) dashboardTable.getValueAt(adapter.row, adapter.column);

                    if (value != null && value.intValue() > 0) {
                        return true;
                    }

                    TreePath path = dashboardTable.getPathForRow(adapter.row);
                    if (path != null) {
                        AbstractDashboardTableNode dashboardTableNode = (AbstractDashboardTableNode) path.getLastPathComponent();
                        if (!dashboardTableNode.isGroupNode()) {
                            DashboardStatus status = dashboardTableNode.getDashboardStatus();
                            if (status.getCodeTemplatesChanged() != null && status.getCodeTemplatesChanged()) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        };

        dashboardTable.addHighlighter(new ColorHighlighter(revisionDeltaHighlighterPredicate, new Color(255, 204, 0), Color.BLACK, new Color(255, 204, 0), Color.BLACK));

        HighlightPredicate lastDeployedHighlighterPredicate = new HighlightPredicate() {
            public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                if (adapter.column == dashboardTable.getColumnViewIndex(LAST_DEPLOYED_COLUMN_NAME)) {
                    Calendar checkAfter = Calendar.getInstance();
                    checkAfter.add(Calendar.MINUTE, -2);

                    Object value = dashboardTable.getValueAt(adapter.row, adapter.column);

                    if (value != null && value instanceof Calendar && ((Calendar) value).after(checkAfter)) {
                        return true;
                    }
                }
                return false;
            }
        };

        dashboardTable.addHighlighter(new ColorHighlighter(lastDeployedHighlighterPredicate, new Color(240, 230, 140), Color.BLACK, new Color(240, 230, 140), Color.BLACK));
    }

    /**
     * Gets the index of the selected status row.
     */
    public synchronized List<DashboardStatus> getSelectedStatuses() {
        List<DashboardStatus> selectedStatuses = new ArrayList<DashboardStatus>();
        List<AbstractDashboardTableNode> selectedNodes = dashboardTable.getSelectedNodes();

        for (AbstractDashboardTableNode node : selectedNodes) {
            if (!node.isGroupNode()) {
                selectedStatuses.add(node.getDashboardStatus());
            }
        }

        return selectedStatuses;
    }

    public synchronized Set<DashboardStatus> getSelectedChannelStatuses() {
        Set<DashboardStatus> selectedStatuses = new HashSet<DashboardStatus>();
        List<AbstractDashboardTableNode> selectedNodes = dashboardTable.getSelectedNodes();

        for (TreeTableNode treeNode : selectedNodes) {
            while (treeNode != null && treeNode instanceof AbstractDashboardTableNode) {
                AbstractDashboardTableNode node = (AbstractDashboardTableNode) treeNode;
                if (!node.isGroupNode() && node.getDashboardStatus().getStatusType() == StatusType.CHANNEL) {
                    if (!selectedStatuses.contains(node.getDashboardStatus())) {
                        selectedStatuses.add(node.getDashboardStatus());
                    }

                    break;
                }

                treeNode = treeNode.getParent();
            }
        }

        return selectedStatuses;
    }

    public synchronized List<DashboardStatus> getSelectedStatusesRecursive() {
        List<DashboardStatus> selectedStatuses = new ArrayList<DashboardStatus>();
        List<AbstractDashboardTableNode> selectedNodes = dashboardTable.getSelectedNodes();

        for (AbstractDashboardTableNode node : selectedNodes) {
            if (!node.isGroupNode()) {
                selectedStatuses.add(node.getDashboardStatus());
                selectedStatuses.addAll(getAllChildStatuses(node.getDashboardStatus()));
            }
        }

        return selectedStatuses;
    }

    public Set<DashboardStatus> getAllChildStatuses(DashboardStatus status) {
        Set<DashboardStatus> statuses = new HashSet<DashboardStatus>();

        for (DashboardStatus childStatus : status.getChildStatuses()) {
            if (!statuses.contains(childStatus)) {
                statuses.add(childStatus);
            }

            statuses.addAll(getAllChildStatuses(childStatus));
        }

        return statuses;
    }

    public Map<Integer, String> getDestinationConnectorNames(String channelId) {
        Map<Integer, String> destinationConnectors = new LinkedHashMap<Integer, String>();
        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();
        TreeTableNode root = model.getRoot();
        int channelCount = model.getChildCount(root);

        for (int i = 0; i < channelCount; i++) {
            AbstractDashboardTableNode node = (AbstractDashboardTableNode) root.getChildAt(i);

            if (node.isGroupNode()) {
                for (Enumeration<? extends MutableTreeTableNode> channelNodes = node.children(); channelNodes.hasMoreElements();) {
                    populateDestinationConnectorNames(channelId, (AbstractDashboardTableNode) channelNodes.nextElement(), destinationConnectors);
                }
            } else {
                populateDestinationConnectorNames(channelId, node, destinationConnectors);
            }
        }

        return destinationConnectors;
    }

    private void populateDestinationConnectorNames(String channelId, AbstractDashboardTableNode channelNode, Map<Integer, String> destinationConnectors) {
        if (channelNode.getDashboardStatus().getChannelId() == channelId) {
            int connectorCount = channelNode.getChildCount();

            for (int j = 0; j < connectorCount; j++) {
                AbstractDashboardTableNode connectorNode = (AbstractDashboardTableNode) channelNode.getChildAt(j);
                DashboardStatus status = connectorNode.getDashboardStatus();
                Integer metaDataId = status.getMetaDataId();

                if (metaDataId > 0) {
                    destinationConnectors.put(metaDataId, status.getName());
                }
            }
        }
    }

    public void deselectRows(boolean loadPanelPlugin) {
        dashboardTable.clearSelection();
        parent.setVisibleTasks(parent.dashboardTasks, parent.dashboardPopupMenu, 1, -1, false);

        if (loadPanelPlugin) {
            loadPanelPlugin(getCurrentTabPlugin());

            for (DashboardPanelPlugin plugin : LoadedExtensions.getInstance().getDashboardTablePlugins().values()) {
                loadPanelPlugin(plugin);
            }
        }
    }

    public static int getNumberOfDefaultColumns() {
        return defaultColumns.length;
    }

    private static class DefaultDashboardTableNodeFactory implements DashboardTableNodeFactory {
        @Override
        public AbstractDashboardTableNode createNode(ChannelGroupStatus groupStatus) {
            return new DashboardTableNode(groupStatus);
        }

        @Override
        public AbstractDashboardTableNode createNode(String channelId, DashboardStatus status) {
            return new DashboardTableNode(channelId, status);
        }
    }

    private void initComponents() {
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPane.setOneTouchExpandable(true);

        topPanel = new JPanel();
        topPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        dashboardTable = null;
        dashboardTableScrollPane = new JScrollPane();
        dashboardTableScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dashboardTableScrollPane.setViewportView(dashboardTable);
        dashboardTableScrollPane.setDoubleBuffered(true);

        controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(164, 164, 164)));
        controlPanel.setPreferredSize(new Dimension(100, 20));

        Set<FilterCompletion> tags = new HashSet<FilterCompletion>();
        for (ChannelTag tag : parent.getCachedChannelTags()) {
            tags.add(new TagFilterCompletion(tag));
        }

        tagField = new MirthTagField(Messages.getString("DashboardPanel.42"), false, tags); //$NON-NLS-1$
        tagField.addUpdateSearchListener(new SearchFilterListener() {
            @Override
            public void doSearch(String filterString) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        parent.doRefreshStatuses();
                    }
                });
            }

            @Override
            public void doDelete(String filterString) {
                parent.doRefreshStatuses();
            }
        });

        tagsLabel = new JLabel() {
            @Override
            public void setText(String text) {
                setToolTipText(text);
                super.setText(text);
            }
        };

        ButtonGroup showStatsButtonGroup = new ButtonGroup();

        showCurrentStatsButton = new JRadioButton(Messages.getString("DashboardPanel.43")); //$NON-NLS-1$
        showCurrentStatsButton.setSelected(true);
        showCurrentStatsButton.setToolTipText(Messages.getString("DashboardPanel.44")); //$NON-NLS-1$
        showCurrentStatsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                showCurrentStatsButtonActionPerformed();
            }
        });
        showStatsButtonGroup.add(showCurrentStatsButton);

        showLifetimeStatsButton = new JRadioButton(Messages.getString("DashboardPanel.45")); //$NON-NLS-1$
        showLifetimeStatsButton.setToolTipText(Messages.getString("DashboardPanel.46")); //$NON-NLS-1$
        showLifetimeStatsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                showLifetimeStatsButtonActionPerformed();
            }
        });
        showStatsButtonGroup.add(showLifetimeStatsButton);

        pluginContainerPanel = new JPanel();

        controlSeparator = new JSeparator(SwingConstants.VERTICAL);

        tagModeTextButton = new IconToggleButton(UIConstants.ICON_TEXT);
        tagModeTextButton.setToolTipText(Messages.getString("DashboardPanel.47")); //$NON-NLS-1$
        tagModeTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                updateTagButtons(!tagTextModeSelected, true, true);
                dashboardTable.updateUI();
            }
        });

        tagModeIconButton = new IconToggleButton(UIConstants.ICON_TAG);
        tagModeIconButton.setToolTipText(Messages.getString("DashboardPanel.48")); //$NON-NLS-1$
        tagModeIconButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                updateTagButtons(!tagIconModeSelected, false, true);
                dashboardTable.updateUI();
            }
        });

        ButtonGroup tableModeButtonGroup = new ButtonGroup();

        tableModeGroupsButton = new IconToggleButton(UIConstants.ICON_GROUP);
        tableModeGroupsButton.setToolTipText(Messages.getString("DashboardPanel.49")); //$NON-NLS-1$
        tableModeGroupsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                switchTableMode(true);
            }
        });
        tableModeButtonGroup.add(tableModeGroupsButton);

        tableModeChannelsButton = new IconToggleButton(UIConstants.ICON_CHANNEL);
        tableModeChannelsButton.setToolTipText(Messages.getString("DashboardPanel.50")); //$NON-NLS-1$
        tableModeChannelsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                switchTableMode(false);
            }
        });
        tableModeButtonGroup.add(tableModeChannelsButton);

        tabPane = new JTabbedPane();

        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(tabPane);
    }

    private void initLayout() {
        setLayout(new MigLayout(Messages.getString("DashboardPanel.51"))); //$NON-NLS-1$

        topPanel.setLayout(new MigLayout(Messages.getString("DashboardPanel.52"))); //$NON-NLS-1$
        topPanel.add(dashboardTableScrollPane, Messages.getString("DashboardPanel.53")); //$NON-NLS-1$

        controlPanel.setLayout(new MigLayout(Messages.getString("DashboardPanel.54"))); //$NON-NLS-1$
        controlPanel.add(new JLabel(Messages.getString("DashboardPanel.55")), Messages.getString("DashboardPanel.56")); //$NON-NLS-1$ //$NON-NLS-2$
        controlPanel.add(tagField, Messages.getString("DashboardPanel.57")); //$NON-NLS-1$
        controlPanel.add(tagsLabel, Messages.getString("DashboardPanel.58")); //$NON-NLS-1$
        controlPanel.add(showCurrentStatsButton, Messages.getString("DashboardPanel.59")); //$NON-NLS-1$
        controlPanel.add(showLifetimeStatsButton);

        pluginContainerPanel.setLayout(new MigLayout(Messages.getString("DashboardPanel.60"))); //$NON-NLS-1$
        controlPanel.add(pluginContainerPanel);

        controlPanel.add(controlSeparator, Messages.getString("DashboardPanel.61")); //$NON-NLS-1$
        controlPanel.add(tagModeTextButton, Messages.getString("DashboardPanel.62")); //$NON-NLS-1$
        controlPanel.add(tagModeIconButton);
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL), Messages.getString("DashboardPanel.63")); //$NON-NLS-1$
        controlPanel.add(tableModeGroupsButton, Messages.getString("DashboardPanel.64")); //$NON-NLS-1$
        controlPanel.add(tableModeChannelsButton);
        topPanel.add(controlPanel, Messages.getString("DashboardPanel.65")); //$NON-NLS-1$

        add(splitPane, Messages.getString("DashboardPanel.66")); //$NON-NLS-1$
    }

    private void showCurrentStatsButtonActionPerformed() {
        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();
        showLifetimeStats = false;
        model.setShowLifetimeStats(showLifetimeStats);
        if (dashboardTable.getSelectedRowCount() == 0) {
            deselectRows(false);
        } else {
            updatePopupMenu(false);
        }

        // TODO: updateTableHighlighting() is called to force the table to refresh, there is probably a more direct way to do this
        updateTableHighlighting();
    }

    private void showLifetimeStatsButtonActionPerformed() {
        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();
        showLifetimeStats = true;
        model.setShowLifetimeStats(showLifetimeStats);
        if (dashboardTable.getSelectedRowCount() == 0) {
            deselectRows(false);
        } else {
            updatePopupMenu(false);
        }

        // TODO: updateTableHighlighting() is called to force the table to refresh, there is probably a more direct way to do this
        updateTableHighlighting();
    }

    private void switchTableMode(boolean groupModeEnabled) {
        if (!canViewChannelGroups) {
            groupModeEnabled = false;
        }

        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();
        if (model.isGroupModeEnabled() != groupModeEnabled) {
            userPreferences.putBoolean(Messages.getString("DashboardPanel.67"), groupModeEnabled); //$NON-NLS-1$

            if (groupModeEnabled) {
                tableModeChannelsButton.setContentFilled(false);
            } else {
                tableModeGroupsButton.setContentFilled(false);
            }

            TableState tableState = getCurrentTableState();
            model.setGroupModeEnabled(groupModeEnabled);
            restoreTableState(tableState);
            updatePopupMenu(false);

            int totalGroupCount = parent.channelPanel.getCachedGroupStatuses().size();
            int totalChannelCount = parent.status != null ? parent.status.size() : 0;

            if (tagField.isEnabled()) {
                int visibleGroupCount = 0;
                int visibleChannelCount = 0;

                if (model.isGroupModeEnabled()) {
                    for (Enumeration<? extends MutableTreeTableNode> groupNodes = ((MutableTreeTableNode) model.getRoot()).children(); groupNodes.hasMoreElements();) {
                        visibleGroupCount++;
                        visibleChannelCount += ((MutableTreeTableNode) groupNodes.nextElement()).getChildCount();
                    }
                } else {
                    visibleChannelCount = ((MutableTreeTableNode) model.getRoot()).getChildCount();
                }

                updateTagsLabel(totalGroupCount, visibleGroupCount, totalChannelCount, visibleChannelCount);
            } else {
                updateTagsLabel(totalGroupCount, totalGroupCount, totalChannelCount, totalChannelCount);
            }
        }
    }

    public TableState getCurrentTableState() {
        Set<String> selectedIds = new HashSet<String>();
        Map<String, Set<Integer>> selectedConnectors = new HashMap<String, Set<Integer>>();
        Set<String> expandedIds = new HashSet<String>();
        Set<String> collapsedIds = new HashSet<String>();

        int[] selectedRows = dashboardTable.getSelectedModelRows();
        for (int row : selectedRows) {
            AbstractDashboardTableNode node = (AbstractDashboardTableNode) dashboardTable.getPathForRow(row).getLastPathComponent();

            if (node.isGroupNode()) {
                selectedIds.add(node.getGroupStatus().getGroup().getId());
            } else {
                DashboardStatus status = node.getDashboardStatus();

                if (status.getStatusType() == StatusType.CHANNEL) {
                    selectedIds.add(status.getChannelId());
                } else if (status.getStatusType() == StatusType.SOURCE_CONNECTOR || status.getStatusType() == StatusType.DESTINATION_CONNECTOR) {
                    Set<Integer> selectedMetaDataIds = selectedConnectors.get(status.getChannelId());
                    if (selectedMetaDataIds == null) {
                        selectedMetaDataIds = new HashSet<Integer>();
                        selectedConnectors.put(status.getChannelId(), selectedMetaDataIds);
                    }
                    selectedMetaDataIds.add(status.getMetaDataId());
                }
            }
        }

        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();
        MutableTreeTableNode root = (MutableTreeTableNode) model.getRoot();

        if (root != null) {
            if (model.isGroupModeEnabled()) {
                for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
                    AbstractDashboardTableNode groupNode = (AbstractDashboardTableNode) groupNodes.nextElement();

                    if (dashboardTable.isExpanded(new TreePath(new Object[] { root, groupNode }))) {
                        expandedIds.add(groupNode.getGroupStatus().getGroup().getId());
                    } else if (groupNode.getChildCount() > 0) {
                        collapsedIds.add(groupNode.getGroupStatus().getGroup().getId());
                    }

                    addChannelNodeExpansionStates(groupNode, expandedIds, collapsedIds);
                }
            } else {
                addChannelNodeExpansionStates(root, expandedIds, collapsedIds);
            }
        }

        return new TableState(selectedIds, selectedConnectors, expandedIds, collapsedIds);
    }

    private void addChannelNodeExpansionStates(MutableTreeTableNode parent, Set<String> expandedIds, Set<String> collapsedIds) {
        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();

        for (Enumeration<? extends MutableTreeTableNode> channelNodes = parent.children(); channelNodes.hasMoreElements();) {
            AbstractDashboardTableNode channelNode = (AbstractDashboardTableNode) channelNodes.nextElement();

            if (dashboardTable.isExpanded(new TreePath(model.getPathToRoot(channelNode)))) {
                expandedIds.add(channelNode.getChannelId());
            } else {
                collapsedIds.add(channelNode.getChannelId());
            }
        }
    }

    private void restoreTableState(TableState tableState) {
        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();
        MutableTreeTableNode root = (MutableTreeTableNode) model.getRoot();

        if (model.isGroupModeEnabled()) {
            for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
                AbstractDashboardTableNode groupNode = (AbstractDashboardTableNode) groupNodes.nextElement();
                String groupId = groupNode.getGroupStatus().getGroup().getId();

                if (tableState.getExpandedIds().contains(groupId) || !tableState.getCollapsedIds().contains(groupId)) {
                    dashboardTable.expandPath(new TreePath(new Object[] { root, groupNode }));
                } else if (tableState.getCollapsedIds().contains(groupId)) {
                    dashboardTable.collapsePath(new TreePath(new Object[] { root, groupNode }));
                }

                setChannelNodeExpansionStates(groupNode, tableState);
            }
        } else {
            setChannelNodeExpansionStates(root, tableState);
        }

        final List<TreePath> selectionPaths = new ArrayList<TreePath>();

        if (model.isGroupModeEnabled()) {
            for (Enumeration<? extends MutableTreeTableNode> groupNodes = root.children(); groupNodes.hasMoreElements();) {
                AbstractDashboardTableNode groupNode = (AbstractDashboardTableNode) groupNodes.nextElement();

                if (tableState.getSelectedIds().contains(groupNode.getGroupStatus().getGroup().getId())) {
                    selectionPaths.add(new TreePath(new Object[] { root, groupNode }));
                }

                selectChannelNodes(groupNode, tableState, selectionPaths);
            }
        } else {
            selectChannelNodes(root, tableState, selectionPaths);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                /*
                 * When setting selection paths the ListSelectionListener will be invoked multiple
                 * times for each row, so remove and re-add it afterwards.
                 */
                dashboardTable.getSelectionModel().removeListSelectionListener(listSelectionListener);

                try {
                    dashboardTable.getTreeSelectionModel().setSelectionPaths(selectionPaths.toArray(new TreePath[selectionPaths.size()]));
                } catch (Exception e) {
                    // It's possible that the model changed already, just ignore this since it's only selecting nodes
                } finally {
                    dashboardTable.getSelectionModel().addListSelectionListener(listSelectionListener);
                    updatePopupMenu(true);
                }
            }
        });
    }

    private void setChannelNodeExpansionStates(MutableTreeTableNode parent, TableState tableState) {
        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();

        for (Enumeration<? extends MutableTreeTableNode> channelNodes = parent.children(); channelNodes.hasMoreElements();) {
            AbstractDashboardTableNode channelNode = (AbstractDashboardTableNode) channelNodes.nextElement();
            String channelId = channelNode.getChannelId();

            if (tableState.getExpandedIds().contains(channelId)) {
                dashboardTable.expandPath(new TreePath(model.getPathToRoot(channelNode)));
            } else if (tableState.getCollapsedIds().contains(channelId)) {
                dashboardTable.collapsePath(new TreePath(model.getPathToRoot(channelNode)));
            }
        }
    }

    private void selectChannelNodes(MutableTreeTableNode parent, TableState tableState, List<TreePath> selectionPaths) {
        DashboardTreeTableModel model = (DashboardTreeTableModel) dashboardTable.getTreeTableModel();

        for (Enumeration<? extends MutableTreeTableNode> channelNodes = parent.children(); channelNodes.hasMoreElements();) {
            AbstractDashboardTableNode channelNode = (AbstractDashboardTableNode) channelNodes.nextElement();

            if (tableState.getSelectedIds().contains(channelNode.getChannelId())) {
                selectionPaths.add(new TreePath(model.getPathToRoot(channelNode)));
            }

            for (Enumeration<? extends MutableTreeTableNode> connectorNodes = channelNode.children(); connectorNodes.hasMoreElements();) {
                AbstractDashboardTableNode connectorNode = (AbstractDashboardTableNode) connectorNodes.nextElement();

                if (tableState.getSelectedConnectors().containsKey(connectorNode.getChannelId()) && tableState.getSelectedConnectors().get(connectorNode.getChannelId()).contains(connectorNode.getDashboardStatus().getMetaDataId())) {
                    selectionPaths.add(new TreePath(model.getPathToRoot(connectorNode)));
                }
            }
        }
    }

    public class TableState {
        private Set<String> selectedIds = new HashSet<String>();
        private Map<String, Set<Integer>> selectedConnectors = new HashMap<String, Set<Integer>>();
        private Set<String> expandedIds = new HashSet<String>();
        private Set<String> collapsedIds = new HashSet<String>();

        public TableState(Set<String> selectedIds, Map<String, Set<Integer>> selectedConnectors, Set<String> expandedIds, Set<String> collapsedIds) {
            this.selectedIds = selectedIds;
            this.selectedConnectors = selectedConnectors;
            this.expandedIds = expandedIds;
            this.collapsedIds = collapsedIds;
        }

        public Set<String> getSelectedIds() {
            return selectedIds;
        }

        public Map<String, Set<Integer>> getSelectedConnectors() {
            return selectedConnectors;
        }

        public Set<String> getExpandedIds() {
            return expandedIds;
        }

        public Set<String> getCollapsedIds() {
            return collapsedIds;
        }
    }

    private JSplitPane splitPane;

    private JPanel topPanel;
    public MirthTreeTable dashboardTable;
    private JScrollPane dashboardTableScrollPane;

    private JPanel controlPanel;
    private MirthTagField tagField;
    private JLabel tagsLabel;
    private JRadioButton showCurrentStatsButton;
    private JRadioButton showLifetimeStatsButton;
    private JPanel pluginContainerPanel;
    private JSeparator controlSeparator;
    private IconToggleButton tagModeTextButton;
    private IconToggleButton tagModeIconButton;
    private IconToggleButton tableModeGroupsButton;
    private IconToggleButton tableModeChannelsButton;

    private JTabbedPane tabPane;
}
