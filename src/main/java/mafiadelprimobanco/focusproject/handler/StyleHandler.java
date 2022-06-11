package mafiadelprimobanco.focusproject.handler;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;

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

	private final StringProperty currentTheme = new SimpleStringProperty("light");
	private final List<String> baseStyles = List.of("fonts", "style");


	private StyleHandler() {
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
		String themeStylesheet = ResourcesLoader.load("css/themes/" + currentTheme.get() + ".css");
		tmpStyles.add(themeStylesheet);

		// to only trigger changes in loadedStyle once
		observableStyles.setAll(tmpStyles);

	}

	public void toggleLightDarkTheme()
	{
		currentTheme.setValue(currentTheme.getValue().equals("light") ? "dark" : "light");
		reloadStyles();
	}

	public void setTheme(String newTheme)
	{
		// if the user can use custom themes then sanitize 'newTheme'
		currentTheme.setValue(newTheme);
		reloadStyles();
	}

	public StringProperty getCurrentTheme()
	{
		return currentTheme;
	}

	public ObservableList<String> getObservableStyles()
	{
		return observableStyles;
	}

}
