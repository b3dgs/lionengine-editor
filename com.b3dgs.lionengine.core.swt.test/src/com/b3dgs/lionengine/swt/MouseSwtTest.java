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
package com.b3dgs.lionengine.swt;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;
import static com.b3dgs.lionengine.UtilAssert.assertFalse;
import static com.b3dgs.lionengine.UtilAssert.assertNotEquals;
import static com.b3dgs.lionengine.UtilAssert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.Test;

import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.swt.graphic.MouseClickSwt;
import com.b3dgs.lionengine.swt.graphic.MouseMoveSwt;
import com.b3dgs.lionengine.swt.graphic.MouseSwt;
import com.b3dgs.lionengine.swt.graphic.ScreenSwtTest;
import com.b3dgs.lionengine.swt.graphic.ToolsSwt;

/**
 * Test {@link MouseSwt}.
 */
public final class MouseSwtTest
{
    /**
     * Create a configured test mouse.
     * 
     * @return The mouse instance.
     */
    public static MouseSwt createMouse()
    {
        final Resolution resolution = new Resolution(320, 240, 60);

        final MouseSwt mouse = new MouseSwt();
        mouse.setConfig(resolution, resolution);

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

        assertFalse(mouse.hasClicked(MouseSwt.LEFT));

        click.mouseDown(createEvent(shell, MouseSwt.LEFT, 0, 0));
        assertTrue(mouse.hasClicked(MouseSwt.LEFT));

        click.mouseUp(createEvent(shell, MouseSwt.LEFT, 0, 0));
        assertFalse(mouse.hasClicked(MouseSwt.LEFT));
        assertFalse(mouse.hasClicked(MouseSwt.RIGHT));

        click.mouseDown(createEvent(shell, MouseSwt.RIGHT, 0, 0));
        assertTrue(mouse.hasClicked(MouseSwt.RIGHT));

        click.mouseUp(createEvent(shell, MouseSwt.RIGHT, 0, 0));
        assertFalse(mouse.hasClicked(MouseSwt.RIGHT));
        assertFalse(mouse.hasClickedOnce(MouseSwt.MIDDLE));

        click.mouseDown(createEvent(shell, MouseSwt.MIDDLE, 0, 0));
        assertTrue(mouse.hasClickedOnce(MouseSwt.MIDDLE));
        assertFalse(mouse.hasClickedOnce(MouseSwt.MIDDLE));

        click.mouseUp(createEvent(shell, MouseSwt.MIDDLE, 0, 0));
        assertFalse(mouse.hasClickedOnce(MouseSwt.MIDDLE));

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

        click.mouseDown(createEvent(shell, MouseSwt.MIDDLE, 0, 0));
        assertEquals(MouseSwt.MIDDLE, mouse.getClick());
        click.mouseUp(createEvent(shell, MouseSwt.MIDDLE, 0, 0));
        assertNotEquals(MouseSwt.MIDDLE, mouse.getClick());

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

        move.mouseMove(createEvent(shell, MouseSwt.LEFT, 0, 0));
        assertEquals(0, mouse.getX());
        assertEquals(0, mouse.getY());

        move.mouseMove(createEvent(shell, MouseSwt.LEFT, 10, 20));
        assertEquals(10, mouse.getX());
        assertEquals(20, mouse.getY());

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

        assertEquals(0, mouse.getMoveX());
        assertEquals(0, mouse.getMoveY());
        assertEquals(0, mouse.getOnScreenX());
        assertEquals(0, mouse.getOnScreenY());
        assertTrue(mouse.hasMoved());
        assertFalse(mouse.hasMoved());

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

        mouse.addActionPressed(MouseSwt.LEFT, () -> left.set(true));
        mouse.addActionPressed(MouseSwt.LEFT, () -> left.set(true));
        mouse.addActionReleased(MouseSwt.LEFT, () -> left.set(false));
        mouse.addActionReleased(MouseSwt.LEFT, () -> left.set(false));

        assertFalse(left.get());

        final MouseClickSwt click = mouse.getClicker();
        click.mouseDown(createEvent(shell, MouseSwt.LEFT, 0, 0));
        assertTrue(left.get());

        click.mouseUp(createEvent(shell, MouseSwt.LEFT, 0, 0));
        assertFalse(left.get());

        click.mouseDown(createEvent(shell, 10, 0, 0));
        assertFalse(left.get());

        click.mouseUp(createEvent(shell, 10, 0, 0));
        assertFalse(left.get());

        click.mouseScrolled(createEvent(shell, MouseSwt.LEFT, 0, 0));
        click.mouseDoubleClick(createEvent(shell, MouseSwt.LEFT, 0, 0));

        shell.dispose();
    }
}
