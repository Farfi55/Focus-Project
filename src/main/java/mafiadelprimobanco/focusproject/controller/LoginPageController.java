package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

public class LoginPageController {

	@FXML
	private MFXPasswordField passwordField;

	@FXML
	private MFXTextField usernameField;

	@FXML
	void doLogin(ActionEvent event) {

	}

	@FXML
	void initialize()
	{
		usernameField.setLeadingIcon(new MFXIconWrapper("mfx-user", 16, Color.GRAY, 24));

		passwordField.getValidator().constraint("La password deve essere almeno lunga 8 caratteri",
				passwordField.textProperty().length().greaterThanOrEqualTo(8));
	}
}
