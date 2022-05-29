package mafiadelprimobanco.focusproject.model;

import javafx.scene.image.Image;

public record TreeTemplate(Integer uuid, String name, String matureTreeSprite, String deadTreeSprite)
{

	public Image createMatureTreeImage()
	{
		return new Image(matureTreeSprite);
	}

	public Image createDeadTreeImage()
	{
		return new Image(deadTreeSprite);
	}

	@Override
	public String toString()
	{
		return name;
	}
}
