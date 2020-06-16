/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import static com.mirth.connect.client.ui.ChannelPanel.DATA_TYPE_COLUMN_NUMBER;
import static com.mirth.connect.client.ui.ChannelPanel.DEPLOYED_REVISION_DELTA_COLUMN_NUMBER;
import static com.mirth.connect.client.ui.ChannelPanel.DESCRIPTION_COLUMN_NUMBER;
import static com.mirth.connect.client.ui.ChannelPanel.ID_COLUMN_NUMBER;
import static com.mirth.connect.client.ui.ChannelPanel.LAST_DEPLOYED_COLUMN_NUMBER;
import static com.mirth.connect.client.ui.ChannelPanel.LAST_MODIFIED_COLUMN_NUMBER;
import static com.mirth.connect.client.ui.ChannelPanel.LOCAL_CHANNEL_ID_COLUMN_NUMBER;
import static com.mirth.connect.client.ui.ChannelPanel.NAME_COLUMN_NUMBER;
import static com.mirth.connect.client.ui.ChannelPanel.STATUS_COLUMN_NUMBER;

import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import com.mirth.connect.plugins.ChannelColumnPlugin;

public class ChannelTableColumnFactory extends ColumnFactory {

    private int colOffset;
    private Map<Integer, ChannelColumnPlugin> plugins = new HashMap<Integer, ChannelColumnPlugin>();

    public ChannelTableColumnFactory() {
        // map column indices to the appropriate plug-ins
        int i = 0;

        for (ChannelColumnPlugin plugin : LoadedExtensions.getInstance().getChannelColumnPlugins().values()) {
            if (plugin.isDisplayFirst()) {
                plugins.put(i++, plugin);
            }
        }

        colOffset = i;

        i += ChannelPanel.getNumberOfDefaultColumns();

        for (ChannelColumnPlugin plugin : LoadedExtensions.getInstance().getChannelColumnPlugins().values()) {
            if (!plugin.isDisplayFirst()) {
                plugins.put(i++, plugin);
            }
        }
    }

    @Override
    public TableColumnExt createAndConfigureTableColumn(TableModel model, int modelIndex) {
        TableColumnExt column = super.createAndConfigureTableColumn(model, modelIndex);

        // the colOffset tells us where the normal columns begin, after any columns added by plugin(s)
        int index = modelIndex - colOffset;

        switch (index) {
            case STATUS_COLUMN_NUMBER:
                column.setMaxWidth(UIConstants.MIN_WIDTH);
                column.setMinWidth(UIConstants.MIN_WIDTH);
                column.setCellRenderer(new ImageCellRenderer());
                column.setToolTipText(Messages.getString("ChannelTableColumnFactory.0")); //$NON-NLS-1$
                break;

            case DATA_TYPE_COLUMN_NUMBER:
                column.setMaxWidth(UIConstants.MIN_WIDTH);
                column.setMinWidth(UIConstants.MIN_WIDTH);
                column.setToolTipText(Messages.getString("ChannelTableColumnFactory.1")); //$NON-NLS-1$
                break;

            case NAME_COLUMN_NUMBER:
                column.setMinWidth(150);
                column.setToolTipText(Messages.getString("ChannelTableColumnFactory.2")); //$NON-NLS-1$
                break;

            case ID_COLUMN_NUMBER:
                column.setMinWidth(215);
                column.setMaxWidth(215);
                column.setToolTipText(Messages.getString("ChannelTableColumnFactory.3")); //$NON-NLS-1$
                break;

            case LOCAL_CHANNEL_ID_COLUMN_NUMBER:
                column.setMinWidth(60);
                column.setMaxWidth(60);
                column.setCellRenderer(new NumberCellRenderer(SwingConstants.CENTER, false));
                column.setToolTipText(Messages.getString("ChannelTableColumnFactory.4")); //$NON-NLS-1$
                break;

            case DESCRIPTION_COLUMN_NUMBER:
                column.setMinWidth(UIConstants.MIN_WIDTH);
                column.setToolTipText(Messages.getString("ChannelTableColumnFactory.5")); //$NON-NLS-1$
                break;

            case DEPLOYED_REVISION_DELTA_COLUMN_NUMBER:
                column.setMaxWidth(50);
                column.setMinWidth(50);
                column.setCellRenderer(new NumberCellRenderer(SwingConstants.CENTER, false));
                column.setResizable(false);
                column.setToolTipText(Messages.getString("ChannelTableColumnFactory.6")); //$NON-NLS-1$
                break;

            case LAST_DEPLOYED_COLUMN_NUMBER:
                column.setMinWidth(95);
                column.setMaxWidth(95);
                column.setCellRenderer(new DateCellRenderer());
                column.setResizable(false);
                column.setToolTipText(Messages.getString("ChannelTableColumnFactory.7")); //$NON-NLS-1$
                break;

            case LAST_MODIFIED_COLUMN_NUMBER:
                column.setMinWidth(95);
                column.setMaxWidth(95);
                column.setCellRenderer(new DateCellRenderer());
                column.setResizable(false);
                column.setToolTipText(Messages.getString("ChannelTableColumnFactory.8")); //$NON-NLS-1$
                break;

            default:
                ChannelColumnPlugin plugin = plugins.get(modelIndex);

                if (plugin != null) {
                    column.setCellRenderer(plugin.getCellRenderer());
                    column.setMaxWidth(plugin.getMaxWidth());
                    column.setMinWidth(plugin.getMinWidth());
                }
                break;
        }

        return column;
    }
}