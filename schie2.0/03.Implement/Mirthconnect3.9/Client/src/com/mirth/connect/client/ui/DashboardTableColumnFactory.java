/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import com.mirth.connect.plugins.DashboardColumnPlugin;

public class DashboardTableColumnFactory extends ColumnFactory {
    private int colOffset;
    private Map<Integer, DashboardColumnPlugin> plugins = new HashMap<Integer, DashboardColumnPlugin>();

    public DashboardTableColumnFactory() {
        // map column indices to the appropriate plug-ins
        int i = 0;

        for (DashboardColumnPlugin plugin : LoadedExtensions.getInstance().getDashboardColumnPlugins().values()) {
            if (plugin.isDisplayFirst()) {
                plugins.put(i++, plugin);
            }
        }

        colOffset = i;

        i += DashboardPanel.getNumberOfDefaultColumns();

        for (DashboardColumnPlugin plugin : LoadedExtensions.getInstance().getDashboardColumnPlugins().values()) {
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
            case 0:
                column.setCellRenderer(new ImageCellRenderer());
                column.setMaxWidth(UIConstants.MIN_WIDTH + 10);
                column.setMinWidth(UIConstants.MIN_WIDTH);
                column.setPreferredWidth(UIConstants.MIN_WIDTH);
                column.setToolTipText(Messages.getString("DashboardTableColumnFactory.0")); //$NON-NLS-1$
                break;

            case 1:
                column.setMinWidth(150);
                column.setToolTipText(Messages.getString("DashboardTableColumnFactory.1")); //$NON-NLS-1$
                break;

            case 2:
                column.setCellRenderer(new NumberCellRenderer(SwingConstants.CENTER, false));
                column.setComparator(new NumberCellComparator());
                column.setMaxWidth(50);
                column.setMinWidth(50);
                column.setToolTipText(Messages.getString("DashboardTableColumnFactory.2")); //$NON-NLS-1$
                break;

            case 3:
                column.setCellRenderer(new DateCellRenderer());
                column.setMaxWidth(95);
                column.setMinWidth(95);
                column.setToolTipText(Messages.getString("DashboardTableColumnFactory.3")); //$NON-NLS-1$
                break;

            case 4:
                column.setCellRenderer(new NumberCellRenderer());
                column.setComparator(new NumberCellComparator());
                column.setMaxWidth(UIConstants.MIN_WIDTH);
                column.setMinWidth(UIConstants.MIN_WIDTH);
                column.setToolTipText(Messages.getString("DashboardTableColumnFactory.4")); //$NON-NLS-1$
                break;

            case 5:
                column.setCellRenderer(new NumberCellRenderer());
                column.setComparator(new NumberCellComparator());
                column.setMaxWidth(UIConstants.MIN_WIDTH);
                column.setMinWidth(UIConstants.MIN_WIDTH);
                column.setToolTipText(Messages.getString("DashboardTableColumnFactory.5")); //$NON-NLS-1$
                break;

            case 6:
                column.setCellRenderer(new NumberCellRenderer());
                column.setComparator(new NumberCellComparator());
                column.setMaxWidth(UIConstants.MIN_WIDTH);
                column.setMinWidth(UIConstants.MIN_WIDTH);
                column.setToolTipText(Messages.getString("DashboardTableColumnFactory.6")); //$NON-NLS-1$
                break;

            case 7:
                column.setCellRenderer(new NumberCellRenderer());
                column.setComparator(new NumberCellComparator());
                column.setMaxWidth(UIConstants.MIN_WIDTH);
                column.setMinWidth(UIConstants.MIN_WIDTH);
                column.setToolTipText(Messages.getString("DashboardTableColumnFactory.7")); //$NON-NLS-1$
                break;

            case 8:
                column.setCellRenderer(new NumberCellRenderer());
                column.setComparator(new NumberCellComparator());
                column.setMaxWidth(UIConstants.MIN_WIDTH);
                column.setMinWidth(UIConstants.MIN_WIDTH);
                column.setToolTipText(Messages.getString("DashboardTableColumnFactory.8")); //$NON-NLS-1$
                break;

            default:
                DashboardColumnPlugin plugin = plugins.get(modelIndex);

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
