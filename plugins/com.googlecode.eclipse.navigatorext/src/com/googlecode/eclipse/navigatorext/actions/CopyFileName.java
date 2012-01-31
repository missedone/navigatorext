package com.googlecode.eclipse.navigatorext.actions;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author $Author$
 * @version $Revision$
 * @since
 */
public class CopyFileName extends BaseNavigateAction {

    public void run(IAction action) {
        if (selections.size() == 0) {
            MessageDialog.openInformation(new Shell(), "Navigator Extension", "Unable to get file info "
                    + selectedClass.getName());
            return;
        }
        Object selectObj = selections.get(0);
        File file = null;
        if (selectObj instanceof IResource) {
            file = new File(((IResource) selectObj).getLocation().toOSString());
        } else if (selectObj instanceof File) {
            file = (File) selectObj;
        }

        String res = file.getName();
        if (file.isDirectory())
            res = res + "\\";

        Object[] data = new Object[] { res };
        Transfer[] transfer = new Transfer[] { TextTransfer.getInstance() };
        new Clipboard(Display.getCurrent()).setContents(data, transfer);
    }
}
