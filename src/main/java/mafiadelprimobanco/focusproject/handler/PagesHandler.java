package mafiadelprimobanco.focusproject.handler;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import mafiadelprimobanco.focusproject.Localization;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.Page;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static mafiadelprimobanco.focusproject.utils.FXMLReferences.*;

public class PagesHandler
{
	public static final Page home = new Page(0, HOME, "homePage.name", true, true, KeyCode.H);
	public static final Page progress = new Page(1, PROGRESS, "progressPage.name", false, false, KeyCode.P);
	public static final Page statistics = new Page(2, STATISTICS, "statisticsPage.name", false, false, KeyCode.S);
	public static final Page info = new Page(3, INFO, "infoPage.name", false, false, KeyCode.I);
	public static final Page registration = new Page(4, REGISTRATION, "RegistrationPage.name", false, false, KeyCode.R);
	public static final Page settings = new Page(5, SETTINGS, "settingsPage.name", true, false, KeyCode.COMMA);
	private static final SimpleBooleanProperty isNavigationEnabled;
	private static final SimpleObjectProperty<Page> currentPage;
	private static List<Page> pages;

	static
	{
		currentPage = new SimpleObjectProperty<>();
		isNavigationEnabled = new SimpleBooleanProperty(true);
		initPages();

		subscribeToEvents();
	}

	private static void initPages()
	{
		pages = new ArrayList<>();
		pages.add(home);
		pages.add(progress);
		pages.add(statistics);
		pages.add(info);
		pages.add(registration);
		pages.add(settings);
	}

	private static void subscribeToEvents()
	{
		ActivityHandler.getInstance().addListener(new ActivityObserver()
		{
			@Override
			public void onActivityStarting(AbstractActivity currentActivity)
			{
				disableNavigation();
			}

			@Override
			public void onActivityEndSafe(AbstractActivity currentActivity)
			{
				enableNavigation();
			}
		});
	}

	private static void enableNavigation()
	{
		isNavigationEnabled.set(true);
	}

	private static void disableNavigation()
	{
		// TODO Move to settings
		//  and listen for changes
		boolean isNavigationDisabledOnActivityRunning = true;

		if (ActivityHandler.getInstance().getCurrentActivityType() != ActivityType.CHRONOMETER
				&& isNavigationDisabledOnActivityRunning)
		{
			isNavigationEnabled.set(false);
		}
	}

	public static void navigateTo(Page page)
	{
		if (!isNavigationEnabled.get() && !page.isNavigationAlwaysEnabled().get())
		{
			Feedback.getInstance().showNotification(Localization.get("error.navigationDisabled.header"),
					Localization.get("error.navigationDisabled.message"));
		}
		else if (page.isSelected().get())
		{
			// not sure if we should give feedback here
			// Feedback.getInstance().showNotification(Localization.get("error.pageAlreadySelected.header"),
			// Localization.get("error.pageAlreadySelected.message"));
		}
		else if (page.FXMLPath().isEmpty())
		{
			return;
		}
		else
		{
			loadPage(page);
		}
	}

	private static void loadPage(Page page)
	{
		try
		{
			// unload current page
			if (currentPage.get() != null)
			{
				currentPage.get().isSelected().set(false);

				if (!currentPage.get().keepInBackground().get())
				{
					currentPage.get().controller().get().terminate();
					currentPage.get().controller().set(null);
					currentPage.get().pageRoot().set(null);
				}
			}

			// we only need to load the page from the disk if it isn't present in background
			if (page.controller().get() == null || !page.keepInBackground().get())
			{
				SceneHandler.getInstance().loadPage(page);
			}
			else
			{
				SceneHandler.getInstance().showPage(page);
			}

			page.isSelected().set(true);
			currentPage.set(page);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Feedback.getInstance().showError(Localization.get("error.loadingPage.header"),
					Localization.get("error.loadingPage.message", Localization.get(page.pageNameKey())));

//			loadPage(home);
		}
	}

	public static SimpleBooleanProperty isNavigationEnabledProperty()
	{
		return isNavigationEnabled;
	}

	public static List<Page> getPages()
	{
		return pages;
	}

	public static Page getCurrentPage()
	{
		return currentPage.get();
	}
	public static SimpleObjectProperty<Page> getCurrentPagePropriety()
	{
		return currentPage;
	}

	public static boolean isNavigationEnabled()
	{
		return isNavigationEnabled.get();
	}


}
