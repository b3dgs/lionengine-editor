/*
 * Copyright (C) 2013-2019 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.b3dgs.lionengine.swt.graphic;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;
import static com.b3dgs.lionengine.UtilAssert.assertNotEquals;
import static com.b3dgs.lionengine.UtilAssert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.b3dgs.lionengine.FactoryMediaDefault;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.graphic.FactoryGraphicTest;
import com.b3dgs.lionengine.graphic.Graphics;
import com.b3dgs.lionengine.graphic.ImageBuffer;
import com.b3dgs.lionengine.graphic.ImageBufferMock;

/**
 * Test the factory graphic provider class.
 */
public class FactoryGraphicSwtTest extends FactoryGraphicTest
{
    /**
     * Prepare test.
     */
    @BeforeAll
    public static void setUp()
    {
        ScreenSwtTest.checkMultipleDisplaySupport();
        Medias.setFactoryMedia(new FactoryMediaDefault());
        Medias.setLoadFromJar(FactoryGraphicSwtTest.class);
        Graphics.setFactoryGraphic(new FactoryGraphicSwt());
    }

    /**
     * Clean test.
     */
    @AfterAll
    public static void cleanUp()
    {
        Medias.setLoadFromJar(null);
        Graphics.setFactoryGraphic(null);
    }

    /**
     * Test rotate.
     */
    @Test
    @Override
    public void testRotate()
    {
        final ImageBuffer image = Graphics.getImageBuffer(Medias.create("image.png"));
        image.prepare();
        final ImageBuffer rotate = Graphics.rotate(image, 90);

        assertNotEquals(image, rotate);
        assertEquals(image.getWidth(), rotate.getHeight());
        assertEquals(image.getHeight(), rotate.getWidth());

        rotate.dispose();
        image.dispose();
    }

    /**
     * Test the get image buffer exception case.
     */
    @Test
    public void testGetImageBufferException()
    {
        assertThrows(() -> Graphics.getImageBuffer(new Media()
        {
            @Override
            public String getName()
            {
                return null;
            }

            @Override
            public String getPath()
            {
                return null;
            }

            @Override
            public String getParentPath()
            {
                return null;
            }

            @Override
            public OutputStream getOutputStream()
            {
                return null;
            }

            @Override
            public InputStream getInputStream()
            {
                return new InputStream()
                {
                    @Override
                    public int read() throws IOException
                    {
                        return 0;
                    }
                };
            }

            @Override
            public File getFile()
            {
                return null;
            }

            @Override
            public Collection<Media> getMedias()
            {
                return null;
            }

            @Override
            public boolean exists()
            {
                return false;
            }
        }), "[null] " + FactoryGraphicSwt.ERROR_IMAGE_READING);
    }

    /**
     * Test the save image exception case.
     */
    @Test
    public void testSaveImageException()
    {
        assertThrows(() -> Graphics.saveImage(new ImageBufferMock(16, 32)
        {
            @Override
            public <T> T getSurface()
            {
                return (T) ToolsSwt.createImage(100, 100, SWT.TRANSPARENCY_NONE);
            }
        }, new Media()
        {
            @Override
            public String getName()
            {
                return null;
            }

            @Override
            public String getPath()
            {
                return null;
            }

            @Override
            public String getParentPath()
            {
                return null;
            }

            @Override
            public OutputStream getOutputStream()
            {
                return new OutputStream()
                {
                    @Override
                    public void write(int b) throws IOException
                    {
                        throw new IOException();
                    }
                };
            }

            @Override
            public InputStream getInputStream()
            {
                return null;
            }

            @Override
            public File getFile()
            {
                return null;
            }

            @Override
            public Collection<Media> getMedias()
            {
                return null;
            }

            @Override
            public boolean exists()
            {
                return false;
            }
        }), "[null] " + FactoryGraphicSwt.ERROR_IMAGE_SAVE);
    }
}
