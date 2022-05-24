package mafiadelprimobanco.focusproject;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class StyleHandler
{
	private static final StyleHandler instance = new StyleHandler();

	public static StyleHandler getInstance()
	{
		return instance;
	}
	private final ObservableList<String> loadedStyles = new SimpleListProperty<>();
	private String currentTheme = "light";
	private final List<String> styles = List.of(currentTheme, "fonts", "style");

	private StyleHandler() { }

	private void reloadStyles()
	{
		List<String> tmpStyles = new ArrayList<>(styles.size());
		for (String style : styles)
		{
			String resource = ResourcesLoader.load("css/" + style + ".css");
			tmpStyles.add(resource);
		}
		// to only trigger changes in loadedStyle once
		loadedStyles.setAll(tmpStyles);
	}

	public void toggleLightDarkTheme()
	{
		currentTheme = currentTheme.equals("light") ? "dark" : "light";
		reloadStyles();
	}

	private void changeTheme(String newTheme)
	{
		// if the user can use custom themes then sanitize 'newTheme'
		currentTheme = newTheme;
		reloadStyles();
	}

	public String getCurrentTheme()
	{
		return currentTheme;
	}

	public ObservableList<String> getLoadedStyles()
	{
		return loadedStyles;
	}

//	public List<String> getStyles()
//	{
//		return styles;
//	}


}
