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
package com.b3dgs.lionengine.editor.animation.properties;

import org.eclipse.core.expressions.PropertyTester;

import com.b3dgs.lionengine.editor.properties.PropertiesModel;
import com.b3dgs.lionengine.game.AnimationConfig;
import com.b3dgs.lionengine.game.FramesConfig;

/**
 * Test the properties node existence.
 */
public final class PropertiesAnimationTester extends PropertyTester
{
    /** Can enable animations. */
    private static final String PROPERTY_ANIMATIONS_ENABLE = "enableAnimations";
    /** Can edit animations. */
    private static final String PROPERTY_ANIMATIONS_EDIT = "editAnimations";
    /** Can disable animations. */
    private static final String PROPERTY_ANIMATIONS_DISABLE = "disableAnimations";

    /**
     * Check result depending of selection.
     * 
     * @param model The properties model.
     * @param data The selection reference.
     * @param property The property to check.
     * @return <code>true</code> if valid, <code>false</code> else.
     */
    private static boolean check(PropertiesModel model, Object data, String property)
    {
        final boolean result;
        if (PROPERTY_ANIMATIONS_ENABLE.equals(property) && model.hasProperty(FramesConfig.NODE_FRAMES))
        {
            result = !model.hasProperty(AnimationConfig.ANIMATION);
        }
        else if (AnimationConfig.ANIMATION.equals(data)
                 && (PROPERTY_ANIMATIONS_EDIT.equals(property) || PROPERTY_ANIMATIONS_DISABLE.equals(property)))
        {
            result = model.hasProperty(AnimationConfig.ANIMATION);
        }
        else
        {
            result = false;
        }
        return result;
    }

    /**
     * Create tester.
     */
    public PropertiesAnimationTester()
    {
        super();
    }

    /*
     * PropertyTester
     */

    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        final PropertiesModel model = PropertiesModel.INSTANCE;
        if (!model.isEmpty())
        {
            final Object data = model.getSelectedData();
            return check(model, data, property);
        }
        return false;
    }
}
