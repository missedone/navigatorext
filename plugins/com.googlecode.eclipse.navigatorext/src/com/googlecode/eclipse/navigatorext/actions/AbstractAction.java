/*******************************************************************************
 * Copyright (c) 2004 Andrei Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/bsd-license.php
 * Contributor:  Andrei Loskutov - initial API and implementation
 *******************************************************************************/
package com.googlecode.eclipse.navigatorext.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * 
 */
public abstract class AbstractAction implements IActionDelegate, IWorkbenchWindowActionDelegate {

    protected IEditorPart editorPart;
    protected IViewPart viewPart;
    protected IFile file;
    protected IWorkbenchWindow window;
    protected ISelection selection;

    public AbstractAction() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        if (action == null) {
            return;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose() {
        editorPart = null;
        viewPart = null;
        window = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
     */
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
     * org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }

    /**
     * @return could return null, if we do not have associated file and operating on editor inputs instead
     */
    public IFile getFile() {
        IFile myFile = file;
        if (myFile == null && getEditorPart() != null) {
            myFile = getFile(getEditorPart().getEditorInput());
        }
        return myFile;
    }

    /**
     * @param file to perform operation on
     */
    public void setFile(IFile file) {
        this.file = file;
    }

    /**
     * @return may be null if this action is not yet initialized
     */
    public IWorkbenchWindow getWindow() {
        return window;
    }

    /**
     * @return the editorPart
     */
    public IEditorPart getEditorPart() {
        return editorPart;
    }

    /**
     * @param editorPart the editorPart to set
     */
    public void setEditorPart(IEditorPart editorPart) {
        this.editorPart = editorPart;
    }

    /**
     * @return the viewPart
     */
    public IViewPart getViewPart() {
        return viewPart;
    }

    /**
     * @param viewPart the viewPart to set
     */
    public void setViewPart(IViewPart viewPart) {
        this.viewPart = viewPart;
    }

    /**
     * @return may return null
     */
    public IFile getFile(IEditorInput input) {
        if (input == null) {
            return null;
        }
        Object adapter = input.getAdapter(IFile.class);
        if (adapter instanceof IFile) {
            return (IFile) adapter;
        }
        adapter = getAdapter(IFile.class);
        if (adapter instanceof IFile) {
            return (IFile) adapter;
        }
        return null;
    }

    private Object getAdapter(Class clazz) {
        if (editorPart == null) {
            return null;
        }
        return editorPart.getAdapter(clazz);
    }
}
