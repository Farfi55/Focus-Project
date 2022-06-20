package mafiadelprimobanco.focusproject.utils;


import javafx.scene.paint.Color;

public class ColorUtils
{
	public static String toHex(Color color)
	{
		return "#" + color.toString().substring(2);
	}

}
