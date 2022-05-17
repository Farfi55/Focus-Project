package mafiadelprimobanco.focusproject.model;

import javafx.scene.paint.Color;

public class Tag
{
	String tagName;
	Color color;

	public Tag(String tagName, Color color)
	{
		this.tagName = tagName;
		this.color = color;
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}
}
