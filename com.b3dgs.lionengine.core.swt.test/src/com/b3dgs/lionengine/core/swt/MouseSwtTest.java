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
package com.b3dgs.lionengine.core.swt;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.Assert;
import org.junit.Test;

import com.b3dgs.lionengine.Config;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.io.swt.Mouse;

/**
 * Test the mouse class.
 */
public class MouseSwtTest
{
    /**
     * Create a configured test mouse.
     * 
     * @return The mouse instance.
     */
    public static MouseSwt createMouse()
    {
        final Resolution resolution = new Resolution(320, 240, 60);
        final Config config = new Config(resolution, 32, false);
        config.setSource(resolution);

        final MouseSwt mouse = new MouseSwt();
        mouse.setConfig(config);

        return mouse;
    }

    /**
     * Create a mouse event.
     * 
     * @param shell The shell reference.
     * @param click The click mouse.
     * @param x The horizontal location.
     * @param y The vertical location.
     * @return The event instance.
     */
    private static MouseEvent createEvent(Shell shell, int click, int x, int y)
    {
        final Event event = new Event();
        event.x = x;
        event.y = y;
        event.button = click;
        event.widget = shell;
        return new MouseEvent(event);
    }

    /**
     * Test the mouse clicked state.
     */
    @Test
    public void testClicked()
    {
        ScreenSwtTest.checkMultipleDisplaySupport();
        final MouseSwt mouse = createMouse();
        final Shell shell = new Shell(ToolsSwt.getDisplay());

        final MouseClickSwt click = mouse.getClicker();

        Assert.assertFalse(mouse.hasClicked(Mouse.LEFT));
        click.mouseDown(createEvent(shell, Mouse.LEFT, 0, 0));
        Assert.assertTrue(mouse.hasClicked(Mouse.LEFT));
        click.mouseUp(createEvent(shell, Mouse.LEFT, 0, 0));
        Assert.assertFalse(mouse.hasClicked(Mouse.LEFT));

        Assert.assertFalse(mouse.hasClicked(Mouse.RIGHT));
        click.mouseDown(createEvent(shell, Mouse.RIGHT, 0, 0));
        Assert.assertTrue(mouse.hasClicked(Mouse.RIGHT));
        click.mouseUp(createEvent(shell, Mouse.RIGHT, 0, 0));
        Assert.assertFalse(mouse.hasClicked(Mouse.RIGHT));

        Assert.assertFalse(mouse.hasClickedOnce(Mouse.MIDDLE));
        click.mouseDown(createEvent(shell, Mouse.MIDDLE, 0, 0));
        Assert.assertTrue(mouse.hasClickedOnce(Mouse.MIDDLE));
        Assert.assertFalse(mouse.hasClickedOnce(Mouse.MIDDLE));
        click.mouseUp(createEvent(shell, Mouse.MIDDLE, 0, 0));
        Assert.assertFalse(mouse.hasClickedOnce(Mouse.MIDDLE));

        shell.dispose();
    }

    /**
     * Test the mouse click.
     */
    @Test
    public void testClick()
    {
        ScreenSwtTest.checkMultipleDisplaySupport();
        final MouseSwt mouse = createMouse();
        final Shell shell = new Shell(ToolsSwt.getDisplay());

        final MouseClickSwt click = mouse.getClicker();

        click.mouseDown(createEvent(shell, Mouse.MIDDLE, 0, 0));
        Assert.assertEquals(Mouse.MIDDLE, mouse.getClick());
        click.mouseUp(createEvent(shell, Mouse.MIDDLE, 0, 0));
        Assert.assertNotEquals(Mouse.MIDDLE, mouse.getClick());

        shell.dispose();
    }

    /**
     * Test the mouse on screen.
     */
    @Test
    public void testLocation()
    {
        ScreenSwtTest.checkMultipleDisplaySupport();
        final MouseSwt mouse = createMouse();
        final Shell shell = new Shell(ToolsSwt.getDisplay());

        final MouseMoveSwt move = mouse.getMover();

        move.mouseMove(createEvent(shell, Mouse.LEFT, 0, 0));
        Assert.assertEquals(0, mouse.getX());
        Assert.assertEquals(0, mouse.getY());

        move.mouseMove(createEvent(shell, Mouse.LEFT, 10, 20));
        Assert.assertEquals(10, mouse.getX());
        Assert.assertEquals(20, mouse.getY());

        shell.dispose();
    }

    /**
     * Test the mouse move.
     */
    @Test
    public void testMouse()
    {
        ScreenSwtTest.checkMultipleDisplaySupport();
        final MouseSwt mouse = createMouse();
        final MouseMoveSwt move = mouse.getMover();
        final Shell shell = new Shell(ToolsSwt.getDisplay());

        move.mouseMove(createEvent(shell, 0, 0, 0));
        move.mouseMove(createEvent(shell, 0, 0, 0));
        mouse.update(1.0);
        Assert.assertEquals(0, mouse.getMoveX());
        Assert.assertEquals(0, mouse.getMoveY());
        Assert.assertEquals(0, mouse.getOnScreenX());
        Assert.assertEquals(0, mouse.getOnScreenY());
        Assert.assertTrue(mouse.hasMoved());
        Assert.assertFalse(mouse.hasMoved());

        shell.dispose();
    }

    /**
     * Test the mouse event.
     */
    @Test
    public void testEvent()
    {
        ScreenSwtTest.checkMultipleDisplaySupport();
        final MouseSwt mouse = createMouse();
        final Shell shell = new Shell(ToolsSwt.getDisplay());
        final AtomicBoolean left = new AtomicBoolean(false);

        mouse.addActionPressed(Mouse.LEFT, () -> left.set(true));
        mouse.addActionPressed(Mouse.LEFT, () -> left.set(true));
        mouse.addActionReleased(Mouse.LEFT, () -> left.set(false));
        mouse.addActionReleased(Mouse.LEFT, () -> left.set(false));
        Assert.assertFalse(left.get());

        final MouseClickSwt click = mouse.getClicker();

        click.mouseDown(createEvent(shell, Mouse.LEFT, 0, 0));
        Assert.assertTrue(left.get());

        click.mouseUp(createEvent(shell, Mouse.LEFT, 0, 0));
        Assert.assertFalse(left.get());

        click.mouseDown(createEvent(shell, 10, 0, 0));
        Assert.assertFalse(left.get());

        click.mouseUp(createEvent(shell, 10, 0, 0));
        Assert.assertFalse(left.get());

        click.mouseScrolled(createEvent(shell, Mouse.LEFT, 0, 0));
        click.mouseDoubleClick(createEvent(shell, Mouse.LEFT, 0, 0));

        shell.dispose();
    }
}
