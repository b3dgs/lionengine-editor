/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.editor.map.imports;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.b3dgs.lionengine.editor.dialog.AbstractDialog;
import com.b3dgs.lionengine.editor.utility.UtilIcon;
import com.b3dgs.lionengine.editor.utility.dialog.UtilDialog;
import com.b3dgs.lionengine.editor.widget.BrowseWidget;
import com.b3dgs.lionengine.editor.world.WorldModel;
import com.b3dgs.lionengine.game.feature.tile.TileGroupsConfig;
import com.b3dgs.lionengine.game.feature.tile.map.LevelRipConverter;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGroup;
import com.b3dgs.lionengine.game.feature.tile.map.TileSheetsConfig;
import com.b3dgs.lionengine.util.UtilFolder;

/**
 * Represents the import map dialog.
 */
public class MapImportDialog extends AbstractDialog
{
    /** Icon. */
    private static final Image ICON = UtilIcon.get("dialog", "map-import.png");

    /** Level rip location. */
    private BrowseWidget levelRip;
    /** Sheets location. */
    private BrowseWidget sheets;
    /** Groups location. */
    private BrowseWidget groups;

    /**
     * Create an import map dialog.
     * 
     * @param parent The shell parent.
     */
    public MapImportDialog(Shell parent)
    {
        super(parent, Messages.Title, Messages.HeaderTitle, Messages.HeaderDesc, ICON);
        createDialog();
        dialog.setMinimumSize(512, 160);
        finish.setEnabled(false);
        finish.forceFocus();
    }

    /**
     * Check if can enable finish button.
     */
    private void checkFinish()
    {
        finish.setEnabled(levelRip.getMedia() != null && sheets.getMedia() != null && groups.getMedia() != null);
    }

    /*
     * AbstractDialog
     */

    @Override
    protected void createContent(Composite content)
    {
        levelRip = new BrowseWidget(content, Messages.LevelRipLocation, UtilDialog.getImageFilter(), true);
        levelRip.addListener(media ->
        {
            if (sheets.getMedia() == null)
            {
                sheets.setLocation(UtilFolder.getPath(media.getParentPath(), TileSheetsConfig.FILENAME));
            }
            if (groups.getMedia() == null)
            {
                groups.setLocation(UtilFolder.getPath(media.getParentPath(), TileGroupsConfig.FILENAME));
            }
            checkFinish();
        });

        sheets = new BrowseWidget(content, Messages.SheetsLocation, UtilDialog.getXmlFilter(), true);
        sheets.addListener(media -> checkFinish());

        groups = new BrowseWidget(content, Messages.GroupsLocation, UtilDialog.getXmlFilter(), true);
        groups.addListener(media -> checkFinish());
    }

    @Override
    protected void onFinish()
    {
        final MapTile map = WorldModel.INSTANCE.getMap();
        map.loadSheets(sheets.getMedia());

        final MapImportProgressDialog progress = new MapImportProgressDialog(dialog, levelRip.getMedia());
        progress.open();

        LevelRipConverter.start(levelRip.getMedia(), map, progress);
        progress.finish();

        if (groups.getMedia().exists())
        {
            map.getFeature(MapTileGroup.class).loadGroups(groups.getMedia());
        }
    }
}
