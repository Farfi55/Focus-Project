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

public class HomeController {

    @FXML
    private Label activityTimeLabel;

    @FXML
    private MFXButton actionBtn;

    @FXML
    private MFXComboBox<Node> activitySelectorComboBox;

    @FXML
    private FontIcon fullScreenBtn;

    @FXML
    private MFXProgressSpinner progressBarTime;

    @FXML
    private MFXComboBox<Node> tagChooserComboBox;

    @FXML
    private ImageView treeImageViewer;

    @FXML
    void doAction(ActionEvent event)
    {
        if (!ActivityHandler.getInstance().isActivityStarted())
        {
            actionBtn.setText("Ferma");
            ActivityHandler.getInstance().startActivity(progressBarTime,activityTimeLabel);
        }
        else
        {
            actionBtn.setText("Avvia");
            ActivityHandler.getInstance().stopCurrActivity(progressBarTime,activityTimeLabel);
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
    void initialize()
    {
    }
}

