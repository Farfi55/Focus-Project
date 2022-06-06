package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mafiadelprimobanco.focusproject.AutentificationHandler;
import mafiadelprimobanco.focusproject.Localization;
import mafiadelprimobanco.focusproject.PagesHandler;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.model.User;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;

import java.io.IOException;

public class LoginPopUpController {

	@FXML private MFXPasswordField passwordField;

	@FXML private MFXTextField emailField;

	@FXML private MFXButton loginButton;

	@FXML private MFXButton registrationButton;

	@FXML void closeLoginPopUp() {
		SceneHandler.getInstance().closeLoginPopup();
	}

	@FXML void doLogin(ActionEvent event)
	{
		AutentificationHandler.getInstance().doLogin(
				new User(emailField.getText(), emailField.getText(), passwordField.getText())
		);
	}

	@FXML void doRegisterUser(ActionEvent event)
	{
		SceneHandler.getInstance().closeLoginPopup();
		PagesHandler.navigateTo(PagesHandler.account);
	}

	@FXML void initialize()
	{
		Localization.setButton(loginButton, "login.signIn");
		Localization.setButton(registrationButton, "login.signUp");
	}

}
