package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import mafiadelprimobanco.focusproject.ActivityHandler;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import org.kordamp.ikonli.javafx.FontIcon;

public class HomeController {

    @FXML
    private MFXTextField activityTimeTextField;

    @FXML
    private MFXButton actionBtn;

    @FXML
    private MFXComboBox<AnchorPane> activitySelectorComboBox;

    @FXML
    private FontIcon fullScreenBtn;

    @FXML
    private MFXProgressSpinner progressBarTime;

    @FXML
    private MFXComboBox<AnchorPane> tagChooserComboBox;

    @FXML
    private ImageView treeImageViewer;

    public void onStopActivityEvent()
    {
        Platform.runLater(() ->
        {
            actionBtn.setText("Avvia");
            activityTimeTextField.setText("00:00");
            progressBarTime.setProgress(0.0);
        });
    }

    public void onTimerUpdateTick()
    {
        Platform.runLater(() ->
        {
            progressBarTime.setProgress(progressBarTime.getProgress() - ActivityHandler.getInstance().getCurrentProgressBarTick());
            activityTimeTextField.setText(ActivityHandler.getInstance().getCurrentTimeTick());
        });
    }
    public void onChronometerUpdateTick(){

        Platform.runLater(() ->
        {
            final double progressbarTick = ActivityHandler.getInstance().getCurrentProgressBarTick();
            final double currProgressValue = progressBarTime.getProgress() + progressbarTick;

            if (currProgressValue < 1.0)
                progressBarTime.setProgress(currProgressValue);
            else
                progressBarTime.setProgress(0.0);

            activityTimeTextField.setText(ActivityHandler.getInstance().getCurrentTimeTick());
        });
    }

    @FXML
    void doAction(ActionEvent event)
    {
        if (!ActivityHandler.getInstance().isActivityStarted())
        {
            actionBtn.setText("Ferma");
            activityTimeTextField.editableProperty().setValue(false);
            ActivityHandler.getInstance().startActivity();
        }
        else
        {
            activityTimeTextField.editableProperty().setValue(true);
            ActivityHandler.getInstance().stopCurrActivity();
        }
    }

    @FXML
    void setActivityType(ActionEvent event)
    { }

    @FXML
    void setTag(ActionEvent event)
    { }

    @FXML
    void setTime(KeyEvent event)
    {


        /*if (event.getCode().equals(KeyCode.ENTER))
        {
            if (text.length() == 2) activityTimeTextField.setText(text + ":00");
            ActivityHandler.getInstance().setExecutionTime();
            return;
        }

        if (text.length() > 4)
        {
            activityTimeTextField.setText(text.substring(0,4));
            return;
        }

        if (text.length() >= 2)
        {
            activityTimeTextField.setText(text + ":");
        }
        else
        {
            activityTimeTextField.setText(text.replace(":", ""));
        }*/
    }


    @FXML
    void initialize()
    {
        /*TagHandler.getInstance().addTagView(new TagView( new Tag ("ciao", Color.RED)));
        tagChooserComboBox.setItems(TagHandler.getInstance().getTags());*/

        ActivityHandler.getInstance().addListener(new ActivityObserver() {
            @Override
            public void onStart()
            {
                switch (ActivityHandler.getInstance().getCurrActivityType())
                {
                    case CRONO -> progressBarTime.setProgress(0.0);
                    case TIMER -> progressBarTime.setProgress(1.0);
                }
            }

            @Override
            public void onUpdate()
            {
                switch (ActivityHandler.getInstance().getCurrActivityType())
                {
                    case CRONO -> onChronometerUpdateTick();
                    case TIMER -> onTimerUpdateTick();
                }
            }

            @Override
            public void onEnd() { onStopActivityEvent(); }
        });

        activityTimeTextField.textProperty().addListener((e) ->
        {
            if (ActivityHandler.getInstance().isActivityStarted()) return;

            String text = activityTimeTextField.getText();

            ActivityHandler.getInstance().setExecutionTime(Integer.parseInt(text));
        });
    }
}

