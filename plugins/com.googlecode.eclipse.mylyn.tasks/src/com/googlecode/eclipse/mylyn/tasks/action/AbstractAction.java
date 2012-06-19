/*******************************************************************************
 * Copyright (c) 2004 Andrei Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/bsd-license.php
 * Contributor:  Andrei Loskutov - initial API and implementation
 *******************************************************************************/
package com.googlecode.eclipse.mylyn.tasks.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


/**
 * @author Andrei
 */
public abstract class AbstractAction implements IActionDelegate, IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    public AbstractAction() {
        super();
    }

    public void run(IAction action) {
        if(action == null){
            return;
        }
    }

    public void dispose() {
        window = null;
    }

    public void init(IWorkbenchWindow window1) {
        window = window1;
    }

    public void selectionChanged(IAction action, ISelection selection) {
        // noop
    }

    /**
     * @return may be null if this action is not yet initialized
     */
    public IWorkbenchWindow getWindow(){
        return window;
    }
}
