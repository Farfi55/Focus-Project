package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import mafiadelprimobanco.focusproject.handler.JsonHandler;
import mafiadelprimobanco.focusproject.handler.TagHandler;
import mafiadelprimobanco.focusproject.model.Tag;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.DayOfWeek.MONDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

public class StatisticController implements Controller{

	@FXML private MFXComboBox<String> dateComboBox;

	@FXML private LineChart<String, Number> weekDayChart;

	@FXML private MFXComboBox<String> tagComboBox;

	@FXML private PieChart tagsPieChart;

	@FXML private StackedBarChart<String, Number> totalDataBarChart;
	@FXML private NumberAxis numberAxisBar;

	@FXML private NumberAxis lineChartNumberAxis;

	public StatisticController() { }

	private void setMonthChart(int monthsToSub)
	{
        LocalDate thisMonth = LocalDate.now().withDayOfMonth(1).minusMonths(monthsToSub);
		List<XYChart.Series<String, Number>> series = new ArrayList<>();

		var tags = TagHandler.getInstance().getTags();

		AtomicReference<Long> upperbound = new AtomicReference<>(0L);

		for (int i = 0; i < thisMonth.lengthOfMonth(); ++i)
		{
			LocalDate day = thisMonth.plusDays(i);
			var tagsOfTheDay = JsonHandler.getAllActivitiesBetween(day.atStartOfDay(), day.plusDays(1).atStartOfDay());
            long oldUpperBound = upperbound.get();

			upperbound.set(0L);

			tags.forEach(tag ->
			{
				long seconds = tagsOfTheDay.stream()
						.filter(activity -> tag.equals(activity.getTag()))
						.mapToLong(activity -> activity.getStartTime().until(activity.getEndTime(), ChronoUnit.SECONDS))
						.sum();

					XYChart.Series<String, Number> serie = new XYChart.Series<>();
					serie.getData().add(new XYChart.Data<>(String.valueOf(day.getDayOfMonth()), seconds));
					serie.setName(tag.getName());

					//our upperbound will be the sum of the value
					upperbound.set(upperbound.get() + seconds);

				if (seconds > 0)
				{
					var dataChart = new PieChart.Data(tag.getName(), seconds);

					Platform.runLater(() ->
					{
						dataChart.getNode().setStyle("-fx-pie-color: #" + tag.getColor().toString().substring(2) + ";");
						Tooltip.install(dataChart.getNode(),
								new Tooltip(String.format("%s = %.2f", tag.getName(), dataChart.getPieValue())));
					});


					tagsPieChart.getData().add(dataChart);
				}

				series.add(serie);
			});

			upperbound.set(Math.max(upperbound.get() + 1, oldUpperBound));
		}

		numberAxisBar.upperBoundProperty().set(upperbound.get());
		totalDataBarChart.getData().addAll(series);

		//Load tag color. Used by the staked bar chart
		//TODO: Colors gets mixed somehow. FIX IT
		for (int i = 0; i < tags.size(); ++i)
			for (Node n : totalDataBarChart.lookupAll(".default-color" + i + ".chart-bar"))
			{
				n.setStyle("-fx-bar-fill: #" + ((Tag)tags.toArray()[i]).getColor().toString().substring(2) + ";");
			}
	}


	private void setWeekChart(Tag tag)
	{
		LocalDate today = LocalDate.now();
		LocalDate firstDay = today.with(previousOrSame(MONDAY));
		var activities = JsonHandler.getAllActivitiesBetween(firstDay.atStartOfDay(), LocalDateTime.MAX).stream().filter(
					activity -> tag.equals(activity.getTag()));

		XYChart.Series<String, Number> weekDayChartSeries = new XYChart.Series<>();

		weekDayChartSeries.setName(tag.getName());

		long[] valuesDay = {0, 0, 0, 0, 0, 0, 0};

		activities.forEach(activity ->{
			final int index = activity.getStartTime().getDayOfWeek().ordinal();

			valuesDay[index] += activity.getStartTime().until(activity.getEndTime(),
					ChronoUnit.SECONDS);
		});

		Platform.runLater(() ->
			weekDayChartSeries.getNode().setStyle("-fx-stroke: #"+ tag.getColor().toString().substring(2) + ";")
		);

		weekDayChartSeries.getData().add(new XYChart.Data<>("Lunedi", valuesDay[0]));
		weekDayChartSeries.getData().add(new XYChart.Data<>("Martedi", valuesDay[1]));
		weekDayChartSeries.getData().add(new XYChart.Data<>("Mercoledi", valuesDay[2]));
		weekDayChartSeries.getData().add(new XYChart.Data<>("Giovedi", valuesDay[3]));
		weekDayChartSeries.getData().add(new XYChart.Data<>("Venerdi", valuesDay[4]));
		weekDayChartSeries.getData().add(new XYChart.Data<>("Sabato", valuesDay[5]));
		weekDayChartSeries.getData().add(new XYChart.Data<>("Domenica", valuesDay[6]));

		weekDayChart.getData().add(weekDayChartSeries);
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

		dateComboBox.setOnAction(e -> {
			totalDataBarChart.getData().clear();
			tagsPieChart.getData().clear();
			setMonthChart(LocalDate.now().getMonthValue() - dateComboBox.getSelectedIndex() -1);
		});

		tagComboBox.setOnAction(e -> {
			weekDayChart.getData().clear();
			setWeekChart(
					(Tag)TagHandler.getInstance().getTags().toArray()[tagComboBox.getSelectedIndex()]);
		});

		totalDataBarChart.getYAxis().autoRangingProperty().set(false);
		weekDayChart.legendVisibleProperty().setValue(false);
		tagsPieChart.legendVisibleProperty().setValue(false);

		numberAxisBar.setTickUnit(1.0);
		lineChartNumberAxis.setTickUnit(1.0);
	}

	@Override
	public void terminate()
	{
	}
}

