package com.googlecode.eclipse.navigatorext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.googlecode.eclipse.navigatorext.l10n.Messages;
import com.googlecode.eclipse.navigatorext.preferences.ContextMenuPreferencePage;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "com.googlecode.eclipse.navigatorext";

    // The shared instance
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        initializeDefaults();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public static String getId() {
        return PLUGIN_ID;
    }

    /**
     * Returns the workspace instance.
     */
    public static Shell getShell() {
        return getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    public static void errorDialog(String message, Throwable error) {
        Shell shell = getShell();
        if (message == null) {
            message = Messages.Dialog_msg_err;
        }
        message = message + " " + error.getMessage();

        getDefault().getLog().log(new Status(IStatus.ERROR, getId(), IStatus.OK, message, error));

        MessageDialog.openError(shell, Messages.Dialog_title_err, message);
    }

    /**
     * @param error
     */
    public static void logError(String message, Throwable error) {
        if (message == null) {
            message = error.getMessage();
            if (message == null) {
                message = error.toString();
            }
        }
        getDefault().getLog().log(new Status(IStatus.ERROR, getId(), IStatus.OK, message, error));
    }

    public static void logInfo(String message) {
        getDefault().getLog().log(new Status(IStatus.INFO, getId(), IStatus.OK, message, null));
    }

    public static void errorDialog(String message) {
        Shell shell = getShell();
        MessageDialog.openError(shell, Messages.Dialog_title_err, message);
    }

    /**
     * Sets the default values of the preferences.
     */
    private void initializeDefaults() {
        String executable = "ShellContextMenu.exe";
        String params = " 10000 {0}";
        String shell = "";
        if (!System.getProperty("os.name").startsWith("Windows")) {
            executable = "scm";
            params = " {0}";
            shell = "bash "; // prefix with shell, otherwise we would need to make executable
        }

        String metadata = getStateLocation().toOSString();
        String file = metadata + System.getProperty("file.separator") + executable;
        String executableWithParams = shell + file + params;

        IPreferenceStore store = getPreferenceStore();
        store.setDefault(ContextMenuPreferencePage.P_TARGET, executableWithParams);

        // copy executable from plugin location (jar or directory depending on
        // how the plugin was installed)
        // to metadata directory (i.e. $HOME/.metadata/.plugins/contextmenu)

        File targetFile = new File(file);
        if (targetFile.exists()) {
            return; // already initialized
        }

        try {
            URL url = getDefault().getBundle().getEntry(executable);
            InputStream in = url.openConnection().getInputStream();
            FileOutputStream out = new FileOutputStream(targetFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
        } catch (IOException e) {
            String msg = "Unable to find " + file + ":\n"
                    + "Please enter manually in Window->Preferences->Context Menu";
            logError(msg, e);
            MessageDialog.openInformation(new Shell(), "ContextMenuPlugin", msg);
        }
    }

    /**
     * Return the target program configured in ContextMenuPreferencePage.
     * 
     * @return executable with parameters
     */
    public String getTarget() {
        return getPreferenceStore().getString(ContextMenuPreferencePage.P_TARGET);
    }

}
