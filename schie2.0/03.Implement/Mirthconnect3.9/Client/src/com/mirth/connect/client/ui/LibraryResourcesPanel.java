/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.ArrayUtils;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.ui.components.MirthTable;
import com.mirth.connect.client.ui.components.MirthTreeTable;
import com.mirth.connect.client.ui.components.MirthTriStateCheckBox;
import com.mirth.connect.donkey.model.channel.DestinationConnectorPropertiesInterface;
import com.mirth.connect.donkey.model.channel.SourceConnectorPropertiesInterface;
import com.mirth.connect.model.Channel;
import com.mirth.connect.model.Connector;
import com.mirth.connect.model.LibraryProperties;
import com.mirth.connect.model.ResourceProperties;
import com.mirth.connect.plugins.LibraryClientPlugin;

public class LibraryResourcesPanel extends JPanel implements ListSelectionListener, TreeSelectionListener {

    private static final int SELECTED_COLUMN = 0;
    private static final int PROPERTIES_COLUMN = 1;
    private static final int TYPE_COLUMN = 2;

    private ChannelDependenciesDialog parent;
    private Map<Integer, Map<String, String>> selectedResourceIds;

    public LibraryResourcesPanel(ChannelDependenciesDialog parent, Channel channel) {
        this.parent = parent;
        selectedResourceIds = new HashMap<Integer, Map<String, String>>();

        Map<String, String> channelResourceIds = channel.getProperties().getResourceIds();
        if (channelResourceIds == null) {
            channelResourceIds = new LinkedHashMap<String, String>();
        }
        selectedResourceIds.put(null, new LinkedHashMap<String, String>(channelResourceIds));

        Map<String, String> sourceResourceIds = ((SourceConnectorPropertiesInterface) channel.getSourceConnector().getProperties()).getSourceConnectorProperties().getResourceIds();
        if (sourceResourceIds == null) {
            sourceResourceIds = new LinkedHashMap<String, String>();
        }
        selectedResourceIds.put(channel.getSourceConnector().getMetaDataId(), new LinkedHashMap<String, String>(sourceResourceIds));

        for (Connector destinationConnector : channel.getDestinationConnectors()) {
            Map<String, String> destinationResourceIds = ((DestinationConnectorPropertiesInterface) destinationConnector.getProperties()).getDestinationConnectorProperties().getResourceIds();
            if (destinationResourceIds == null) {
                destinationResourceIds = new LinkedHashMap<String, String>();
            }
            selectedResourceIds.put(destinationConnector.getMetaDataId(), new LinkedHashMap<String, String>(destinationResourceIds));
        }

        initComponents(channel);
        initLayout();
    }

    public void initialize() {
        final String workingId = PlatformUI.MIRTH_FRAME.startWorking(Messages.getString("LibraryResourcesPanel.0")); //$NON-NLS-1$

        SwingWorker<List<LibraryProperties>, Void> worker = new SwingWorker<List<LibraryProperties>, Void>() {

            @Override
            public List<LibraryProperties> doInBackground() throws ClientException {
                List<ResourceProperties> resourceProperties = PlatformUI.MIRTH_FRAME.mirthClient.getResources();
                List<LibraryProperties> libraryProperties = new ArrayList<LibraryProperties>();
                for (ResourceProperties resource : resourceProperties) {
                    if (resource instanceof LibraryProperties) {
                        libraryProperties.add((LibraryProperties) resource);
                    }
                }
                return libraryProperties;
            }

            @Override
            public void done() {
                try {
                    List<LibraryProperties> resources = get();
                    if (resources == null) {
                        resources = new ArrayList<LibraryProperties>();
                    }

                    Object[][] data = new Object[resources.size()][3];
                    int i = 0;

                    for (LibraryProperties properties : resources) {
                        data[i][SELECTED_COLUMN] = null;
                        data[i][PROPERTIES_COLUMN] = properties;
                        data[i][TYPE_COLUMN] = properties.getType();
                        i++;

                        for (Map<String, String> resourceIds : selectedResourceIds.values()) {
                            if (resourceIds.containsKey(properties.getId())) {
                                resourceIds.put(properties.getId(), properties.getName());
                            }
                        }
                    }

                    ((RefreshTableModel) resourceTable.getModel()).refreshDataVector(data);

                    treeTable.getSelectionModel().setSelectionInterval(0, 0);
                    treeTable.getTreeSelectionModel().setSelectionPath(treeTable.getPathForRow(0));
                    parent.resourcesReady();
                } catch (Throwable t) {
                    if (t instanceof ExecutionException) {
                        t = t.getCause();
                    }
                    PlatformUI.MIRTH_FRAME.alertThrowable(PlatformUI.MIRTH_FRAME, t, Messages.getString("LibraryResourcesPanel.1") + t.toString()); //$NON-NLS-1$
                } finally {
                    PlatformUI.MIRTH_FRAME.stopWorking(workingId);
                }
            }
        };

        worker.execute();
    }

