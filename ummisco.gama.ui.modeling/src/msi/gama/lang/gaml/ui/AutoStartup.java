/*********************************************************************************************
 *
 * 'AutoStartup.java, in plugin ummisco.gama.ui.modeling, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.ui;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import msi.gama.common.preferences.GamaPreferences;
import msi.gama.common.preferences.IPreferenceChangeListener;
import msi.gama.common.preferences.Pref;
import msi.gama.lang.gaml.GamlRuntimeModule;
import msi.gama.lang.gaml.ui.editor.GamlEditorBindings;
import msi.gama.lang.gaml.ui.reference.GamlReferenceSearch;
import msi.gama.lang.gaml.ui.reference.OperatorsReferenceMenu;
import msi.gama.util.GamaColor;
import msi.gama.util.GamaFont;
import msi.gaml.types.IType;

public class AutoStartup implements IStartup {

	public static Pref<String> OPERATORS_MENU_SORT =
			GamaPreferences.create("pref_menu_operators_sort", "Sort operators menu by", "Category", IType.STRING)
					.among("Name", "Category").in(GamaPreferences.Interface.NAME, GamaPreferences.Interface.MENUS)
					.addChangeListener(new IPreferenceChangeListener<String>() {

						@Override
						public boolean beforeValueChange(final String newValue) {
							return true;
						}

						@Override
						public void afterValueChange(final String newValue) {
							OperatorsReferenceMenu.byName = newValue.equals("Name");
							// final GamlReferenceMenu menu = GamlReferenceTools.getInstance().getOperatorsMenu();
							// if (menu != null) {
							// menu.reset();
							// }
						}
					});
	public static final Pref<Boolean> CORE_CLOSE_CURLY =
			GamaPreferences.create("pref_editor_close_curly", "Close curly brackets ( { )", true, IType.BOOL)
					.in(GamaPreferences.Modeling.NAME, GamaPreferences.Modeling.TEXT);
	public static final Pref<Boolean> CORE_CLOSE_SQUARE =
			GamaPreferences.create("pref_editor_close_square", "Close square brackets ( [ )", true, IType.BOOL)
					.in(GamaPreferences.Modeling.NAME, GamaPreferences.Modeling.TEXT);
	public static final Pref<Boolean> CORE_CLOSE_PARENTHESES =
			GamaPreferences.create("pref_editor_close_parentheses", "Close parentheses", true, IType.BOOL)
					.in(GamaPreferences.Modeling.NAME, GamaPreferences.Modeling.TEXT);
	public static final Pref<Boolean> EDITOR_CLEAN_UP =
			GamaPreferences.create("pref_editor_save_format", "Apply formatting on save", false, IType.BOOL)
					.in(GamaPreferences.Modeling.NAME, GamaPreferences.Modeling.OPTIONS);
	public static final Pref<Boolean> EDITOR_SAVE = GamaPreferences
			.create("pref_editor_save_all", "Save files before lauching an experiment", true, IType.BOOL)
			.in(GamaPreferences.Modeling.NAME, GamaPreferences.Modeling.OPTIONS).activates("pref_editor_ask_save");
	public static final Pref<Boolean> EDITOR_SAVE_ASK =
			GamaPreferences.create("pref_editor_ask_save", "Ask before saving each file", false, IType.BOOL)
					.in(GamaPreferences.Modeling.NAME, GamaPreferences.Modeling.OPTIONS);
	public static final Pref<Boolean> EDITBOX_ENABLED =
			GamaPreferences.create("pref_editor_editbox_on", "Turn on colorization of code sections", false, IType.BOOL)
					.in(GamaPreferences.Modeling.NAME, GamaPreferences.Modeling.TEXT);
	public static final Pref<Boolean> EDITOR_SHOW_TOOLBAR =
			GamaPreferences.create("pref_editor_show_toolbar", "Show edition toolbar by default", true, IType.BOOL)
					.in(GamaPreferences.Modeling.NAME, GamaPreferences.Modeling.TEXT).hidden();
	static final Pref<GamaFont> EDITOR_BASE_FONT =
			GamaPreferences.create("pref_editor_font", "Font of editors", getDefaultFontData(), IType.FONT)
					.in(GamaPreferences.Modeling.NAME, GamaPreferences.Modeling.TEXT)
					.addChangeListener(new IPreferenceChangeListener<GamaFont>() {

						@Override
						public boolean beforeValueChange(final GamaFont newValue) {
							return true;
						}

						@Override
						public void afterValueChange(final GamaFont font) {
							try {
								final FontData newValue = new FontData(font.getName(), font.getSize(), font.getStyle());
								PreferenceConverter.setValue(EditorsPlugin.getDefault().getPreferenceStore(),
										JFaceResources.TEXT_FONT, newValue);
							} catch (final Exception e) {}
						}
					});
	public static final Pref<GamaColor> EDITOR_BACKGROUND_COLOR =
			GamaPreferences
					.create("pref_editor_background_color", "Background color of editors", getDefaultBackground(),
							IType.COLOR)
					.in(GamaPreferences.Modeling.NAME, GamaPreferences.Modeling.TEXT)
					.addChangeListener(new IPreferenceChangeListener<GamaColor>() {

						@Override
						public boolean beforeValueChange(final GamaColor newValue) {
							return true;
						}

						@Override
						public void afterValueChange(final GamaColor c) {
							final RGB rgb = new RGB(c.getRed(), c.getGreen(), c.getBlue());
							PreferenceConverter.setValue(EditorsPlugin.getDefault().getPreferenceStore(),
									AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, rgb);
						}
					});

	private static GamaColor getDefaultBackground() {
		EditorsPlugin.getDefault().getPreferenceStore()
				.setValue(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT, false);
		final RGB rgb = PreferenceConverter.getColor(EditorsPlugin.getDefault().getPreferenceStore(),
				AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND);
		return new GamaColor(rgb.red, rgb.green, rgb.blue);
	}

	public static GamaFont getDefaultFontData() {
		final FontData fd = PreferenceConverter.getFontData(EditorsPlugin.getDefault().getPreferenceStore(),
				JFaceResources.TEXT_FONT);
		return new GamaFont(fd.getName(), fd.getStyle(), fd.getHeight());
	}

	@Override
	public void earlyStartup() {
		GamlRuntimeModule.staticInitialize();
		GamlEditorBindings.install();
		GamlReferenceSearch.install();
	}

}
