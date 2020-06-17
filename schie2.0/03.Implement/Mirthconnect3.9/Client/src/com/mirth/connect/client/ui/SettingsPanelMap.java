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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.core.TaskConstants;
import com.mirth.connect.client.ui.components.MirthButton;
import com.mirth.connect.client.ui.components.MirthDialogTableCellEditor;
import com.mirth.connect.client.ui.components.MirthPasswordTableCellRenderer;
import com.mirth.connect.client.ui.components.MirthTable;
import com.mirth.connect.util.ConfigurationProperty;

public class SettingsPanelMap extends AbstractSettingsPanel {

    public static final String TAB_NAME = Messages.getString("SettingsPanelMap.0"); //$NON-NLS-1$
    private static final String SHOW_VALUES_KEY = Messages.getString("SettingsPanelMap.1"); //$NON-NLS-1$
    private static Preferences userPreferences = Preferences.userNodeForPackage(Mirth.class);

    public SettingsPanelMap(String tabName) {
        super(tabName);

        initComponents();

        addTask(TaskConstants.SETTINGS_CONFIGURATION_MAP_IMPORT, Messages.getString("SettingsPanelMap.2"), Messages.getString("SettingsPanelMap.3"), Messages.getString("SettingsPanelMap.4"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("SettingsPanelMap.5")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        addTask(TaskConstants.SETTINGS_CONFIGURATION_MAP_EXPORT, Messages.getString("SettingsPanelMap.6"), Messages.getString("SettingsPanelMap.7"), Messages.getString("SettingsPanelMap.8"), new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("SettingsPanelMap.9")))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        setVisibleTasks(2, 3, true);
    }

    public void doRefresh() {
        if (PlatformUI.MIRTH_FRAME.alertRefresh()) {
            return;
        }
        // close any open cell editor before refreshing
        if (this.configurationMapTable.getCellEditor() != null) {
            this.configurationMapTable.getCellEditor().stopCellEditing();
        }

        boolean showConfigMapValues = userPreferences.getBoolean(SHOW_VALUES_KEY, false);
        showValuesCheckbox.setSelected(showConfigMapValues);

        final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelMap.10") + getTabName() + Messages.getString("SettingsPanelMap.11")); //$NON-NLS-1$ //$NON-NLS-2$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            Map<String, ConfigurationProperty> configurationMap = null;

            public Void doInBackground() {
                try {
                    configurationMap = getFrame().mirthClient.getConfigurationMap();
                } catch (ClientException e) {
                    getFrame().alertThrowable(getFrame(), e);
                }
                return null;
            }

            @Override
            public void done() {
                // null if it failed to get the map settings or if confirmLeave returned false
                if (configurationMap != null) {
                    updateConfigurationTable(configurationMap, showConfigMapValues, true);
                }
                getFrame().stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public boolean doSave() {
        // close any open cell editor before saving
        if (this.configurationMapTable.getCellEditor() != null) {
            this.configurationMapTable.getCellEditor().stopCellEditing();
        }

        final Map<String, ConfigurationProperty> configurationMap = getConfigurationMapFromTable();
        if (configurationMap == null) {
            return false;
        }

        final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelMap.12") + getTabName() + Messages.getString("SettingsPanelMap.13")); //$NON-NLS-1$ //$NON-NLS-2$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    getFrame().mirthClient.setConfigurationMap(configurationMap);
                } catch (ClientException e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                return null;
            }

            @Override
            public void done() {
                setSaveEnabled(false);
                getFrame().stopWorking(workingId);
            }
        };

        worker.execute();

        return true;
    }

    private Map<String, ConfigurationProperty> getConfigurationMapFromTable() {
        // Using a LinkedHashMap so that we can preserve row order when the user is toggling obfuscation on/off.
        final Map<String, ConfigurationProperty> configurationMap = new LinkedHashMap<String, ConfigurationProperty>();
        RefreshTableModel model = (RefreshTableModel) configurationMapTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String key = (String) model.getValueAt(i, 0);
            String value = (String) model.getValueAt(i, 1);
            String comment = (String) model.getValueAt(i, 2);

            if (StringUtils.isNotBlank(key)) {
                configurationMap.put(key, new ConfigurationProperty(value, comment));
            } else {
                if (StringUtils.isNotBlank(value) || StringUtils.isNotBlank(comment)) {
                    getFrame().alertWarning(this, Messages.getString("SettingsPanelMap.14")); //$NON-NLS-1$
                    return null;
                }
            }
        }
        return configurationMap;
    }

