/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;

import com.mirth.connect.client.ui.components.rsta.MirthRTextScrollPane;
import com.mirth.connect.model.codetemplates.ContextType;
import com.mirth.connect.util.JavaScriptSharedUtil;

public class ScriptPanel extends JPanel {

    public static final String DEPLOY_SCRIPT = Messages.getString("ScriptPanel.0"); //$NON-NLS-1$
    public static final String UNDEPLOY_SCRIPT = Messages.getString("ScriptPanel.1"); //$NON-NLS-1$
    public static final String PREPROCESSOR_SCRIPT = Messages.getString("ScriptPanel.2"); //$NON-NLS-1$
    public static final String POSTPROCESSOR_SCRIPT = Messages.getString("ScriptPanel.3"); //$NON-NLS-1$

    private static final String[] SCRIPT_TYPES = new String[] { DEPLOY_SCRIPT, UNDEPLOY_SCRIPT,
            PREPROCESSOR_SCRIPT, POSTPROCESSOR_SCRIPT };

    public ScriptPanel(boolean global) {
        initComponents(global);
        initLayout();
        scriptComboBox.setSelectedIndex(0);
    }

    public void setScripts(Map<String, String> scripts) {
        for (Entry<String, String> entry : scripts.entrySet()) {
            scriptTextAreaMap.get(entry.getKey()).setText(entry.getValue());
        }
    }

    public Map<String, String> getScripts() {
        Map<String, String> scripts = new HashMap<String, String>();
        for (Entry<String, MirthRTextScrollPane> entry : scriptTextAreaMap.entrySet()) {
            scripts.put(entry.getKey(), entry.getValue().getText());
        }
        return scripts;
    }

    public void updateDisplayOptions() {
        for (MirthRTextScrollPane scriptTextArea : scriptTextAreaMap.values()) {
            scriptTextArea.updateDisplayOptions();
        }
        for (FunctionList functionList : functionListMap.values()) {
            functionList.updateUserTemplates();
        }
    }

    public void validateCurrentScript() {
        StringBuilder sb = new StringBuilder();
        Context context = JavaScriptSharedUtil.getGlobalContextForValidation();
        try {
            context.compileString(Messages.getString("ScriptPanel.4") + getScriptTextArea().getText() + Messages.getString("ScriptPanel.5"), PlatformUI.MIRTH_FRAME.mirthClient.getGuid(), 1, null); //$NON-NLS-1$ //$NON-NLS-2$
            sb.append(Messages.getString("ScriptPanel.6")); //$NON-NLS-1$
        } catch (EvaluatorException e) {
            sb.append(Messages.getString("ScriptPanel.7") + e.lineNumber() + Messages.getString("ScriptPanel.8") + e.getMessage() + Messages.getString("ScriptPanel.9")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } catch (Exception e) {
            sb.append(Messages.getString("ScriptPanel.10")); //$NON-NLS-1$
        } finally {
            Context.exit();
        }

        PlatformUI.MIRTH_FRAME.alertInformation(this, sb.toString());
    }

    public String validateScript(String script) {
        String error = null;
        Context context = JavaScriptSharedUtil.getGlobalContextForValidation();
        try {
            context.compileString(Messages.getString("ScriptPanel.11") + script + Messages.getString("ScriptPanel.12"), UUID.randomUUID().toString(), 1, null); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (EvaluatorException e) {
            error = Messages.getString("ScriptPanel.13") + e.lineNumber() + Messages.getString("ScriptPanel.14") + e.getMessage() + Messages.getString("ScriptPanel.15"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } catch (Exception e) {
            error = Messages.getString("ScriptPanel.16"); //$NON-NLS-1$
        } finally {
            Context.exit();
        }

        return error;
    }

    private void initComponents(boolean global) {
        setBackground(UIConstants.BACKGROUND_COLOR);

        scriptLabel = new JLabel(Messages.getString("ScriptPanel.17")); //$NON-NLS-1$

        scriptComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(SCRIPT_TYPES));
        scriptComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                scriptComboBoxActionPerformed();
            }
        });

        scriptTextAreaMap = new HashMap<String, MirthRTextScrollPane>();
        functionListMap = new HashMap<String, FunctionList>();

        for (String scriptType : SCRIPT_TYPES) {
            ContextType contextType = getContextType(scriptType, global);

            MirthRTextScrollPane scriptTextArea = new MirthRTextScrollPane(contextType, true);
            scriptTextArea.setBorder(BorderFactory.createEtchedBorder());
            scriptTextAreaMap.put(scriptType, scriptTextArea);

            FunctionList functionList = new FunctionList(contextType);
            functionList.setDefaultDropDownValue();
            functionListMap.put(scriptType, functionList);
        }
    }

    private void initLayout() {
        setLayout(new MigLayout(Messages.getString("ScriptPanel.18"))); //$NON-NLS-1$
        add(scriptLabel, Messages.getString("ScriptPanel.19")); //$NON-NLS-1$
        add(scriptComboBox);

        for (FunctionList functionList : functionListMap.values()) {
            add(functionList, Messages.getString("ScriptPanel.20")); //$NON-NLS-1$
        }

        for (MirthRTextScrollPane scriptTextArea : scriptTextAreaMap.values()) {
            add(scriptTextArea, Messages.getString("ScriptPanel.21")); //$NON-NLS-1$
        }
    }

    private ContextType getContextType(String scriptType, boolean global) {
        if (scriptType.equals(DEPLOY_SCRIPT)) {
            return global ? ContextType.GLOBAL_DEPLOY : ContextType.CHANNEL_DEPLOY;
        } else if (scriptType.equals(UNDEPLOY_SCRIPT)) {
            return global ? ContextType.GLOBAL_UNDEPLOY : ContextType.CHANNEL_UNDEPLOY;
        } else if (scriptType.equals(PREPROCESSOR_SCRIPT)) {
            return global ? ContextType.GLOBAL_PREPROCESSOR : ContextType.CHANNEL_PREPROCESSOR;
        } else if (scriptType.equals(POSTPROCESSOR_SCRIPT)) {
            return global ? ContextType.GLOBAL_POSTPROCESSOR : ContextType.CHANNEL_POSTPROCESSOR;
        }

        return null;
    }

    private MirthRTextScrollPane getScriptTextArea() {
        return scriptTextAreaMap.get((String) scriptComboBox.getSelectedItem());
    }

    private void scriptComboBoxActionPerformed() {
        String scriptType = (String) scriptComboBox.getSelectedItem();

        for (Entry<String, MirthRTextScrollPane> entry : scriptTextAreaMap.entrySet()) {
            entry.getValue().setVisible(entry.getKey().equals(scriptType));
        }

        for (Entry<String, FunctionList> entry : functionListMap.entrySet()) {
            entry.getValue().setVisible(entry.getKey().equals(scriptType));
        }
    }

    private JLabel scriptLabel;
    private JComboBox<String> scriptComboBox;
    private Map<String, MirthRTextScrollPane> scriptTextAreaMap;
    private Map<String, FunctionList> functionListMap;
}
