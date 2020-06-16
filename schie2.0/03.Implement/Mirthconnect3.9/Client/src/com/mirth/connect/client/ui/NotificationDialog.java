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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.core.ConnectServiceUtil;
import com.mirth.connect.client.ui.util.DisplayUtil;
import com.mirth.connect.model.converters.ObjectXMLSerializer;
import com.mirth.connect.model.notification.Notification;

public class NotificationDialog extends MirthDialog {

    private NotificationModel notificationModel = new NotificationModel();
    private Notification currentNotification;
    private String checkForNotifications = null;
    private String showNotificationPopup = null;
    private boolean checkForNotificationsSetting = false;
    private int unarchivedCount = 0;
    private Set<Integer> archivedNotifications = new HashSet<Integer>();
    Properties userPreferences = new Properties();
    private Color borderColor = new Color(110, 110, 110);

    public NotificationDialog() {
        super(PlatformUI.MIRTH_FRAME, true);
        parent = PlatformUI.MIRTH_FRAME;

        DisplayUtil.setResizable(this, false);
        setTitle(Messages.getString("NotificationDialog.0")); //$NON-NLS-1$
        setPreferredSize(new Dimension(750, 625));
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
        Dimension dlgSize = getPreferredSize();
        Dimension frmSize = parent.getSize();
        Point loc = parent.getLocation();

        if ((frmSize.width == 0 && frmSize.height == 0) || (loc.x == 0 && loc.y == 0)) {
            setLocationRelativeTo(null);
        } else {
            setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        }
        loadNotifications();
        initLayout();
        pack();
        setVisible(true);
    }

    private void loadNotifications() {
        // Get user preferences
        Set<String> preferenceNames = new HashSet<String>();
        preferenceNames.add(Messages.getString("NotificationDialog.1")); //$NON-NLS-1$
        preferenceNames.add(Messages.getString("NotificationDialog.2")); //$NON-NLS-1$
        preferenceNames.add(Messages.getString("NotificationDialog.3")); //$NON-NLS-1$
        preferenceNames.add(Messages.getString("NotificationDialog.4")); //$NON-NLS-1$
        try {
            userPreferences = parent.mirthClient.getUserPreferences(parent.getCurrentUser(parent).getId(), preferenceNames);
        } catch (ClientException e) {
        }
        String archivedNotificationString = userPreferences.getProperty(Messages.getString("NotificationDialog.5")); //$NON-NLS-1$
        if (archivedNotificationString != null) {
            archivedNotifications = ObjectXMLSerializer.getInstance().deserialize(archivedNotificationString, Set.class);
        }
        showNotificationPopup = userPreferences.getProperty(Messages.getString("NotificationDialog.6")); //$NON-NLS-1$
        checkForNotifications = userPreferences.getProperty(Messages.getString("NotificationDialog.7")); //$NON-NLS-1$

        // Build UI
        initComponents();

        // Pull notifications
        final String workingId = parent.startWorking(Messages.getString("NotificationDialog.8")); //$NON-NLS-1$

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            List<Notification> notifications = new ArrayList<Notification>();

            public Void doInBackground() {
                try {
                    notifications = ConnectServiceUtil.getNotifications(PlatformUI.SERVER_ID, PlatformUI.SERVER_VERSION, LoadedExtensions.getInstance().getExtensionVersions(), PlatformUI.HTTPS_PROTOCOLS, PlatformUI.HTTPS_CIPHER_SUITES);
                } catch (Exception e) {
                    PlatformUI.MIRTH_FRAME.alertError(PlatformUI.MIRTH_FRAME, Messages.getString("NotificationDialog.9")); //$NON-NLS-1$
                }
                return null;
            }

            public void done() {
                notificationModel.setData(notifications);

                for (Notification notification : notifications) {
                    if (archivedNotifications.contains(notification.getId())) {
                        notificationModel.setArchived(true, notifications.indexOf(notification));
                    } else {
                        unarchivedCount++;
                    }
                }
                updateUnarchivedCountLabel();
                list.setModel(notificationModel);
                list.setSelectedIndex(0);
                parent.stopWorking(workingId);
            }
        };

