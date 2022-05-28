package mafiadelprimobanco.focusproject;

import mafiadelprimobanco.focusproject.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler
{
	private static DatabaseHandler instance = new DatabaseHandler();
	private String url = "jdbc:sqlite:database.db";
	private Connection con = null;

	private DatabaseHandler()
	{
		try
		{
			con = DriverManager.getConnection(url);

			if(con != null && !con.isClosed())
				System.out.println("Connected!");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}


	public static DatabaseHandler getInstance()
	{
		return instance;
	}

	public void insertUser(User newUser, String encrPwd)
	{
		PreparedStatement stmt = null;

		try
		{
			stmt = con.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?);");

			//TODO: make the id wokring correctly
			stmt.setInt(1,1); //ID

			stmt.setString(2, newUser.firstName()); // Name
			stmt.setString(3, newUser.lastName()); // Lastname
			stmt.setString(4, newUser.username()); //username
			stmt.setString(5, encrPwd); //password

			stmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public User loadUser()
	{
		PreparedStatement stmt = null;

		try
		{
			//TODO: check if there are users inside the database
			/*
				if(con.prepareStatement("select count(*) FROM users;").getResultSet().getInt(1) == 0)
					return null;
			 */

			stmt = con.prepareStatement("select * FROM users;");
			stmt.execute();

			var queryResult = stmt.getResultSet();

			return new User(queryResult.getString(4), queryResult.getString(5),
					queryResult.getString(2), queryResult.getString(3));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public void removeUser(User newUser)
	{
		PreparedStatement stmt = null;

		try
		{
			stmt = con.prepareStatement("DELETE FROM users WHERE id = 1;");
			stmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public boolean userAlreadyExist(User user)
	{
		PreparedStatement stmt = null;

		try
		{
			stmt = con.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?;");
			stmt.setString(1, user.username());
			stmt.execute();

			return stmt.getResultSet().getInt(1) > 0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	void closeConnection() {
		try
		{
			con.close();
		}
		catch (SQLException ignored) { }
	}
}
