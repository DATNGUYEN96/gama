/*********************************************************************************************
 *
 * 'JParameterListWizard.java, in plugin ummisco.gama.ui.viewers, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.viewers.gis.geotools.control;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.geotools.data.DataUtilities;
import org.geotools.data.Parameter;

/**
 * The parameter list wizard.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public class JParameterListWizard extends Wizard {

	private final Map<String, Object> connectionParameters;

	private final JParameterListPage userPage;
	private JParameterListPage advancedPage;

	/**
	 * Constructor.
	 *
	 * @param title
	 *            title for the dialog
	 * @param description
	 *            brief description to be displayed on the page
	 * @param contents
	 *            a {@code List} of {@code Parameter} objects defining the data
	 *            being requested
	 */
	public JParameterListWizard(final String title, final String description, final List<Parameter<?>> contents) {
		this(title, description, contents, new HashMap<String, Object>());
	}

	public JParameterListWizard(final String title, final String description, final List<Parameter<?>> contents,
			final Map<String, Object> connectionParams) {

		this.connectionParameters = connectionParams == null ? new HashMap<String, Object>() : connectionParams;
		fillInDefaults(contents, this.connectionParameters);

		final List<Parameter<?>> userContents = contentsForLevel(contents, "user");

		userPage = new JParameterListPage(title, description, userContents, connectionParameters);

		final List<Parameter<?>> advancedContents = contentsForLevel(contents, "advanced");

		if (advancedContents.size() > 0) {
			advancedPage = new JParameterListPage(title, description, advancedContents, connectionParameters);
		}

	}

	@Override
	public void addPages() {
		super.addPages();
		if (userPage != null)
			addPage(userPage);
		if (advancedPage != null)
			addPage(advancedPage);
	}

	@Override
	public boolean performFinish() {
		return false;
	}

	/**
	 * Method used to fill in any required "programming" level defaults such as
	 * dbtype.
	 * 
	 * @param contents
	 * @param connectionParams
	 *            a {@code Map} of initial parameter values
	 */
	private void fillInDefaults(final List<Parameter<?>> contents, final Map<String, Object> connectionParams) {
		if (connectionParams == null)
			return;

		for (final Parameter<?> param : contents) {
			if (param.required && "program".equals(param.getLevel())) {
				if (!connectionParams.containsKey(param.key)) {
					connectionParams.put(param.key, param.sample);
				}
			}
		}
	}

	List<Parameter<?>> contentsForLevel(final List<Parameter<?>> contents, String level) {
		final List<Parameter<?>> list = new ArrayList<Parameter<?>>();
		if (level == null) {
			level = "user";
		}
		if (contents != null) {
			for (final Parameter<?> param : contents) {

				String check = param.metadata == null ? "user" : (String) param.metadata.get(Parameter.LEVEL);
				if (check == null) {
					check = "user";
				}
				if (level.equals(check)) {
					// we are good this is the one we want
					list.add(param);
				}
			}
		}
		return list;
	}

	/**
	 * Retrieve the connection parameters entered
	 *
	 * @return the {@code Map} of connection parameters
	 */
	public Map<String, Object> getConnectionParameters() {
		return connectionParameters;
	}

	/**
	 * Helper method that returns the "url" element of the connection parameters
	 * as a File, if present. Equivalent to:
	 * 
	 * <pre>
	 * <code>
	 *     URL url = (URL) myWizard.getConnectionParameters().get("url");
	 *     File file = DataUtilities.urlToFile(url);
	 * </code>
	 * </pre>
	 *
	 * @return url parameter as a File, or null if not applicable
	 */
	public File getFile() {
		final URL url = (URL) connectionParameters.get("url");
		return DataUtilities.urlToFile(url);
	}

}
