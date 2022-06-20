package mafiadelprimobanco.focusproject.handler;

import javafx.application.Platform;
import mafiadelprimobanco.focusproject.client.Client;
import mafiadelprimobanco.focusproject.client.ConnectionException;
import mafiadelprimobanco.focusproject.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class AuthenticationHandler
{
	private static AuthenticationHandler instance = new AuthenticationHandler();


	private AuthenticationHandler() { }

	private boolean isLogged = false;

	private User user;

	private final Path idDatabaseFile = Path.of("ids.db");
	JSONObject jsonFilesID = new JSONObject();

	public static AuthenticationHandler getInstance()
	{
		return instance;
	}

	public void initIdDatabase()
	{
		try
		{
			if (idDatabaseFile.toFile().exists())
			{
				jsonFilesID = new JSONObject(new String(Files.readAllBytes(idDatabaseFile)));
			}else{
				jsonFilesID.put("tagFID", "");
				jsonFilesID.put("activitiesFID", "");
				Files.writeString(idDatabaseFile, jsonFilesID.toString(), StandardOpenOption.CREATE);
			}
		}
		catch (IOException e)
		{
			Feedback.getInstance().showError(Localization.get("error.authentication.connectionError.header"),
					Localization.get("error.authentication.connectionError.message"));
		}

		isLogged = false;
	}

	public void registerUser(User user)
	{
		try
		{
			String id = Client.getInstance().register(user.email(), user.password());

			if (id == null)
			{
				Feedback.getInstance().showError(Localization.get("error.authentication.UserExists.header"),
						Localization.get("error.authentication.UserExists.message"));
				return;
			}

			if (Client.getInstance().sendEmailVerification()) Feedback.getInstance().showError(Localization.get(
							"info.authentication.emailConfirm.header"),
					Localization.get("info.authentication.emailConfirm.message"));
		}
		catch (IOException | ConnectionException e)
		{
			Feedback.getInstance().showError(Localization.get("error.authentication.connectionError.header"),
					Localization.get("error.authentication.connectionError.message"));
		}
	}

	public boolean doLogin(User user)
	{
		try
		{
			String token = Client.getInstance().login(user.email(), user.password());

			if (token == null)
			{
				Feedback.getInstance().showError(Localization.get("error.authentication.UoPNotValid.header"),
						Localization.get("error.authentication.UoPNotValid.message"));
				return false;
			}

			if (!Client.getInstance().isEmailVerified())
			{
				Feedback.getInstance().showError(Localization.get("error.authentication.emailNotValid.header"),
						Localization.get("error.authentication.emailNotValid.message"));
				return false;
			}

			initIdDatabase();

			Feedback.getInstance().showNotification(Localization.get("info.authentication.loginSuccessful.header"),
					Localization.get("info.authentication.loginSuccessful.message", user.username()));

			this.user = user;

			isLogged = true;
			return true;
		}
		catch (IOException | ConnectionException e)
		{
			Feedback.getInstance().showError(Localization.get("error.authentication.connectionError.header"),
					Localization.get("error.authentication.connectionError.message"));
		}

		return false;
	}

	public void getDataFromServer()
	{
		if (!isLogged) return;

		Client.getInstance().get("userdata",
			result -> {
				var jsonArray = result.result().getJSONArray("userdata");
				var jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
				JsonHandler.loadTags(new JSONObject(jsonObject.getString("tags")));
				JsonHandler.loadActivities(new JSONObject(jsonObject.getString("activities")));
			}, e -> { System.out.println(e); }
		);
	}

	public void updateTagsToServer()
	{
		if (!isLogged) return;

		Client.getInstance().insert("userdata", JsonHandler.getTagsActivities(), e -> {},databaseReference -> {System.out.println(databaseReference);});
	}

	public void updateActivitiesToServer()
	{
		if (!isLogged) return;

		Client.getInstance().insert("userdata", JsonHandler.getTagsActivities(), e -> {},databaseReference -> {System.out.println(databaseReference);});
	}

	public void doLogout()
	{
		try
		{
			Client.getInstance().logout();
		}
		catch (IOException | ConnectionException e)
		{
			e.printStackTrace();
		}

		isLogged = false;
	}

	public boolean isUserLogged() { return isLogged; }
	public String getUser() { return user.username(); }
}
