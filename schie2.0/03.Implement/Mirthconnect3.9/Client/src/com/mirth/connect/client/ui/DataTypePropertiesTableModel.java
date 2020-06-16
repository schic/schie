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

import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;

import com.mirth.connect.model.datatype.DataTypeProperties;
import com.mirth.connect.model.datatype.DataTypePropertiesGroup;

public class DataTypePropertiesTableModel extends SortableTreeTableModel {

    private AbstractSortableTreeTableNode root;

    public DataTypePropertiesTableModel() {
        root = new AbstractSortableTreeTableNode() {
            @Override
            public Object getValueAt(int column) {
                return null;
            }

            @Override
            public int getColumnCount() {
                return 0;
            }
        };

        setRoot(root);
    }

    @Override
    public int getHierarchicalColumn() {
        return 0;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return null;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        if (node instanceof DataTypePropertiesTableNode) {
            return ((DataTypePropertiesTableNode) node).isEditable(column);
        }

        return false;
    }

    /**
     * Adds the property set to the tree table. All DataTypeProperties in the properties list and
     * the default properties must be for the same data type
     */
    public void addProperties(boolean inbound, List<DataTypePropertiesContainer> propertiesContainers, DataTypeProperties defaultProperties) {
        if (propertiesContainers != null) {

            // Create a list for each DataTypePropertiesGroup
            List<DataTypePropertiesGroup> serializationProperties = new ArrayList<DataTypePropertiesGroup>();
            List<DataTypePropertiesGroup> deserializationProperties = new ArrayList<DataTypePropertiesGroup>();
            List<DataTypePropertiesGroup> batchProperties = new ArrayList<DataTypePropertiesGroup>();
            List<DataTypePropertiesGroup> responseGenerationProperties = new ArrayList<DataTypePropertiesGroup>();
            List<DataTypePropertiesGroup> responseValidationProperties = new ArrayList<DataTypePropertiesGroup>();

            // Load the lists with the properties from each DataTypeProperties objects
            for (DataTypePropertiesContainer dataTypePropertiesContainer : propertiesContainers) {
                DataTypeProperties dataTypeProperties = dataTypePropertiesContainer.getProperties();

                if (dataTypeProperties.getSerializationProperties() != null) {
                    serializationProperties.add(dataTypeProperties.getSerializationProperties());
                }

                if (dataTypeProperties.getDeserializationProperties() != null) {
                    deserializationProperties.add(dataTypeProperties.getDeserializationProperties());
                }

                if (dataTypeProperties.getBatchProperties() != null && dataTypePropertiesContainer.getType() == TransformerType.SOURCE) {
                    batchProperties.add(dataTypeProperties.getBatchProperties());
                }

                if (dataTypeProperties.getResponseGenerationProperties() != null && dataTypePropertiesContainer.getType() == TransformerType.SOURCE) {
                    responseGenerationProperties.add(dataTypeProperties.getResponseGenerationProperties());
                }

                if (dataTypeProperties.getResponseValidationProperties() != null && dataTypePropertiesContainer.getType() == TransformerType.RESPONSE) {
                    responseValidationProperties.add(dataTypeProperties.getResponseValidationProperties());
                }
            }

            if (inbound) {
                // Show serialization if inbound
                if (!serializationProperties.isEmpty()) {
                    createAndInsertNode(Messages.getString("DataTypePropertiesTableModel.0"), Messages.getString("DataTypePropertiesTableModel.1"), serializationProperties, defaultProperties.getSerializationProperties()); //$NON-NLS-1$ //$NON-NLS-2$
                }
            } else {
                // Show deserialization if outbound
                if (!deserializationProperties.isEmpty()) {
                    createAndInsertNode(Messages.getString("DataTypePropertiesTableModel.2"), Messages.getString("DataTypePropertiesTableModel.3"), deserializationProperties, defaultProperties.getDeserializationProperties()); //$NON-NLS-1$ //$NON-NLS-2$
                }

                // Show serialization as Template Serialization if outbound
                if (!serializationProperties.isEmpty()) {
                    createAndInsertNode(Messages.getString("DataTypePropertiesTableModel.4"), Messages.getString("DataTypePropertiesTableModel.5"), serializationProperties, defaultProperties.getSerializationProperties()); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }

            // Show batch if inbound
            if (!batchProperties.isEmpty() && inbound) {
                createAndInsertNode(Messages.getString("DataTypePropertiesTableModel.6"), Messages.getString("DataTypePropertiesTableModel.7"), batchProperties, defaultProperties.getBatchProperties()); //$NON-NLS-1$ //$NON-NLS-2$
            }

            // Show response generation if inbound
            if (!responseGenerationProperties.isEmpty() && inbound) {
                createAndInsertNode(Messages.getString("DataTypePropertiesTableModel.8"), Messages.getString("DataTypePropertiesTableModel.9"), responseGenerationProperties, defaultProperties.getResponseGenerationProperties()); //$NON-NLS-1$ //$NON-NLS-2$
            }

            // Show response validation if outbound
            if (!responseValidationProperties.isEmpty() && inbound) {
                createAndInsertNode(Messages.getString("DataTypePropertiesTableModel.10"), Messages.getString("DataTypePropertiesTableModel.11"), responseValidationProperties, defaultProperties.getResponseValidationProperties()); //$NON-NLS-1$ //$NON-NLS-2$
            }

            // If no properties have been added, indicate that the data type has no properties
            if (root.getChildCount() == 0) {
                insertNodeInto(new DataTypePropertiesTableNode(Messages.getString("DataTypePropertiesTableModel.12"), Messages.getString("DataTypePropertiesTableModel.13")), root); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    /**
     * Inserts the group node for each properties group and the children (properties)
     */
    private void createAndInsertNode(String groupName, String groupDescription, List<DataTypePropertiesGroup> propertiesGroups, DataTypePropertiesGroup defaultPropertiesGroup) {
        DataTypePropertiesTableNode groupNode = new DataTypePropertiesTableNode(groupName, groupDescription);
        insertNodeInto(groupNode, root);

        for (String key : propertiesGroups.get(0).getPropertyDescriptors().keySet()) {
            insertNodeInto(new DataTypePropertiesTableNode(key, propertiesGroups, defaultPropertiesGroup), groupNode);
        }
    }

    /**
     * Remove all nodes from the tree table
     */
    public void clear() {
        int childCount = root.getChildCount();

        for (int i = 0; i < childCount; i++) {
            removeNodeFromParent((MutableTreeTableNode) root.getChildAt(0));
        }
    }

    /**
     * Resets the node and all of its children to the data type's default values
     */
    public void resetToDefault(TreeTableNode node) {
        if (node == null) {
            node = root;
        }

        if (node instanceof DataTypePropertiesTableNode) {
            DataTypePropertiesTableNode tableNode = (DataTypePropertiesTableNode) node;

            tableNode.resetToDefault();
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            resetToDefault(node.getChildAt(i));
        }
    }

    /**
     * Returns if the node and all of its children have are set to their default values
     */
    public boolean isDefaultProperties(TreeTableNode node) {
        if (node == null) {
            node = root;
        }

        if (node instanceof DataTypePropertiesTableNode) {
            DataTypePropertiesTableNode tableNode = (DataTypePropertiesTableNode) node;

            if (!tableNode.isDefaultProperty()) {
                return false;
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            if (!isDefaultProperties(node.getChildAt(i))) {
                return false;
            }
        }

        return true;
    }

}
