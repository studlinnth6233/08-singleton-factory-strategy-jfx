package de.thro.inf.prg3.a08;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entrypoint of application
 * Launches JavaFX app by loading main.fxml file
 */
public class App extends Application
{

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Parent root = FXMLLoader.<Parent>load(getClass().getResource("views/main.fxml"));
		primaryStage.setTitle("Cafeteria");
		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.show();
	}
}
