package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import mafiadelprimobanco.focusproject.handler.ActivityStatsHandler;
import mafiadelprimobanco.focusproject.handler.Localization;
import mafiadelprimobanco.focusproject.handler.SettingsHandler;
import mafiadelprimobanco.focusproject.handler.TagHandler;
import mafiadelprimobanco.focusproject.model.Settings;
import mafiadelprimobanco.focusproject.model.Tag;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StatisticPageController implements Controller
{

	@FXML private MFXComboBox<String> dateComboBox;

	@FXML private LineChart<String, Number> weekDayChart;

	@FXML private MFXComboBox<String> tagComboBox;

	@FXML private PieChart tagsPieChart;

	@FXML private StackedBarChart<String, Number> totalDataBarChart;

	@FXML private NumberAxis lineChartNumberAxis;
	@FXML private CategoryAxis dayAxisStackedBar;
	@FXML private NumberAxis numberAxisBar;
	@FXML private CategoryAxis weekAxisLineChart;

	public StatisticPageController() { }

	private void setPieChart(Tag tag, long seconds)
	{
		var dataChart = tagsPieChart.getData().stream().filter(piece -> piece.getName().equals(tag.getName())).toList();

		if (dataChart.size() > 0)
		{
			var newVal = dataChart.get(0).getPieValue() + seconds;
			dataChart.get(0).setPieValue(newVal);
		}
		else
		{
			var newDataChart = new PieChart.Data(tag.getName(), seconds);

			setPieColor(tag, newDataChart);

			tagsPieChart.getData().add(newDataChart);
		}
	}

	private void setPieColor(Tag tag, PieChart.Data newDataChart)
	{
		Platform.runLater(() ->
		{
			newDataChart.getNode().setStyle("-fx-pie-color: #" + tag.getColor().toString().substring(2) + ";");
			Tooltip.install(newDataChart.getNode(),
					new Tooltip(String.format("%s = %.2f", tag.getName(), newDataChart.getPieValue())));
		});
	}

	private void setMonthChart(int monthsToSub)
	{
		var tags = TagHandler.getInstance().getTags();

		LocalDate thisMonth = LocalDate.now().withDayOfMonth(1).minusMonths(monthsToSub);

		TreeMap<Integer, XYChart.Series<String, Number>> tagSeries = new TreeMap<>();

		List<Long> upperbound = new ArrayList<>();
		List<Boolean> tagUsed = new ArrayList<>();

		for (int i = 0; i < thisMonth.lengthOfMonth(); ++i) upperbound.add(0L);

		tags.forEach(tag ->
		{
			var ActivitiesMapDay = ActivityStatsHandler.getInstance().getAllActivitiesBetweenWithTag(
					thisMonth.atStartOfDay(), thisMonth.plusDays(thisMonth.lengthOfMonth() - 1).atStartOfDay(), tag);

			tagSeries.put(tag.getUuid(), new XYChart.Series<>());
			var serie = tagSeries.get(tag.getUuid());

			for (int day = 1; day <= thisMonth.lengthOfMonth(); ++day)
			{
				serie.getData().add(new XYChart.Data<>(String.valueOf(day), 0));
			}

			tagUsed.add(false);

			ActivitiesMapDay.forEach(activity ->
			{

				var activityDay = activity.getStartTime().getDayOfMonth();
				var seconds = activity.getFinalDuration();
				var oldValue = serie.getData().get(activityDay - 1).getYValue().intValue();
				var newVal = seconds + oldValue;
				var idx = activityDay - 1;

				upperbound.set(idx, upperbound.get(idx) + seconds);

				serie.getData().get(idx).setYValue(newVal);
				serie.setName(tag.getName());

				if (seconds > 0)
					setPieChart(tag, seconds);

				tagUsed.set(tagUsed.size() - 1, true);
			});

			totalDataBarChart.getData().add(tagSeries.get(tag.getUuid()));
		});


		Long bound = upperbound.stream().max(Long::compareTo).get();

		numberAxisBar.setTickUnit(bound > 100 ? (int)(bound / 10) : 1);
		numberAxisBar.setUpperBound(bound + 1);

		setBarStyle(tags, tagUsed);
	}



	private void setBarStyle(Collection<Tag> tags, List<Boolean> tagUsed)
	{
		//Load tag color. Used by the staked bar chart

		for (int i = 0; i < tags.size(); ++i)
		{
			Tag t = ((Tag)tags.toArray()[i]);
			//totalDataBarChart.lookup(".default-color" + i + ".chart-bar").setStyle("-fx-bar-fill: #" + t.getColor().toString().substring(2) + ";");
			for (Node n : totalDataBarChart.lookupAll(".default-color" + i + ".chart-bar"))
			{
				n.setStyle("-fx-bar-fill: #" + t.getColor().toString().substring(2) + ";");
			}
		}
	}


	private void setWeekChart(Tag tag)
	{
		LocalDate today = LocalDate.now();
		LocalDate firstDay = today.minusDays(28);
		var activities = ActivityStatsHandler.getInstance().getAllActivitiesBetweenWithTag(firstDay.atStartOfDay(),
				LocalDateTime.MAX, tag);

		XYChart.Series<String, Number> weekDayChartSeries = new XYChart.Series<>();

		weekDayChartSeries.setName(tag.getName());

		long[] valuesDay = {0, 0, 0, 0, 0, 0, 0};

		activities.forEach(activity ->
		{
			final int index = activity.getStartTime().getDayOfWeek().ordinal();

			valuesDay[index] += activity.getStartTime().until(activity.getEndTime(), ChronoUnit.SECONDS);
		});

		Platform.runLater(() ->
		{
			String color = tag.getColor().toString().substring(2);

			for (Node n : weekDayChart.lookupAll(".series0"))
				n.setStyle("-fx-background-color: #" + color + ";");

			weekDayChartSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #" + color + ";");
		});

		DateFormatSymbols symbols = new DateFormatSymbols(SettingsHandler.getInstance().getSettings().language.get().language);
		String[] dayNames = symbols.getShortWeekdays();

		for (int day = 0; day < 7; day++)
		weekDayChartSeries.getData().add(new XYChart.Data<>(dayNames[((day+1)%7) + 1], valuesDay[day]));


		weekDayChart.getData().add(weekDayChartSeries);
	}

	@FXML void selectTagToShow() {
		weekDayChart.getData().clear();
		setWeekChart((Tag)TagHandler.getInstance().getTags().toArray()[tagComboBox.getSelectedIndex()]);
	}

	@FXML void setMonth() {
		totalDataBarChart.getData().clear();
		tagsPieChart.getData().clear();
		setMonthChart(LocalDate.now().getMonthValue() - dateComboBox.getSelectedIndex() - 1);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		var tags = TagHandler.getInstance().getTags();

		tags.forEach(tag -> tagComboBox.getItems().add(tag.getName()));
		tagComboBox.selectFirst();

		var months = new DateFormatSymbols();
		for (int i = 0; i < LocalDate.now().getMonthValue(); ++i)
			dateComboBox.getItems().add(months.getMonths()[i]);

		//select the current month as first data to display
		dateComboBox.selectIndex(LocalDate.now().getMonthValue() - 1);

		dayAxisStackedBar.labelProperty().setValue(Localization.get("statistics.chart.day"));
		numberAxisBar.labelProperty().setValue(Localization.get("statistics.chart.seconds"));
		weekAxisLineChart.labelProperty().setValue(Localization.get("statistics.chart.day"));
		lineChartNumberAxis.labelProperty().setValue(Localization.get("statistics.chart.seconds"));

		dateComboBox.floatingTextProperty().setValue(Localization.get("statistics.monthRange"));
		tagComboBox.floatingTextProperty().setValue(Localization.get("statistics.tagWeekRange"));

		totalDataBarChart.getYAxis().setAutoRanging(false);
		totalDataBarChart.legendVisibleProperty().set(false);

		weekDayChart.legendVisibleProperty().setValue(false);
		tagsPieChart.legendVisibleProperty().setValue(false);

		lineChartNumberAxis.setTickUnit(1.0);
	}

	@Override
	public void terminate()
	{
	}
}

