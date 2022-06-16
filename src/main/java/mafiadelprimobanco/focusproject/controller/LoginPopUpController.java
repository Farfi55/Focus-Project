package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mafiadelprimobanco.focusproject.handler.AutentificationHandler;
import mafiadelprimobanco.focusproject.handler.Localization;
import mafiadelprimobanco.focusproject.handler.PagesHandler;
import mafiadelprimobanco.focusproject.handler.SceneHandler;
import mafiadelprimobanco.focusproject.model.User;

public class LoginPopUpController {

	@FXML private MFXPasswordField passwordField;

	@FXML private MFXTextField emailField;

	@FXML private MFXButton loginButton;

	@FXML private MFXButton registrationButton;

	@FXML void closeLoginPopUp() {
		SceneHandler.getInstance().closeLoginPopup();
	}

	void clean()
	{
		emailField.setText("");
		passwordField.setText("");
	}

	@FXML void doLogin(ActionEvent event)
	{
		if (AutentificationHandler.getInstance().doLogin(
				new User(emailField.getText(), emailField.getText(), passwordField.getText()))
			)
		{
			SceneHandler.getInstance().closeLoginPopup();
			clean();
		}
	}

	@FXML void doRegisterUser(ActionEvent event)
	{
		SceneHandler.getInstance().closeLoginPopup();
		PagesHandler.navigateTo(PagesHandler.registration);

		clean();
	}

	@FXML void initialize()
	{
		Localization.setButton(loginButton, "login.signIn");
		Localization.setButton(registrationButton, "login.signUp");
	}

}
