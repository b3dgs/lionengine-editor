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
package com.b3dgs.lionengine.editor;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.editor.world.WorldModel;
import com.b3dgs.lionengine.game.Configurer;
import com.b3dgs.lionengine.game.FramesConfig;
import com.b3dgs.lionengine.game.SizeConfig;
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
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.Sprite;
import com.b3dgs.lionengine.graphic.drawable.SpriteAnimated;

/**
 * Object representation of any user object. This allows to avoid constructor error, especially with features.
 */
public class ObjectRepresentation extends FeaturableModel
{
    /** Error animation. */
    private static final String ERROR_ANIMATION = "Unable to get animation data from: ";

    /**
     * Get sprite from setup.
     * 
     * @param setup The setup reference.
     * @param transformable The transformable reference.
     * @return The prepared sprite.
     */
    private static Sprite getSprite(Setup setup, Transformable transformable)
    {
        try
        {
            final SpriteAnimated sprite = getSprite(setup, setup.getSurface());
            sprite.prepare();
            if (!setup.hasNode(SizeConfig.NODE_SIZE))
            {
                transformable.setSize(sprite.getTileWidth(), sprite.getTileHeight());
            }
            sprite.setFrameOffsets((sprite.getTileWidth() - transformable.getWidth()) / 2, 0);
            return sprite;
        }
        catch (@SuppressWarnings("unused") final LionEngineException exception)
        {
            final ImageBuffer buffer = Graphics.createImageBuffer(16, 16);
            final Graphic g = buffer.createGraphic();
            g.setColor(ColorRgba.RED);
            g.drawRect(0, 0, buffer.getWidth(), buffer.getHeight(), true);
            g.dispose();

            final Sprite sprite = Drawable.loadSprite(buffer);
            sprite.prepare();
            if (!setup.hasNode(SizeConfig.NODE_SIZE))
            {
                transformable.setSize(sprite.getWidth(), sprite.getHeight());
            }

            return sprite;
        }
    }

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
    /** Media path. */
    private final Media media;

    /**
     * Create the object.
     * 
     * @param setup The setup reference.
     * @throws LionEngineException If error.
     */
    public ObjectRepresentation(Setup setup)
    {
        super(WorldModel.INSTANCE.getServices(), setup);

        media = setup.getMedia();

        final Transformable transformable = addFeatureAndGet(new TransformableModel(WorldModel.INSTANCE.getServices(),
                                                                                    setup));
        final Sprite surface = getSprite(setup, transformable);
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

    /*
     * FeaturableModel
     */

    @Override
    public boolean isLoadFeaturesEnabled()
    {
        return false;
    }

    @Override
    public Media getMedia()
    {
        return media;
    }
}
