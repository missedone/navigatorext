package com.googlecode.eclipse.navigatorext.actions;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 
 * @author $Author$
 * @version $Revision$
 * @since
 */
public abstract class BaseNavigateAction extends AbstractHandler implements IObjectActionDelegate {

    protected Vector selections = new Vector();

    @SuppressWarnings("unchecked")
    protected Class selectedClass = null;

    public Object execute(ExecutionEvent evt) throws ExecutionException {
        ISelection selection = HandlerUtil.getCurrentSelection(evt);
        selectionChanged((IAction) null, selection);

        run((IAction) null);

        return null;
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
        selections.removeAllElements();

        IAdaptable adaptable = null;
        if (selection instanceof IStructuredSelection) {
            Iterator it = ((IStructuredSelection) selection).iterator();
            while (it.hasNext()) {
                Object selectObj = null;
                adaptable = (IAdaptable) it.next();
                this.selectedClass = adaptable.getClass();
                if (adaptable instanceof IResource) {
                    selectObj = (IResource) adaptable;
                } else if (adaptable instanceof PackageFragment
                        && ((PackageFragment) adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot) {
                    selectObj = getJarFile(((PackageFragment) adaptable).getPackageFragmentRoot());
                } else if (adaptable instanceof JarPackageFragmentRoot) {
                    selectObj = getJarFile(adaptable);
                } else {
                    selectObj = (IResource) adaptable.getAdapter(IResource.class);
                }
                selections.add(selectObj);
            }
        }
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

}
