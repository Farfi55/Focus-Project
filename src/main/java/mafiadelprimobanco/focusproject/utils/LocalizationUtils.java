package mafiadelprimobanco.focusproject.utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import mafiadelprimobanco.focusproject.Localization;

public class LocalizationUtils
{
	public static void bindLabelText(final Labeled label, final String key, final Object... args)
	{
		label.textProperty().bind(Localization.createStringBinding(key, args));
	}

	public static void bindButtonText(final Button button, final String key, final Object... args)
	{
		button.textProperty().bind(Localization.createStringBinding(key, args));
	}
}
