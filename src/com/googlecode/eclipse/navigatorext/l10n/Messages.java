/**
 * Copyright 2004 - 2007 Blue Bamboo International Inc. 
 *           All rights reserved.
 *
 *
 * Blue Bamboo PROPRIETARY/CONFIDENTIAL.
 *
 */

/** 
 * File name:            Messages.java
 * 
 * Originally developed: Nick.Tan
 *
 * Create date :         2008-4-11
 * 
 * Description:          The demo of the header on evey java code
 *                       These header could be configured in the Eclipse
 *                       The Eclipse template will come out next. 
 * 
 * Version:              0.1
 * 
 * Contributors:         
 * 
 * Modifications: 
 * name          version           reasons
 * 
 * $Log:$
 */
package com.googlecode.eclipse.navigatorext.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * 
 */
public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.googlecode.eclipse.navigatorext.l10n.Messages";
    static {
        initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String Dialog_title_err;
    public static String Dialog_msg_err;
    public static String Dialog_msg_fileIsReadOnly;
    public static String Dialog_msg_continueOperation;
}
