/*********************************************************************************************
 *
 * 'GamaBundleLoader.java, in plugin msi.gama.core, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gaml.compilation.kernel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import msi.gama.common.interfaces.ICreateDelegate;
import msi.gama.common.interfaces.IEventLayerDelegate;
import msi.gama.outputs.layers.EventLayerStatement;
import msi.gaml.compilation.IGamlAdditions;
import msi.gaml.expressions.IExpressionCompiler;
import msi.gaml.operators.Strings;
import msi.gaml.statements.CreateStatement;
import msi.gaml.types.Types;

/**
 * The class GamaBundleLoader.
 *
 * @author drogoul
 * @since 24 janv. 2012
 *
 */
public class GamaBundleLoader {

	private static final boolean DEBUG = true;

	public static void LOG(final String s) {
		if (DEBUG) {
			System.out.println(s);
		}
	}

	public static void ERROR(final Exception e) {
		ERRORED = true;
		LAST_EXCEPTION = e;
		e.printStackTrace();
	}

	public volatile static boolean LOADED = false;
	public volatile static boolean ERRORED = false;
	public volatile static Exception LAST_EXCEPTION = null;
	public static Bundle CORE_PLUGIN = Platform.getBundle("msi.gama.core");
	public static Bundle CORE_MODELS = Platform.getBundle("msi.gama.models");
	public static String CORE_TESTS = "tests";
	public static String CURRENT_PLUGIN_NAME = CORE_PLUGIN.getSymbolicName();
	public static String ADDITIONS = "gaml.additions.GamlAdditions";
	public static String GRAMMAR_EXTENSION_DEPRECATED = "gaml.grammar.addition";
	public static String GRAMMAR_EXTENSION = "gaml.extension";
	public static String CREATE_EXTENSION = "gama.create";
	public static String EVENT_LAYER_EXTENSION = "gama.event_layer";
	public static String MODELS_EXTENSION = "gama.models";
	public static String REGULAR_MODELS_LAYOUT = "models";
	public static String REGULAR_TESTS_LAYOUT = "tests";
	public static String GENERATED_TESTS_LAYOUT = "gaml/tests";
	public static String CONTENT_EXTENSION = "org.eclipse.core.contenttype.contentTypes";
	private static Set<Bundle> GAMA_PLUGINS = new HashSet<>();
	private static Multimap<Bundle, String> MODEL_PLUGINS = ArrayListMultimap.create();
	private static Multimap<Bundle, String> TEST_PLUGINS = ArrayListMultimap.create();
	public static Set<String> HANDLED_FILE_EXTENSIONS = new HashSet<>();

