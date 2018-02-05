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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Config;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.FactoryGraphic;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.ImageBuffer;
import com.b3dgs.lionengine.graphic.Screen;
import com.b3dgs.lionengine.graphic.Text;
import com.b3dgs.lionengine.graphic.TextStyle;
import com.b3dgs.lionengine.graphic.Transform;
import com.b3dgs.lionengine.graphic.Transparency;

/**
 * Graphic factory implementation.
 */
public final class FactoryGraphicSwt implements FactoryGraphic
{
    /** Reading image message. */
    private static final String ERROR_IMAGE_READING = "Error on reading image !";
    /** Save image message. */
    private static final String ERROR_IMAGE_SAVE = "Unable to save image: ";

    /**
     * Constructor.
     */
    public FactoryGraphicSwt()
    {
        super();
    }

    /*
     * FactoryGraphic
     */

    @Override
    public Screen createScreen(Config config)
    {
        if (config.isWindowed())
        {
            return new ScreenWindowedSwt(config);
        }
        return new ScreenFullSwt(config);
    }

    @Override
    public Graphic createGraphic()
    {
        return new GraphicSwt();
    }

    @Override
    public Transform createTransform()
    {
        return new TransformSwt();
    }

    @Override
    public Text createText(String fontName, int size, TextStyle style)
    {
        return new TextSwt(ToolsSwt.getDisplay(), fontName, size, style);
    }

    @Override
    public ImageBuffer createImageBuffer(int width, int height)
    {
        Check.superiorOrEqual(width, 0);
        Check.superiorOrEqual(height, 0);

        final Image image = ToolsSwt.createImage(width, height, ToolsSwt.getTransparency(Transparency.OPAQUE));
        final ImageBufferSwt buffer = new ImageBufferSwt(image);

        final Graphic g = buffer.createGraphic();
        g.setColor(ColorRgba.BLACK);
        g.drawRect(0, 0, width, height, true);
        g.dispose();

        return buffer;
    }

    @Override
    public ImageBuffer createImageBuffer(int width, int height, ColorRgba transparency)
    {
        Check.superiorOrEqual(width, 0);
        Check.superiorOrEqual(height, 0);

        final Image image = ToolsSwt.createImage(width, height, transparency);
        return new ImageBufferSwt(image);
    }

    @Override
    public ImageBuffer getImageBuffer(Media media)
    {
        try (InputStream input = media.getInputStream())
        {
            return new ImageBufferSwt(ToolsSwt.getDisplay(), ToolsSwt.getImageData(input));
        }
        catch (final SWTException | IOException exception)
        {
            throw new LionEngineException(exception, ERROR_IMAGE_READING);
        }
    }

    @Override
    public ImageBuffer getImageBuffer(ImageBuffer image)
    {
        return new ImageBufferSwt(ToolsSwt.getImage((Image) image.getSurface()));
    }

    @Override
    public ImageBuffer applyMask(ImageBuffer image, ColorRgba maskColor)
    {
        return new ImageBufferSwt(ToolsSwt.applyMask((Image) image.getSurface(), maskColor.getRgba()));
    }

    @Override
    public ImageBuffer[] splitImage(ImageBuffer image, int h, int v)
    {
        final Image[] images = ToolsSwt.splitImage((Image) image.getSurface(), h, v);
        final ImageBuffer[] imageBuffers = new ImageBuffer[images.length];
        for (int i = 0; i < imageBuffers.length; i++)
        {
            imageBuffers[i] = new ImageBufferSwt(images[i]);
        }
        return imageBuffers;
    }

    @Override
    public ImageBuffer rotate(ImageBuffer image, int angle)
    {
        return new ImageBufferSwt(ToolsSwt.rotate((Image) image.getSurface(), angle));
    }

    @Override
    public ImageBuffer resize(ImageBuffer image, int width, int height)
    {
        return new ImageBufferSwt(ToolsSwt.resize((Image) image.getSurface(), width, height));
    }

    @Override
    public ImageBuffer flipHorizontal(ImageBuffer image)
    {
        return new ImageBufferSwt(ToolsSwt.flipHorizontal((Image) image.getSurface()));
    }

    @Override
    public ImageBuffer flipVertical(ImageBuffer image)
    {
        return new ImageBufferSwt(ToolsSwt.flipVertical((Image) image.getSurface()));
    }

    @Override
    public void saveImage(ImageBuffer image, Media media)
    {
        try (OutputStream output = media.getOutputStream())
        {
            ToolsSwt.saveImage((Image) image.getSurface(), output);
        }
        catch (final SWTException | IOException exception)
        {
            throw new LionEngineException(exception, ERROR_IMAGE_SAVE);
        }
    }

    @Override
    public ImageBuffer getRasterBuffer(ImageBuffer image, int fr, int fg, int fb, int er, int eg, int eb, int ref)
    {
        return new ImageBufferSwt(ToolsSwt.getRasterBuffer((Image) image.getSurface(), fr, fg, fb, er, eg, eb, ref));
    }
}
