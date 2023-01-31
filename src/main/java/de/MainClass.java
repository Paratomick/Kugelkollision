package de;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainClass extends Application {

	Umgebung umgebung;


	long[] lastTime = {System.nanoTime()};
	long[] dT = new long[10];
	double[] d = {0};
	int[] count = {0};
	EventHandler<Event> eventHandler;

	public void start(Stage stage) throws Exception {
		StackPane root = new StackPane();
		Scene scene = new Scene(root,1700, 1000);
		stage.setScene(scene);
		stage.setFullScreen(false);

		new Input(scene);

		umgebung = new Umgebung(root);
		umgebung.getCanvas().widthProperty().bind(scene.widthProperty());
		umgebung.getCanvas().heightProperty().bind(scene.heightProperty());
		umgebung.init();

		stage.show();

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long l) { loop(l);
			}
		};
		timer.start();

		//TODO Updates/Render durchführen.
	}

	private void loop(long l) {

		long deltaNS = l - lastTime[0];
		lastTime[0] = l;

		d[0] += -dT[count[0]] + deltaNS;
		dT[count[0]] = deltaNS;
		count[0] = (count[0] + 1) % 10;
		double delta = d[0]/1e7;

		System.out.println((1000/delta));

		umgebung.update(delta);
		umgebung.render();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
