/**
 * Copyright 2004 - 2007 Blue Bamboo International Inc. 
 *           All rights reserved.
 *
 *
 * Blue Bamboo PROPRIETARY/CONFIDENTIAL.
 *
 */

package com.googlecode.eclipse.mylyn.tasks.action;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.mylyn.tasks.core.TaskMapping;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.ui.PlatformUI;

public class ReportAsBugAction extends AbstractOpenAction {

    @Override
    protected void handleAction(IDocument doc, ISelectionProvider selectionProvider) {
        ISelection selection = selectionProvider.getSelection();
        if (selection instanceof ITextSelection) {
            final String selectionText = ((ITextSelection) selection).getText();
            if (selectionText.length() == 0) {
                return;
            }

            TaskMapping mapping = new TaskMapping() {
                @Override
                public String getSummary() {
                    int lfIdx = selectionText.indexOf("\r\n");
                    if (lfIdx > 0) {
                        return selectionText.substring(0, lfIdx);
                    }
                    return selectionText;
                }

                @Override
                public String getDescription() {
                    return selectionText;
                }
            };

            TasksUiUtil.openNewTaskEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), mapping,
                    null);
        }
    }

}
