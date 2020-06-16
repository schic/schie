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
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.JTextComponent.KeyBinding;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.mirth.connect.client.core.Client;
import com.mirth.connect.client.core.ClientException;

/**
 * The main mirth class. Sets up the login and then authenticates the login information and sets up
 * Frame (the main application window).
 */
public class Mirth {

    private static Preferences userPreferences;

    /**
     * Construct and show the application.
     */
    public Mirth(Client mirthClient) throws ClientException {
        PlatformUI.MIRTH_FRAME = new Frame();

        UIManager.put(Messages.getString("Mirth.0"), UIConstants.LEAF_ICON); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.1"), UIConstants.OPEN_ICON); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.2"), UIConstants.CLOSED_ICON); //$NON-NLS-1$

        userPreferences = Preferences.userNodeForPackage(Mirth.class);
        LoginPanel.getInstance().setStatus(Messages.getString("Mirth.3")); //$NON-NLS-1$
        PlatformUI.MIRTH_FRAME.setupFrame(mirthClient);

        boolean maximized;
        int width;
        int height;

        if (SystemUtils.IS_OS_MAC) {
            /*
             * The window is only maximized when there is no width or height preference saved.
             * Previously, we just set the dimensions on mac and didn't bother with the maximized
             * state because the user could maximize the window then manually resize it, leaving the
             * maximum state as true. As of MIRTH-3691, this no longer happens.
             */
            maximized = (userPreferences.get(Messages.getString("Mirth.4"), null) == null || userPreferences.get(Messages.getString("Mirth.5"), null) == null) || (userPreferences.getInt(Messages.getString("Mirth.6"), Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            width = userPreferences.getInt(Messages.getString("Mirth.7"), UIConstants.MIRTH_WIDTH); //$NON-NLS-1$
            height = userPreferences.getInt(Messages.getString("Mirth.8"), UIConstants.MIRTH_WIDTH); //$NON-NLS-1$
        } else {
            /*
             * Maximize it if it's supposed to be maximized or if there is no maximized preference
             * saved. Unmaximizing will bring the window back to default size.
             */
            maximized = (userPreferences.getInt(Messages.getString("Mirth.9"), Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH); //$NON-NLS-1$

            if (maximized) {
                // If it is maximized, use the default width and height for unmaximizing
                width = UIConstants.MIRTH_WIDTH;
                height = UIConstants.MIRTH_HEIGHT;
            } else {
                // If it's not maximized, get the saved width and height
                width = userPreferences.getInt(Messages.getString("Mirth.10"), UIConstants.MIRTH_WIDTH); //$NON-NLS-1$
                height = userPreferences.getInt(Messages.getString("Mirth.11"), UIConstants.MIRTH_HEIGHT); //$NON-NLS-1$
            }
        }

        // Now set the width and height (saved or default)
        PlatformUI.MIRTH_FRAME.setSize(width, height);
        PlatformUI.MIRTH_FRAME.setLocationRelativeTo(null);

        if (maximized) {
            PlatformUI.MIRTH_FRAME.setExtendedState(Frame.MAXIMIZED_BOTH);
        }

        PlatformUI.MIRTH_FRAME.setVisible(true);
    }

    /**
     * About menu item on Mac OS X
     */
    public static void aboutMac() {
        new AboutMirth();
    }

    /**
     * Quit menu item on Mac OS X. Only exit if on the login window, or if logout is successful
     * 
     * @return quit
     */
    public static boolean quitMac() {
        return (LoginPanel.getInstance().isVisible() || (PlatformUI.MIRTH_FRAME != null && PlatformUI.MIRTH_FRAME.logout(true)));
    }

    /**
     * Create the alternate key bindings for the menu shortcut key mask. This is called if the menu
     * shortcut key mask is not the CTRL key (i.e. COMMAND on OSX)
     */
    private static void createAlternateKeyBindings() {
        int acceleratorKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        // Add the common KeyBindings for macs
        KeyBinding[] defaultBindings = {
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_C, acceleratorKey), DefaultEditorKit.copyAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_V, acceleratorKey), DefaultEditorKit.pasteAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_X, acceleratorKey), DefaultEditorKit.cutAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_A, acceleratorKey), DefaultEditorKit.selectAllAction),
                // deleteNextWordAction and deletePrevWordAction were not available in Java 1.5
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, acceleratorKey), DefaultEditorKit.deleteNextWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, acceleratorKey), DefaultEditorKit.deletePrevWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, acceleratorKey), DefaultEditorKit.nextWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, acceleratorKey), DefaultEditorKit.nextWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, acceleratorKey), DefaultEditorKit.previousWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, acceleratorKey), DefaultEditorKit.previousWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, acceleratorKey | InputEvent.SHIFT_MASK), DefaultEditorKit.selectionNextWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, acceleratorKey | InputEvent.SHIFT_MASK), DefaultEditorKit.selectionNextWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, acceleratorKey | InputEvent.SHIFT_MASK), DefaultEditorKit.selectionPreviousWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, acceleratorKey | InputEvent.SHIFT_MASK), DefaultEditorKit.selectionPreviousWordAction) };

        keyMapBindings(new javax.swing.JTextField(), defaultBindings);
        keyMapBindings(new javax.swing.JPasswordField(), defaultBindings);
        keyMapBindings(new javax.swing.JTextPane(), defaultBindings);
        keyMapBindings(new javax.swing.JTextArea(), defaultBindings);
        keyMapBindings(new com.mirth.connect.client.ui.components.MirthTextField(), defaultBindings);
        keyMapBindings(new com.mirth.connect.client.ui.components.MirthPasswordField(), defaultBindings);
        keyMapBindings(new com.mirth.connect.client.ui.components.MirthTextPane(), defaultBindings);
        keyMapBindings(new com.mirth.connect.client.ui.components.MirthTextArea(), defaultBindings);
    }

    private static void keyMapBindings(JTextComponent comp, KeyBinding[] bindings) {
        JTextComponent.loadKeymap(comp.getKeymap(), bindings, comp.getActions());
    }

    public static void initUIManager() {
        try {
            PlasticLookAndFeel.setPlasticTheme(new MirthTheme());
            PlasticXPLookAndFeel look = new PlasticXPLookAndFeel();
            UIManager.setLookAndFeel(look);
            UIManager.put(Messages.getString("Mirth.12"), Messages.getString("Mirth.13")); //$NON-NLS-1$ //$NON-NLS-2$
            LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class);

            /*
             * MIRTH-1225 and MIRTH-2019: Create alternate key bindings if CTRL is not the same as
             * the menu shortcut key (i.e. COMMAND on OSX)
             */
            if (InputEvent.CTRL_MASK != Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) {
                createAlternateKeyBindings();
            }

            if (SystemUtils.IS_OS_MAC) {
                OSXAdapter.setAboutHandler(Mirth.class, Mirth.class.getDeclaredMethod(Messages.getString("Mirth.14"), (Class[]) null)); //$NON-NLS-1$
                OSXAdapter.setQuitHandler(Mirth.class, Mirth.class.getDeclaredMethod(Messages.getString("Mirth.15"), (Class[]) null)); //$NON-NLS-1$
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // keep the tooltips from disappearing
        ToolTipManager.sharedInstance().setDismissDelay(3600000);

        // TabbedPane defaults
        // UIManager.put("TabbedPane.selected", new Color(0xffffff));
        // UIManager.put("TabbedPane.background",new Color(225,225,225));
        // UIManager.put("TabbedPane.tabAreaBackground",new Color(225,225,225));
        UIManager.put(Messages.getString("Mirth.16"), new Color(225, 225, 225)); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.17"), new Color(0xc3c3c3)); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.18"), new InsetsUIResource(0, 0, 0, 0)); //$NON-NLS-1$

        // TaskPane defaults
        UIManager.put(Messages.getString("Mirth.19"), new Color(0xffffff)); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.20"), new Color(0xffffff)); //$NON-NLS-1$

        // Set fonts
        UIManager.put(Messages.getString("Mirth.21"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.22"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.23"), UIConstants.DIALOG_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.24"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.25"), UIConstants.DIALOG_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.26"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.27"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.28"), UIConstants.DIALOG_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.29"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.30"), UIConstants.DIALOG_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.31"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.32"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.33"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.34"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.35"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.36"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.37"), UIConstants.TEXTFIELD_BOLD_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.38"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.39"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.40"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.41"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.42"), UIConstants.DIALOG_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.43"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.44"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.45"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.46"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.47"), UIConstants.TEXTFIELD_BOLD_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.48"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.49"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.50"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.51"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.52"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.53"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.54"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.55"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.56"), UIConstants.BANNER_FONT); //$NON-NLS-1$
        UIManager.put(Messages.getString("Mirth.57"), UIConstants.TEXTFIELD_PLAIN_FONT); //$NON-NLS-1$

        InputMap im = (InputMap) UIManager.get(Messages.getString("Mirth.58")); //$NON-NLS-1$
        im.put(KeyStroke.getKeyStroke(Messages.getString("Mirth.59")), Messages.getString("Mirth.60")); //$NON-NLS-1$ //$NON-NLS-2$
        im.put(KeyStroke.getKeyStroke(Messages.getString("Mirth.61")), Messages.getString("Mirth.62")); //$NON-NLS-1$ //$NON-NLS-2$

        try {
            UIManager.put(Messages.getString("Mirth.63"), ImageIO.read(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Mirth.64")))); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Application entry point. Sets up the login panel and its layout as well.
     * 
     * @param args
     *            String[]
     */
    public static void main(String[] args) {
        String server = Messages.getString("Mirth.65"); //$NON-NLS-1$
        String version = Messages.getString("Mirth.66"); //$NON-NLS-1$
        String username = Messages.getString("Mirth.67"); //$NON-NLS-1$
        String password = Messages.getString("Mirth.68"); //$NON-NLS-1$
        String protocols = Messages.getString("Mirth.69"); //$NON-NLS-1$
        String cipherSuites = Messages.getString("Mirth.70"); //$NON-NLS-1$

        if (args.length > 0) {
            server = args[0];
        }
        if (args.length > 1) {
            version = args[1];
        }
        if (args.length > 2) {
            if (StringUtils.equalsIgnoreCase(args[2], Messages.getString("Mirth.71"))) { //$NON-NLS-1$
                // <server> <version> -ssl [<protocols> [<ciphersuites> [<username> [<password>]]]]
                if (args.length > 3) {
                    protocols = args[3];
                }
                if (args.length > 4) {
                    cipherSuites = args[4];
                }
                if (args.length > 5) {
                    username = args[5];
                }
                if (args.length > 6) {
                    password = args[6];
                }
            } else {
                // <server> <version> <username> [<password> [-ssl [<protocols> [<ciphersuites>]]]]
                username = args[2];
                if (args.length > 3) {
                    password = args[3];
                }
                if (args.length > 4 && StringUtils.equalsIgnoreCase(args[4], Messages.getString("Mirth.72"))) { //$NON-NLS-1$
                    if (args.length > 5) {
                        protocols = args[5];
                    }
                    if (args.length > 6) {
                        cipherSuites = args[6];
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(protocols)) {
            PlatformUI.HTTPS_PROTOCOLS = StringUtils.split(protocols, ',');
        }
        if (StringUtils.isNotBlank(cipherSuites)) {
            PlatformUI.HTTPS_CIPHER_SUITES = StringUtils.split(cipherSuites, ',');
        }

        start(server, version, username, password);
    }

    private static void start(final String server, final String version, final String username, final String password) {
        // disable the velocity logging
        Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, Messages.getString("Mirth.73")); //$NON-NLS-1$

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initUIManager();
                PlatformUI.BACKGROUND_IMAGE = new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource(Messages.getString("Mirth.74"))); //$NON-NLS-1$
                LoginPanel.getInstance().initialize(server, version, username, password);
            }
        });
    }
}
