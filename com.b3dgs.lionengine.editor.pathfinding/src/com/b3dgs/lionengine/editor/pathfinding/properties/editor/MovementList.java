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
package com.b3dgs.lionengine.editor.pathfinding.properties.editor;

import java.util.Collection;

import com.b3dgs.lionengine.editor.ObjectList;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.MovementTile;

/**
 * Represents the movements list, allowing to add and remove {@link MovementTile}.
 */
public class MovementList extends ObjectList<MovementTile>
{
    /**
     * Create movement list and associate its properties.
     */
    public MovementList()
    {
        super(MovementTile.class);
    }

    /**
     * Load the existing movements from the object configurer.
     * 
     * @param movements The movements reference.
     */
    public void loadMovements(Collection<MovementTile> movements)
    {
        loadObjects(movements);
    }

    /*
     * ObjectList
     */

    @Override
    protected MovementTile copyObject(MovementTile movement)
    {
        return movement;
    }

    @Override
    protected MovementTile createObject(String name)
    {
        return MovementTile.valueOf(name);
    }
}
