package mafiadelprimobanco.focusproject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Collection;

import org.json.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class AutentificationHandler
{
	private static AutentificationHandler instance = new AutentificationHandler();

	private Path databaseFile;

	private SecureRandom random = new SecureRandom();
	private JSONObject databaseData;

	private String user = "";

	private AutentificationHandler() {

		databaseFile = Path.of("database.db");

		try
		{
			if (databaseFile.toFile().exists())
			{
				String data = new String(Files.readAllBytes(databaseFile));
				databaseData = new JSONObject(data);
			}
		}
		catch (IOException ignored) { }
	}

	private boolean isLogged = false;

	public static AutentificationHandler getInstance()
	{
		return instance;
	}

	private boolean checkLoginData()
	{
		if (databaseData == null || databaseData.isEmpty()) return false;

		return !((JSONObject)databaseData.get("default_user")).isEmpty();
	}

	public String getUser()
	{
		return user;
	}

	public boolean doLoginFromDatabase()
	{
		if (!checkLoginData()) return false;

		user = (String)((JSONObject)databaseData.get("default_user")).get("username");

		isLogged = true;

		return true;
	}

	public boolean registerUser(String username, String password)
	{
		JSONObject jsonDataArray;

		if (databaseData == null)
		{
			JSONObject default_user = new JSONObject();

			databaseData = new JSONObject();

			default_user.put("password", "");
			default_user.put("username", "");

			databaseData.put("default_user", default_user);
		}

		jsonDataArray = (JSONObject)databaseData.get("default_user");

		if (jsonDataArray.get("username").equals(username))
		{
			Feedback.getInstance().showError("Errore", "Utente già esistente");
			return false;
		}

		try
		{
			byte[] salt = new byte[16];

			random.nextBytes(salt);
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

			jsonDataArray.put("username", username);
			jsonDataArray.put("password", new String(factory.generateSecret(spec).getEncoded()));

			if (!databaseFile.toFile().exists())
				Files.createFile(databaseFile);

			Files.writeString(databaseFile, databaseData.toString());
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e)
		{
			e.printStackTrace();
		}


		return true;
	}

	public boolean doLogin(String username, String password)
	{
		//TODO: firebase online check
		isLogged = true;
		return true;
	}

	public boolean isUserLogged() { return isLogged; }
}