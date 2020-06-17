/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.ui.browsers.message.MessageBrowser;
import com.mirth.connect.client.ui.components.MirthButton;
import com.mirth.connect.client.ui.components.MirthCheckBox;
import com.mirth.connect.client.ui.components.MirthRadioButton;
import com.mirth.connect.client.ui.components.MirthTextField;
import com.mirth.connect.donkey.model.message.Message;
import com.mirth.connect.model.MessageImportResult;
import com.mirth.connect.util.MessageImporter;
import com.mirth.connect.util.MessageImporter.MessageImportInvalidPathException;
import com.mirth.connect.util.messagewriter.MessageWriter;
import com.mirth.connect.util.messagewriter.MessageWriterException;

public class MessageImportDialog extends MirthDialog {
    private String channelId;
    private Frame parent;
    private Preferences userPreferences;

    public MessageImportDialog() {
        super(PlatformUI.MIRTH_FRAME);
        parent = PlatformUI.MIRTH_FRAME;
        userPreferences = Frame.userPreferences;

        setTitle(Messages.getString("MessageImportDialog.0")); //$NON-NLS-1$
        setLocationRelativeTo(null);
        setModal(true);
        initComponents();
        initLayout();
        pack();
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setMessageBrowser(MessageBrowser messageBrowser) {
        this.messageBrowser = messageBrowser;
    }

    private void initComponents() {
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);

        importFromLabel = new JLabel(Messages.getString("MessageImportDialog.1")); //$NON-NLS-1$

        ActionListener importDestinationChanged = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (importServerRadio.isSelected()) {
                    fileTextField.setText(null);
                    browseButton.setEnabled(false);
                } else {
                    fileTextField.setText(null);
                    browseButton.setEnabled(true);
                }
            }
        };

        importServerRadio = new MirthRadioButton(Messages.getString("MessageImportDialog.2")); //$NON-NLS-1$
        importServerRadio.setSelected(true);
        importServerRadio.setBackground(UIConstants.BACKGROUND_COLOR);
        importServerRadio.addActionListener(importDestinationChanged);
        importServerRadio.setToolTipText(Messages.getString("MessageImportDialog.3")); //$NON-NLS-1$

        importLocalRadio = new MirthRadioButton(Messages.getString("MessageImportDialog.4")); //$NON-NLS-1$
        importLocalRadio.setBackground(UIConstants.BACKGROUND_COLOR);
        importLocalRadio.addActionListener(importDestinationChanged);
        importLocalRadio.setToolTipText(Messages.getString("MessageImportDialog.5")); //$NON-NLS-1$

        importFromButtonGroup = new ButtonGroup();
        importFromButtonGroup.add(importServerRadio);
        importFromButtonGroup.add(importLocalRadio);

        browseButton = new MirthButton(Messages.getString("MessageImportDialog.6")); //$NON-NLS-1$
        browseButton.setEnabled(false);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseSelected();
            }
        });

        fileLabel = new JLabel(Messages.getString("MessageImportDialog.7")); //$NON-NLS-1$

        fileTextField = new MirthTextField();
        fileTextField.setToolTipText(Messages.getString("MessageImportDialog.8")); //$NON-NLS-1$

        subfoldersCheckbox = new MirthCheckBox(Messages.getString("MessageImportDialog.9")); //$NON-NLS-1$
        subfoldersCheckbox.setSelected(true);
        subfoldersCheckbox.setBackground(UIConstants.BACKGROUND_COLOR);
        subfoldersCheckbox.setToolTipText(Messages.getString("MessageImportDialog.10")); //$NON-NLS-1$

        noteLabel = new JLabel(Messages.getString("MessageImportDialog.11")); //$NON-NLS-1$

        importButton = new MirthButton(Messages.getString("MessageImportDialog.12")); //$NON-NLS-1$
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importMessages();
            }
        });

        cancelButton = new MirthButton(Messages.getString("MessageImportDialog.13")); //$NON-NLS-1$
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

    private void browseSelected() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        if (userPreferences != null) {
            File currentDir = new File(userPreferences.get(Messages.getString("MessageImportDialog.14"), Messages.getString("MessageImportDialog.15"))); //$NON-NLS-1$ //$NON-NLS-2$

            if (currentDir.exists()) {
                chooser.setCurrentDirectory(currentDir);
            }
        }

        if (chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
            if (userPreferences != null) {
                userPreferences.put(Messages.getString("MessageImportDialog.16"), chooser.getCurrentDirectory().getPath()); //$NON-NLS-1$
            }

            fileTextField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void initLayout() {
        setLayout(new MigLayout(Messages.getString("MessageImportDialog.17"), Messages.getString("MessageImportDialog.18"), Messages.getString("MessageImportDialog.19"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        add(importFromLabel);
        add(importServerRadio, Messages.getString("MessageImportDialog.20")); //$NON-NLS-1$
        add(importLocalRadio);
        add(browseButton);

        add(fileLabel);
        add(fileTextField, Messages.getString("MessageImportDialog.21")); //$NON-NLS-1$

        add(subfoldersCheckbox, Messages.getString("MessageImportDialog.22")); //$NON-NLS-1$

        add(noteLabel, Messages.getString("MessageImportDialog.23")); //$NON-NLS-1$
        add(new JSeparator(), Messages.getString("MessageImportDialog.24")); //$NON-NLS-1$
        add(importButton, Messages.getString("MessageImportDialog.25")); //$NON-NLS-1$
        add(cancelButton, Messages.getString("MessageImportDialog.26")); //$NON-NLS-1$
    }

    private void importMessages() {
        if (StringUtils.isBlank(fileTextField.getText())) {
            fileTextField.setBackground(UIConstants.INVALID_COLOR);
            parent.alertError(parent, Messages.getString("MessageImportDialog.27")); //$NON-NLS-1$
            setVisible(true);
            return;
        } else {
            fileTextField.setBackground(null);
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MessageImportResult result;

        try {
            if (importLocalRadio.isSelected()) {
                MessageWriter messageWriter = new MessageWriter() {
                    @Override
                    public boolean write(Message message) throws MessageWriterException {
                        try {
                            parent.mirthClient.importMessage(channelId, message);
                        } catch (ClientException e) {
                            throw new MessageWriterException(e);
                        }

                        return true;
                    }

                    @Override
                    public void finishWrite() {}

                    @Override
                    public void close() throws MessageWriterException {}
                };

                try {
                    result = new MessageImporter().importMessages(fileTextField.getText(), subfoldersCheckbox.isSelected(), messageWriter, SystemUtils.getUserHome().getAbsolutePath());
                } catch (MessageImportInvalidPathException e) {
                    setCursor(Cursor.getDefaultCursor());
                    parent.alertError(parent, e.getMessage());
                    setVisible(true);
                    return;
                }
            } else {
                result = parent.mirthClient.importMessagesServer(channelId, fileTextField.getText(), subfoldersCheckbox.isSelected());
            }

            setVisible(false);
            setCursor(Cursor.getDefaultCursor());

            if (result.getSuccessCount() == 0 && result.getTotalCount() == 0) {
                parent.alertInformation(parent, Messages.getString("MessageImportDialog.28")); //$NON-NLS-1$
            } else {
                if (result.getSuccessCount() > 0 && messageBrowser != null) {
                    messageBrowser.updateFilterButtonFont(Font.BOLD);
                }

                parent.alertInformation(parent, result.getSuccessCount() + Messages.getString("MessageImportDialog.29") + result.getTotalCount() + Messages.getString("MessageImportDialog.30") + fileTextField.getText() + Messages.getString("MessageImportDialog.31")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        } catch (Exception e) {
            setCursor(Cursor.getDefaultCursor());
            Throwable cause = (e.getCause() == null) ? e : e.getCause();
            parent.alertThrowable(parent, cause);
        }
    }

    private MessageBrowser messageBrowser;
    private JLabel importFromLabel;
    private ButtonGroup importFromButtonGroup;
    private MirthRadioButton importServerRadio;
    private MirthRadioButton importLocalRadio;
    private MirthButton browseButton;
    private JLabel fileLabel;
    private MirthTextField fileTextField;
    private MirthCheckBox subfoldersCheckbox;
    private JLabel noteLabel;
    private MirthButton importButton;
    private MirthButton cancelButton;
}
