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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.StringUtils;

import com.mirth.connect.client.ui.components.ItemSelectionTable;
import com.mirth.connect.client.ui.components.ItemSelectionTableModel;
import com.mirth.connect.client.ui.components.MirthTable;
import com.mirth.connect.client.ui.util.DisplayUtil;
import com.mirth.connect.model.filters.MessageFilter;

public class ReprocessMessagesDialog extends MirthDialog {

    private Frame parent;
    private MessageFilter filter = null;
    private Long messageId = null;
    private String channelId;
    private boolean showWarning;

    public ReprocessMessagesDialog(String channelId, MessageFilter filter, Long messageId, Map<Integer, String> destinationsConnectors, Integer selectedMetaDataId, boolean showWarning) {
        super(PlatformUI.MIRTH_FRAME);
        this.parent = PlatformUI.MIRTH_FRAME;
        this.showWarning = showWarning;
        initComponents();
        initLayout();
        this.channelId = channelId;
        this.filter = filter;
        this.messageId = messageId;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        DisplayUtil.setResizable(this, false);
        Dimension dlgSize = getPreferredSize();
        Dimension frmSize = parent.getSize();
        Point loc = parent.getLocation();

        if ((frmSize.width == 0 && frmSize.height == 0) || (loc.x == 0 && loc.y == 0)) {
            setLocationRelativeTo(null);
        } else {
            setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        }

        setTitle(Messages.getString("ReprocessMessagesDialog.0")); //$NON-NLS-1$

        makeIncludedDestinationsTable(destinationsConnectors, selectedMetaDataId);
        okButton.requestFocus();
        setVisible(true);
    }

    /**
     * Makes the alert table with a parameter that is true if a new alert should be added as well.
     */
    public void makeIncludedDestinationsTable(Map<Integer, String> destinationsConnectors, Integer selectedMetaDataId) {
        List<Integer> selectedMetaDataIds = null;

        if (selectedMetaDataId != null && selectedMetaDataId > 0) {
            selectedMetaDataIds = new ArrayList<Integer>();
            selectedMetaDataIds.add(selectedMetaDataId);
        }

        includedDestinationsTable = new ItemSelectionTable();
        includedDestinationsTable.setModel(new ItemSelectionTableModel<Integer, String>(destinationsConnectors, selectedMetaDataIds, Messages.getString("ReprocessMessagesDialog.1"), Messages.getString("ReprocessMessagesDialog.2"))); //$NON-NLS-1$ //$NON-NLS-2$
        includedDestinationsPane.setViewportView(includedDestinationsTable);
    }

