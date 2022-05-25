package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Validated;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;

import java.io.IOException;
import java.util.List;

public class RegistrationPageController
{
	private final MFXTextField loginField;
	private final MFXPasswordField passwordField;
	private final MFXTextField firstNameField;
	private final MFXTextField lastNameField;
	private final MFXComboBox<String> genderCombo;
	private final MFXCheckbox checkbox;

	@FXML
	private MFXStepper stepper;

	public RegistrationPageController() {
		loginField = new MFXTextField();
		passwordField = new MFXPasswordField();
		firstNameField = new MFXTextField();
		lastNameField = new MFXTextField();
		genderCombo = new MFXComboBox<>();
		checkbox = new MFXCheckbox("Vuoi confermare i dati?");
	}


	@FXML
	void initialize() {
		loginField.setPromptText("Username");
		loginField.getValidator().constraint("L'username deve essere lungo almeno 6 caratteri", loginField.textProperty().length().greaterThanOrEqualTo(6));
		loginField.setLeadingIcon(new MFXIconWrapper("mfx-user", 16, Color.web("#4D4D4D"), 24));
		passwordField.getValidator().constraint("La password deve essere lunga almeno 8 caratteri", passwordField.textProperty().length().greaterThanOrEqualTo(8));
		passwordField.setPromptText("Password");

		firstNameField.setPromptText("Nome");
		lastNameField.setPromptText("Cognome");
		genderCombo.setItems(FXCollections.observableArrayList("Maschio", "Femmina", "Altro"));

		List<MFXStepperToggle> stepperToggles = createSteps();
		stepper.getStepperToggles().addAll(stepperToggles);
	}

	private List<MFXStepperToggle> createSteps() {
		MFXStepperToggle step1 = new MFXStepperToggle("Step 1", new MFXFontIcon("mfx-lock", 16, Color.web("#f1c40f")));
		VBox step1Box = new VBox(20, wrapNodeForValidation(loginField), wrapNodeForValidation(passwordField));
		step1Box.setAlignment(Pos.CENTER);
		step1.setContent(step1Box);
		step1.getValidator().dependsOn(loginField.getValidator()).dependsOn(passwordField.getValidator());

		MFXStepperToggle step2 = new MFXStepperToggle("Step 2", new MFXFontIcon("mfx-user", 16, Color.web("#49a6d7")));
		VBox step2Box = new VBox(20, firstNameField, lastNameField, genderCombo);
		step2Box.setAlignment(Pos.CENTER);
		step2.setContent(step2Box);

		MFXStepperToggle step3 = new MFXStepperToggle("Step 3", new MFXFontIcon("mfx-variant7-mark", 16, Color.web("#85CB33")));
		Node step3Grid = createGrid();
		step3.setContent(step3Grid);
		step3.getValidator().constraint("Devi confermare i dati", checkbox.selectedProperty());

		return List.of(step1, step2, step3);
	}

	private <T extends Node & Validated> Node wrapNodeForValidation(T node) {
		Label errorLabel = new Label();
		errorLabel.getStyleClass().add("error-label");
		errorLabel.setManaged(false);
		stepper.addEventHandler(MFXStepper.MFXStepperEvent.VALIDATION_FAILED_EVENT, event -> {
			MFXValidator validator = node.getValidator();
			List<Constraint> validate = validator.validate();
			if (!validate.isEmpty()) {
				errorLabel.setText(validate.get(0).getMessage());
			}
		});
		stepper.addEventHandler(MFXStepper.MFXStepperEvent.NEXT_EVENT, event -> errorLabel.setText(""));
		VBox wrap = new VBox(3, node, errorLabel) {
			@Override
			protected void layoutChildren() {
				super.layoutChildren();

				double x = node.getBoundsInParent().getMinX();
				double y = node.getBoundsInParent().getMaxY() + getSpacing();
				double width = getWidth();
				double height = errorLabel.prefHeight(-1);
				errorLabel.resizeRelocate(x, y, width, height);
			}

			@Override
			protected double computePrefHeight(double width) {
				return super.computePrefHeight(width) + errorLabel.getHeight() + getSpacing();
			}
		};
		wrap.setAlignment(Pos.CENTER);
		return wrap;
	}

	private Node createGrid() {
		MFXTextField usernameLabel1 = createLabel("Username: ");
		MFXTextField usernameLabel2 = createLabel("");
		usernameLabel2.textProperty().bind(loginField.textProperty());

		MFXTextField firstNameLabel1 = createLabel("Nome: ");
		MFXTextField firstNameLabel2 = createLabel("");
		firstNameLabel2.textProperty().bind(firstNameField.textProperty());

		MFXTextField lastNameLabel1 = createLabel("Cognome: ");
		MFXTextField lastNameLabel2 = createLabel("");
		lastNameLabel2.textProperty().bind(lastNameField.textProperty());

		MFXTextField genderLabel1 = createLabel("Sesso: ");
		MFXTextField genderLabel2 = createLabel("");
		genderLabel2.textProperty().bind(Bindings.createStringBinding(
				() -> genderCombo.getValue() != null ? genderCombo.getValue() : "",
				genderCombo.valueProperty()
		));

		usernameLabel1.getStyleClass().add("header-label");
		firstNameLabel1.getStyleClass().add("header-label");
		lastNameLabel1.getStyleClass().add("header-label");
		genderLabel1.getStyleClass().add("header-label");

		HBox b1 = new HBox(usernameLabel1, usernameLabel2);
		HBox b2 = new HBox(firstNameLabel1, firstNameLabel2);
		HBox b3 = new HBox(lastNameLabel1, lastNameLabel2);
		HBox b4 = new HBox(genderLabel1, genderLabel2);

		b1.setMaxWidth(Region.USE_PREF_SIZE);
		b2.setMaxWidth(Region.USE_PREF_SIZE);
		b3.setMaxWidth(Region.USE_PREF_SIZE);
		b4.setMaxWidth(Region.USE_PREF_SIZE);

		VBox box = new VBox(10, b1, b2, b3, b4, checkbox);
		box.setAlignment(Pos.CENTER);
		StackPane.setAlignment(box, Pos.CENTER);

		stepper.setOnLastNext(event -> {
			try
			{
				SceneHandler.getInstance().navTo(FXMLReferences.HOME);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
		stepper.setOnBeforePrevious(event -> {
			if (stepper.isLastToggle()) {
				checkbox.setSelected(false);
				box.getChildren().setAll(b1, b2, b3, b4, checkbox);
			}
		});

		return box;
	}

	private MFXTextField createLabel(String text) {
		MFXTextField label = MFXTextField.asLabel(text);
		label.setAlignment(Pos.CENTER_LEFT);
		label.setPrefWidth(200);
		label.setMinWidth(Region.USE_PREF_SIZE);
		label.setMaxWidth(Region.USE_PREF_SIZE);
		return label;
	}
}
