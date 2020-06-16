/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.StringEscapeUtils;

import com.mirth.connect.model.Channel;
import com.mirth.connect.model.codetemplates.CodeTemplateLibrary;

public class ExportChannelLibrariesDialog extends MirthDialog {

    private int result = JOptionPane.CLOSED_OPTION;

    public ExportChannelLibrariesDialog(Channel channel) {
        super(PlatformUI.MIRTH_FRAME, true);
        initComponents(channel);
        initLayout();
        setMaximumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(Messages.getString("ExportChannelLibrariesDialog.0")); //$NON-NLS-1$
        pack();
        setLocationRelativeTo(PlatformUI.MIRTH_FRAME);
        yesButton.requestFocus();
        setVisible(true);
    }

    public int getResult() {
        return result;
    }

    private void initComponents(Channel channel) {
        label1 = new JLabel(Messages.getString("ExportChannelLibrariesDialog.1")); //$NON-NLS-1$
        label1.setIcon(UIManager.getIcon(Messages.getString("ExportChannelLibrariesDialog.2"))); //$NON-NLS-1$

        librariesTextPane = new JTextPane();
        librariesTextPane.setContentType(Messages.getString("ExportChannelLibrariesDialog.3")); //$NON-NLS-1$
        HTMLEditorKit editorKit = new HTMLEditorKit();
        StyleSheet styleSheet = editorKit.getStyleSheet();
        styleSheet.addRule(Messages.getString("ExportChannelLibrariesDialog.4")); //$NON-NLS-1$
        librariesTextPane.setEditorKit(editorKit);
        librariesTextPane.setEditable(false);
        librariesTextPane.setBackground(getBackground());
        librariesTextPane.setBorder(null);

        StringBuilder librariesText = new StringBuilder(Messages.getString("ExportChannelLibrariesDialog.5")); //$NON-NLS-1$
        for (CodeTemplateLibrary library : PlatformUI.MIRTH_FRAME.codeTemplatePanel.getCachedCodeTemplateLibraries().values()) {
            if (library.getEnabledChannelIds().contains(channel.getId()) || (library.isIncludeNewChannels() && !library.getDisabledChannelIds().contains(channel.getId()))) {
                librariesText.append(Messages.getString("ExportChannelLibrariesDialog.6")).append(StringEscapeUtils.escapeHtml4(library.getName())).append(Messages.getString("ExportChannelLibrariesDialog.7")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        librariesText.append(Messages.getString("ExportChannelLibrariesDialog.8")); //$NON-NLS-1$
        librariesTextPane.setText(librariesText.toString());
        librariesTextPane.setCaretPosition(0);

        librariesScrollPane = new JScrollPane(librariesTextPane);

        label2 = new JLabel(Messages.getString("ExportChannelLibrariesDialog.9")); //$NON-NLS-1$

        alwaysChooseCheckBox = new JCheckBox(Messages.getString("ExportChannelLibrariesDialog.10")); //$NON-NLS-1$

        yesButton = new JButton(Messages.getString("ExportChannelLibrariesDialog.11")); //$NON-NLS-1$
        yesButton.setMnemonic('Y');
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                result = JOptionPane.YES_OPTION;
                if (alwaysChooseCheckBox.isSelected()) {
                    Preferences.userNodeForPackage(Mirth.class).putBoolean(Messages.getString("ExportChannelLibrariesDialog.12"), true); //$NON-NLS-1$
                }
                dispose();
            }
        });

        noButton = new JButton(Messages.getString("ExportChannelLibrariesDialog.13")); //$NON-NLS-1$
        noButton.setMnemonic('N');
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                result = JOptionPane.NO_OPTION;
                if (alwaysChooseCheckBox.isSelected()) {
                    Preferences.userNodeForPackage(Mirth.class).putBoolean(Messages.getString("ExportChannelLibrariesDialog.14"), false); //$NON-NLS-1$
                }
                dispose();
            }
        });

        cancelButton = new JButton(Messages.getString("ExportChannelLibrariesDialog.15")); //$NON-NLS-1$
        cancelButton.setMnemonic('C');
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                result = JOptionPane.CANCEL_OPTION;
                dispose();
            }
        });
    }

    private void initLayout() {
        setLayout(new MigLayout(Messages.getString("ExportChannelLibrariesDialog.16"))); //$NON-NLS-1$
        add(label1);
        add(librariesScrollPane, Messages.getString("ExportChannelLibrariesDialog.17")); //$NON-NLS-1$
        add(label2, Messages.getString("ExportChannelLibrariesDialog.18")); //$NON-NLS-1$
        add(alwaysChooseCheckBox, Messages.getString("ExportChannelLibrariesDialog.19")); //$NON-NLS-1$
        add(yesButton, Messages.getString("ExportChannelLibrariesDialog.20")); //$NON-NLS-1$
        add(noButton, Messages.getString("ExportChannelLibrariesDialog.21")); //$NON-NLS-1$
        add(cancelButton, Messages.getString("ExportChannelLibrariesDialog.22")); //$NON-NLS-1$
    }

    private JLabel label1;
    private JTextPane librariesTextPane;
    private JScrollPane librariesScrollPane;
    private JLabel label2;
    private JCheckBox alwaysChooseCheckBox;
    private JButton yesButton;
    private JButton noButton;
    private JButton cancelButton;
}