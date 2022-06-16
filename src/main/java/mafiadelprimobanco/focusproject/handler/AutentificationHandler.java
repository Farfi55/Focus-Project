package mafiadelprimobanco.focusproject.handler;

import javafx.application.Platform;
import mafiadelprimobanco.focusproject.client.Client;
import mafiadelprimobanco.focusproject.client.ConnectionException;
import mafiadelprimobanco.focusproject.model.User;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


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

				String token = obj.getString("userToken");

				String res = Client.getInstance().loginWithCustomToken(token);

				if (res != null) {
					Feedback.getInstance().showNotification(Localization.get("info.autentification.loginSuccessfull.Header"),
							Localization.get("info.autentification.loginSuccessfull.Msg", user.username()));
					isLogged = true;
					return true;
				}
			}
		}
		catch (IOException | ConnectionException e)
		{
			e.printStackTrace();
		}

		isLogged = false;
		return false;
	}

	public void registerUser(User user)
	{
		new Thread(() ->
		{
			Platform.runLater(() ->
			{
				try
				{
					String id = Client.getInstance().register(user.email(), user.password());

					if (id == null)
					{
						Feedback.getInstance().showError(Localization.get("error.autentification.UserExistsHeader"),
								Localization.get("error.autentification.UserExistsMsg"));
						return;
					}

					if (Client.getInstance().sendEmailVerification()) Feedback.getInstance().showError(Localization.get("info.autentification.EmailConfirmHeader"),
							Localization.get("info.autentification.EmailConfirmMsg"));
				}
				catch (IOException | ConnectionException e)
				{
					e.printStackTrace();
				}
			});
		}).start();
	}

	public boolean doLogin(User user)
	{
		try
		{
			String token = Client.getInstance().login(user.email(), user.password());

			if (token == null)
			{
				Feedback.getInstance().showError(Localization.get("error.autentification.UoPNotValidHeader"),
						Localization.get("error.autentification.UoPNotValidMsg"));
				return false;
			}

			if (!Client.getInstance().isEmailVerified())
			{
				Feedback.getInstance().showError(Localization.get("error.autentification.emailNotValidHeader"),
						Localization.get("error.autentification.emailNotValidMsg"));
				return false;
			}

			if (localDatabaseFile.toFile().exists())
				Files.delete(localDatabaseFile);


			JSONObject jsonUser = new JSONObject();

			jsonUser.put("userToken", token);

			Files.writeString(localDatabaseFile, jsonUser.toString(), StandardOpenOption.CREATE);

			Feedback.getInstance().showNotification(Localization.get("info.autentification.loginSuccessfull.Header"),
					Localization.get("info.autentification.loginSuccessfull.Msg", user.username()));

			this.user = user;

			isLogged = true;
			return true;
		}
		catch (IOException | ConnectionException e)
		{
			Feedback.getInstance().showError(Localization.get("error.autentification.connectionErrorHeader"),
					Localization.get("error.autentification.connectionErrorMsg"));
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
