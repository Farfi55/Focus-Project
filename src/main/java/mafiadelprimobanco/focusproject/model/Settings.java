package mafiadelprimobanco.focusproject.model;

import javafx.beans.property.*;
import mafiadelprimobanco.focusproject.utils.Language;
import mafiadelprimobanco.focusproject.utils.Theme;

public class Settings
{
	public final SimpleObjectProperty<Language> currentLanguage = new SimpleObjectProperty<>();
	public final SimpleObjectProperty<Theme> currentTheme = new SimpleObjectProperty<>();
	public final SimpleBooleanProperty blockNavigation = new SimpleBooleanProperty();
	public final SimpleBooleanProperty hideTutorial = new SimpleBooleanProperty();

	public final SimpleDoubleProperty musicVolume = new SimpleDoubleProperty();
	public final SimpleDoubleProperty soundVolume = new SimpleDoubleProperty();

	public final SimpleIntegerProperty minimumTimerTime = new SimpleIntegerProperty();
	public final SimpleBooleanProperty requestConfirmationOnFinishedTimerActivity = new SimpleBooleanProperty();

	public final SimpleIntegerProperty successfulActivityMinimumChronometerTime = new SimpleIntegerProperty();
	public final SimpleBooleanProperty requestConfirmationOnFinishedChronometerActivity = new SimpleBooleanProperty();

	public final SimpleBooleanProperty showAdvancedOptions = new SimpleBooleanProperty();
	public final SimpleBooleanProperty resetTutorial = new SimpleBooleanProperty();
	public final SimpleBooleanProperty confirmBeforeExit = new SimpleBooleanProperty();

	public Settings()
	{
	}
}
