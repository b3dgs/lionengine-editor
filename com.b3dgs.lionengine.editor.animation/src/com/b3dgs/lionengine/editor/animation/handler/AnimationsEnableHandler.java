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
package com.b3dgs.lionengine.editor.animation.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Tree;

import com.b3dgs.lionengine.Animation;
import com.b3dgs.lionengine.editor.animation.properties.PropertiesAnimation;
import com.b3dgs.lionengine.editor.properties.PropertiesPart;
import com.b3dgs.lionengine.editor.utility.UtilPart;
import com.b3dgs.lionengine.game.AnimationConfig;
import com.b3dgs.lionengine.game.Configurer;

/**
 * Enable animations handler.
 */
public final class AnimationsEnableHandler
{
    /**
     * Create handler.
     */
    public AnimationsEnableHandler()
    {
        super();
    }

    /**
     * Execute the handler.
     */
    @Execute
    public void execute()
    {
        final PropertiesPart part = UtilPart.getPart(PropertiesPart.ID, PropertiesPart.class);
        PropertiesAnimation.createAttributeAnimations(part.getTree());

        final Tree properties = part.getTree();
        final Configurer configurer = (Configurer) properties.getData();
        final int min = Animation.MINIMUM_FRAME;
        final Animation animation = new Animation(Animation.DEFAULT_NAME, min, min + 1, 0.1, false, false);
        AnimationConfig.exports(configurer.getRoot(), animation);

        configurer.save();
        part.setInput(properties, configurer);
    }
}