    public Map<Integer, Map<String, String>> getSelectedResourceIds() {
        return selectedResourceIds;
    }

    private void initComponents(Channel channel) {
        setBackground(UIConstants.BACKGROUND_COLOR);

        AbstractMutableTreeTableNode channelNode = new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.2"), -1, null)); //$NON-NLS-1$

        AbstractMutableTreeTableNode channelScriptsNode = new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.3"), null, null)); //$NON-NLS-1$
        channelScriptsNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.4"), null, null, false))); //$NON-NLS-1$
        channelScriptsNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.5"), null, null, false))); //$NON-NLS-1$
        channelScriptsNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.6"), null, null, false))); //$NON-NLS-1$
        channelScriptsNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.7"), null, null, false))); //$NON-NLS-1$
        channelScriptsNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.8"), null, null, false))); //$NON-NLS-1$
        channelScriptsNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.9"), null, null, false))); //$NON-NLS-1$
        channelNode.add(channelScriptsNode);

        AbstractMutableTreeTableNode sourceConnectorNode = new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.10"), 0, channel.getSourceConnector().getTransportName())); //$NON-NLS-1$
        sourceConnectorNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.11"), channel.getSourceConnector().getMetaDataId(), channel.getSourceConnector().getTransportName(), false))); //$NON-NLS-1$
        sourceConnectorNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.12"), channel.getSourceConnector().getMetaDataId(), channel.getSourceConnector().getTransportName(), false))); //$NON-NLS-1$
        channelNode.add(sourceConnectorNode);

        for (Connector destinationConnector : channel.getDestinationConnectors()) {
            AbstractMutableTreeTableNode destinationConnectorNode = new DefaultMutableTreeTableNode(new ConnectorEntry(destinationConnector.getName(), destinationConnector.getMetaDataId(), destinationConnector.getTransportName()));
            destinationConnectorNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.13"), destinationConnector.getMetaDataId(), destinationConnector.getTransportName(), false))); //$NON-NLS-1$
            destinationConnectorNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.14"), destinationConnector.getMetaDataId(), destinationConnector.getTransportName(), false))); //$NON-NLS-1$
            destinationConnectorNode.add(new DefaultMutableTreeTableNode(new ConnectorEntry(Messages.getString("LibraryResourcesPanel.15"), destinationConnector.getMetaDataId(), destinationConnector.getTransportName(), false))); //$NON-NLS-1$
            channelNode.add(destinationConnectorNode);
        }

        treeTable = new MirthTreeTable();

        DefaultTreeTableModel model = new SortableTreeTableModel(channelNode);
        model.setColumnIdentifiers(Arrays.asList(new String[] { Messages.getString("LibraryResourcesPanel.16") })); //$NON-NLS-1$
        treeTable.setTreeTableModel(model);

        treeTable.setRootVisible(true);
        treeTable.setDragEnabled(false);
        treeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        treeTable.getTreeSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeTable.setRowHeight(UIConstants.ROW_HEIGHT);
        treeTable.setFocusable(true);
        treeTable.setOpaque(true);
        treeTable.getTableHeader().setReorderingAllowed(false);
        treeTable.setEditable(false);
        treeTable.setSortable(false);
        treeTable.addTreeSelectionListener(this);
        treeTable.getSelectionModel().addListSelectionListener(this);
        treeTable.putClientProperty(Messages.getString("LibraryResourcesPanel.17"), Messages.getString("LibraryResourcesPanel.18")); //$NON-NLS-1$ //$NON-NLS-2$
        treeTable.setShowGrid(true, true);

        if (Preferences.userNodeForPackage(Mirth.class).getBoolean(Messages.getString("LibraryResourcesPanel.19"), true)) { //$NON-NLS-1$
            treeTable.setHighlighters(HighlighterFactory.createAlternateStriping(UIConstants.HIGHLIGHTER_COLOR, UIConstants.BACKGROUND_COLOR));
        }

        final String toolTipText = Messages.getString("LibraryResourcesPanel.20"); //$NON-NLS-1$

        treeTable.setTreeCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                setToolTipText(toolTipText);
                setEnabled(((ConnectorEntry) ((AbstractMutableTreeTableNode) value).getUserObject()).enabled);
                return this;
            }
        });

        treeTable.setOpenIcon(null);
        treeTable.setClosedIcon(null);
        treeTable.setLeafIcon(null);
        treeTable.getColumnExt(0).setToolTipText(toolTipText);

        treeTableScrollPane = new JScrollPane(treeTable);

        resourceTable = new MirthTable();
        resourceTable.setModel(new RefreshTableModel(new Object[] { Messages.getString("LibraryResourcesPanel.21"), Messages.getString("LibraryResourcesPanel.22"), Messages.getString("LibraryResourcesPanel.23") }, 0) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == SELECTED_COLUMN;
            }
        });
        resourceTable.setDragEnabled(false);
        resourceTable.setRowSelectionAllowed(false);
        resourceTable.setRowHeight(UIConstants.ROW_HEIGHT);
        resourceTable.setFocusable(false);
        resourceTable.setOpaque(true);
        resourceTable.getTableHeader().setReorderingAllowed(false);
        resourceTable.setEditable(true);
        resourceTable.setSortable(false);

        if (Preferences.userNodeForPackage(Mirth.class).getBoolean(Messages.getString("LibraryResourcesPanel.24"), true)) { //$NON-NLS-1$
            resourceTable.setHighlighters(HighlighterFactory.createAlternateStriping(UIConstants.HIGHLIGHTER_COLOR, UIConstants.BACKGROUND_COLOR));
        }

        resourceTable.getColumnModel().getColumn(SELECTED_COLUMN).setMinWidth(20);
        resourceTable.getColumnModel().getColumn(SELECTED_COLUMN).setMaxWidth(20);
        resourceTable.getColumnModel().getColumn(SELECTED_COLUMN).setCellRenderer(new CheckBoxRenderer());
        resourceTable.getColumnModel().getColumn(SELECTED_COLUMN).setCellEditor(new CheckBoxEditor());

        resourceTable.getColumnModel().getColumn(TYPE_COLUMN).setMinWidth(75);
        resourceTable.getColumnModel().getColumn(TYPE_COLUMN).setMaxWidth(200);

        resourceTableScrollPane = new JScrollPane(resourceTable);
    }

    private void initLayout() {
        setLayout(new MigLayout(Messages.getString("LibraryResourcesPanel.25"))); //$NON-NLS-1$
        add(treeTableScrollPane, Messages.getString("LibraryResourcesPanel.26")); //$NON-NLS-1$
        add(resourceTableScrollPane, Messages.getString("LibraryResourcesPanel.27")); //$NON-NLS-1$
    }

    @Override
    public void valueChanged(ListSelectionEvent evt) {
        ((AbstractTableModel) resourceTable.getModel()).fireTableDataChanged();
    }

    @Override
    public void valueChanged(TreeSelectionEvent evt) {
        MutableTreeTableNode node = (MutableTreeTableNode) evt.getPath().getLastPathComponent();
        ConnectorEntry entry = (ConnectorEntry) node.getUserObject();

        if (node.equals(treeTable.getTreeTableModel().getRoot())) {
            resourceTable.setEnabled(true);
            for (int row = 0; row < resourceTable.getRowCount(); row++) {
                LibraryProperties properties = (LibraryProperties) resourceTable.getModel().getValueAt(row, PROPERTIES_COLUMN);

                boolean allChildrenChecked = true;
                boolean allChildrenUnchecked = true;
                for (Enumeration<? extends MutableTreeTableNode> en = node.children(); en.hasMoreElements();) {
                    if (selectedResourceIds.get(((ConnectorEntry) en.nextElement().getUserObject()).metaDataId).containsKey(properties.getId())) {
                        allChildrenUnchecked = false;
                    } else {
                        allChildrenChecked = false;
                    }
                }

                Boolean value = null;
                if (allChildrenChecked) {
                    value = true;
                } else if (allChildrenUnchecked) {
                    value = false;
                }
                resourceTable.getModel().setValueAt(value, row, SELECTED_COLUMN);
            }
        } else {
            resourceTable.setEnabled(node.getParent().equals(treeTable.getTreeTableModel().getRoot()));
            for (int row = 0; row < resourceTable.getRowCount(); row++) {
                LibraryProperties properties = (LibraryProperties) resourceTable.getModel().getValueAt(row, PROPERTIES_COLUMN);
                resourceTable.getModel().setValueAt(selectedResourceIds.get(entry.metaDataId).containsKey(properties.getId()), row, SELECTED_COLUMN);
            }
        }
    }

    private class ConnectorEntry {
        public String name;
        public Integer metaDataId;
        public String transportName;
        public boolean enabled;

        public ConnectorEntry(String name, Integer metaDataId, String transportName) {
            this(name, metaDataId, transportName, true);
        }

        public ConnectorEntry(String name, Integer metaDataId, String transportName, boolean enabled) {
            this.name = name;
            this.metaDataId = metaDataId;
            this.transportName = transportName;
            this.enabled = enabled;
        }

        @Override
        public String toString() {
            return name + (enabled && transportName != null ? Messages.getString("LibraryResourcesPanel.28") + transportName + Messages.getString("LibraryResourcesPanel.29") : Messages.getString("LibraryResourcesPanel.30")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
    }

    private class CheckBoxRenderer extends JPanel implements TableCellRenderer {
        private MirthTriStateCheckBox checkBox;

        public CheckBoxRenderer() {
            super(new MigLayout(Messages.getString("LibraryResourcesPanel.31"))); //$NON-NLS-1$
            checkBox = new MirthTriStateCheckBox();
            add(checkBox, Messages.getString("LibraryResourcesPanel.32")); //$NON-NLS-1$
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (Preferences.userNodeForPackage(Mirth.class).getBoolean(Messages.getString("LibraryResourcesPanel.33"), true)) { //$NON-NLS-1$
                setBackground(row % 2 == 0 ? UIConstants.HIGHLIGHTER_COLOR : UIConstants.BACKGROUND_COLOR);
                checkBox.setBackground(getBackground());
            }
            boolean enabled = table.isEnabled();

            if (treeTable.getSelectedRow() >= 0) {
                LibraryProperties properties = (LibraryProperties) resourceTable.getModel().getValueAt(row, PROPERTIES_COLUMN);
                LibraryClientPlugin plugin = LoadedExtensions.getInstance().getLibraryClientPlugins().get(properties.getPluginPointName());
                if (plugin != null) {
                    ConnectorEntry entry = (ConnectorEntry) treeTable.getValueAt(treeTable.getSelectedRow(), 0);
                    if (entry.transportName != null && ArrayUtils.contains(plugin.getUnselectableTransportNames(), entry.transportName)) {
                        enabled = false;
                    }
                }
            }

            checkBox.setEnabled(enabled);
            checkBox.setState(value == null ? MirthTriStateCheckBox.PARTIAL : (Boolean) value ? MirthTriStateCheckBox.CHECKED : MirthTriStateCheckBox.UNCHECKED);
            return this;
        }
    }

    private class CheckBoxEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JPanel panel;
        private MirthTriStateCheckBox checkBox;

        public CheckBoxEditor() {
            panel = new JPanel(new MigLayout(Messages.getString("LibraryResourcesPanel.34"))); //$NON-NLS-1$
            checkBox = new MirthTriStateCheckBox();
            checkBox.addActionListener(this);
            panel.add(checkBox, Messages.getString("LibraryResourcesPanel.35")); //$NON-NLS-1$
        }

        @Override
        public boolean isCellEditable(EventObject evt) {
            if (treeTable.getSelectedRow() >= 0 && evt instanceof MouseEvent) {
                LibraryProperties properties = (LibraryProperties) resourceTable.getModel().getValueAt(resourceTable.rowAtPoint(((MouseEvent) evt).getPoint()), PROPERTIES_COLUMN);
                LibraryClientPlugin plugin = LoadedExtensions.getInstance().getLibraryClientPlugins().get(properties.getPluginPointName());
                if (plugin != null) {
                    ConnectorEntry entry = (ConnectorEntry) treeTable.getValueAt(treeTable.getSelectedRow(), 0);
                    if (entry.transportName != null && ArrayUtils.contains(plugin.getUnselectableTransportNames(), entry.transportName)) {
                        return false;
                    }
                }
            }

            return super.isCellEditable(evt);
        }

        @Override
        public Object getCellEditorValue() {
            return checkBox.getState() == MirthTriStateCheckBox.PARTIAL ? null : checkBox.getState() == MirthTriStateCheckBox.CHECKED;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (Preferences.userNodeForPackage(Mirth.class).getBoolean(Messages.getString("LibraryResourcesPanel.36"), true)) { //$NON-NLS-1$
                panel.setBackground(row % 2 == 0 ? UIConstants.HIGHLIGHTER_COLOR : UIConstants.BACKGROUND_COLOR);
                checkBox.setBackground(panel.getBackground());
            }
            checkBox.setState(value == null ? MirthTriStateCheckBox.PARTIAL : (Boolean) value ? MirthTriStateCheckBox.CHECKED : MirthTriStateCheckBox.UNCHECKED);
            return panel;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            MutableTreeTableNode node = (MutableTreeTableNode) treeTable.getTreeSelectionModel().getSelectionPath().getLastPathComponent();
            ConnectorEntry entry = (ConnectorEntry) node.getUserObject();
            LibraryProperties properties = (LibraryProperties) resourceTable.getModel().getValueAt(resourceTable.getEditingRow(), PROPERTIES_COLUMN);
            LibraryClientPlugin plugin = LoadedExtensions.getInstance().getLibraryClientPlugins().get(properties.getPluginPointName());

            boolean selected = ((JCheckBox) evt.getSource()).isSelected();

            if (node.equals(treeTable.getTreeTableModel().getRoot())) {
                for (Enumeration<? extends MutableTreeTableNode> en = node.children(); en.hasMoreElements();) {
                    ConnectorEntry child = (ConnectorEntry) en.nextElement().getUserObject();

                    if (plugin == null || child.transportName == null || !ArrayUtils.contains(plugin.getUnselectableTransportNames(), child.transportName)) {
                        if (selected) {
                            selectedResourceIds.get(child.metaDataId).put(properties.getId(), properties.getName());
                        } else {
                            selectedResourceIds.get(child.metaDataId).remove(properties.getId());
                        }
                    }
                }
            } else {
                if (selected) {
                    selectedResourceIds.get(entry.metaDataId).put(properties.getId(), properties.getName());
                } else {
                    selectedResourceIds.get(entry.metaDataId).remove(properties.getId());
                }
            }

            if (plugin != null && plugin.singleSelectionOnly()) {
                for (int row = 0; row < resourceTable.getRowCount(); row++) {
                    LibraryProperties props = (LibraryProperties) resourceTable.getModel().getValueAt(row, PROPERTIES_COLUMN);

                    if (row != resourceTable.getEditingRow() && props.getType().equals(properties.getType())) {
                        if (node.equals(treeTable.getTreeTableModel().getRoot())) {
                            for (Enumeration<? extends MutableTreeTableNode> en = node.children(); en.hasMoreElements();) {
                                ConnectorEntry child = (ConnectorEntry) en.nextElement().getUserObject();

                                if (child.transportName == null || !ArrayUtils.contains(plugin.getUnselectableTransportNames(), child.transportName)) {
                                    selectedResourceIds.get(child.metaDataId).remove(props.getId());
                                }
                            }
                        } else {
                            selectedResourceIds.get(entry.metaDataId).remove(props.getId());
                        }
                    }
                }
            }

            for (int row = 0; row < resourceTable.getRowCount(); row++) {
                LibraryProperties props = (LibraryProperties) resourceTable.getModel().getValueAt(row, PROPERTIES_COLUMN);
                Boolean newValue;

                if (node.equals(treeTable.getTreeTableModel().getRoot())) {
                    boolean allChecked = true;
                    boolean allUnchecked = true;

                    for (Enumeration<? extends MutableTreeTableNode> en = node.children(); en.hasMoreElements();) {
                        if (selectedResourceIds.get(((ConnectorEntry) en.nextElement().getUserObject()).metaDataId).containsKey(props.getId())) {
                            allUnchecked = false;
                        } else {
                            allChecked = false;
                        }
                    }

                    if (allChecked) {
                        newValue = true;
                    } else if (allUnchecked) {
                        newValue = false;
                    } else {
                        newValue = null;
                    }
                } else {
                    newValue = selectedResourceIds.get(entry.metaDataId).containsKey(props.getId());
                }

                resourceTable.getModel().setValueAt(newValue, row, SELECTED_COLUMN);
            }

            ((AbstractTableModel) resourceTable.getModel()).fireTableDataChanged();
            cancelCellEditing();
        }
    }

    private MirthTreeTable treeTable;
    private JScrollPane treeTableScrollPane;
    private MirthTable resourceTable;
    private JScrollPane resourceTableScrollPane;
}