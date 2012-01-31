package com.googlecode.eclipse.navigatorext.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.eclipse.navigatorext.Activator;

/**
 * ContextMenuPlugin preference page.
 */
public class ContextMenuPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    public static final String P_TARGET = "targetPreference";

    public ContextMenuPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Set up your file context menu application.");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        addField(new StringFieldEditor(P_TARGET, "&Target:", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }

}