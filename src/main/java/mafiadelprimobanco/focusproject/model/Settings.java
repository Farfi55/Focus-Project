package mafiadelprimobanco.focusproject.model;

import javafx.beans.property.*;
import mafiadelprimobanco.focusproject.utils.Language;
import mafiadelprimobanco.focusproject.utils.Theme;

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

	public Settings()
	{
	}
}