    public void doImportMap() {
        // close any open cell editor before importing
        if (this.configurationMapTable.getCellEditor() != null) {
            this.configurationMapTable.getCellEditor().stopCellEditing();
        }

        File file = getFrame().browseForFile(Messages.getString("SettingsPanelMap.15")); //$NON-NLS-1$

        if (file != null) {
            try {
                PropertiesConfiguration properties = new PropertiesConfiguration();
                properties.setDelimiterParsingDisabled(true);
                properties.setListDelimiter((char) 0);
                properties.load(file);

                Map<String, ConfigurationProperty> configurationMap = new HashMap<String, ConfigurationProperty>();
                Iterator<String> iterator = properties.getKeys();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = properties.getString(key);
                    String comment = properties.getLayout().getCanonicalComment(key, false);

                    configurationMap.put(key, new ConfigurationProperty(value, comment));
                }

                updateConfigurationTable(configurationMap, showValuesCheckbox.isSelected(), true);
                setSaveEnabled(true);
            } catch (Exception e) {
                getFrame().alertThrowable(getFrame(), e, Messages.getString("SettingsPanelMap.16")); //$NON-NLS-1$
            }
        }
    }

    public void doExportMap() {
        // close any open cell editor
        if (this.configurationMapTable.getCellEditor() != null) {
            this.configurationMapTable.getCellEditor().stopCellEditing();
        }

        if (isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, Messages.getString("SettingsPanelMap.17")); //$NON-NLS-1$

            if (option == JOptionPane.YES_OPTION) {
                if (!doSave()) {
                    return;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }

        final String workingId = getFrame().startWorking(Messages.getString("SettingsPanelMap.18") + getTabName() + Messages.getString("SettingsPanelMap.19")); //$NON-NLS-1$ //$NON-NLS-2$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            private Map<String, ConfigurationProperty> configurationMap;

            public Void doInBackground() {
                try {
                    File file = getFrame().createFileForExport(null, Messages.getString("SettingsPanelMap.20")); //$NON-NLS-1$
                    if (file != null) {
                        PropertiesConfiguration properties = new PropertiesConfiguration();
                        properties.setDelimiterParsingDisabled(true);
                        properties.setListDelimiter((char) 0);
                        properties.clear();
                        PropertiesConfigurationLayout layout = properties.getLayout();

                        configurationMap = getFrame().mirthClient.getConfigurationMap();
                        Map<String, ConfigurationProperty> sortedMap = new TreeMap<String, ConfigurationProperty>(String.CASE_INSENSITIVE_ORDER);
                        sortedMap.putAll(configurationMap);

                        for (Entry<String, ConfigurationProperty> entry : sortedMap.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue().getValue();
                            String comment = entry.getValue().getComment();

                            if (StringUtils.isNotBlank(key)) {
                                properties.setProperty(key, value);
                                layout.setComment(key, StringUtils.isBlank(comment) ? null : comment);
                            }
                        }

                        properties.save(file);
                    }
                } catch (Exception e) {
                    getFrame().alertThrowable(getFrame(), e);
                }

                return null;
            }

            @Override
            public void done() {
                getFrame().stopWorking(workingId);
            }
        };

        worker.execute();
    }

    private void updateConfigurationTable(Map<String, ConfigurationProperty> map, boolean show, boolean sort) {
        RefreshTableModel model = (RefreshTableModel) configurationMapTable.getModel();
        String[][] data = new String[map.size()][3];
        Map<String, ConfigurationProperty> sortedMap = null;
        if (sort) {
            sortedMap = new TreeMap<String, ConfigurationProperty>(String.CASE_INSENSITIVE_ORDER);
            sortedMap.putAll(map);
        } else {
            sortedMap = map;
        }

        int index = 0;
        for (Entry<String, ConfigurationProperty> entry : sortedMap.entrySet()) {
            data[index][0] = entry.getKey();
            data[index][1] = entry.getValue().getValue();
            data[index++][2] = entry.getValue().getComment();
        }

        updateCellRenderer(show);
        model.refreshDataVector(data);
    }

    private void updateCellRenderer(boolean show) {
        if (show) {
            configurationMapTable.getColumnExt(Messages.getString("SettingsPanelMap.21")).setCellRenderer(new DefaultTableCellRenderer()); //$NON-NLS-1$
        } else {
            configurationMapTable.getColumnExt(Messages.getString("SettingsPanelMap.22")).setCellRenderer(new MirthPasswordTableCellRenderer()); //$NON-NLS-1$
        }
    }

    private void showValuesCheckboxActionPerformed(boolean show) {
        updateCellRenderer(show);
        ((RefreshTableModel) configurationMapTable.getModel()).fireTableDataChanged();
        userPreferences.putBoolean(SHOW_VALUES_KEY, show);
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setLayout(new MigLayout(Messages.getString("SettingsPanelMap.23"))); //$NON-NLS-1$

        showValuesLabel = new JLabel(Messages.getString("SettingsPanelMap.24")); //$NON-NLS-1$
        showValuesCheckbox = new JCheckBox();
        String tooltip = Messages.getString("SettingsPanelMap.25"); //$NON-NLS-1$
        showValuesCheckbox.setToolTipText(tooltip);
        showValuesCheckbox.setBackground(Color.WHITE);
        showValuesCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showValuesCheckboxActionPerformed(showValuesCheckbox.isSelected());
            }
        });

        configurationMapTable = new MirthTable();
        configurationMapTable.putClientProperty(Messages.getString("SettingsPanelMap.26"), Boolean.TRUE); //$NON-NLS-1$
        configurationMapTable.getTableHeader().setReorderingAllowed(false);
        configurationMapTable.setSortable(false);
        configurationMapTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configurationMapTable.setModel(new RefreshTableModel(new String[][] {}, new String[] {
                Messages.getString("SettingsPanelMap.27"), Messages.getString("SettingsPanelMap.28"), Messages.getString("SettingsPanelMap.29") })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        TableCellEditor cellEditor = new TextFieldCellEditor() {

            @Override
            protected boolean valueChanged(String value) {
                PlatformUI.MIRTH_FRAME.setSaveEnabled(true);
                return true;
            }

        };
        configurationMapTable.getColumnExt(Messages.getString("SettingsPanelMap.30")).setCellEditor(cellEditor); //$NON-NLS-1$
        configurationMapTable.getColumnExt(Messages.getString("SettingsPanelMap.31")).setCellEditor(cellEditor); //$NON-NLS-1$
        configurationMapTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                int selectedRow;
                if (configurationMapTable.isEditing()) {
                    selectedRow = configurationMapTable.getEditingRow();
                } else {
                    selectedRow = configurationMapTable.getSelectedRow();
                }
                removeButton.setEnabled(selectedRow != -1);
            }
        });

        configurationMapTable.getColumnExt(Messages.getString("SettingsPanelMap.32")).setCellEditor(new MirthDialogTableCellEditor(configurationMapTable)); //$NON-NLS-1$

        if (Preferences.userNodeForPackage(Mirth.class).getBoolean(Messages.getString("SettingsPanelMap.33"), true)) { //$NON-NLS-1$
            configurationMapTable.setHighlighters(HighlighterFactory.createAlternateStriping(UIConstants.HIGHLIGHTER_COLOR, UIConstants.BACKGROUND_COLOR));
        }

        configurationMapScrollPane = new JScrollPane();
        configurationMapScrollPane.setViewportView(configurationMapTable);

        addButton = new MirthButton(Messages.getString("SettingsPanelMap.34")); //$NON-NLS-1$
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((RefreshTableModel) configurationMapTable.getModel()).addRow(new String[] { Messages.getString("SettingsPanelMap.35"), //$NON-NLS-1$
                        Messages.getString("SettingsPanelMap.36") }); //$NON-NLS-1$

                if (configurationMapTable.getRowCount() == 1) {
                    configurationMapTable.setRowSelectionInterval(0, 0);
                }

                PlatformUI.MIRTH_FRAME.setSaveEnabled(true);
            }

        });
        removeButton = new MirthButton(Messages.getString("SettingsPanelMap.37")); //$NON-NLS-1$
        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (configurationMapTable.getSelectedModelIndex() != -1 && !configurationMapTable.isEditing()) {
                    Integer selectedModelIndex = configurationMapTable.getSelectedModelIndex();

                    RefreshTableModel model = (RefreshTableModel) configurationMapTable.getModel();

                    int newViewIndex = configurationMapTable.convertRowIndexToView(selectedModelIndex);
                    if (newViewIndex == (model.getRowCount() - 1)) {
                        newViewIndex--;
                    }

                    // must set lastModelRow to -1 so that when setting the new
                    // row selection below the old data won't try to be saved.
                    model.removeRow(selectedModelIndex);

                    if (model.getRowCount() != 0) {
                        configurationMapTable.setRowSelectionInterval(newViewIndex, newViewIndex);
                    }

                    PlatformUI.MIRTH_FRAME.setSaveEnabled(true);
                }
            }

        });

        configurationMapPanel = new JPanel();
        configurationMapPanel.setBackground(Color.WHITE);
        configurationMapPanel.setLayout(new MigLayout(Messages.getString("SettingsPanelMap.38"), Messages.getString("SettingsPanelMap.39"), Messages.getString("SettingsPanelMap.40"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        configurationMapPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)), Messages.getString("SettingsPanelMap.41"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font(Messages.getString("SettingsPanelMap.42"), 1, 11))); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$

        JPanel showValuesPanel = new JPanel();
        showValuesPanel.setBackground(Color.WHITE);
        showValuesPanel.add(showValuesCheckbox);
        showValuesPanel.add(showValuesLabel);
        configurationMapPanel.add(showValuesPanel, Messages.getString("SettingsPanelMap.43")); //$NON-NLS-1$

        JPanel configurationMapSubPanel = new JPanel();
        configurationMapSubPanel.setBackground(Color.WHITE);
        configurationMapSubPanel.setLayout(new MigLayout(Messages.getString("SettingsPanelMap.44"), Messages.getString("SettingsPanelMap.45"), Messages.getString("SettingsPanelMap.46"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        configurationMapSubPanel.add(configurationMapScrollPane, Messages.getString("SettingsPanelMap.47")); //$NON-NLS-1$
        configurationMapSubPanel.add(addButton, Messages.getString("SettingsPanelMap.48")); //$NON-NLS-1$
        configurationMapSubPanel.add(removeButton, Messages.getString("SettingsPanelMap.49")); //$NON-NLS-1$
        configurationMapPanel.add(configurationMapSubPanel, Messages.getString("SettingsPanelMap.50")); //$NON-NLS-1$

        add(configurationMapPanel, Messages.getString("SettingsPanelMap.51")); //$NON-NLS-1$
    }

    private JLabel showValuesLabel;
    private JCheckBox showValuesCheckbox;
    private JPanel configurationMapPanel;
    private JScrollPane configurationMapScrollPane;
    private MirthTable configurationMapTable;
    private MirthButton addButton;
    private MirthButton removeButton;
}