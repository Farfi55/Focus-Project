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

import mafiadelprimobanco.focusproject.model.User;
import org.json.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class AutentificationHandler
{
	private static AutentificationHandler instance = new AutentificationHandler();

	private User user;

	private AutentificationHandler() { }

	private boolean isLogged = false;

	public static AutentificationHandler getInstance()
	{
		return instance;
	}

	public String getUser()
	{
		return user.username();
	}

	public boolean doLoginFromDatabase()
	{
		User user = DatabaseHandler.getInstance().loadUser();

		if (user == null) return false;

		this.user = user;

		isLogged = true;

		return true;
	}

	public boolean registerUser(User user)
	{

		if (DatabaseHandler.getInstance().userAlreadyExist(user))
		{
			Feedback.getInstance().showError("Errore", "Utente già esistente");
			return false;
		}

		String encryptedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt(12));
		DatabaseHandler.getInstance().insertUser(user, encryptedPassword);

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