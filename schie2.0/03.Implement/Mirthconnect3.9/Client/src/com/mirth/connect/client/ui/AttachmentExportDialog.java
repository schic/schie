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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.StringUtils;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.ui.components.MirthButton;
import com.mirth.connect.client.ui.util.DisplayUtil;
import com.mirth.connect.donkey.model.message.attachment.Attachment;
import com.mirth.connect.util.AttachmentUtil;

public class AttachmentExportDialog extends MirthDialog {
    private Preferences userPreferences;

    public AttachmentExportDialog() {
        super(PlatformUI.MIRTH_FRAME);
        userPreferences = Preferences.userNodeForPackage(Mirth.class);

        setTitle(Messages.getString("AttachmentExportDialog.0")); //$NON-NLS-1$
        setPreferredSize(new Dimension(500, 155));
        getContentPane().setBackground(Color.white);
        setLocationRelativeTo(null);
        DisplayUtil.setResizable(this, false);
        setModal(true);

        initComponents();
        initLayout();
        pack();
    }

    private void initComponents() {
        binaryButton = new JRadioButton(Messages.getString("AttachmentExportDialog.1")); //$NON-NLS-1$
        binaryButton.setSelected(true);
        binaryButton.setBackground(Color.white);
        binaryButton.setFocusable(false);

        textButton = new JRadioButton(Messages.getString("AttachmentExportDialog.2")); //$NON-NLS-1$
        textButton.setBackground(Color.white);
        textButton.setFocusable(false);

        ButtonGroup typeButtonGroup = new ButtonGroup();
        typeButtonGroup.add(binaryButton);
        typeButtonGroup.add(textButton);

        serverButton = new JRadioButton(Messages.getString("AttachmentExportDialog.3")); //$NON-NLS-1$
        serverButton.setBackground(Color.white);
        serverButton.setFocusable(false);

        localButton = new JRadioButton(Messages.getString("AttachmentExportDialog.4")); //$NON-NLS-1$
        localButton.setBackground(Color.white);
        localButton.setSelected(true);
        localButton.setFocusable(false);

        browseButton = new MirthButton(Messages.getString("AttachmentExportDialog.5")); //$NON-NLS-1$

        localButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseButton.setEnabled(true);
                fileField.setText(null);
            }
        });

        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseButton.setEnabled(false);
                fileField.setText(null);
            }
        });

        ButtonGroup locationButtonGroup = new ButtonGroup();
        locationButtonGroup.add(serverButton);
        locationButtonGroup.add(localButton);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseSelected();
            }
        });

        fileField = new JTextField();

        exportButton = new JButton(Messages.getString("AttachmentExportDialog.6")); //$NON-NLS-1$
        cancelButton = new JButton(Messages.getString("AttachmentExportDialog.7")); //$NON-NLS-1$

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                export();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

    private void initLayout() {
        setLayout(new MigLayout(Messages.getString("AttachmentExportDialog.8"), Messages.getString("AttachmentExportDialog.9"))); //$NON-NLS-1$ //$NON-NLS-2$

        add(new JLabel(Messages.getString("AttachmentExportDialog.10"))); //$NON-NLS-1$
        add(binaryButton, Messages.getString("AttachmentExportDialog.11")); //$NON-NLS-1$
        add(textButton, Messages.getString("AttachmentExportDialog.12")); //$NON-NLS-1$

        add(new JLabel(Messages.getString("AttachmentExportDialog.13"))); //$NON-NLS-1$
        add(serverButton, Messages.getString("AttachmentExportDialog.14")); //$NON-NLS-1$
        add(localButton);
        add(browseButton, Messages.getString("AttachmentExportDialog.15")); //$NON-NLS-1$

        add(new JLabel(Messages.getString("AttachmentExportDialog.16"))); //$NON-NLS-1$
        add(fileField, Messages.getString("AttachmentExportDialog.17")); //$NON-NLS-1$

        add(new JSeparator(), Messages.getString("AttachmentExportDialog.18")); //$NON-NLS-1$
        add(exportButton, Messages.getString("AttachmentExportDialog.19")); //$NON-NLS-1$
        add(cancelButton, Messages.getString("AttachmentExportDialog.20")); //$NON-NLS-1$
    }

    private void browseSelected() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (userPreferences != null) {
            File currentDir = new File(userPreferences.get(Messages.getString("AttachmentExportDialog.21"), Messages.getString("AttachmentExportDialog.22"))); //$NON-NLS-1$ //$NON-NLS-2$

            if (currentDir.exists()) {
                chooser.setCurrentDirectory(currentDir);
            }
        }

        if (chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
            if (userPreferences != null) {
                userPreferences.put(Messages.getString("AttachmentExportDialog.23"), chooser.getCurrentDirectory().getPath()); //$NON-NLS-1$
            }

            fileField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void export() {
        if (StringUtils.isBlank(fileField.getText())) {
            PlatformUI.MIRTH_FRAME.alertError(this, Messages.getString("AttachmentExportDialog.24")); //$NON-NLS-1$
            fileField.setBackground(UIConstants.INVALID_COLOR);
            return;
        } else {
            fileField.setBackground(Color.white);
        }

        if (PlatformUI.MIRTH_FRAME.messageBrowser.getSelectedMimeType().equalsIgnoreCase(Messages.getString("AttachmentExportDialog.25"))) { //$NON-NLS-1$
            PlatformUI.MIRTH_FRAME.alertError(this, Messages.getString("AttachmentExportDialog.26")); //$NON-NLS-1$
            return;
        }

        doExport();

        setVisible(false);
        setCursor(Cursor.getDefaultCursor());
    }

    private void doExport() {
        final String workingId = PlatformUI.MIRTH_FRAME.startWorking(Messages.getString("AttachmentExportDialog.27")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private String errorMessage = Messages.getString("AttachmentExportDialog.28"); //$NON-NLS-1$

            public Void doInBackground() {
                boolean binary = binaryButton.isSelected();
                try {
                    if (localButton.isSelected()) {
                        AttachmentUtil.writeToFile(fileField.getText(), getSelectedAttachment(), binary);
                    } else {
                        PlatformUI.MIRTH_FRAME.mirthClient.exportAttachmentServer(PlatformUI.MIRTH_FRAME.messageBrowser.getChannelId(), PlatformUI.MIRTH_FRAME.messageBrowser.getSelectedMessageId(), PlatformUI.MIRTH_FRAME.messageBrowser.getSelectedAttachmentId(), fileField.getText(), binary);
                    }
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                }

                return null;
            }

            public void done() {
                PlatformUI.MIRTH_FRAME.stopWorking(workingId);
                PlatformUI.MIRTH_FRAME.alertInformation(PlatformUI.MIRTH_FRAME, StringUtils.isBlank(errorMessage) ? Messages.getString("AttachmentExportDialog.29") + fileField.getText() : Messages.getString("AttachmentExportDialog.30") + errorMessage); //$NON-NLS-1$ //$NON-NLS-2$
            }
        };

        worker.execute();
    }

    public Attachment getSelectedAttachment() throws ClientException {
        return PlatformUI.MIRTH_FRAME.mirthClient.getAttachment(PlatformUI.MIRTH_FRAME.messageBrowser.getChannelId(), PlatformUI.MIRTH_FRAME.messageBrowser.getSelectedMessageId(), PlatformUI.MIRTH_FRAME.messageBrowser.getSelectedAttachmentId());
    }

    private JRadioButton textButton;
    private JRadioButton binaryButton;

    private JRadioButton serverButton;
    private JRadioButton localButton;
    private JButton browseButton;

    private JTextField fileField;

    private JButton exportButton;
    private JButton cancelButton;
}