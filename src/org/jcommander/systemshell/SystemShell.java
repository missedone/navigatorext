/*
 * Created on Feb 6, 2005
 */
package org.jcommander.systemshell;

import java.io.File;

/**
 * 
 * @author sky_halud
 */
public class SystemShell {
    private static final String SYSTEM_SHELL_DLL = "systemshell";

    static {
        System.loadLibrary(SYSTEM_SHELL_DLL);
    }

    /**
     * Displays the system shell's contextual menu for the specified file object. The menu is positioned relative to the
     * mouse cursor.
     * 
     * @param file
     */
    public static void showContextMenu(File[] files, int x, int y) {
        displayContextMenu(files, x, y);
    }

    public static native void displayContextMenu(File[] files, int x, int y);
}
