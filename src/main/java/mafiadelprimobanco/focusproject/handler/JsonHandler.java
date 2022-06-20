package mafiadelprimobanco.focusproject.handler;

import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import mafiadelprimobanco.focusproject.model.activity.ChronometerActivity;
import mafiadelprimobanco.focusproject.model.activity.TimerActivity;
import mafiadelprimobanco.focusproject.utils.ColorUtils;
import org.json.JSONObject;


import javafx.scene.paint.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

		ActivityHandler.getInstance().addListener(new ActivityObserver()
		{
			@Override
			public void onActivityEnd(AbstractActivity currentActivity)
			{
				JsonHandler.addFinishedActivity(currentActivity.getStartTime(), currentActivity);
			}
		});

		TagHandler.getInstance().addListener(new TagsObserver()
		{
			@Override
			public void onTagAdded(Tag tag)
			{
				if(tag.getUuid() != 0) addTag(tag);
			}

			@Override
			public void onTagRemoving(Tag tag) { deleteTag(tag); }

			@Override
			public void onTagChanged(Tag tag) { editTag(tag); }
		});

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

		AuthenticationHandler.getInstance().updateTagsToServer();
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

		AuthenticationHandler.getInstance().updateActivitiesToServer();
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

	static void loadActivities(JSONObject data)
	{
		Map<String, Object> map = userActivities.toMap();
		map.putAll(data.toMap());
		userActivities = new JSONObject(map);

		ActivityStatsHandler.getInstance().loadActivities();
	}

	static void loadTags(JSONObject data)
	{
		Map<String, Object> map = userTags.toMap();
		map.putAll(data.toMap());
		userTags = new JSONObject(map);

		data.keys().forEachRemaining(key ->
		{
			JSONObject currTag = (JSONObject)userTags.get(key);
			TagHandler.getInstance().addTag(key, Color.valueOf(currTag.getString("Color")), currTag.getInt("UUID"));
		});
	}

	static JSONObject getTagsActivities()
	{
		return new JSONObject().put("tags", userTags.toString()).put("activities", userActivities.toString());
	}

	static void loadTags()
	{
		userTags.keys().forEachRemaining(key ->
		{
			JSONObject currTag = (JSONObject)userTags.get(key);

			String name = currTag.getString("name");
			Color color = Color.valueOf(currTag.getString("color"));
			Integer uuid = currTag.getInt("uuid");
			TagHandler.getInstance().addTag(name, color, uuid);
		});
	}


	public static void addTag(Tag tag)
	{
		System.out.println(tag.toString());
		userTags.put(tag.getUuid().toString(), new JSONObject(tag.toString()));

		updateTagFile();
	}

	public static void editTag(Tag tag)
	{
		userTags.remove(tag.getUuid().toString());
		addTag(tag);
	}

	public static void deleteTag(Tag tag)
	{
		userTags.remove(tag.getUuid().toString());
		updateTagFile();
	}

}
