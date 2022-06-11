package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Validated;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.handler.AutentificationHandler;
import mafiadelprimobanco.focusproject.handler.Localization;
import mafiadelprimobanco.focusproject.handler.PagesHandler;
import mafiadelprimobanco.focusproject.model.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationPageController implements Controller
{
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

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		loginField.setPromptText("Email");
		loginField.getValidator().constraint(Localization.get("registration.emailNotNull"),
				loginField.textProperty().isNotEmpty());
		loginField.setLeadingIcon(new MFXIconWrapper("mfx-user", 16, Color.web("#4D4D4D"), 24));
		passwordField.getValidator().constraint(Localization.get("registration.passwordLengthError"),
				passwordField.textProperty().length().greaterThanOrEqualTo(8));
		passwordField.setPromptText("Password");

		usernameField.setPromptText("Username");

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
		MFXTextField usernameLabel1 = createLabel("Email: ");
		MFXTextField usernameLabel2 = createLabel("");
		usernameLabel2.textProperty().bind(loginField.textProperty());

		MFXTextField firstNameLabel1 = createLabel("Username: ");
		MFXTextField firstNameLabel2 = createLabel("");
		firstNameLabel2.textProperty().bind(usernameField.textProperty());


		usernameLabel1.getStyleClass().add("header-label");
		firstNameLabel1.getStyleClass().add("header-label");

		HBox b1 = new HBox(usernameLabel1, usernameLabel2);
		HBox b2 = new HBox(firstNameLabel1, firstNameLabel2);

		b1.setMaxWidth(Region.USE_PREF_SIZE);
		b2.setMaxWidth(Region.USE_PREF_SIZE);

		VBox box = new VBox(10, b1, b2, checkbox);
		box.setAlignment(Pos.CENTER);
		StackPane.setAlignment(box, Pos.CENTER);

		stepper.setOnLastNext(event ->
		{
			User user = new User(loginField.getText(), usernameField.getText(), passwordField.getText());
			AutentificationHandler.getInstance().registerUser(user);
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

	private MFXTextField createLabel(String text)
	{
		MFXTextField label = MFXTextField.asLabel(text);
		label.setAlignment(Pos.CENTER_LEFT);
		label.setPrefWidth(200);
		label.setMinWidth(Region.USE_PREF_SIZE);
		label.setMaxWidth(Region.USE_PREF_SIZE);
		return label;
	}

	@Override
	public void terminate()
	{

	}
}
