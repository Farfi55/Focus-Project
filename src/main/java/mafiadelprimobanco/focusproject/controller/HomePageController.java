package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import mafiadelprimobanco.focusproject.ActivityHandler;
import org.kordamp.ikonli.javafx.FontIcon;

public class HomePageController
{
	@FXML
	private MFXButton fullScreenButton;

	@FXML
	private FontIcon fullScreenIcon;

	@FXML
	private MFXComboBox<Node> activityTypeComboBox;

	@FXML
	private MFXComboBox<Node> tagComboBox;

	@FXML
	private Label activityTimeLabel;

	@FXML
	private MFXProgressSpinner progressBarTime;

	@FXML
	private ImageView treeImageViewer;

	@FXML
	private MFXButton startActivityButton;

	@FXML
	void initialize()
	{
	}

	@FXML
	void toggleActivityStatus(ActionEvent event)
	{
		if (!ActivityHandler.getInstance().isActivityStarted())
		{
			startActivityButton.setText("Ferma");
			ActivityHandler.getInstance().startActivity(progressBarTime, activityTimeLabel);
		}
		else
		{
			startActivityButton.setText("Avvia");
			ActivityHandler.getInstance().stopCurrActivity(progressBarTime, activityTimeLabel);
		}
	}

	@FXML
	void setActivityType(ActionEvent event)
	{
	}

	@FXML
	void setTag(ActionEvent event)
	{
	}


    @FXML
    void onFullScreenClick(ActionEvent event) {

    }


}

