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
package com.b3dgs.lionengine.editor.collision.object;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Tree;

import com.b3dgs.lionengine.editor.properties.PropertiesModel;
import com.b3dgs.lionengine.game.Configurer;

/**
 * Start collisions editor handler.
 */
public final class CollisionsObjectEditHandler
{
    /**
     * Create handler.
     */
    public CollisionsObjectEditHandler()
    {
        super();
    }

    /**
     * Execute the handler.
     */
    @Execute
    public void execute()
    {
        final Tree tree = PropertiesModel.INSTANCE.getTree();
        final Configurer configurer = (Configurer) tree.getData();
        final CollisionsObjectEditDialog editor = new CollisionsObjectEditDialog(tree, configurer);
        editor.create();
        editor.openAndWait();
    }
}
