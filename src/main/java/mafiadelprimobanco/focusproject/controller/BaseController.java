package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXTooltip;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import mafiadelprimobanco.focusproject.ActivityHandler;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseController implements ActivityObserver
{
	List<MFXRectangleToggleNode> navButtons = new ArrayList<>();
	@FXML private AnchorPane root;
	@FXML private StackPane contentRoot;
	@FXML private MFXRectangleToggleNode homeButton;
	@FXML private MFXRectangleToggleNode progressButton;
	@FXML private MFXRectangleToggleNode statisticsButton;
	@FXML private MFXRectangleToggleNode tagButton;
	@FXML private MFXRectangleToggleNode infoButton;
	@FXML private MFXRectangleToggleNode accountButton;
	@FXML private MFXRectangleToggleNode settingsButton;
	@FXML private ToggleGroup nav_toggle_group;
	private boolean canNavigate = true;

	@FXML
	void initialize()
	{
//		SceneHandler.getInstance().setContentRoot(contentRoot);
		MFXTooltip.of(homeButton, "Home page (ctrl+H)").install();
		MFXTooltip.of(progressButton, "Progress page (ctrl+P)").install();
		MFXTooltip.of(statisticsButton, "Statistics page (ctrl+S)").install();
		MFXTooltip.of(tagButton, "Tag page (ctrl+T)").install();
		MFXTooltip.of(infoButton, "Help (ctrl+I)").install();
		MFXTooltip.of(accountButton, "Account settings page (ctrl+U)").install();
		MFXTooltip.of(settingsButton, "Settings page (ctrl+,)").install();
		//
		//		navButtons = nav_toggle_group.getToggles();
		navButtons.add(homeButton);
		navButtons.add(progressButton);
		navButtons.add(statisticsButton);
		navButtons.add(tagButton);
		navButtons.add(infoButton);
		navButtons.add(accountButton);
		navButtons.add(settingsButton);
		root.setOnKeyPressed(this::handleKeyPress);
		setNavigationEnabled(true);
		ActivityHandler.getInstance().addListener(this);

		onHomeClick();
	}

	private void handleKeyPress(KeyEvent keyEvent)
	{
		if (keyEvent.isControlDown() || keyEvent.isShortcutDown())
		{
			System.out.println(keyEvent.getCode());
			switch (keyEvent.getCode())
			{
				case DIGIT1, H -> onHomeClick();
				case DIGIT2, P -> onProgressClick();
				case DIGIT3, S -> onStatisticsClick();
				case DIGIT4, T -> onTagClick();
				case DIGIT5, I -> onInfoClick();
				case DIGIT6, U -> onAccountClick();
				case DIGIT7, COMMA -> onSettingsClick();
				case DIGIT0 -> setNavigationEnabled(!canNavigate);
				// todo remove: for debug only
			}
		}
		else if (keyEvent.getCode() == KeyCode.F1) onInfoClick();
	}

	@FXML
	void onHomeClick()
	{
		navigateTo(FXMLReferences.HOME, homeButton, "Home");
	}

	@FXML
	void onProgressClick()
	{
		navigateTo(FXMLReferences.PROGRESS, progressButton, "Progressi");
	}

	@FXML
	void onStatisticsClick()
	{
		navigateTo(FXMLReferences.STATISTICS, statisticsButton, "Statistiche");
	}

	@FXML
	void onTagClick()
	{
		navigateTo(FXMLReferences.TAGS, tagButton, "Tag");
	}

	@FXML
	void onInfoClick()
	{
		navigateTo(FXMLReferences.INFO, infoButton, "Informazioni");
	}

	@FXML
	void onAccountClick()
	{
		navigateTo(FXMLReferences.ACCOUNT, accountButton, "Account");
	}

	@FXML
	void onSettingsClick()
	{
		navigateTo(FXMLReferences.SETTINGS, settingsButton, "Impostazioni");
	}

	private boolean navigateTo(String pagePathRef, MFXRectangleToggleNode button, String pageName)
	{
		// A bit of a hack but whatever
		// todo: to be added to the if once pages refs are defined:
		// && !pagePathRef.equals(FXMLReferences.SETTINGS)
		if (!canNavigate)
		{
			SceneHandler.getInstance().showInfoMessage("Navigazione disabilitata",
					"La navigazione è disabilitata una volta iniziata l'attività");
			return false;
		}
		try
		{
			var page = SceneHandler.getInstance().loadPage(pagePathRef);
			contentRoot.getChildren().setAll(page);
			button.setSelected(true);
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			SceneHandler.getInstance().showErrorMessage("Errore Di caricamento",
					"Impossibile caricare la pagina " + pageName);
			return false;
		}
	}

	@Override
	public void onActivityStart()
	{
		System.out.println(ActivityHandler.getInstance().getCurrActivityType());
		switch (ActivityHandler.getInstance().getCurrActivityType())
		{
			case TIMER, TOMATO_TIMER -> setNavigationEnabled(false);
		}
	}

	@Override
	public void onActivityEnd()
	{
		setNavigationEnabled(true);
	}

	private void setNavigationEnabled(boolean value)
	{
		System.out.println(value);
		canNavigate = value;
		for (MFXRectangleToggleNode button : navButtons)
		{
			button.setDisable(!value);
		}
		settingsButton.setDisable(false);
	}


}
