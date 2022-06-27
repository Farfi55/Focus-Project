package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Validated;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.handler.AuthenticationHandler;
import mafiadelprimobanco.focusproject.handler.Localization;
import mafiadelprimobanco.focusproject.handler.PagesHandler;
import mafiadelprimobanco.focusproject.model.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationPageController implements Controller
{
	/*
	 * Parte di questa classe è stato copiato dalla libreria MaterialFX
	 * https://github.com/palexdev/MaterialFX/wiki
	 */

	private final MFXTextField loginField;
	private final MFXPasswordField passwordField;
	private final MFXTextField usernameField;
	private final MFXCheckbox checkbox;

	@FXML private MFXStepper stepper;

	public RegistrationPageController()
	{
		loginField = new MFXTextField();
		passwordField = new MFXPasswordField();
		usernameField = new MFXTextField();
		checkbox = new MFXCheckbox(Localization.get("registration.confirm"));
	}

	public static Boolean isValidEmail(String email)
	{
		String emailPattern = "^[a-zA-Z0-9_.]+@[a-zA-Z.]+?\\.[a-zA-Z]{2,3}$";
		Pattern p = Pattern.compile(emailPattern);
		return p.matcher(email).matches();
	}

	public static Boolean isValidPassword(String password)
	{
		//                           uppercase    lowercase    number    special
		String passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[\\d])(?=.*?[^\\w]).{8,}$";
		Pattern p = Pattern.compile(passwordPattern);
		return p.matcher(password).matches();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		loginField.setPromptText("Email");
		passwordField.setPromptText("Password");
		usernameField.setPromptText("Username");

		loginField.setLeadingIcon(new MFXIconWrapper("mfx-user", 16, Color.web("#4D4D4D"), 24));

		loginField.getValidator().constraint(Localization.get("registration.emailNotNull"),
				loginField.textProperty().isNotEmpty());

		loginField.getValidator().constraint(Localization.get("registration.emailNotValid"),
				Bindings.createBooleanBinding(() -> isValidEmail(loginField.getText()), loginField.textProperty()));

		passwordField.getValidator().constraint(Localization.get("registration.passwordLengthError"),
				passwordField.textProperty().length().greaterThanOrEqualTo(8));

		passwordField.getValidator().constraint(Localization.get("registration.passwordWeakError"),
				Bindings.createBooleanBinding(() -> isValidPassword(passwordField.getText()), passwordField.textProperty()));

		usernameField.getValidator().constraint(Localization.get("registration.usernameNotNull"),usernameField.textProperty().isNotEmpty());

		List<MFXStepperToggle> stepperToggles = createSteps();
		stepper.getStepperToggles().addAll(stepperToggles);

	}

	private List<MFXStepperToggle> createSteps()
	{
		MFXStepperToggle step1 = new MFXStepperToggle("Step 1", new MFXFontIcon("mfx-lock", 16, Color.web("#f1c40f")));
		VBox step1Box = new VBox(20, wrapNodeForValidation(usernameField), wrapNodeForValidation(loginField),
				wrapNodeForValidation(passwordField));
		step1Box.setAlignment(Pos.CENTER);
		step1.setContent(step1Box);
		step1.getValidator().dependsOn(loginField.getValidator()).dependsOn(passwordField.getValidator());

		MFXStepperToggle step2 = new MFXStepperToggle("Step 2",
				new MFXFontIcon("mfx-variant7-mark", 16, Color.web("#85CB33")));
		Node step3Grid = createGrid();
		step2.setContent(step3Grid);
		step2.getValidator().constraint(Localization.get("registration.confirmError"), checkbox.selectedProperty());

		return List.of(step1, step2);
	}

	private <T extends Node & Validated> Node wrapNodeForValidation(T node)
	{
		/*
		 * Parte di questa funzione è stato copiato dalla libreria MaterialFX
		 * https://github.com/palexdev/MaterialFX/wiki
		 */
		Label errorLabel = new Label();
		errorLabel.getStyleClass().add("error-label");
		errorLabel.setManaged(false);
		stepper.addEventHandler(MFXStepper.MFXStepperEvent.VALIDATION_FAILED_EVENT, event ->
		{
			MFXValidator validator = node.getValidator();
			List<Constraint> validate = validator.validate();
			if (!validate.isEmpty())
			{
				errorLabel.setText(validate.get(0).getMessage());
			}
		});
		stepper.addEventHandler(MFXStepper.MFXStepperEvent.NEXT_EVENT, event -> errorLabel.setText(""));
		VBox wrap = new VBox(3, node, errorLabel)
		{
			@Override
			protected double computePrefHeight(double width)
			{
				return super.computePrefHeight(width) + errorLabel.getHeight() + getSpacing();
			}

			@Override
			protected void layoutChildren()
			{
				super.layoutChildren();

				double x = node.getBoundsInParent().getMinX();
				double y = node.getBoundsInParent().getMaxY() + getSpacing();
				double width = getWidth();
				double height = errorLabel.prefHeight(-1);
				errorLabel.resizeRelocate(x, y, width, height);
			}
		};
		wrap.setAlignment(Pos.CENTER);
		return wrap;
	}

	private Node createGrid()
	{
		/*
		 * Parte di questa funzione è stato copiato dalla libreria MaterialFX
		 * https://github.com/palexdev/MaterialFX/wiki
		 */
		Label emailLabel = new Label("Email: ");
		Label emailValueLabel = new Label("");
		emailValueLabel.textProperty().bind(loginField.textProperty());
		emailLabel.setMinWidth(80);
		emailValueLabel.setMinWidth(100);

		Label usernameLabel = new Label("Username: ");
		usernameLabel.setMinWidth(80);
		Label usernameValueLabel = new Label("");
		usernameValueLabel.setMinWidth(100);
		usernameValueLabel.textProperty().bind(usernameField.textProperty());


		emailLabel.getStyleClass().add("header-label");
		usernameLabel.getStyleClass().add("header-label");

		HBox b1 = new HBox(20, emailLabel, emailValueLabel);
		HBox b2 = new HBox(20, usernameLabel, usernameValueLabel);
		b1.setAlignment(Pos.CENTER);
		b2.setAlignment(Pos.CENTER);
		b1.getStyleClass().add("registrationFieldBox");
		b1.getStyleClass().add("registrationFieldBox");

		VBox box = new VBox(20, b1, b2, checkbox);
		box.getStyleClass().add("registrationFieldsContainer");
		box.setAlignment(Pos.CENTER);
		StackPane.setAlignment(box, Pos.CENTER);

		stepper.setOnLastNext(event ->
		{
			User user = new User(loginField.getText(), usernameField.getText(), passwordField.getText());
			AuthenticationHandler.getInstance().registerUser(user);
			PagesHandler.navigateTo(PagesHandler.home);
		});
		stepper.setOnBeforePrevious(event ->
		{
			if (stepper.isLastToggle())
			{
				checkbox.setSelected(false);
				box.getChildren().setAll(b1, b2, checkbox);
			}
		});

		return box;
	}

	@Override
	public void terminate()
	{

	}
}
