package mafiadelprimobanco.focusproject.model;

public class AutentificationHandler
{
	private static AutentificationHandler instance = new AutentificationHandler();

	private AutentificationHandler() { }

	private boolean isLogged = false;

	public static AutentificationHandler getInstance()
	{
		return instance;
	}

	public boolean checkLogin()
	{
		//TODO check inside the database if there's userdata login
		return true;
	}

	public boolean doLoginFromDatabase()
	{
		isLogged = true;
		return true;
	}

	public boolean doLogin(String username, String password)
	{
		//load the data from database and check it with firebase
		isLogged = true;
		return true;
	}

	public boolean isUserLogged() { return isLogged; }
}