        worker.execute();
    }

    @Override
    public void onCloseAction() {
        doSave();
    }

    private void initComponents() {
        setLayout(new MigLayout(Messages.getString("NotificationDialog.10"), Messages.getString("NotificationDialog.11"), Messages.getString("NotificationDialog.12"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        notificationPanel = new JPanel();
        notificationPanel.setLayout(new MigLayout(Messages.getString("NotificationDialog.13"), Messages.getString("NotificationDialog.14"), Messages.getString("NotificationDialog.15"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        notificationPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        archiveAll = new JLabel(Messages.getString("NotificationDialog.16")); //$NON-NLS-1$
        archiveAll.setForeground(java.awt.Color.blue);
        archiveAll.setText(Messages.getString("NotificationDialog.17")); //$NON-NLS-1$
        archiveAll.setToolTipText(Messages.getString("NotificationDialog.18")); //$NON-NLS-1$
        archiveAll.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        newNotificationsLabel = new JLabel();
        newNotificationsLabel.setFont(newNotificationsLabel.getFont().deriveFont(Font.BOLD));
        headerListPanel = new JPanel();
        headerListPanel.setBackground(UIConstants.HIGHLIGHTER_COLOR);
        headerListPanel.setLayout(new MigLayout(Messages.getString("NotificationDialog.19"))); //$NON-NLS-1$
        headerListPanel.setBorder(BorderFactory.createLineBorder(borderColor));

        list = new JList();
        list.setCellRenderer(new NotificationListCellRenderer());
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    currentNotification = (Notification) list.getSelectedValue();
                    if (currentNotification != null) {
                        notificationNameTextField.setText(currentNotification.getName());
                        contentTextPane.setText(currentNotification.getContent());
                        archiveSelected();
                    }
                }
            }
        });
        listScrollPane = new JScrollPane();
        listScrollPane.setBackground(UIConstants.BACKGROUND_COLOR);
        listScrollPane.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, borderColor));
        listScrollPane.setViewportView(list);
        listScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        archiveLabel = new JLabel();
        archiveLabel.setForeground(java.awt.Color.blue);
        archiveLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        notificationNameTextField = new JTextField();
        notificationNameTextField.setFont(notificationNameTextField.getFont().deriveFont(Font.BOLD));
        notificationNameTextField.setEditable(false);
        notificationNameTextField.setFocusable(false);
        notificationNameTextField.setBorder(BorderFactory.createEmptyBorder());
        notificationNameTextField.setBackground(UIConstants.HIGHLIGHTER_COLOR);
        DefaultCaret nameCaret = (DefaultCaret) notificationNameTextField.getCaret();
        nameCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        headerContentPanel = new JPanel();
        headerContentPanel.setLayout(new MigLayout(Messages.getString("NotificationDialog.20"))); //$NON-NLS-1$
        headerContentPanel.setBorder(BorderFactory.createLineBorder(borderColor));
        headerContentPanel.setBackground(UIConstants.HIGHLIGHTER_COLOR);

        contentTextPane = new JTextPane();
        contentTextPane.setContentType(Messages.getString("NotificationDialog.21")); //$NON-NLS-1$
        contentTextPane.setEditable(false);
        contentTextPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                if (evt.getEventType() == EventType.ACTIVATED && Desktop.isDesktopSupported()) {
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(evt.getURL().toURI());
                        } else {
                            BareBonesBrowserLaunch.openURL(evt.getURL().toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        DefaultCaret contentCaret = (DefaultCaret) contentTextPane.getCaret();
        contentCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        contentScrollPane = new JScrollPane();
        contentScrollPane.setViewportView(contentTextPane);
        contentScrollPane.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, borderColor));

        archiveLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int index = list.getSelectedIndex();
                if (currentNotification.isArchived()) {
                    notificationModel.setArchived(false, index);
                    unarchivedCount++;
                } else {
                    notificationModel.setArchived(true, index);
                    unarchivedCount--;
                }
                archiveSelected();
                updateUnarchivedCountLabel();
            }
        });

        archiveAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                for (int i = 0; i < notificationModel.getSize(); i++) {
                    notificationModel.setArchived(true, i);
                }
                unarchivedCount = 0;
                archiveSelected();
                updateUnarchivedCountLabel();
            }
        });

        notificationCheckBox = new JCheckBox(Messages.getString("NotificationDialog.22")); //$NON-NLS-1$
        notificationCheckBox.setBackground(UIConstants.BACKGROUND_COLOR);

        if (checkForNotifications == null || BooleanUtils.toBoolean(checkForNotifications)) {
            checkForNotificationsSetting = true;
            if (showNotificationPopup == null || BooleanUtils.toBoolean(showNotificationPopup)) {
                notificationCheckBox.setSelected(true);
            } else {
                notificationCheckBox.setSelected(false);
            }
        } else {
            notificationCheckBox.setSelected(false);
        }

        notificationCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (notificationCheckBox.isSelected() && !checkForNotificationsSetting) {
                    alertSettingsChange();
                }
            }
        });

        closeButton = new JButton(Messages.getString("NotificationDialog.23")); //$NON-NLS-1$
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSave();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doSave();
            }
        });
    }

    private void initLayout() {
        headerListPanel.add(newNotificationsLabel, Messages.getString("NotificationDialog.24")); //$NON-NLS-1$
        headerListPanel.add(archiveAll, Messages.getString("NotificationDialog.25")); //$NON-NLS-1$

        headerContentPanel.add(notificationNameTextField, Messages.getString("NotificationDialog.26")); //$NON-NLS-1$
        headerContentPanel.add(archiveLabel, Messages.getString("NotificationDialog.27")); //$NON-NLS-1$

        notificationPanel.add(headerListPanel, Messages.getString("NotificationDialog.28")); //$NON-NLS-1$
        notificationPanel.add(headerContentPanel, Messages.getString("NotificationDialog.29")); //$NON-NLS-1$
        notificationPanel.add(listScrollPane, Messages.getString("NotificationDialog.30")); //$NON-NLS-1$
        notificationPanel.add(contentScrollPane, Messages.getString("NotificationDialog.31")); //$NON-NLS-1$

        add(notificationPanel, Messages.getString("NotificationDialog.32")); //$NON-NLS-1$
        add(new JSeparator(), Messages.getString("NotificationDialog.33")); //$NON-NLS-1$
        add(notificationCheckBox, Messages.getString("NotificationDialog.34")); //$NON-NLS-1$
        add(closeButton, Messages.getString("NotificationDialog.35")); //$NON-NLS-1$
    }

    private void updateUnarchivedCountLabel() {
        if (unarchivedCount == 0) {
            newNotificationsLabel.setText(Messages.getString("NotificationDialog.36")); //$NON-NLS-1$
        } else {
            newNotificationsLabel.setText(unarchivedCount + Messages.getString("NotificationDialog.37")); //$NON-NLS-1$
        }
    }

    private void archiveSelected() {
        if (currentNotification != null) {
            if (currentNotification.isArchived()) {
                archiveLabel.setText(Messages.getString("NotificationDialog.38")); //$NON-NLS-1$
                archiveLabel.setToolTipText(Messages.getString("NotificationDialog.39")); //$NON-NLS-1$
            } else {
                archiveLabel.setText(Messages.getString("NotificationDialog.40")); //$NON-NLS-1$
                archiveLabel.setToolTipText(Messages.getString("NotificationDialog.41")); //$NON-NLS-1$
            }
        }
    }

    private void alertSettingsChange() {
        boolean option = parent.alertOption(this, Messages.getString("NotificationDialog.42")); //$NON-NLS-1$
        if (option) {
            checkForNotificationsSetting = true;
        } else {
            notificationCheckBox.setSelected(false);
        }
    }

    private void doSave() {
        final Properties personPreferences = new Properties();

        if (!StringUtils.equals(checkForNotifications, Boolean.toString(checkForNotificationsSetting))) {
            personPreferences.put(Messages.getString("NotificationDialog.43"), Boolean.toString(checkForNotificationsSetting)); //$NON-NLS-1$
        }
        if (!StringUtils.equals(showNotificationPopup, Boolean.toString(notificationCheckBox.isSelected()))) {
            personPreferences.put(Messages.getString("NotificationDialog.44"), Boolean.toString(notificationCheckBox.isSelected())); //$NON-NLS-1$
        }

        Set<Integer> currentArchivedNotifications = notificationModel.getArchivedNotifications();
        if (!archivedNotifications.equals(currentArchivedNotifications)) {
            personPreferences.put(Messages.getString("NotificationDialog.45"), ObjectXMLSerializer.getInstance().serialize(currentArchivedNotifications)); //$NON-NLS-1$
        }

        if (!personPreferences.isEmpty()) {
            final String workingId = parent.startWorking(Messages.getString("NotificationDialog.46")); //$NON-NLS-1$

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                public Void doInBackground() {
                    try {
                        parent.mirthClient.setUserPreferences(parent.getCurrentUser(parent).getId(), personPreferences);
                    } catch (ClientException e) {
                        parent.alertThrowable(parent, e);
                    }
                    return null;
                }

                @Override
                public void done() {
                    parent.stopWorking(workingId);
                }
            };

            worker.execute();
        }

        parent.updateNotificationTaskName(unarchivedCount);

        this.dispose();
    }

    private class NotificationListCellRenderer extends DefaultListCellRenderer {
        private JPanel panel;
        private JLabel nameLabel;
        private JLabel dateLabel;

        public NotificationListCellRenderer() {
            nameLabel = new JLabel();

            dateLabel = new JLabel();
            dateLabel.setFont(list.getFont().deriveFont(10f));
            dateLabel.setForeground(Color.GRAY);

            panel = new JPanel();
            panel.setLayout(new MigLayout(Messages.getString("NotificationDialog.47"))); //$NON-NLS-1$
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.HIGHLIGHTER_COLOR));
            panel.add(nameLabel);
            panel.add(dateLabel);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Notification notification = ((Notification) value);

            nameLabel.setText(notification.getName());
            dateLabel.setText(notification.getDate());

            if (notification.isArchived()) {
                nameLabel.setFont(list.getFont().deriveFont(Font.PLAIN));
            } else {
                nameLabel.setFont(list.getFont().deriveFont(Font.BOLD));
            }

            if (isSelected) {
                panel.setBackground(list.getSelectionBackground());
                panel.setForeground(list.getSelectionForeground());
            } else {
                panel.setBackground(list.getBackground());
                panel.setForeground(list.getForeground());
            }
            return panel;
        }
    }

    private class NotificationModel extends AbstractListModel {
        private List<Notification> notifications = new ArrayList<Notification>();

        @Override
        public Notification getElementAt(int index) {
            return notifications.get(index);
        }

        public void setArchived(boolean archived, int index) {
            getElementAt(index).setArchived(archived);
            fireContentsChanged(this, index, index);
        }

        public Set<Integer> getArchivedNotifications() {
            Set<Integer> archivedNotifications = new HashSet<Integer>();
            for (Notification notification : notifications) {
                if (notification.isArchived()) {
                    archivedNotifications.add(notification.getId());
                }
            }
            return archivedNotifications;
        }

        @Override
        public int getSize() {
            return notifications.size();
        }

        public void setData(List<Notification> notifications) {
            int size = getSize();
            this.notifications.clear();
            fireIntervalRemoved(this, 0, size - 1);

            this.notifications.addAll(notifications);
            fireIntervalAdded(this, 0, getSize() - 1);
        }
    }

    private Frame parent;
    private JPanel notificationPanel;

    // List header panel
    private JPanel headerListPanel;
    private JLabel newNotificationsLabel;
    private JLabel archiveAll;

    // List panel
    private JList list;
    private JScrollPane listScrollPane;

    // Content header panel
    private JPanel headerContentPanel;
    private JTextField notificationNameTextField;
    private JLabel archiveLabel;

    // Content panel
    private JTextPane contentTextPane;
    private JScrollPane contentScrollPane;

    // Dialog footer
    private JCheckBox notificationCheckBox;
    private JButton closeButton;
}
