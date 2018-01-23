/*********************************************************************************************
 *
 * 'WrappedFolder.java, in plugin ummisco.gama.ui.navigator, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.navigator.contents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import ummisco.gama.ui.resources.GamaColors.GamaUIColor;
import ummisco.gama.ui.resources.GamaFonts;
import ummisco.gama.ui.resources.GamaIcons;
import ummisco.gama.ui.resources.IGamaColors;

/**
 * Class ImportFolder.
 *
 * @author drogoul
 * @since 5 févr. 2015
 *
 */
public class Category extends VirtualContent<WrappedFile> {

	final Collection<String> fileNames;

	/**
	 * @param root
	 * @param name
	 */
	public Category(final WrappedFile root, final Collection<String> object, final String name) {
		super(root, name);
		fileNames = object;
	}

	/**
	 * Method hasChildren()
	 * 
	 * @see ummisco.gama.ui.navigator.contents.VirtualContent#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return !fileNames.isEmpty();
	}

	@Override
	public Font getFont() {
		return GamaFonts.getSmallFont(); // by default
	}

	@Override
	public WrappedFile getParent() {
		return super.getParent();
	}

	/**
	 * Method getNavigatorChildren()
	 * 
	 * @see ummisco.gama.ui.navigator.contents.VirtualContent#getNavigatorChildren()
	 */
	@Override
	public Object[] getNavigatorChildren() {
		if (fileNames.isEmpty()) { return EMPTY; }
		final List<LinkedFile> files = new ArrayList<>();
		final IFile file = getParent().getResource();
		final IPath filePath = file.getLocation();
		final IPath projectPath = file.getProject().getLocation();
		for (final String s : fileNames) {
			if (s.startsWith("http"))
				continue;
			IPath resPath = new Path(s);
			if (!resPath.isAbsolute()) {
				final URI fileURI = URI.createFileURI(filePath.toString());
				final URI resURI = URI.createURI(resPath.toString()).resolve(fileURI);
				final String fileString = resURI.toFileString();
				if (fileString != null)
					resPath = new Path(fileString).makeRelativeTo(projectPath);
			} else {
				resPath = resPath.makeRelativeTo(projectPath);
			}
			IFile newFile = null;
			try {
				newFile = file.getProject().getFile(resPath);
			} catch (final IllegalArgumentException e) {}
			if (newFile != null && newFile.exists()) {
				final LinkedFile proxy = new LinkedFile(this, newFile);
				files.add(proxy);
			}
		}
		return files.toArray();
	}

	/**
	 * Method getImage()
	 * 
	 * @see ummisco.gama.ui.navigator.contents.VirtualContent#getImage()
	 */
	@Override
	public Image getImage() {
		return GamaIcons.create("gaml/_" + getName().toLowerCase()).image();
	}

	/**
	 * Method getColor()
	 * 
	 * @see ummisco.gama.ui.navigator.contents.VirtualContent#getColor()
	 */
	@Override
	public Color getColor() {
		return IGamaColors.BLACK.color();
	}

	@Override
	public void getSuffix(final StringBuilder sb) {}

	@Override
	public int findMaxProblemSeverity() {
		return 0;
	}

	@Override
	public ImageDescriptor getOverlay() {
		return null;
	}

	@Override
	public VirtualContentType getType() {
		return VirtualContentType.CATEGORY;
	}

	@Override
	public String getStatusMessage() {
		return "Virtual Folder";
	}

	@Override
	public GamaUIColor getStatusColor() {
		return IGamaColors.GRAY_LABEL;
	}

	@Override
	public Image getStatusImage() {
		return getImage();
	}

}
