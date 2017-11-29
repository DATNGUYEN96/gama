/*********************************************************************************************
 *
 * 'VirtualContent.java, in plugin ummisco.gama.ui.navigator, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.navigator.contents;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import gnu.trove.map.hash.TIntObjectHashMap;
import ummisco.gama.ui.resources.GamaFonts;
import ummisco.gama.ui.resources.GamaIcons;

public abstract class VirtualContent {

	public static enum VirtualContentType {
		ROOT, VIRTUAL_FOLDER, PROJECT, FOLDER, FILE, FILE_REFERENCE, CATEGORY, GAML_ELEMENT
	}

	public static ILabelProvider DEFAULT_LABEL_PROVIDER = WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider();

	public static final TIntObjectHashMap<ImageDescriptor> DESCRIPTORS = new TIntObjectHashMap<ImageDescriptor>() {
		{

			put(CLOSED, GamaIcons.create("overlay.closed2").descriptor());
			put(NO_PROBLEM, GamaIcons.create("overlay.ok2").descriptor());
			put(IMarker.SEVERITY_INFO, GamaIcons.create("overlay.ok2").descriptor());
			put(IMarker.SEVERITY_WARNING, GamaIcons.create("overlay.warning2").descriptor());
			put(IMarker.SEVERITY_ERROR, GamaIcons.create("overlay.error2").descriptor());
		}
	};

	public static final int NO_PROBLEM = -1;
	public static final int CLOSED = -2;
	public static WrappedResource<?>[] EMPTY = new WrappedResource<?>[0];
	public static WrappedProject[] EMPTY_PROJECTS = new WrappedProject[0];

	private final Object root;
	private final String name;

	public VirtualContent(final Object root, final String name) {
		this.root = root;
		this.name = name;
	}

	public ResourceManager getMapper() {
		return NavigatorRoot.INSTANCE.mapper;
	}

	public abstract VirtualContentType getType();

	/**
	 * Should both perform something and answer whether or not it has performed it, so that the navigator knows whether
	 * it should handle double-clicks itself
	 * 
	 * @return
	 */
	public boolean handleDoubleClick() {
		return false;
	}

	public String getName() {
		return name;
	}

	public Object getParent() {
		return root;
	}

	public abstract boolean hasChildren();

	public abstract Object[] getNavigatorChildren();

	public abstract Image getImage();

	public abstract Color getColor();

	public abstract void getSuffix(StringBuilder sb);

	public Font getFont() {
		return GamaFonts.getNavigFolderFont(); // by default
	}

	public abstract int findMaxProblemSeverity();

	public abstract ImageDescriptor getOverlay();

	public TopLevelFolder getTopLevelFolder() {
		final Object p = getParent();
		if (p instanceof VirtualContent)
			return ((VirtualContent) p).getTopLevelFolder();
		return null;
	}

}