    private void initComponents() {
        warningPane = new JEditorPane(Messages.getString("ReprocessMessagesDialog.3"), Messages.getString("ReprocessMessagesDialog.4")); //$NON-NLS-1$ //$NON-NLS-2$
        warningPane.setBorder(BorderFactory.createEmptyBorder());
        warningPane.setBackground(getBackground());
        warningPane.setEditable(false);
        HTMLEditorKit editorKit = new HTMLEditorKit();
        StyleSheet styleSheet = editorKit.getStyleSheet();
        styleSheet.addRule(Messages.getString("ReprocessMessagesDialog.5")); //$NON-NLS-1$
        warningPane.setEditorKit(editorKit);
        warningPane.setText(Messages.getString("ReprocessMessagesDialog.6")); //$NON-NLS-1$

        overwriteCheckBox = new JCheckBox();
        overwriteCheckBox.setText(Messages.getString("ReprocessMessagesDialog.7")); //$NON-NLS-1$

        reprocessLabel = new JLabel(Messages.getString("ReprocessMessagesDialog.8")); //$NON-NLS-1$

        selectAllLabel = new JLabel(Messages.getString("ReprocessMessagesDialog.9")); //$NON-NLS-1$
        selectAllLabel.setForeground(Color.BLUE);
        selectAllLabel.addMouseListener(new MouseAdapter() {
            @Override
            @SuppressWarnings("unchecked")
            public void mouseReleased(MouseEvent evt) {
                ((ItemSelectionTableModel<Integer, String>) includedDestinationsTable.getModel()).selectAllKeys();
            }
        });

        selectSeparatorLabel = new JLabel(Messages.getString("ReprocessMessagesDialog.10")); //$NON-NLS-1$

        deselectAllLabel = new JLabel(Messages.getString("ReprocessMessagesDialog.11")); //$NON-NLS-1$
        deselectAllLabel.setForeground(Color.BLUE);
        deselectAllLabel.addMouseListener(new MouseAdapter() {
            @Override
            @SuppressWarnings("unchecked")
            public void mouseReleased(MouseEvent evt) {
                ((ItemSelectionTableModel<Integer, String>) includedDestinationsTable.getModel()).unselectAllKeys();
            }
        });

        includedDestinationsTable = new MirthTable();
        includedDestinationsPane = new JScrollPane(includedDestinationsTable);

        okButton = new JButton();
        okButton.setText(Messages.getString("ReprocessMessagesDialog.12")); //$NON-NLS-1$
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (showWarning && Preferences.userNodeForPackage(Mirth.class).getBoolean(Messages.getString("ReprocessMessagesDialog.13"), true)) { //$NON-NLS-1$
                    String result = DisplayUtil.showInputDialog(ReprocessMessagesDialog.this, Messages.getString("ReprocessMessagesDialog.14"), Messages.getString("ReprocessMessagesDialog.15"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
                    if (!StringUtils.equals(result, Messages.getString("ReprocessMessagesDialog.16"))) { //$NON-NLS-1$
                        parent.alertWarning(ReprocessMessagesDialog.this, Messages.getString("ReprocessMessagesDialog.17")); //$NON-NLS-1$
                        return;
                    }
                }

                ItemSelectionTableModel<Integer, String> model = (ItemSelectionTableModel<Integer, String>) includedDestinationsTable.getModel();
                List<Integer> metaDataIds = model.getKeys(true);

                if (metaDataIds.size() == model.getRowCount()) {
                    metaDataIds = null;
                }

                parent.reprocessMessage(channelId, filter, messageId, overwriteCheckBox.isSelected(), metaDataIds);
                ReprocessMessagesDialog.this.dispose();
            }
        });

        cancelButton = new JButton();
        cancelButton.setText(Messages.getString("ReprocessMessagesDialog.18")); //$NON-NLS-1$
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ReprocessMessagesDialog.this.dispose();
            }
        });
    }

    private void initLayout() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(Messages.getString("ReprocessMessagesDialog.19"), Messages.getString("ReprocessMessagesDialog.20"))); //$NON-NLS-1$ //$NON-NLS-2$
        contentPane.setPreferredSize(new Dimension(425, 330));

        if (showWarning) {
            contentPane.add(warningPane, Messages.getString("ReprocessMessagesDialog.21")); //$NON-NLS-1$
            contentPane.add(new JSeparator(), Messages.getString("ReprocessMessagesDialog.22")); //$NON-NLS-1$
        }
        contentPane.add(overwriteCheckBox, Messages.getString("ReprocessMessagesDialog.23")); //$NON-NLS-1$
        contentPane.add(reprocessLabel, Messages.getString("ReprocessMessagesDialog.24")); //$NON-NLS-1$
        contentPane.add(selectAllLabel, Messages.getString("ReprocessMessagesDialog.25")); //$NON-NLS-1$
        contentPane.add(selectSeparatorLabel);
        contentPane.add(deselectAllLabel, Messages.getString("ReprocessMessagesDialog.26")); //$NON-NLS-1$
        contentPane.add(includedDestinationsPane, Messages.getString("ReprocessMessagesDialog.27")); //$NON-NLS-1$
        contentPane.add(new JSeparator(), Messages.getString("ReprocessMessagesDialog.28")); //$NON-NLS-1$
        contentPane.add(okButton, Messages.getString("ReprocessMessagesDialog.29")); //$NON-NLS-1$
        contentPane.add(cancelButton, Messages.getString("ReprocessMessagesDialog.30")); //$NON-NLS-1$

        pack();
    }

    private JEditorPane warningPane;
    private JCheckBox overwriteCheckBox;
    private JLabel reprocessLabel;
    private JLabel selectAllLabel;
    private JLabel selectSeparatorLabel;
    private JLabel deselectAllLabel;
    private JScrollPane includedDestinationsPane;
    private MirthTable includedDestinationsTable;
    private JButton okButton;
    private JButton cancelButton;
}
