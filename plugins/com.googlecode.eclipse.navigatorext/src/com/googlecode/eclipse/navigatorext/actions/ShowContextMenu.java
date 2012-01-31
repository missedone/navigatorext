/**
 * Copyright 2004 - 2010  Blue Bamboo International Inc. 
 *           All rights reserved.
 *
 *
 *  Blue Bamboo PROPRIETARY/CONFIDENTIAL.
 *
 */

package com.googlecode.eclipse.navigatorext.actions;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcommander.systemshell.SystemShell;

import com.googlecode.eclipse.navigatorext.Activator;

/**
 * 
 */
public class ShowContextMenu extends AbstractHandler implements IViewActionDelegate, IEditorActionDelegate,
        IObjectActionDelegate {

    private IViewPart viewPart;
    private IEditorPart editorPart;
    private ISelection selection;
    private List<File> fileList = new ArrayList<File>();

    public Object execute(ExecutionEvent evt) throws ExecutionException {
        selection = HandlerUtil.getCurrentSelection(evt);
        editorPart = HandlerUtil.getActiveEditor(evt);
        IWorkbenchPart part = HandlerUtil.getActivePart(evt);
        if (part instanceof IViewPart) {
            viewPart = (IViewPart) part;
        }
        run(null);
        return null;
    }

    public void run(IAction action) {
        fileList.clear();
        if (viewPart != null && selection instanceof IStructuredSelection) {
            IStructuredSelection ssl = (IStructuredSelection) selection;
            for (Iterator it = ssl.iterator(); it.hasNext();) {
                IAdaptable adaptable = (IAdaptable) it.next();
                File file = null;
                if (adaptable instanceof IResource) {
                    file = new File(((IResource) adaptable).getLocation().toOSString());
                } else if (adaptable instanceof PackageFragment
                        && ((PackageFragment) adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot) {
                    file = getJarFile(((PackageFragment) adaptable).getPackageFragmentRoot());
                } else if (adaptable instanceof JarPackageFragmentRoot) {
                    file = getJarFile(adaptable);
                } else {
                    file = new File(((IResource) adaptable.getAdapter(IResource.class)).getLocation().toOSString());
                }
                fileList.add(file);
            }
        } else if (editorPart != null) {
            File file = new File(getFile(editorPart.getEditorInput()).getLocation().toOSString());
            fileList.add(file);
        }
        if (fileList.size() > 0) {
            showContextMenu((File[]) fileList.toArray(new File[fileList.size()]));
        }
    }

    private void showContextMenu(File[] files) {
        String os = System.getProperty("os.name");
        if ("Windows Vista".equalsIgnoreCase(os) || "Windows 7".equalsIgnoreCase(os)) {
            String target = Activator.getDefault().getTarget();
            if (target.indexOf("{0}") == -1) {
                target = target.trim() + " {0}";
            }
            try {
                Runtime.getRuntime().exec(MessageFormat.format(target, (Object[]) files));
            } catch (IOException e) {
                Activator.logError(e.getMessage(), e);
            }
        } else {
            SystemShell.showContextMenu(files, -1, -1);
        }
    }

    public void init(IViewPart view) {
        this.viewPart = view;
        this.editorPart = null;
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        this.editorPart = targetEditor;
        this.viewPart = null;
    }

    protected File getJarFile(IAdaptable adaptable) {
        JarPackageFragmentRoot jpfr = (JarPackageFragmentRoot) adaptable;
        File selected = (File) jpfr.getPath().makeAbsolute().toFile();
        if (!((File) selected).exists()) {
            File projectFile = new File(jpfr.getJavaProject().getProject().getLocation().toOSString());
            selected = new File(projectFile.getParent() + selected.toString());
        }
        return selected;
    }

    private IFile getFile(IEditorInput input) {
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

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        if (targetPart instanceof IViewPart) {
            viewPart = (IViewPart) targetPart;
            editorPart = null;
        } else if (targetPart instanceof IEditorPart) {
            editorPart = (IEditorPart) targetPart;
            viewPart = null;
        }
    }

}
