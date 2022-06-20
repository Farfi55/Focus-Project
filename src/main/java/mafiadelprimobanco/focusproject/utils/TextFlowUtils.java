package mafiadelprimobanco.focusproject.utils;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TextFlowUtils
{

	public static void addBoldText(TextFlow textFlow, String text) { addText(textFlow, text, FontWeight.BOLD); }


	public static void addText(TextFlow textFlow, String text)
	{ addText(textFlow, text, FontWeight.NORMAL, 14); }

	public static void addText(TextFlow textFlow, String text, FontWeight weight)
	{ addText(textFlow, text, weight, 14); }

	public static void addText(TextFlow textFlow, String text, FontWeight weight, double size)
	{
		Text boldText = new Text(text);
		boldText.setFont(Font.font("Roboto", weight, size));
		textFlow.getChildren().add(boldText);
	}

	public static void addText(TextFlow textFlow, Text text)
	{
		textFlow.getChildren().add(text);
	}
}
