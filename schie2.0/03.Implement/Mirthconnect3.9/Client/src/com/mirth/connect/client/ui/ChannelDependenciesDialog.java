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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

import com.mirth.connect.client.ui.codetemplate.CodeTemplateLibrariesPanel;
import com.mirth.connect.client.ui.codetemplate.CodeTemplatePanel.UpdateSwingWorker;
import com.mirth.connect.client.ui.dependencies.ChannelDependenciesPanel;
import com.mirth.connect.donkey.model.channel.DestinationConnectorPropertiesInterface;
import com.mirth.connect.donkey.model.channel.SourceConnectorPropertiesInterface;
import com.mirth.connect.model.Channel;
import com.mirth.connect.model.Connector;
import com.mirth.connect.model.codetemplates.CodeTemplate;
import com.mirth.connect.model.codetemplates.CodeTemplateLibrary;

public class ChannelDependenciesDialog extends MirthDialog {

    private Channel channel;
    private boolean saved;
    private boolean resourcesReady;
    private boolean codeTemplateLibrariesReady;

    public ChannelDependenciesDialog(Channel channel) {
        super(PlatformUI.MIRTH_FRAME, true);
        this.channel = channel;

        initComponents();
        initLayout();
        setPreferredSize(new Dimension(450, 434));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(Messages.getString("ChannelDependenciesDialog.0")); //$NON-NLS-1$
        pack();
        setLocationRelativeTo(PlatformUI.MIRTH_FRAME);

        okButton.setEnabled(false);

        resourcesPanel.initialize();
        codeTemplateLibrariesPanel.initialize();

        setVisible(true);
    }

    public boolean wasSaved() {
        return saved;
    }

    public Map<Integer, Map<String, String>> getSelectedResourceIds() {
        return resourcesPanel.getSelectedResourceIds();
    }

    public void resourcesReady() {
        resourcesReady = true;
        checkReady();
    }

    public void codeTemplateLibrariesReady() {
        codeTemplateLibrariesReady = true;
        checkReady();
    }

    private void checkReady() {
        if (resourcesReady && codeTemplateLibrariesReady) {
            okButton.setEnabled(true);
        }
    }

