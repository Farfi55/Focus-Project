package mafiadelprimobanco.focusproject.handler;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import mafiadelprimobanco.focusproject.handler.SettingsHandler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Localization
{
	/**
	 * the current selected Locale.
	 */
	private static final ObjectProperty<Locale> locale;

	static
	{
		locale = new SimpleObjectProperty<>(getDefaultLocale());
		locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
		SettingsHandler.getInstance().getSettings().language.addListener((observable, oldValue, newValue) -> {
			setLocale(SettingsHandler.getInstance().getSettings().language.get().language);
		});
	}


	/**
	 * get the supported Locales.
	 *
	 * @return List of Locale objects.
	 */
	public static List<Locale> getSupportedLocales()
	{
		return new ArrayList<>(Arrays.asList(Locale.ITALIAN, Locale.ENGLISH));
	}

	/**
	 * get the default locale. This is the systems default if contained in the supported locales, english otherwise.
	 *
	 * @return
	 */
	public static Locale getDefaultLocale()
	{
		Locale sysDefault = Locale.getDefault();
		return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.ENGLISH;
	}

	public static Locale getLocale()
	{
		return locale.get();
	}

	public static void setLocale(Locale locale)
	{
		localeProperty().set(locale);
		Locale.setDefault(locale);
	}

	public static ObjectProperty<Locale> localeProperty()
	{
		return locale;
	}

	/**
	 * gets the string with the given key from the resource bundle for the current locale and uses it as first argument
	 * to MessageFormat.format, passing in the optional args and returning the result.
	 *
	 * @param key  message key
	 * @param args optional arguments for the message
	 * @return localized formatted string
	 */
	public static String get(final String key, final Object... args)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("lang", getLocale());
		return MessageFormat.format(bundle.getString(key), args);
	}

	public static String get(final String key, Locale local, final Object... args)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("lang", local);
		return MessageFormat.format(bundle.getString(key), args);
	}

	/**
	 * creates a String binding to a localized String for the given message bundle key
	 *
	 * @param key key
	 * @return String binding
	 */
	public static StringBinding createStringBinding(final String key, Object... args)
	{
		return Bindings.createStringBinding(() -> get(key, args), locale);
	}


//	public static void setMFXComboboxFloatingText(final MFXComboBox<String> comboBox, final String key, final Object... args)
//	{
//		comboBox.floatingTextProperty().bind(createStringBinding(key, args));
//	}
}
