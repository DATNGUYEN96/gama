/*********************************************************************************************
 *
 * 'ExceptionMonitor.java, in plugin ummisco.gama.ui.viewers, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/

package ummisco.gama.ui.viewers.gis.geotools.control;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;

import ummisco.gama.ui.utils.WorkbenchHelper;

/**
 * Utility which enables exception messages to be displayed.
 *
 * @since 2.7
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public final class ExceptionMonitor {
	/**
	 * The creation of {@code ExceptionMonitor} class objects is forbidden.
	 */
	private ExceptionMonitor() {}

	/**
	 * Displays an error message for the specified exception. Note that this method can be called from any thread (not
	 * necessarily the <cite>Swing</cite> thread).
	 *
	 * @param parent
	 *            The parent {@link Shell}.
	 * @param exception
	 *            Exception which has been thrown and is to be reported to the user.
	 */
	public static void show(final Shell parent, final Throwable exception) {
		show(parent, exception, exception.getLocalizedMessage());
	}

	/**
	 * Displays an error message for the specified exception. Note that this method can be called from any thread (not
	 * necessarily the <cite>Swing</cite> thread).
	 *
	 * @param parent
	 *            The parent {@link Shell}.
	 * @param exception
	 *            Exception which has been thrown and is to be reported to the user.
	 * @param message
	 *            Message to display.
	 */
	@SuppressWarnings ("nls")
	public static void show(final Shell parent, final Throwable exception, final String message) {
		if (WorkbenchHelper.getDisplay() != null) {
			final Status status = new Status(IStatus.ERROR, "My Plug-in ID", 0, "Status Error Message", exception);
			ErrorDialog.openError(parent, "", message, status);
		} else {
			WorkbenchHelper.asyncRun(() -> {
				final Status status = new Status(IStatus.ERROR, "My Plug-in ID", 0, "Status Error Message", exception);
				ErrorDialog.openError(parent, "", message, status);
			});
		}
	}

}
