package mafiadelprimobanco.focusproject.handler;

import mafiadelprimobanco.focusproject.handler.TagHandler;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import mafiadelprimobanco.focusproject.model.activity.ChronometerActivity;
import mafiadelprimobanco.focusproject.model.activity.TimerActivity;
import org.json.JSONObject;


import javafx.scene.paint.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Vector;

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
			if (!localTagFile.toFile().exists()) Files.writeString(localTagFile, "{}", StandardOpenOption.CREATE);

			if (!localActivitiesFile.toFile().exists()) Files.writeString(localActivitiesFile, "{}",
					StandardOpenOption.CREATE);


			userTags = new JSONObject(new String(Files.readAllBytes(localTagFile)));
			userActivities = new JSONObject(new String(Files.readAllBytes(localActivitiesFile)));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		loadTags();
	}

	//Use an enum to deal with that
	private static void updateTagFile()
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

	private static void updateActivitiesFile()
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

	private static void getActivity(List<AbstractActivity> activityList, String dataKey)
	{
		JSONObject activity = (JSONObject)userActivities.get(dataKey);

		String type = activity.getString("type");

		if (ActivityType.CHRONOMETER.key.equals(type)) activityList.add(
				new ChronometerActivity(activity.getInt("tagUuid"), activity.getInt("treeUuid"),
						LocalDateTime.parse(activity.getString("startTime")),
						LocalDateTime.parse(activity.getString("endTime"))));

		else if (ActivityType.TIMER.key.equals(type)) activityList.add(
				new TimerActivity(activity.getInt("tagUuid"), activity.getInt("treeUuid"),
						LocalDateTime.parse(activity.getString("startTime")),
						LocalDateTime.parse(activity.getString("endTime")), activity.getInt("chosenDuration")));
	}

	public static List<AbstractActivity> getAllActivities()
	{
		List<AbstractActivity> activityList = new Vector<>();

		userActivities.keys().forEachRemaining(dataKey -> getActivity(activityList, dataKey));

		return activityList;
	}


	public static List<AbstractActivity> getAllActivitiesBetween(LocalDateTime startData, LocalDateTime endData)
	{
		List<AbstractActivity> activityList = new Vector<>();

		userActivities.keys().forEachRemaining(dataKey ->
		{
			LocalDateTime data = LocalDateTime.parse(dataKey);

			if (startData.isBefore(data) && endData.isAfter(data))
			{
				getActivity(activityList, dataKey);
			}
		});

		return activityList;
	}


	static void loadTags()
	{
		userTags.keys().forEachRemaining(key ->
		{
			JSONObject currTag = (JSONObject)userTags.get(key);
			TagHandler.getInstance().addTag(key, Color.valueOf(currTag.getString("Color")), currTag.getInt("UUID"));
		});
	}

	public static void addTag(String name, String colorHex, Integer UUID)
	{
		userTags.put(name, new JSONObject("{" + "UUID:" + UUID + ", Color:" + colorHex + "}"));
		updateTagFile();
	}

	public static void editTag(String oldTagName, String name, String colorHex, Integer UUID)
	{
		userTags.remove(oldTagName);
		addTag(name, colorHex, UUID);
	}

	public static void deleteTag(String tagName)
	{
		userTags.remove(tagName);
		updateTagFile();
	}

}
