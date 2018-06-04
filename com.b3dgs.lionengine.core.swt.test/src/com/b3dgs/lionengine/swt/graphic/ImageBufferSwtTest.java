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
package com.b3dgs.lionengine.swt.graphic;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;
import static com.b3dgs.lionengine.UtilAssert.assertNotEquals;
import static com.b3dgs.lionengine.UtilAssert.assertNotNull;
import static com.b3dgs.lionengine.UtilAssert.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.b3dgs.lionengine.UtilReflection;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.Graphics;
import com.b3dgs.lionengine.graphic.ImageBuffer;
import com.b3dgs.lionengine.graphic.Transparency;

/**
 * Test {@link ImageBufferSwt}.
 */
public final class ImageBufferSwtTest
{
    /**
     * Prepare test.
     */
    @BeforeAll
    public static void beforeTests()
    {
        Graphics.setFactoryGraphic(new FactoryGraphicSwt());
    }

    /**
     * Clean test.
     */
    @AfterAll
    public static void afterTests()
    {
        Graphics.setFactoryGraphic(null);
    }

    /**
     * Test the image.
     * 
     * @throws InvocationTargetException If error.
     * @throws IllegalAccessException If error.
     * @throws IllegalArgumentException If error.
     * @throws NoSuchMethodException If error.
     */
    @Test
    public void testImage() throws IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException
    {
        final Image buffer = ToolsSwt.createImage(100, 100, SWT.TRANSPARENCY_NONE);
        final ImageData data = buffer.getImageData();
        final ImageBuffer image = new ImageBufferSwt(buffer);

        assertNotNull(image.createGraphic());
        assertEquals(buffer, UtilReflection.getMethod(image, "getBuffer"));

        image.prepare();
        assertNotEquals(buffer, image.getSurface());

        assertEquals(-1, image.getRgb(0, 0));
        assertNotNull(image.getRgb(0, 0, 1, 1, new int[1], 0, 0));
        assertEquals(Transparency.OPAQUE, image.getTransparency());
        assertEquals(data.width, image.getWidth());
        assertEquals(data.height, image.getHeight());

        image.setRgb(0, 0, ColorRgba.BLUE.getRgba());
        assertEquals(ColorRgba.BLUE.getRgba(), image.getRgb(0, 0));
        image.setRgb(0, 0, 0, 0, new int[1], 0, 0);

        assertEquals(Transparency.BITMASK, ImageBufferSwt.getTransparency(SWT.TRANSPARENCY_MASK));

        image.dispose();
    }

    /**
     * Test the image transparency
     */
    @Test
    public void testImageTransparency()
    {
        final ImageBuffer image = Graphics.createImageBuffer(100, 100, ColorRgba.RED);

        assertTrue(image.getTransparency() == Transparency.BITMASK && ColorRgba.RED.equals(image.getTransparentColor())
                   || image.getTransparency() == Transparency.TRANSLUCENT && image.getTransparentColor() == null,
                   image.getTransparency() + " " + image.getTransparentColor());
        assertEquals(Transparency.TRANSLUCENT, ImageBufferSwt.getTransparency(SWT.TRANSPARENCY_ALPHA));
        assertEquals(Transparency.OPAQUE, ImageBufferSwt.getTransparency(-1));
    }
}