/*******************************************************************************
 * Copyright (c) 2004 Andrei Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/bsd-license.php
 * Contributor:  Andrei Loskutov - initial API and implementation
 *******************************************************************************/

package com.googlecode.eclipse.mylyn.tasks.action;

import java.lang.reflect.Method;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBookView;


/**
 * @author Andrei
 */
public abstract class AbstractOpenAction
    extends AbstractAction
    implements IViewActionDelegate {

    /** for actions with views */
    public static final String ACTION_REPORT_AS_BUG = "com.bluebamboo.mylyn.tasks.bugs.action.reportAsBug";  //$NON-NLS-1$
    private IViewPart viewPart;

    public AbstractOpenAction() {
        super();
    }

    public void run(IAction action) {
        super.run(action);
        if (action.getId().startsWith(ACTION_REPORT_AS_BUG)) {
            // view Action
            IViewPart vp = getViewPart();
            if (vp instanceof PageBookView) {
                doConsoleAction((PageBookView) vp);
                return;
            }
            TextViewer viewer = (TextViewer)vp.getAdapter(TextViewer.class);
            runWithViewer(viewer);
        }
    }

    /**
     * @param viewer
     */
    protected void runWithViewer(ITextViewer viewer) {
        if(viewer == null){
            return;
        }
        ISelectionProvider selProvider = viewer.getSelectionProvider();
        IDocument doc = viewer.getDocument();
        handleAction(doc, selProvider);
    }

    /**
     * @param cv
     */
    private void doConsoleAction(PageBookView cv){
        IPage page = cv.getCurrentPage();
        runWithViewer(getViewer(page));
    }

    protected ITextViewer getViewer(IPage page) {
        if(page instanceof TextConsolePage) {
            return ((TextConsolePage)page).getViewer();
        }
        try {
            /*
             * org.eclipse.cdt.internal.ui.buildconsole.BuildConsolePage does not
             * extend TextConsolePage, so we get access to the viewer with dirty tricks
             */
            Method method = page.getClass().getDeclaredMethod("getViewer", null);
            method.setAccessible(true);
            return (ITextViewer) method.invoke(page, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract void handleAction(
        IDocument doc,
        ISelectionProvider selectionProvider);

    public final void init(IViewPart view) {
        setViewPart(view);
    }

    private void setViewPart(IViewPart viewPart) {
        this.viewPart = viewPart;
    }

    protected IViewPart getViewPart() {
        return viewPart;
    }

    public void dispose() {
        viewPart = null;
        super.dispose();
    }
}