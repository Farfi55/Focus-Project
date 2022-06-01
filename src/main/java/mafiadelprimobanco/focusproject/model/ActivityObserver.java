package mafiadelprimobanco.focusproject.model;

import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;

public interface ActivityObserver
{

	/**
	 * make sure to use Platform.runLater if you need to change a view
	 * @param currentActivity
	 */
	default void onActivityStarting(AbstractActivity currentActivity) { }

	/**
	 * make sure to use Platform.runLater if you need to change a view
	 * @param currentActivity
	 */
	default void onActivityUpdate(AbstractActivity currentActivity) { }

	/**
	 * make sure to use Platform.runLater if you need to change a view
	 * @param currentActivity
	 */
	default void onActivityEnd(AbstractActivity currentActivity) { }

	/**
	 * Thread-safe version of onActivityStart()
	 * @param currentActivity
	 */
	default void onActivityStartingSafe(AbstractActivity currentActivity) { }

	/**
	 * Thread-safe version of onActivityUpdate()
	 * @param currentActivity
	 */
	default void onActivityUpdateSafe(AbstractActivity currentActivity) { }

	/**
	 * Thread-safe version of onActivityEnd()
	 * @param currentActivity
	 */
	default void onActivityEndSafe(AbstractActivity currentActivity) { }
}
