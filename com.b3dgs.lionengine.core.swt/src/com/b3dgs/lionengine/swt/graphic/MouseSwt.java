/*
 * Copyright (C) 2013-2018 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.swt.graphic;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.swt.EventAction;
import com.b3dgs.lionengine.swt.Mouse;

/**
 * Mouse input implementation.
 */
public final class MouseSwt implements Mouse
{
    /** Left click. */
    public static final int LEFT = 1;
    /** Middle click. */
    public static final int MIDDLE = 2;
    /** Right click. */
    public static final int RIGHT = 3;

    /** Move click. */
    private final MouseClickSwt clicker = new MouseClickSwt();
    /** Mouse move. */
    private final MouseMoveSwt mover = new MouseMoveSwt();
    /** Screen horizontal ratio. */
    private double xRatio;
    /** Screen vertical ratio. */
    private double yRatio;

    /**
     * Constructor.
     */
    public MouseSwt()
    {
        super();
    }

    /**
     * Set the resolution used. This will compute mouse horizontal and vertical ratio.
     * 
     * @param output The resolution output (must not be <code>null</code>).
     * @param source The resolution source (must not be <code>null</code>).
     * @throws LionEngineException If invalid argument.
     */
    public void setConfig(Resolution output, Resolution source)
    {
        Check.notNull(output);
        Check.notNull(source);

        xRatio = output.getWidth() / (double) source.getWidth();
        yRatio = output.getHeight() / (double) source.getHeight();
    }

    /**
     * Get the click handler.
     * 
     * @return The click handler.
     */
    public MouseClickSwt getClicker()
    {
        return clicker;
    }

    /**
     * Get the movement handler.
     * 
     * @return The movement handler.
     */
    public MouseMoveSwt getMover()
    {
        return mover;
    }

    /*
     * Mouse
     */

    @Override
    public void addActionPressed(int click, EventAction action)
    {
        clicker.addActionPressed(click, action);
    }

    @Override
    public void addActionReleased(int click, EventAction action)
    {
        clicker.addActionReleased(click, action);
    }

    @Override
    public int getOnScreenX()
    {
        return mover.getX();
    }

    @Override
    public int getOnScreenY()
    {
        return mover.getY();
    }

    /*
     * InputDevicePointer
     */

    @Override
    public int getX()
    {
        return (int) (mover.getWx() / xRatio);
    }

    @Override
    public int getY()
    {
        return (int) (mover.getWy() / yRatio);
    }

    @Override
    public int getMoveX()
    {
        return mover.getMx();
    }

    @Override
    public int getMoveY()
    {
        return mover.getMy();
    }

    @Override
    public int getClick()
    {
        return clicker.getClick();
    }

    @Override
    public boolean hasClicked(int click)
    {
        return clicker.hasClicked(click);
    }

    @Override
    public boolean hasClickedOnce(int click)
    {
        return clicker.hasClickedOnce(click);
    }

    @Override
    public boolean hasMoved()
    {
        return mover.hasMoved();
    }

    /*
     * Updatable
     */

    @Override
    public void update(double extrp)
    {
        mover.update();
    }
}
