package mafiadelprimobanco.focusproject.handler;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;
import mafiadelprimobanco.focusproject.utils.Theme;

import java.util.ArrayList;
import java.util.List;

public class StyleHandler
{
	private static final StyleHandler instance = new StyleHandler();

	public static StyleHandler getInstance()
	{
		return instance;
	}

	private final List<String> loadedStyles = new ArrayList<>();
	private final ObservableList<String> observableStyles = FXCollections.observableList(loadedStyles);

	private final SimpleObjectProperty<Theme> currentTheme = new SimpleObjectProperty<>();
	private final List<String> baseStyles = List.of("fonts", "style");


	private StyleHandler()
	{
		updateTheme();
		SettingsHandler.getInstance().getSettings().theme.addListener(observable -> updateTheme());
	}

	private void updateTheme()
	{
		currentTheme.setValue(SettingsHandler.getInstance().getSettings().theme.get());
		reloadStyles();
	}

	private void reloadStyles()
	{
		List<String> tmpStyles = new ArrayList<>();
		for (String style : baseStyles)
		{
			String resource = ResourcesLoader.load("css/" + style + ".css");
			tmpStyles.add(resource);
		}
		String themeStylesheet = ResourcesLoader.load("css/themes/" + currentTheme.get().fileName + ".css");
		tmpStyles.add(themeStylesheet);

		// to only trigger changes in loadedStyle once
		observableStyles.setAll(tmpStyles);

	}

	public void toggleLightDarkTheme()
	{
		currentTheme.setValue(currentTheme.getValue().equals(Theme.LIGHT) ? Theme.DARK : Theme.LIGHT);
		reloadStyles();
	}

	public void setTheme(Theme newTheme)
	{
		// if the user can use custom themes then sanitize 'newTheme'
		currentTheme.setValue(newTheme);
		reloadStyles();
	}

	public SimpleObjectProperty<Theme> getCurrentTheme()
	{
		return currentTheme;
	}

	public ObservableList<String> getObservableStyles()
	{
		return observableStyles;
	}

}
