package mafiadelprimobanco.focusproject.handler;

import mafiadelprimobanco.focusproject.client.Client;
import mafiadelprimobanco.focusproject.client.ConnectionException;
import mafiadelprimobanco.focusproject.model.User;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class AutentificationHandler
{
	private static AutentificationHandler instance = new AutentificationHandler();


	private AutentificationHandler() { }

	private boolean isLogged = false;

	private User user;

	private Path localDatabaseFile = Path.of("user.db");

	public static AutentificationHandler getInstance()
	{
		return instance;
	}

	public boolean doLoginFromDatabase()
	{
		try
		{
			if (localDatabaseFile.toFile().exists())
			{
				JSONObject obj = new JSONObject(new String(Files.readAllBytes(localDatabaseFile)));

				String email = obj.getString("email");
				String username = obj.getString("username");
				String password = obj.getString("password");

				boolean res = doLogin(new User(email, username, password));

				if (res) {
					isLogged = true;
					return true;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		isLogged = false;
		return false;
	}

	public void registerUser(User user)
	{
		new Thread(() -> {
			try
			{
				String id = Client.getInstance().register(user.email(), user.password());

				if (id == null) {
					Feedback.getInstance().showError("Errore", "Utente già esistente");
					return;
				}

				if (Client.getInstance().sendEmailVerification())
					Feedback.getInstance().showInfo("Info", "Controlla la tua mail per confermare la registrazione");

			}
			catch (IOException | ConnectionException e)
			{
				e.printStackTrace();
			}
		}).start();
	}

	public boolean doLogin(User user)
	{
		try
		{
			String id = Client.getInstance().login(user.email(), user.password());

			if (id == null)
			{
				Feedback.getInstance().showError("Errore", "Username o password errata");
				return false;
			}

			if (!Client.getInstance().isEmailVerified())
			{
				Feedback.getInstance().showError("Errore", "Email non verificata");
				return false;
			}

			/*if (localDatabaseFile.toFile().exists())
				Files.delete(localDatabaseFile);


			JSONObject jsonUser = new JSONObject();

			jsonUser.put("email", user.email());
			jsonUser.put("username", user.username());
			jsonUser.put("password", user.password());

			Files.writeString(localDatabaseFile, jsonUser.toString(), StandardOpenOption.CREATE_NEW);*/

			this.user = user;

			isLogged = true;
			return true;
		}
		catch (IOException | ConnectionException e)
		{
			e.printStackTrace();
		}

		return false;
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
	}

	public boolean isUserLogged() { return isLogged; }
	public String getUser() { return user.username(); }
}