	public static void preBuildContributions() {
		final long start = System.currentTimeMillis();
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		// We retrieve the elements declared as extensions to the GAML language,
		// either with the new or the deprecated extension
		final Set<IExtension> extensions = new HashSet<>();
		try {
			IExtensionPoint p = registry.getExtensionPoint(GRAMMAR_EXTENSION);
			extensions.addAll(Arrays.asList(p.getExtensions()));
			p = registry.getExtensionPoint(GRAMMAR_EXTENSION_DEPRECATED);
			extensions.addAll(Arrays.asList(p.getExtensions()));
		} catch (final InvalidRegistryObjectException e) {
			ERROR(e);
		}

		// We retrieve their contributor plugin and add them to the
		// GAMA_PLUGINS. In addition, we verify if they declare a folder called
		// `models` or `tests` or if they have generated tests
		// TEST_PLUGINS.put(CORE_MODELS, REGULAR_TESTS_LAYOUT);
		MODEL_PLUGINS.put(CORE_MODELS, REGULAR_MODELS_LAYOUT);
		for (final IExtension e : extensions) {
			final IContributor plugin = e.getContributor();
			final Bundle bundle = Platform.getBundle(plugin.getName());

			GAMA_PLUGINS.add(bundle);
			if (bundle.getEntry(REGULAR_MODELS_LAYOUT) != null) {
				MODEL_PLUGINS.put(bundle, REGULAR_MODELS_LAYOUT);
			}
			if (bundle.getEntry(REGULAR_TESTS_LAYOUT) != null) {
				TEST_PLUGINS.put(bundle, REGULAR_TESTS_LAYOUT);
			}
			if (bundle.getEntry(GENERATED_TESTS_LAYOUT) != null) {
				TEST_PLUGINS.put(bundle, GENERATED_TESTS_LAYOUT);
			}
		}
		// LOG(">GAMA plugins with language additions: "
		// + StreamEx.of(GAMA_PLUGINS).map(e -> e.getSymbolicName()).toSet());
		// LOG(">GAMA plugins with models: " + StreamEx.of(MODEL_PLUGINS.keySet()).map(e ->
		// e.getSymbolicName()).toSet());
		// LOG(">GAMA plugins with tests: " + StreamEx.of(TEST_PLUGINS.keySet()).map(e -> e.getSymbolicName()).toSet());

		// We remove the core plugin, in order to build it first (important)
		GAMA_PLUGINS.remove(CORE_PLUGIN);
		try {
			preBuild(CORE_PLUGIN);
		} catch (final Exception e2) {
			LOG("Error in loading plugin " + CORE_PLUGIN.getSymbolicName());
			ERROR(e2);
			return;
		}
		// We then build the other extensions to the language
		for (final Bundle addition : GAMA_PLUGINS) {
			CURRENT_PLUGIN_NAME = addition.getSymbolicName();
			try {
				preBuild(addition);
			} catch (final Exception e1) {
				LOG("Error in loading plugin " + addition.getSymbolicName());
				ERROR(e1);
				return;
			}
		}
		CURRENT_PLUGIN_NAME = null;
		// We gather all the extensions to the `create` statement and add them
		// as delegates to CreateStatement. If an exception occurs, we discard it
		for (final IConfigurationElement e : registry.getConfigurationElementsFor(CREATE_EXTENSION)) {
			ICreateDelegate cd = null;
			try {
				// TODO Add the defining plug-in
				cd = (ICreateDelegate) e.createExecutableExtension("class");
				if (cd != null) {
					CreateStatement.addDelegate(cd);
				}
			} catch (final CoreException e1) {
				CreateStatement.removeDelegate(cd);
				ERROR(e1);
				return;
			}
		}

		// We gather all the extensions to the `create` statement and add them
		// as delegates to EventLayerStatement
		for (final IConfigurationElement e : registry.getConfigurationElementsFor(EVENT_LAYER_EXTENSION)) {
			try {
				// TODO Add the defining plug-in
				EventLayerStatement.addDelegate((IEventLayerDelegate) e.createExecutableExtension("class"));
			} catch (final CoreException e1) {
				ERROR(e1);
				return;
			}
		}

		// We gather all the GAMA_PLUGINS that explicitly declare models using
		// the non-default scheme (plugin > models ...).
		for (final IConfigurationElement e : registry.getConfigurationElementsFor(MODELS_EXTENSION)) {
			MODEL_PLUGINS.put(Platform.getBundle(e.getContributor().getName()), e.getAttribute("name"));
		}
		// CRUCIAL INITIALIZATIONS
		LOADED = true;
		GamaMetaModel.INSTANCE.build();
		Types.init();

		// We gather all the content types extensions defined in GAMA plugins
		// (not in the other ones)
		final IExtensionPoint contentType = registry.getExtensionPoint(CONTENT_EXTENSION);
		final Set<IExtension> contentExtensions = new HashSet<>();
		contentExtensions.addAll(Arrays.asList(contentType.getExtensions()));
		for (final IExtension ext : contentExtensions) {
			final IConfigurationElement[] configs = ext.getConfigurationElements();
			for (final IConfigurationElement config : configs) {
				final String s = config.getAttribute("file-extensions");
				if (s != null) {
					HANDLED_FILE_EXTENSIONS.addAll(Arrays.asList(s.split(",")));
				}
			}
		}

		// We reinit the type hierarchy to gather additional types
		Types.init();
		performStaticInitializations();
		//
		LOG(">GAMA total load time " + (System.currentTimeMillis() - start) + " ms.");
	}

	private static void performStaticInitializations() {
		//new Thread(() -> {
			// final long start = System.currentTimeMillis();
			IExpressionCompiler.OPERATORS.forEachValue(object -> {
				object.compact();
				return true;
			});
			IExpressionCompiler.OPERATORS.compact();
		//}).start();

	}

	@SuppressWarnings ("unchecked")
	public static void preBuild(final Bundle bundle) throws Exception {
		GamaClassLoader.getInstance().addBundle(bundle);

		final long start = System.currentTimeMillis();
		Class<IGamlAdditions> gamlAdditions = null;
		try {
			gamlAdditions = (Class<IGamlAdditions>) bundle.loadClass(ADDITIONS);
		} catch (final ClassNotFoundException e1) {
			LOG(">> Impossible to load additions from " + bundle.toString() + " because of " + e1);
			throw e1;
		}
		IGamlAdditions add = null;
		try {
			add = gamlAdditions.newInstance();
		} catch (final InstantiationException e) {
			LOG(">> Impossible to instantiate additions from " + bundle);
			throw e;
		} catch (final IllegalAccessException e) {
			LOG(">> Impossible to access additions from " + bundle);
			throw e;
		}
		try {
			add.initialize();
		} catch (final SecurityException e) {
			LOG(">> Impossible to instantiate additions from " + bundle);
			throw e;
		} catch (final NoSuchMethodException e) {
			LOG(">> Impossible to instantiate additions from " + bundle);
			throw e;
		}
		LOG(">GAMA plugin loaded in " + (System.currentTimeMillis() - start) + " ms: " + Strings.TAB + bundle);

	}

	/**
	 * The list of GAMA_PLUGINS declaring models, together with the inner path to the folder containing model projects
	 * 
	 * @return
	 */
	public static Multimap<Bundle, String> getPluginsWithModels() {
		return MODEL_PLUGINS;
	}

	public static Multimap<Bundle, String> getPluginsWithTests() {
		return TEST_PLUGINS;
	}

}
