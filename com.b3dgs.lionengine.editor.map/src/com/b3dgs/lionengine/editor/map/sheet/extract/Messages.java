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
package com.b3dgs.lionengine.editor.map.sheet.extract;

import java.util.Locale;

import org.eclipse.osgi.util.NLS;

/**
 * Messages internationalization.
 */
public final class Messages extends NLS
{
    /** Dialog title. */
    public static String Title;
    /** Dialog progress title. */
    public static String Title_Progress;
    /** Title header. */
    public static String HeaderTitle;
    /** Description header. */
    public static String HeaderDesc;
    /** No level rip defined. */
    public static String NoLevelRipDefined;
    /** Config. */
    public static String Config;
    /** Tile width. */
    public static String TileWidth;
    /** Tile height. */
    public static String TileHeight;
    /** Horizontal tiles. */
    public static String HorizontalTiles;
    /** Vertical tiles. */
    public static String VerticalTiles;
    /** Extraction destination. */
    public static String Destination;
    /** Import sheets progress. */
    public static String Progress;

    /**
     * Initialize.
     */
    static
    {
        NLS.initializeMessages(Messages.class.getName().toLowerCase(Locale.ENGLISH), Messages.class);
    }
}
