/**
 * Copyright 2004 - 2007 Blue Bamboo International Inc. 
 *           All rights reserved.
 *
 *
 * Blue Bamboo PROPRIETARY/CONFIDENTIAL.
 *
 */

/** 
 * File name:            ThreadDumpSection.java
 * 
 * Originally developed: Nick.Tan
 *
 * Create date :         2008-8-7
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
package com.googlecode.eclipse;

import java.io.PrintWriter;
import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.eclipse.ui.about.ISystemSummarySection;

/**
 * DOCME
 */
public class ThreadDumpSection implements ISystemSummarySection {

	public void write(PrintWriter w) {
		OperatingSystemMXBean osMXBean = ManagementFactory
				.getOperatingSystemMXBean();
		int availableProcessors = osMXBean.getAvailableProcessors();

		w.format("Available processors: %s\r\n", availableProcessors);

		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

		ThreadInfo[] threads = threadMXBean.getThreadInfo(threadMXBean
				.getAllThreadIds());
		for (ThreadInfo info : threads) {
			long threadId = info.getThreadId();
			String threadName = info.getThreadName();
			State threadState = info.getThreadState();

			long blockedCount = info.getBlockedCount();
			long waitedCount = info.getWaitedCount();

			w.format(
					"%s : %s  State: %s  Blocked: %s  Waited: %s\r\n", //
					threadId, threadName, threadState, blockedCount,
					waitedCount);

			long lockOwnerId = info.getLockOwnerId();
			if (lockOwnerId > -1) {
				String lockOwnerName = info.getLockOwnerName();
				String lockName = info.getLockName();
				w.format("    Lock: %s by %s\r\n", lockName, lockOwnerName);
			}

			for (StackTraceElement e : info.getStackTrace()) {
				int line = e.getLineNumber();
				String className = e.getClassName();
				String methodName = e.getMethodName();
				String fileName = e.getFileName();
				if (line < 0) {
					w
							.format("  %s.%s(%s)\r\n", className, methodName,
									fileName);
				}
				else {
					w.format("  %s.%s(%s:%s)\r\n", className, methodName,
							fileName, line);
				}
			}
			w.write("\r\n");
		}

		w.write("\r\n");
	}

}