    private void initComponents() {
        setBackground(UIConstants.BACKGROUND_COLOR);
        getContentPane().setBackground(getBackground());

        containerPanel = new JPanel();
        tabPane = new JTabbedPane();

        resourcesContainerPanel = new JPanel();
        resourcesContainerPanel.setBackground(getBackground());
        resourcesPanel = new LibraryResourcesPanel(this, channel);

        codeTemplateLibrariesContainerPanel = new JPanel();
        codeTemplateLibrariesContainerPanel.setBackground(getBackground());
        codeTemplateLibrariesPanel = new CodeTemplateLibrariesPanel(this, channel);

        dependenciesContainerPanel = new JPanel();
        dependenciesContainerPanel.setBackground(getBackground());
        dependenciesPanel = new ChannelDependenciesPanel(this, channel);

        bottomPanel = new JPanel();
        bottomPanel.setBackground(getBackground());
        separator = new JSeparator();

        buttonPanel = new JPanel();
        buttonPanel.setBackground(getBackground());

        okButton = new JButton(Messages.getString("ChannelDependenciesDialog.1")); //$NON-NLS-1$
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed();
            }
        });

        cancelButton = new JButton(Messages.getString("ChannelDependenciesDialog.2")); //$NON-NLS-1$
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed();
            }
        });
    }

    private void initLayout() {
        setLayout(new MigLayout(Messages.getString("ChannelDependenciesDialog.3"))); //$NON-NLS-1$

        containerPanel.setLayout(new MigLayout(Messages.getString("ChannelDependenciesDialog.4"))); //$NON-NLS-1$

        codeTemplateLibrariesContainerPanel.setLayout(new MigLayout(Messages.getString("ChannelDependenciesDialog.5"))); //$NON-NLS-1$
        codeTemplateLibrariesContainerPanel.add(codeTemplateLibrariesPanel, Messages.getString("ChannelDependenciesDialog.6")); //$NON-NLS-1$
        tabPane.add(Messages.getString("ChannelDependenciesDialog.7"), codeTemplateLibrariesContainerPanel); //$NON-NLS-1$

        resourcesContainerPanel.setLayout(new MigLayout(Messages.getString("ChannelDependenciesDialog.8"))); //$NON-NLS-1$
        resourcesContainerPanel.add(resourcesPanel, Messages.getString("ChannelDependenciesDialog.9")); //$NON-NLS-1$
        tabPane.add(Messages.getString("ChannelDependenciesDialog.10"), resourcesContainerPanel); //$NON-NLS-1$

        dependenciesContainerPanel.setLayout(new MigLayout(Messages.getString("ChannelDependenciesDialog.11"))); //$NON-NLS-1$
        dependenciesContainerPanel.add(dependenciesPanel, Messages.getString("ChannelDependenciesDialog.12")); //$NON-NLS-1$
        tabPane.add(Messages.getString("ChannelDependenciesDialog.13"), dependenciesContainerPanel); //$NON-NLS-1$

        containerPanel.add(tabPane, Messages.getString("ChannelDependenciesDialog.14")); //$NON-NLS-1$
        add(containerPanel, Messages.getString("ChannelDependenciesDialog.15")); //$NON-NLS-1$

        bottomPanel.setLayout(new MigLayout(Messages.getString("ChannelDependenciesDialog.16"))); //$NON-NLS-1$
        bottomPanel.add(separator, Messages.getString("ChannelDependenciesDialog.17")); //$NON-NLS-1$
        buttonPanel.setLayout(new MigLayout(Messages.getString("ChannelDependenciesDialog.18"))); //$NON-NLS-1$
        buttonPanel.add(okButton, Messages.getString("ChannelDependenciesDialog.19")); //$NON-NLS-1$
        buttonPanel.add(cancelButton, Messages.getString("ChannelDependenciesDialog.20")); //$NON-NLS-1$
        bottomPanel.add(buttonPanel, Messages.getString("ChannelDependenciesDialog.21")); //$NON-NLS-1$
        add(bottomPanel, Messages.getString("ChannelDependenciesDialog.22")); //$NON-NLS-1$
    }

    private void okButtonActionPerformed() {
        if (!dependenciesPanel.saveChanges()) {
            return;
        }

        boolean resourcesChanged = false;
        Map<Integer, Map<String, String>> selectedResourceIds = resourcesPanel.getSelectedResourceIds();
        if (!Objects.equals(channel.getProperties().getResourceIds(), selectedResourceIds.get(null))) {
            resourcesChanged = true;
        }
        if (!Objects.equals(((SourceConnectorPropertiesInterface) channel.getSourceConnector().getProperties()).getSourceConnectorProperties().getResourceIds(), selectedResourceIds.get(channel.getSourceConnector().getMetaDataId()))) {
            resourcesChanged = true;
        }
        for (Connector destinationConnector : channel.getDestinationConnectors()) {
            if (!Objects.equals(((DestinationConnectorPropertiesInterface) destinationConnector.getProperties()).getDestinationConnectorProperties().getResourceIds(), selectedResourceIds.get(destinationConnector.getMetaDataId()))) {
                resourcesChanged = true;
                break;
            }
        }
        final boolean resourcesChangedFinal = resourcesChanged;

        Map<String, CodeTemplateLibrary> libraryMap = codeTemplateLibrariesPanel.getLibraryMap();
        if (codeTemplateLibrariesPanel.wasChanged() && !PlatformUI.MIRTH_FRAME.codeTemplatePanel.getCachedCodeTemplateLibraries().equals(libraryMap)) {
            if (!PlatformUI.MIRTH_FRAME.alertOption(this, Messages.getString("ChannelDependenciesDialog.23"))) { //$NON-NLS-1$
                return;
            }

            UpdateSwingWorker worker = PlatformUI.MIRTH_FRAME.codeTemplatePanel.getSwingWorker(libraryMap, new HashMap<String, CodeTemplateLibrary>(), new HashMap<String, CodeTemplate>(), new HashMap<String, CodeTemplate>(), false);
            worker.setActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (resourcesChangedFinal) {
                        saved = true;
                    }
                    dispose();
                }
            });
            worker.execute();
        } else {
            if (resourcesChangedFinal) {
                saved = true;
            }
            dispose();
        }
    }

    private void cancelButtonActionPerformed() {
        dispose();
    }

    private JPanel containerPanel;
    private JTabbedPane tabPane;
    private JPanel resourcesContainerPanel;
    private LibraryResourcesPanel resourcesPanel;
    private JPanel codeTemplateLibrariesContainerPanel;
    private CodeTemplateLibrariesPanel codeTemplateLibrariesPanel;
    private JPanel dependenciesContainerPanel;
    private ChannelDependenciesPanel dependenciesPanel;

    private JPanel bottomPanel;
    private JSeparator separator;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton cancelButton;
}