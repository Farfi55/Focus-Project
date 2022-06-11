package mafiadelprimobanco.focusproject;

import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import org.json.JSONObject;


import javafx.scene.paint.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Random;

public final class JsonHandler
{

	private static Path localTagFile = Path.of("tags.json");
	private static Path localActivitiesFile = Path.of("activities.json");
	static JSONObject userTags;
	static JSONObject userActivities;

	public static void init()
	{
		try
		{
			if (!localTagFile.toFile().exists())
				Files.writeString(localTagFile, "{}", StandardOpenOption.CREATE);

			if (!localActivitiesFile.toFile().exists())
				Files.writeString(localActivitiesFile, "{}", StandardOpenOption.CREATE);


			userTags = new JSONObject(new String(Files.readAllBytes(localTagFile)));
			userActivities = new JSONObject(new String(Files.readAllBytes(localActivitiesFile)));
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
			Files.writeString(localTagFile, userTags.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	static void updateActivitiesFile()
	{
		try
		{
			Files.writeString(localActivitiesFile, userActivities.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void addFinishedActivity(LocalDateTime key, AbstractActivity activity)
	{
		userActivities.put(key.toString(), activity.toJsonObject());
		updateActivitiesFile();
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
		userTags.put(name, new JSONObject("{"
				+ "UUID:"+UUID+", Color:"+colorHex +
				"}"));
		updateTagFile();
	}

	static void editTag(String oldTagName, String name, String colorHex, Integer UUID)
	{
		userTags.remove(oldTagName);
		userTags.put(name, new JSONObject("{"
				+ "UUID:"+UUID+", Color:"+colorHex +
				"}"));
		updateTagFile();
	}

	static void deleteTag(String tagName)
	{
		userTags.remove(tagName);
		updateTagFile();
	}






}
