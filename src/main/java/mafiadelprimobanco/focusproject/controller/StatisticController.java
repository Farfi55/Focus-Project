package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import mafiadelprimobanco.focusproject.handler.JsonHandler;
import mafiadelprimobanco.focusproject.handler.TagHandler;
import mafiadelprimobanco.focusproject.model.Tag;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.time.DayOfWeek.MONDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

public class StatisticController implements Controller{

	@FXML private MFXComboBox<String> dateComboBox;

	@FXML private LineChart<String, Number> weekDayChart;

	@FXML private MFXComboBox<String> tagComboBox;

	@FXML private PieChart tagsPieChart;

	@FXML private StackedBarChart<String, Number> totalDataBarChart;

	public StatisticController() { }

	private void setPieChart()
	{
		TagHandler.getInstance().getTags().forEach(tag ->
		{
			long totalTime = JsonHandler.getAllActivitiesBetween(LocalDateTime.MIN, LocalDateTime.MAX).stream().filter(
					activity -> tag.equals(activity.getTag())).mapToLong(activity -> activity.getStartTime()
					.until(activity.getEndTime(), ChronoUnit.SECONDS)).sum();

			if (totalTime > 0) tagsPieChart.getData().add(new PieChart.Data(tag.getName(), totalTime));
		});
	}

	private void setStackedBarChart()
	{
        LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
		List<XYChart.Series<String, Number>> series = new ArrayList<>();

		var tags = TagHandler.getInstance().getTags();

		for (int i = 0; i < thisMonth.lengthOfMonth(); ++i)
		{
			LocalDate day = thisMonth.plusDays(i);
			var tagsOfTheDay = JsonHandler.getAllActivitiesBetween(day.atStartOfDay(), day.plusDays(1).atStartOfDay());

			tags.forEach(tag ->
			{
				long seconds = tagsOfTheDay.stream()
						.filter(activity -> tag.equals(activity.getTag()))
						.mapToLong(activity -> activity.getStartTime().until(activity.getEndTime(), ChronoUnit.SECONDS))
						.sum();

					XYChart.Series<String, Number> serie = new XYChart.Series<>();
					serie.getData().add(new XYChart.Data<>(String.valueOf(day.getDayOfMonth()), seconds));
					serie.setName(tag.getName());
					series.add(serie);
			});
		}

		totalDataBarChart.getData().addAll(series);
	}


	private void setBarChart(Tag tag)
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
		TagHandler.getInstance().getTags().forEach(tag -> tagComboBox.getItems().add(tag.getName()));
		setPieChart();
		setStackedBarChart();
		setBarChart(TagHandler.getInstance().getTag(0));
	}

	@Override
	public void terminate()
	{
	}
}

