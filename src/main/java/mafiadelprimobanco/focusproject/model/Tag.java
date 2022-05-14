package mafiadelprimobanco.focusproject.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;

public class Tag
{
	String tagName;
	Color color;

	FXMLLoader tagFxmlLoader;

	public Tag(String tagName, Color color)
	{
		this.tagName = tagName;
		this.color = color;
	}
}
