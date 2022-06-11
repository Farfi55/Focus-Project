package mafiadelprimobanco.focusproject;

import org.json.JSONObject;


import javafx.scene.paint.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JsonHandler
{

	private static Path localTagFile = Path.of("userdata.json");
	static JSONObject userTags;

	public static void init()
	{
		if (!localTagFile.toFile().exists()) return;

		try
		{
			userTags = new JSONObject(new String(Files.readAllBytes(localTagFile)));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		loadTags();
	}

	static void updateTagFile()
	{
		try
		{
			if (!localTagFile.toFile().exists()) Files.createFile(localTagFile);
			Files.writeString(localTagFile, userTags.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	static void loadTags()
	{
		userTags.keys().forEachRemaining(key -> {
				JSONObject currTag = (JSONObject)userTags.get(key);
				TagHandler.getInstance().addTag(key, Color.valueOf(currTag.getString("Color")));
		});
	}

	static void addTag(String name, String colorHex, Integer UUID)
	{
		JSONObject newTag = new JSONObject("{"
				+ "UUID:"+UUID+", Color:"+colorHex +
				"}");
		userTags.put(name, newTag);
		updateTagFile();
	}

	static void deleteTag(String tagName)
	{
		userTags.remove(tagName);
		updateTagFile();
	}






}
