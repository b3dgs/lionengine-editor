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
package com.b3dgs.lionengine.editor.formula.editor;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.editor.project.ProjectModel;

/**
 * Edit selected formula file.
 */
public final class FormulasEditHandler
{
    /**
     * Execute the handler.
     * 
     * @param parent The shell parent.
     */
    public static void executeHandler(Shell parent)
    {
        final Media selection = ProjectModel.INSTANCE.getSelection();
        final FormulasEditDialog dialog = new FormulasEditDialog(parent, selection);
        dialog.open();
    }

    /**
     * Create handler.
     */
    public FormulasEditHandler()
    {
        super();
    }

    /**
     * Execute the handler.
     * 
     * @param parent The shell parent.
     */
    @Execute
    public void execute(Shell parent)
    {
        executeHandler(parent);
    }
}
