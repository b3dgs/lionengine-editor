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
package com.b3dgs.lionengine.io.swt;

import com.b3dgs.lionengine.io.InputDevicePointer;

/**
 * Mouse input.
 */
public interface Mouse extends InputDevicePointer
{
    /** Left click. */
    int LEFT = 1;
    /** Middle click. */
    int MIDDLE = 2;
    /** Right click. */
    int RIGHT = 3;

    /**
     * Add an action that will be triggered on pressed state.
     * <p>
     * Alternative usage with classic programming style can be achieved with {@link #hasClicked(int)} or
     * {@link #hasClickedOnce(int)}.
     * </p>
     * 
     * @param click The action key.
     * @param action The action reference.
     */
    void addActionPressed(int click, EventAction action);

    /**
     * Add an action that will be triggered on released state.
     * <p>
     * Alternative usage with classic programming style can be achieved with {@link #hasClicked(int)} or
     * {@link #hasClickedOnce(int)}.
     * </p>
     * 
     * @param click The action key.
     * @param action The action reference.
     */
    void addActionReleased(int click, EventAction action);

    /**
     * Get location on screen x.
     * 
     * @return The location on screen x.
     */
    int getOnScreenX();

    /**
     * Get location on screen y.
     * 
     * @return The location on screen y.
     */
    int getOnScreenY();
}
