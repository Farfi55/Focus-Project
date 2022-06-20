package mafiadelprimobanco.focusproject.model;

import javafx.beans.property.*;
import mafiadelprimobanco.focusproject.client.util.JSONUtil;
import mafiadelprimobanco.focusproject.handler.JsonHandler;
import mafiadelprimobanco.focusproject.utils.Language;
import mafiadelprimobanco.focusproject.utils.Theme;
import org.json.JSONObject;

public class Settings
{
	public final SimpleObjectProperty<Language> language = new SimpleObjectProperty<>();
	public final SimpleObjectProperty<Theme> theme = new SimpleObjectProperty<>();
	public final SimpleBooleanProperty navigationDisabledDuringActivity = new SimpleBooleanProperty();
	public final SimpleBooleanProperty isTutorialHidden = new SimpleBooleanProperty();

	public final SimpleDoubleProperty musicVolume = new SimpleDoubleProperty();
	public final SimpleDoubleProperty soundVolume = new SimpleDoubleProperty();

	public final SimpleIntegerProperty minimumTimerDuration = new SimpleIntegerProperty();
	public final SimpleBooleanProperty confirmInterruptTimerActivity = new SimpleBooleanProperty();

	public final SimpleIntegerProperty minimumSuccessfulChronometerDuration = new SimpleIntegerProperty();
	public final SimpleBooleanProperty confirmInterruptChronometerActivity = new SimpleBooleanProperty();

	public final SimpleBooleanProperty areAdvancedOptionsShown = new SimpleBooleanProperty();
	public final SimpleBooleanProperty resetTutorial = new SimpleBooleanProperty();
	public final SimpleBooleanProperty confirmQuitApplication = new SimpleBooleanProperty();

	@Override
	public String toString()
	{
		return '{' + "language:" + language.get().key + ", theme:" + theme.get().key + ", navigationDisabledDuringActivity:"
				+ navigationDisabledDuringActivity.get() + ", isTutorialHidden:" + isTutorialHidden.get() + ", musicVolume:"
				+ musicVolume.get() + ", soundVolume:" + soundVolume.get() + ", minimumTimerDuration:" + minimumTimerDuration.get()
				+ ", confirmInterruptTimerActivity:" + confirmInterruptTimerActivity.get()
				+ ", minimumSuccessfulChronometerDuration:" + minimumSuccessfulChronometerDuration.get()
				+ ", confirmInterruptChronometerActivity:" + confirmInterruptChronometerActivity.get()
				+ ", areAdvancedOptionsShown:" + areAdvancedOptionsShown.get() + ", resetTutorial:" + resetTutorial.get()
				+ ", confirmQuitApplication:" + confirmQuitApplication.get() + '}';
	}

	public Settings()
	{
	}

	public void setListeners()
	{

		//Settings listeners -- Used to keep update the json file

		System.out.println(this);

		language.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		theme.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		navigationDisabledDuringActivity.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		isTutorialHidden.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		musicVolume.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		soundVolume.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		minimumTimerDuration.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		confirmInterruptTimerActivity.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		minimumSuccessfulChronometerDuration.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		confirmInterruptChronometerActivity.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		areAdvancedOptionsShown.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		resetTutorial.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
		confirmQuitApplication.addListener((observableValue, ignored, t1) -> JsonHandler.updateSettingsFile(
				new JSONObject(this.toString())));
	}

}