/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.lang3.StringUtils;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.core.TaskConstants;
import com.mirth.connect.client.ui.util.DisplayUtil;

public class RemoveMessagesDialog extends MirthDialog {
    private Frame parent;
    private Set<String> channelIds;

    public RemoveMessagesDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        DisplayUtil.setResizable(this, false);
        this.parent = parent;
    }

    public void init(Set<String> selectedChannelIds, boolean restartCheckboxEnabled) {
        yesButton.requestFocus();

        boolean canClearStats = AuthorizationControllerFactory.getAuthorizationController().checkTask(TaskConstants.DASHBOARD_KEY, TaskConstants.DASHBOARD_CLEAR_STATS);
        clearStatsCheckBox.setSelected(canClearStats);
        clearStatsCheckBox.setEnabled(canClearStats);

        includeRunningChannels.setSelected(false);
        channelIds = selectedChannelIds;

        includeRunningChannels.setEnabled(restartCheckboxEnabled);
    }

    // @formatter:off
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        messageLabel = new javax.swing.JLabel();
        clearStatsCheckBox = new com.mirth.connect.client.ui.components.MirthCheckBox();
        includeRunningChannels = new com.mirth.connect.client.ui.components.MirthCheckBox();
        buttonPanel = new javax.swing.JPanel();
        yesButton = new com.mirth.connect.client.ui.components.MirthButton();
        noButton = new com.mirth.connect.client.ui.components.MirthButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Messages.getString("RemoveMessagesDialog.0")); //$NON-NLS-1$

        messageLabel.setText(Messages.getString("RemoveMessagesDialog.1")); //$NON-NLS-1$

        clearStatsCheckBox.setText(Messages.getString("RemoveMessagesDialog.2")); //$NON-NLS-1$

        includeRunningChannels.setText(Messages.getString("RemoveMessagesDialog.3")); //$NON-NLS-1$

        yesButton.setText(Messages.getString("RemoveMessagesDialog.4")); //$NON-NLS-1$
        yesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        yesButton.setMaximumSize(new java.awt.Dimension(75, 22));
        yesButton.setMinimumSize(new java.awt.Dimension(75, 22));
        yesButton.setPreferredSize(new java.awt.Dimension(75, 22));
        yesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        yesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(yesButton);

        noButton.setText(Messages.getString("RemoveMessagesDialog.5")); //$NON-NLS-1$
        noButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        noButton.setMaximumSize(new java.awt.Dimension(75, 22));
        noButton.setMinimumSize(new java.awt.Dimension(75, 22));
        noButton.setPreferredSize(new java.awt.Dimension(75, 22));
        noButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        noButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(noButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(includeRunningChannels, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(messageLabel)
                            .addComponent(clearStatsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(129, 129, 129))))
            .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(messageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(includeRunningChannels, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearStatsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void noButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_noButtonActionPerformed

    private void yesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonActionPerformed
        if (Preferences.userNodeForPackage(Mirth.class).getBoolean(Messages.getString("RemoveMessagesDialog.6"), true)) { //$NON-NLS-1$
            String result = DisplayUtil.showInputDialog(this, Messages.getString("RemoveMessagesDialog.7"), Messages.getString("RemoveMessagesDialog.8"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
            if (!StringUtils.equals(result, Messages.getString("RemoveMessagesDialog.9"))) { //$NON-NLS-1$
                parent.alertWarning(this, Messages.getString("RemoveMessagesDialog.10")); //$NON-NLS-1$
                return;
            }
        }
        
        final String workingId = parent.startWorking(Messages.getString("RemoveMessagesDialog.11")); //$NON-NLS-1$
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                try {
                    parent.mirthClient.removeAllMessages(channelIds, includeRunningChannels.isSelected(), clearStatsCheckBox.isSelected());
                } catch (ClientException e) {
                    parent.alertThrowable(PlatformUI.MIRTH_FRAME, e);
                }
                
                return null;
            }
            
            public void done() {
                parent.doRefreshStatuses(true);
                
                if (parent.currentContentPage == parent.messageBrowser) {
                    parent.messageBrowser.refresh(1, true);
                }
                
                parent.stopWorking(workingId);
            }
        };
        
        worker.execute();
        setVisible(false);
    }//GEN-LAST:event_yesButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private com.mirth.connect.client.ui.components.MirthCheckBox clearStatsCheckBox;
    private com.mirth.connect.client.ui.components.MirthCheckBox includeRunningChannels;
    private javax.swing.JLabel messageLabel;
    private com.mirth.connect.client.ui.components.MirthButton noButton;
    private com.mirth.connect.client.ui.components.MirthButton yesButton;
    // End of variables declaration//GEN-END:variables
    // @formatter:on
}
