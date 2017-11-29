/*********************************************************************************************
 *
 * 'RefreshHandler.java, in plugin ummisco.gama.ui.navigator, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

import msi.gama.common.interfaces.IGui;
import ummisco.gama.ui.navigator.GamaNavigator;
import ummisco.gama.ui.utils.WorkbenchHelper;

public class RefreshHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		run((IResource) null);
		return null;
	}

	public static void run(final IResource resource) {
		final Display d = PlatformUI.getWorkbench().getDisplay();
		if (d.isDisposed())
			return;
		final UIJob job = new UIJob("Refreshing navigator") {

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor) {
				final IWorkbenchPage page = WorkbenchHelper.getPage();
				if (page == null)
					return Status.OK_STATUS;
				final IViewPart view = page.findView(IGui.NAVIGATOR_VIEW_ID);
				if (view == null) { return Status.OK_STATUS; }
				((GamaNavigator) view).safeRefresh(resource == null ? null : resource.getParent());
				if (resource != null)
					((GamaNavigator) view).selectReveal(new StructuredSelection(resource));
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

}
