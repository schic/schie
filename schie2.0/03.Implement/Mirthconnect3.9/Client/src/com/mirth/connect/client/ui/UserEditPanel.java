/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;

import com.mirth.connect.client.ui.components.MirthFieldConstraints;
import com.mirth.connect.model.User;

public class UserEditPanel extends javax.swing.JPanel {

    private User user;
    private UserDialogInterface dialog;
    private Frame parent;
    private final String DEFAULT_OPTION = Messages.getString("UserEditPanel.0"); //$NON-NLS-1$

    public UserEditPanel() {
        this.parent = PlatformUI.MIRTH_FRAME;
        initComponents();

        username.setDocument(new MirthFieldConstraints(40, false, false, false));
        password.setDocument(new MirthFieldConstraints(40, false, false, false));
        confirmPassword.setDocument(new MirthFieldConstraints(40, false, false, false));
        firstName.setDocument(new MirthFieldConstraints(40, false, false, false));
        lastName.setDocument(new MirthFieldConstraints(40, false, false, false));
        organization.setDocument(new MirthFieldConstraints(255, false, false, false));
        email.setDocument(new MirthFieldConstraints(255, false, false, false));
        phone.setDocument(new MirthFieldConstraints(40, false, false, false));
        description.setDocument(new MirthFieldConstraints(255, false, false, false));

        industry.getModel().setSelectedItem(DEFAULT_OPTION);

        List<String> industries = new ArrayList<String>();
        industries.add(Messages.getString("UserEditPanel.1")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.2")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.3")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.4")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.5")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.6")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.7")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.8")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.9")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.10")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.11")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.12")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.13")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.14")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.15")); //$NON-NLS-1$
        industries.add(Messages.getString("UserEditPanel.16")); //$NON-NLS-1$

        for (String item : industries) {
            industry.addItem(item);
        }

        // Disable scroll bar
        industry.setMaximumRowCount(industry.getModel().getSize());
    }

    public void setUser(UserDialogInterface dialog, User user) {
        this.dialog = dialog;
        this.user = user;

        username.setText(user.getUsername());
        password.setText(Messages.getString("UserEditPanel.17")); //$NON-NLS-1$
        confirmPassword.setText(Messages.getString("UserEditPanel.18")); //$NON-NLS-1$
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        organization.setText(user.getOrganization());
        email.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());
        description.setText(user.getDescription());
        if (!StringUtils.isBlank(user.getIndustry())) {
            industry.setSelectedItem(user.getIndustry());
        }
    }

    public User getUser() {

        user.setUsername(username.getText());
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setOrganization(organization.getText());
        user.setEmail(email.getText());
        user.setPhoneNumber(phone.getText());
        user.setDescription(description.getText());
        if (!industry.getSelectedItem().equals(DEFAULT_OPTION)) {
            user.setIndustry((String) industry.getSelectedItem());
        }

        return user;
    }

    public String getPassword() {
        return String.valueOf(password.getPassword());
    }

    /**
     * This method checks if the finish button can now be enabled
     */
    public boolean checkIfAbleToFinish() {
        boolean finishEnabled = true;
        // Any of the following clauses cause the finish button to be disabled
        if ((StringUtils.isBlank(username.getText())) || (firstNameAsteriskLabel.isVisible() && StringUtils.isBlank(firstName.getText())) || (lastNameAsteriskLabel.isVisible() && StringUtils.isBlank(lastName.getText())) || (emailAsteriskLabel.isVisible() && StringUtils.isBlank(email.getText())) || (industryAsteriskLabel.isVisible() && industry.getSelectedItem().equals(DEFAULT_OPTION)) || (organizationAsteriskLabel.isVisible() && StringUtils.isBlank(organization.getText())) || (passwordAsteriskLabel.isVisible() && (StringUtils.isBlank(String.valueOf(password.getPassword())) || StringUtils.isBlank(String.valueOf(confirmPassword.getPassword()))))) {
            finishEnabled = false;
        }

        dialog.setFinishButtonEnabled(finishEnabled);
        return finishEnabled;
    }

    public void setRequiredFields(boolean firstName, boolean lastName, boolean email, boolean organization, boolean password, boolean industry) {
        firstNameAsteriskLabel.setVisible(firstName);
        lastNameAsteriskLabel.setVisible(lastName);
        emailAsteriskLabel.setVisible(email);
        organizationAsteriskLabel.setVisible(organization);
        passwordAsteriskLabel.setVisible(password);
        confirmPasswordAsteriskLabel.setVisible(password);
        industryAsteriskLabel.setVisible(industry);

        checkIfAbleToFinish();
    }

    public String validateUser() {
        if (!checkIfAbleToFinish()) {
            return Messages.getString("UserEditPanel.19"); //$NON-NLS-1$
        }

        // If it's a new user or the username was changed, make sure the username isn't already used.
        if (user.getId() == null || !user.getUsername().equals(username.getText())) {
            for (int i = 0; i < parent.users.size(); i++) {
                if (parent.users.get(i).getUsername().equals(username.getText())) {
                    return Messages.getString("UserEditPanel.20"); //$NON-NLS-1$
                }
            }
        }

        if (!String.valueOf(password.getPassword()).equals(String.valueOf(confirmPassword.getPassword()))) {
            return Messages.getString("UserEditPanel.21"); //$NON-NLS-1$
        }

        try {
            String emailAddress = email.getText();
            if (StringUtils.isNotBlank(emailAddress)) {
                new InternetAddress(emailAddress).validate();
            }
        } catch (Exception e) {
            return Messages.getString("UserEditPanel.22") + e.getMessage(); //$NON-NLS-1$
        }

        return null;
    }

    private void checkAndTriggerFinishButton(java.awt.event.KeyEvent evt) {
        if (checkIfAbleToFinish() && (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)) {
            dialog.triggerFinishButton();
        }
    }

    // @formatter:off
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        firstName = new com.mirth.connect.client.ui.components.MirthTextField();
        firstNameLabel = new javax.swing.JLabel();
        lastNameLabel = new javax.swing.JLabel();
        lastName = new com.mirth.connect.client.ui.components.MirthTextField();
        organization = new com.mirth.connect.client.ui.components.MirthTextField();
        organizationLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        email = new com.mirth.connect.client.ui.components.MirthTextField();
        phone = new com.mirth.connect.client.ui.components.MirthTextField();
        phoneLabel = new javax.swing.JLabel();
        usernameAsteriskLabel = new javax.swing.JLabel();
        passwordAsteriskLabel = new javax.swing.JLabel();
        organizationAsteriskLabel = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        usernameLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();
        descriptionLabel = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        passwordLabel = new javax.swing.JLabel();
        confirmPasswordLabel = new javax.swing.JLabel();
        confirmPassword = new javax.swing.JPasswordField();
        confirmPasswordAsteriskLabel = new javax.swing.JLabel();
        emailAsteriskLabel = new javax.swing.JLabel();
        firstNameAsteriskLabel = new javax.swing.JLabel();
        lastNameAsteriskLabel = new javax.swing.JLabel();
        industry = new javax.swing.JComboBox();
        industryLabel = new javax.swing.JLabel();
        industryAsteriskLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        firstName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                firstNameKeyReleased(evt);
            }
        });

        firstNameLabel.setText(Messages.getString("UserEditPanel.23")); //$NON-NLS-1$

        lastNameLabel.setText(Messages.getString("UserEditPanel.24")); //$NON-NLS-1$

        lastName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lastNameKeyReleased(evt);
            }
        });

        organization.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                organizationKeyReleased(evt);
            }
        });

        organizationLabel.setText(Messages.getString("UserEditPanel.25")); //$NON-NLS-1$

        emailLabel.setText(Messages.getString("UserEditPanel.26")); //$NON-NLS-1$

        email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                emailKeyReleased(evt);
            }
        });

        phone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                phoneKeyReleased(evt);
            }
        });

        phoneLabel.setText(Messages.getString("UserEditPanel.27")); //$NON-NLS-1$

        usernameAsteriskLabel.setForeground(new java.awt.Color(255, 0, 0));
        usernameAsteriskLabel.setText(Messages.getString("UserEditPanel.28")); //$NON-NLS-1$

        passwordAsteriskLabel.setForeground(new java.awt.Color(255, 0, 0));
        passwordAsteriskLabel.setText(Messages.getString("UserEditPanel.29")); //$NON-NLS-1$

        organizationAsteriskLabel.setForeground(new java.awt.Color(255, 0, 0));
        organizationAsteriskLabel.setText(Messages.getString("UserEditPanel.30")); //$NON-NLS-1$

        username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                usernameKeyReleased(evt);
            }
        });

        usernameLabel.setText(Messages.getString("UserEditPanel.31")); //$NON-NLS-1$

        description.setColumns(20);
        description.setFont(new java.awt.Font(Messages.getString("UserEditPanel.32"), 0, 11)); // NOI18N //$NON-NLS-1$
        description.setLineWrap(true);
        description.setRows(4);
        description.setWrapStyleWord(true);
        description.setAutoscrolls(false);
        jScrollPane1.setViewportView(description);

        descriptionLabel.setText(Messages.getString("UserEditPanel.33")); //$NON-NLS-1$

        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passwordKeyReleased(evt);
            }
        });

        passwordLabel.setText(Messages.getString("UserEditPanel.34")); //$NON-NLS-1$

        confirmPasswordLabel.setText(Messages.getString("UserEditPanel.35")); //$NON-NLS-1$

        confirmPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                confirmPasswordKeyReleased(evt);
            }
        });

        confirmPasswordAsteriskLabel.setForeground(new java.awt.Color(255, 0, 0));
        confirmPasswordAsteriskLabel.setText(Messages.getString("UserEditPanel.36")); //$NON-NLS-1$

        emailAsteriskLabel.setForeground(new java.awt.Color(255, 0, 0));
        emailAsteriskLabel.setText(Messages.getString("UserEditPanel.37")); //$NON-NLS-1$

        firstNameAsteriskLabel.setForeground(new java.awt.Color(255, 0, 0));
        firstNameAsteriskLabel.setText(Messages.getString("UserEditPanel.38")); //$NON-NLS-1$

        lastNameAsteriskLabel.setForeground(new java.awt.Color(255, 0, 0));
        lastNameAsteriskLabel.setText(Messages.getString("UserEditPanel.39")); //$NON-NLS-1$

        industry.setToolTipText(Messages.getString("UserEditPanel.40")); //$NON-NLS-1$
        industry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                industryActionPerformed(evt);
            }
        });

        industryLabel.setText(Messages.getString("UserEditPanel.41")); //$NON-NLS-1$

        industryAsteriskLabel.setForeground(new java.awt.Color(255, 0, 0));
        industryAsteriskLabel.setText(Messages.getString("UserEditPanel.42")); //$NON-NLS-1$

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(usernameLabel)
                    .addComponent(passwordLabel)
                    .addComponent(confirmPasswordLabel)
                    .addComponent(firstNameLabel)
                    .addComponent(lastNameLabel)
                    .addComponent(organizationLabel)
                    .addComponent(descriptionLabel)
                    .addComponent(phoneLabel)
                    .addComponent(industryLabel)
                    .addComponent(emailLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(confirmPassword)
                    .addComponent(username)
                    .addComponent(password)
                    .addComponent(lastName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(organization, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(phone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(firstName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(industry, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lastNameAsteriskLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(organizationAsteriskLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordAsteriskLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usernameAsteriskLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(confirmPasswordAsteriskLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(firstNameAsteriskLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(industryAsteriskLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(emailAsteriskLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameAsteriskLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordAsteriskLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmPasswordLabel)
                    .addComponent(confirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmPasswordAsteriskLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNameLabel)
                    .addComponent(firstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstNameAsteriskLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastNameLabel)
                    .addComponent(lastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastNameAsteriskLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailLabel)
                    .addComponent(emailAsteriskLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(organizationLabel)
                    .addComponent(organization, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(organizationAsteriskLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(industry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(industryLabel)
                    .addComponent(industryAsteriskLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(descriptionLabel)
                        .addGap(0, 65, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addGap(4, 4, 4))
        );
    }// </editor-fold>//GEN-END:initComponents
    // @formatter:on

    private void usernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernameKeyReleased
        checkAndTriggerFinishButton(evt);
    }//GEN-LAST:event_usernameKeyReleased

    private void passwordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyReleased
        checkAndTriggerFinishButton(evt);
    }//GEN-LAST:event_passwordKeyReleased

    private void confirmPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmPasswordKeyReleased
        checkAndTriggerFinishButton(evt);
    }//GEN-LAST:event_confirmPasswordKeyReleased

    private void firstNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_firstNameKeyReleased
        checkAndTriggerFinishButton(evt);
    }//GEN-LAST:event_firstNameKeyReleased

    private void lastNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lastNameKeyReleased
        checkAndTriggerFinishButton(evt);
    }//GEN-LAST:event_lastNameKeyReleased

    private void organizationKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_organizationKeyReleased
        checkAndTriggerFinishButton(evt);
    }//GEN-LAST:event_organizationKeyReleased

    private void emailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailKeyReleased
        checkAndTriggerFinishButton(evt);
    }//GEN-LAST:event_emailKeyReleased

    private void phoneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneKeyReleased
        checkAndTriggerFinishButton(evt);
    }//GEN-LAST:event_phoneKeyReleased

    private void industryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_industryActionPerformed
        if (dialog != null) {
            checkIfAbleToFinish();
        }
    }//GEN-LAST:event_industryActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField confirmPassword;
    private javax.swing.JLabel confirmPasswordAsteriskLabel;
    private javax.swing.JLabel confirmPasswordLabel;
    private javax.swing.JTextArea description;
    private javax.swing.JLabel descriptionLabel;
    private com.mirth.connect.client.ui.components.MirthTextField email;
    private javax.swing.JLabel emailAsteriskLabel;
    private javax.swing.JLabel emailLabel;
    private com.mirth.connect.client.ui.components.MirthTextField firstName;
    private javax.swing.JLabel firstNameAsteriskLabel;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JComboBox industry;
    private javax.swing.JLabel industryAsteriskLabel;
    private javax.swing.JLabel industryLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private com.mirth.connect.client.ui.components.MirthTextField lastName;
    private javax.swing.JLabel lastNameAsteriskLabel;
    private javax.swing.JLabel lastNameLabel;
    private com.mirth.connect.client.ui.components.MirthTextField organization;
    private javax.swing.JLabel organizationAsteriskLabel;
    private javax.swing.JLabel organizationLabel;
    private javax.swing.JPasswordField password;
    private javax.swing.JLabel passwordAsteriskLabel;
    private javax.swing.JLabel passwordLabel;
    private com.mirth.connect.client.ui.components.MirthTextField phone;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JTextField username;
    private javax.swing.JLabel usernameAsteriskLabel;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
