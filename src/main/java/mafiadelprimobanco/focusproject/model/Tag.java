package mafiadelprimobanco.focusproject.model;

import javafx.scene.paint.Color;

public class Tag
{
	String name;
	Color color;
	Integer uuid;

	public Tag(String name, Color color, Integer uuid)
	{
		this.name = name;
		this.color = color;
		this.uuid = uuid;
	}

	public String getName()
	{
		return name;
	}

	public Color getColor()
	{
		return color;
	}

	public Integer getUuid()
	{
		return uuid;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}


}
