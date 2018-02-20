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
package com.b3dgs.lionengine.editor.project;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.swt.widgets.Tree;

import com.b3dgs.lionengine.LionEngineException;

/**
 * File modification watcher for project tree.
 */
public final class FolderModificationWatcher
{
    /** Default check time in milliseconds. */
    static final int CHECK_TIME = 1000;
    /** Watcher already started error. */
    private static final String ERROR_STARTED = "Watcher already started !";

    /** Thread executor service. */
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    /** Future task. */
    private Future<?> future;
    /** Started flag. */
    private boolean started;

    /**
     * Create the watcher.
     */
    public FolderModificationWatcher()
    {
        super();
    }

    /**
     * Listen to directory modification.
     * 
     * @param project The project to watch.
     * @param tree The tree viewer reference.
     * @param creator The creator reference.
     * @throws LionEngineException If already started.
     */
    public synchronized void start(Project project, Tree tree, ProjectTreeCreator creator)
    {
        if (started)
        {
            throw new LionEngineException(ERROR_STARTED);
        }
        future = executor.submit(new Watcher(project, tree, creator));
        started = true;
    }

    /**
     * Stop the watcher.
     */
    public synchronized void stop()
    {
        if (future != null)
        {
            future.cancel(true);
        }
        future = null;
        started = false;
    }

    /**
     * Terminate definitely the watcher.
     */
    public synchronized void terminate()
    {
        if (started)
        {
            stop();
        }
        executor.shutdownNow();
    }
}
