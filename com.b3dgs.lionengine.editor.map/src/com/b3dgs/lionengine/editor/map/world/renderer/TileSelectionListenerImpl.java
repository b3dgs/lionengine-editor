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
package com.b3dgs.lionengine.editor.map.world.renderer;

import com.b3dgs.lionengine.editor.map.world.updater.TileSelectionListener;
import com.b3dgs.lionengine.game.feature.tile.Tile;

/**
 * 
 */
/**
 * Listen to tile selection to show tile properties.
 */
final class TileSelectionListenerImpl implements TileSelectionListener
{
    /** Selected tile. */
    private Tile tile;
    /** Selected tile group. */
    private String tileGroup;

    /**
     * Create listener.
     */
    TileSelectionListenerImpl()
    {
        super();
    }

    /**
     * Get the selected tile.
     * 
     * @return the tile The selected tile.
     */
    public Tile getTile()
    {
        return tile;
    }

    /**
     * Get the selected tile group.
     * 
     * @return the tileGroup The selected tile group.
     */
    public String getTileGroup()
    {
        return tileGroup;
    }

    /*
     * TileSelectionListener
     */

    @Override
    public void notifyTileSelected(int click, Tile tile)
    {
        this.tile = tile;
    }

    @Override
    public void notifyTileGroupSelected(String group)
    {
        tileGroup = group;
    }
}
