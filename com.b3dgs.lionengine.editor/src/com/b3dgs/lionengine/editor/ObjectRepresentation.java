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
package com.b3dgs.lionengine.editor;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.core.drawable.Drawable;
import com.b3dgs.lionengine.editor.world.WorldModel;
import com.b3dgs.lionengine.game.Configurer;
import com.b3dgs.lionengine.game.FramesConfig;
import com.b3dgs.lionengine.game.feature.Camera;
import com.b3dgs.lionengine.game.feature.DisplayableModel;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.RefreshableModel;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.geom.Rectangle;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.Graphics;
import com.b3dgs.lionengine.graphic.ImageBuffer;
import com.b3dgs.lionengine.graphic.Sprite;
import com.b3dgs.lionengine.graphic.SpriteAnimated;

/**
 * Object representation of any user object. This allows to avoid constructor error, especially with features.
 */
public class ObjectRepresentation extends FeaturableModel
{
    /** Error animation. */
    private static final String ERROR_ANIMATION = "Unable to get animation data from: ";

    /**
     * Get the sprite depending of the configuration.
     * 
     * @param configurer The configurer reference.
     * @param surface The surface reference.
     * @return The sprite instance.
     */
    private static SpriteAnimated getSprite(Configurer configurer, ImageBuffer surface)
    {
        try
        {
            final FramesConfig frames = FramesConfig.imports(configurer);
            return Drawable.loadSpriteAnimated(surface, frames.getHorizontal(), frames.getVertical());
        }
        catch (final LionEngineException exception)
        {
            Verbose.exception(exception, ERROR_ANIMATION, configurer.getMedia().getPath());
            return Drawable.loadSpriteAnimated(surface, 1, 1);
        }
    }

    /** Rectangle. */
    private final Rectangle rectangle = new Rectangle();
    /** Camera reference. */
    private final Camera camera = WorldModel.INSTANCE.getCamera();

    /**
     * Create the object.
     * 
     * @param setup The setup reference.
     * @throws LionEngineException If error.
     */
    public ObjectRepresentation(Setup setup)
    {
        super();

        final Transformable transformable = addFeatureAndGet(new TransformableModel(setup));
        final Sprite surface;
        if (setup.getSurface() != null)
        {
            final SpriteAnimated anim = getSprite(setup, setup.getSurface());
            anim.prepare();
            surface = anim;
            transformable.setSize(anim.getTileWidth(), anim.getTileHeight());
        }
        else
        {
            final ImageBuffer buffer = Graphics.createImageBuffer(16, 16);
            final Graphic g = buffer.createGraphic();
            g.setColor(ColorRgba.RED);
            g.drawRect(0, 0, buffer.getWidth(), buffer.getHeight(), true);
            g.dispose();

            surface = Drawable.loadSprite(buffer);
            surface.prepare();
            transformable.setSize(surface.getWidth(), surface.getHeight());
        }

        surface.setOrigin(Origin.BOTTOM_LEFT);

        addFeature(new RefreshableModel(extrp ->
        {
            rectangle.set(camera.getViewpointX(transformable.getX()),
                          camera.getViewpointY(transformable.getY()) - transformable.getHeight(),
                          transformable.getWidth(),
                          transformable.getHeight());
            surface.setLocation(camera, transformable);
        }));

        addFeature(new DisplayableModel(surface::render));
    }

    /**
     * Get the rectangle representation on screen.
     * 
     * @return The rectangle representation on screen.
     */
    public Rectangle getRectangle()
    {
        return rectangle;
    }
}
