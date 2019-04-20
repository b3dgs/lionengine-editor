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
package com.b3dgs.lionengine.swt;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;
import static com.b3dgs.lionengine.UtilAssert.assertFalse;
import static com.b3dgs.lionengine.UtilAssert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.junit.jupiter.api.Test;

import com.b3dgs.lionengine.swt.graphic.KeyboardSwt;
import com.b3dgs.lionengine.swt.graphic.ScreenSwtTest;
import com.b3dgs.lionengine.swt.graphic.ToolsSwt;

/**
 * Test {@link KeyboardSwt}.
 */
public final class KeyboardSwtTest
{
    /**
     * Create a key event.
     * 
     * @param widget The widget parent.
     * @param key The event key.
     * @return The event instance.
     */
    private static KeyEvent createEvent(Widget widget, Integer key)
    {
        final Event event = new Event();
        event.widget = widget;
        event.keyCode = key.intValue();
        event.character = ' ';

        return new KeyEvent(event);
    }

    /**
     * Test the keyboard not pressed state.
     */
    @Test
    public void testNotPressed()
    {
        ScreenSwtTest.checkMultipleDisplaySupport();
        final KeyboardSwt keyboard = new KeyboardSwt();

        assertFalse(keyboard.isPressed(KeyboardSwt.ALT));
        assertFalse(keyboard.isPressed(KeyboardSwt.BACK_SPACE));
        assertFalse(keyboard.isPressed(KeyboardSwt.CONTROL));
        assertFalse(keyboard.isPressed(KeyboardSwt.DOWN));
        assertFalse(keyboard.isPressed(KeyboardSwt.ENTER));
        assertFalse(keyboard.isPressed(KeyboardSwt.ESCAPE));
        assertFalse(keyboard.isPressed(KeyboardSwt.LEFT));
        assertFalse(keyboard.isPressed(KeyboardSwt.RIGHT));
        assertFalse(keyboard.isPressed(KeyboardSwt.BACK_SPACE));
        assertFalse(keyboard.isPressed(KeyboardSwt.TAB));
        assertFalse(keyboard.isPressed(KeyboardSwt.UP));
        assertFalse(keyboard.isPressedOnce(KeyboardSwt.UP));
    }

    /**
     * Test the keyboard pressed.
     */
    @Test
    public void testPressed()
    {
        ScreenSwtTest.checkMultipleDisplaySupport();
        final KeyboardSwt keyboard = new KeyboardSwt();
        final Shell shell = new Shell(ToolsSwt.getDisplay());

        keyboard.keyPressed(createEvent(shell, KeyboardSwt.ALT));

        assertTrue(keyboard.isPressed(KeyboardSwt.ALT));
        assertTrue(keyboard.isPressedOnce(KeyboardSwt.ALT));
        assertFalse(keyboard.isPressedOnce(KeyboardSwt.ALT));
        assertEquals(keyboard.getKeyCode(), KeyboardSwt.ALT);
        assertEquals(keyboard.getKeyName(), ' ');
        assertTrue(keyboard.used());

        keyboard.keyReleased(createEvent(shell, KeyboardSwt.ALT));

        assertFalse(keyboard.isPressed(KeyboardSwt.ALT));
        assertFalse(keyboard.used());
    }

    /**
     * Test the keyboard directions.
     */
    @Test
    public void testDirections()
    {
        ScreenSwtTest.checkMultipleDisplaySupport();
        final KeyboardSwt keyboard = new KeyboardSwt();
        final Shell shell = new Shell(ToolsSwt.getDisplay());

        keyboard.setHorizontalControlNegative(KeyboardSwt.LEFT);
        keyboard.setVerticalControlNegative(KeyboardSwt.DOWN);
        keyboard.setHorizontalControlPositive(KeyboardSwt.RIGHT);
        keyboard.setVerticalControlPositive(KeyboardSwt.UP);

        assertEquals(KeyboardSwt.LEFT, keyboard.getHorizontalControlNegative());
        assertEquals(KeyboardSwt.DOWN, keyboard.getVerticalControlNegative());
        assertEquals(KeyboardSwt.RIGHT, keyboard.getHorizontalControlPositive());
        assertEquals(KeyboardSwt.UP, keyboard.getVerticalControlPositive());

        keyboard.keyPressed(createEvent(shell, KeyboardSwt.RIGHT));
        assertEquals(1.0, keyboard.getHorizontalDirection());

        keyboard.keyReleased(createEvent(shell, KeyboardSwt.RIGHT));
        keyboard.keyPressed(createEvent(shell, KeyboardSwt.LEFT));
        assertEquals(-1.0, keyboard.getHorizontalDirection());

        keyboard.keyReleased(createEvent(shell, KeyboardSwt.LEFT));
        assertEquals(0.0, keyboard.getHorizontalDirection());

        keyboard.keyPressed(createEvent(shell, KeyboardSwt.UP));
        assertEquals(1.0, keyboard.getVerticalDirection());

        keyboard.keyReleased(createEvent(shell, KeyboardSwt.UP));
        keyboard.keyPressed(createEvent(shell, KeyboardSwt.DOWN));
        assertEquals(-1.0, keyboard.getVerticalDirection());

        keyboard.keyReleased(createEvent(shell, KeyboardSwt.DOWN));
        assertEquals(0.0, keyboard.getVerticalDirection());

        shell.dispose();
    }

    /**
     * Test the keyboard events.
     */
    @Test
    public void testEvents()
    {
        ScreenSwtTest.checkMultipleDisplaySupport();
        final KeyboardSwt keyboard = new KeyboardSwt();
        final Shell shell = new Shell(ToolsSwt.getDisplay());
        final AtomicBoolean left = new AtomicBoolean(false);

        keyboard.addActionPressed(KeyboardSwt.LEFT, () -> left.set(true));
        keyboard.addActionPressed(KeyboardSwt.LEFT, () -> left.set(true));
        keyboard.addActionReleased(KeyboardSwt.LEFT, () -> left.set(false));
        keyboard.addActionReleased(KeyboardSwt.LEFT, () -> left.set(false));
        assertFalse(left.get());

        keyboard.keyPressed(createEvent(shell, KeyboardSwt.LEFT));
        assertTrue(left.get());

        keyboard.keyReleased(createEvent(shell, KeyboardSwt.LEFT));
        assertFalse(left.get());

        keyboard.removeActionsPressed();
        keyboard.removeActionsReleased();

        keyboard.keyPressed(createEvent(shell, KeyboardSwt.LEFT));
        assertFalse(left.get());

        keyboard.keyReleased(createEvent(shell, KeyboardSwt.LEFT));
        assertFalse(left.get());

        shell.dispose();
    }
}
