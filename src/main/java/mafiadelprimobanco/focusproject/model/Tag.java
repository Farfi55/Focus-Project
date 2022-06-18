package mafiadelprimobanco.focusproject.model;

import javafx.scene.paint.Color;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tag tag = (Tag)o;
		return uuid.equals(tag.uuid);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(uuid);
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
