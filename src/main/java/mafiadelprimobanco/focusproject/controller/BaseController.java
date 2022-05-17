package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXTooltip;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;

import java.util.ArrayList;
import java.util.List;

public class BaseController
{
	List<MFXRectangleToggleNode> navButtons = new ArrayList<>();
	@FXML private AnchorPane root;
	@FXML private StackPane contentRoot;
	@FXML private MFXRectangleToggleNode homeButton;
	@FXML private MFXRectangleToggleNode progressButton;
	@FXML private MFXRectangleToggleNode statisticsButton;
	@FXML private MFXRectangleToggleNode tagButton;
	@FXML private MFXRectangleToggleNode infoButton;
	@FXML private MFXRectangleToggleNode userButton;
	@FXML private MFXRectangleToggleNode settingsButton;
	@FXML private ToggleGroup nav_toggle_group;
	private boolean canNavigate = true;

	@FXML
	void initialize()
	{
		SceneHandler.getInstance().setContentRoot(contentRoot);

		MFXTooltip.of(homeButton, "Home page (ctrl+H)").install();
		MFXTooltip.of(progressButton, "Progress page (ctrl+P)").install();
		MFXTooltip.of(statisticsButton, "Statistics page (ctrl+S)").install();
		MFXTooltip.of(tagButton, "Tag page (ctrl+T)").install();
		MFXTooltip.of(infoButton, "Help (ctrl+I)").install();
		MFXTooltip.of(userButton, "Account settings page (ctrl+U)").install();
		MFXTooltip.of(settingsButton, "Settings page (ctrl+,)").install();
		navButtons.add(homeButton);
		navButtons.add(progressButton);
		navButtons.add(statisticsButton);
		navButtons.add(tagButton);
		navButtons.add(infoButton);
		navButtons.add(userButton);
		navButtons.add(settingsButton);
		root.setOnKeyPressed(this::handleKeyPress);
	}

	private void handleKeyPress(KeyEvent keyEvent)
	{
		if (keyEvent.isControlDown() || keyEvent.isShortcutDown())
		{
			switch (keyEvent.getCode())
			{
				case DIGIT1, H -> onNavHomeClick(null);
				case DIGIT2, P -> onNavProgressClick(null);
				case DIGIT3, S -> onNavStatisticsClick(null);
				case DIGIT4, T -> onNavTagClick(null);
				case DIGIT5, I -> onNavInfoClick(null);
				case DIGIT6, U -> onNavUserClick(null);
				case DIGIT7, COMMA -> onNavSettingsClick(null);
			}
		}
		else if (keyEvent.getCode() == KeyCode.F1) onNavInfoClick(null);
	}

	void setNavigationEnabled(boolean value){
		canNavigate = value;
		for (var button: navButtons)
			button.setDisable(value);
	}

	@FXML
	void onNavHomeClick(ActionEvent event)
	{
		navTo(FXMLReferences.HOME, "Home");
		homeButton.setSelected(true);
	}

	@FXML
	void onNavProgressClick(ActionEvent event)
	{
		navTo(FXMLReferences.PROGRESS, "Progressi");
		progressButton.setSelected(true);
	}

	@FXML
	void onNavStatisticsClick(ActionEvent event)
	{
		navTo(FXMLReferences.STATISTICS, "Statistiche");
		statisticsButton.setSelected(true);
	}

	@FXML
	void onNavTagClick(ActionEvent event)
	{
		navTo(FXMLReferences.TAGS, "Tag");
		tagButton.setSelected(true);
	}

	@FXML
	void onNavInfoClick(ActionEvent event)
	{
		// todo: implement
		infoButton.setSelected(true);
	}

	@FXML
	void onNavUserClick(ActionEvent event)
	{
		// todo: implement
		userButton.setSelected(true);
	}

	@FXML
	void onNavSettingsClick(ActionEvent event)
	{
		// todo: implement
		settingsButton.setSelected(true);
	}

	private void navTo(String pagePathRef, String pageName)
	{
		if(!canNavigate)
		{
			// todo: give feedback
			return;
		}
		/* disabled for now
		try
		{
			SceneHandler.getInstance().loadPage(pagePathRef);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			SceneHandler.getInstance().showErrorMessage("Errore Di caricamento",
					"Impossibile caricare la pagina " + pageName);
		}
		*/
	}


}
