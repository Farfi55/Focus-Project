package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import mafiadelprimobanco.focusproject.ActivityHandler;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.Tag;
import org.kordamp.ikonli.javafx.FontIcon;

public class HomeController {

    @FXML
    private MFXTextField activityTimeTextField;

    @FXML
    private MFXButton actionBtn;

    @FXML
    private MFXComboBox<String> activitySelectorComboBox;

    @FXML
    private FontIcon fullScreenBtn;

    @FXML
    private SplitPane splitPaneView;

    @FXML
    private MFXProgressSpinner progressBarTime;

    @FXML
    private VBox tagSidebar;

    @FXML
    private ImageView treeImageViewer;

    public void onStopActivityEvent()
    {
        Platform.runLater(() ->
        {
            actionBtn.setText("Avvia");
            activityTimeTextField.editableProperty().setValue(true);
            activityTimeTextField.setText("00:00");
            progressBarTime.setProgress(0.0);
            tagSidebar.setVisible(true);
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
            ActivityHandler.getInstance().startActivity();
        }
        else
        {
            ActivityHandler.getInstance().stopCurrActivity();
        }
    }

    @FXML
    void setActivityType(ActionEvent event)
    {
        ActivityHandler.getInstance().setActivityType(ActivityType.values()[activitySelectorComboBox.getSelectedIndex()]);
        System.out.println(ActivityHandler.getInstance().getCurrActivityType());
    }

    @FXML
    void setTag(ActionEvent event)
    {
        //System.out.println(tagChooserComboBox.getSelectedIndex());
    }

    @FXML
    void setTime(KeyEvent event)
    {
    }


    @FXML
    void initialize()
    {
        for (Tag tag : TagHandler.getInstance().tags)
        {
            // todo: load new view using sceneHandler or something
            var tagController = new TagController();
            tagController.setTag(tag);
            tagSidebar.getChildren().add(tagController);
        }


        activitySelectorComboBox.getItems().addAll("Chronometer", "Timer", "Tomato");

        activitySelectorComboBox.selectFirst();

        ActivityHandler.getInstance().addListener(new ActivityObserver() {
            @Override
            public void onStart()
            {
                actionBtn.setText("Ferma");
                activityTimeTextField.editableProperty().setValue(false);

                splitPaneView.setDividerPositions(1.0);

                //tagSidebar.setVisible(false);

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

            int len = text.length();

            if (len == 0) return;

            int minutes = 0;
            int seconds = 0;

            /*TODO fix focus shit to make that working again */
            /*
                if (len == 3)
                    activityTimeTextField.setText(text.substring(0, len-2) + ":" + text.substring(len - 2, len));
            */

            if (len >= 4)
            {
                minutes = Integer.parseInt(text.split(":")[0]);
                seconds = Integer.parseInt(text.split(":")[1]);
            }else
                seconds = Integer.parseInt(text);

            if (seconds > 59)
            {
                SceneHandler.getInstance().showErrorMessage("Errore", "Errore nella formattazione dei secondi.\n" +
                                                                                   "Prova ad inserire un valore inferiore a 59");
                return;
            }

            ActivityHandler.getInstance().setExecutionTime(minutes * 60 + seconds);
        });

    }
}

