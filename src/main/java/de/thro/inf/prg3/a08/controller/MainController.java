package de.thro.inf.prg3.a08.controller;

import com.google.gson.Gson;
import de.thro.inf.prg3.a08.filtering.MealsFilterFactory;
import de.thro.inf.prg3.a08.model.Meal;
import de.thro.inf.prg3.a08.services.OpenMensaAPIService;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller for main.fxml
 *
 * @author Peter Kurfer
 */
public class MainController implements Initializable
{
	private static final Logger logger;
	private static final DateFormat openMensaDateFormat;

	private final ObservableList<Meal> meals;

	@FXML private ChoiceBox<String> filterChoiceBox;
	@FXML private ListView<Meal>    mealsListView;

	static
	{
		logger = LogManager.getLogger(MainController.class);
		openMensaDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	}

	/**
	 * Default constructor
	 */
	public MainController()
	{
		meals = FXCollections.observableArrayList();
	}

	/**
	 * Initialization method of the UI controller
	 * Called after the FXML fields are assigned
	 *
	 * @param location
	 * @param resources1
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		mealsListView.setItems(meals);

		filterChoiceBox.setItems(FXCollections.observableList(Arrays.asList(new Gson().fromJson(new InputStreamReader(getClass().getResourceAsStream("/filters.json")), String[].class))));
		filterChoiceBox.getSelectionModel().selectFirst();
		filterChoiceBox.getSelectionModel()
			.selectedItemProperty()
			.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> doGetMeals());
	}

	/**
	 * Handles fetching of meals from OpenMensa API
	 */
	private void doGetMeals()
	{
		OpenMensaAPIService.instance().getAPI().getMeals(openMensaDateFormat.format(new Date())).enqueue(new Callback<List<Meal>>()
		{
			@Override
			public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response)
			{
				logger.debug("Got response");

				if (!response.isSuccessful() || response.body() == null)
				{
					logger.error(String.format("Got response with not successfull code %d", response.code()));

					Platform.runLater(() ->
					{
						Alert alert = new Alert(Alert.AlertType.ERROR);

						alert.setHeaderText("Unsuccessful HTTP call");
						alert.setContentText("Failed to get meals from OpenMensaAPI");
						alert.show();
					});

					return;
				}

				Platform.runLater(() -> meals.setAll(MealsFilterFactory.getStrategy(filterChoiceBox.getValue()).filter(response.body())));
			}

			@Override
			public void onFailure(Call<List<Meal>> call, Throwable t)
			{
				logger.error("Failed to fetch meals");

				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText("Failed HTTP call");
				alert.setContentText("Failed to submit HTTP call to fetch meals.");
				alert.show();
			}
		});
	}

	@FXML
	public void refresh()
	{
		doGetMeals();
	}
}
