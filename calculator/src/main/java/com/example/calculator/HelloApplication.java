package com.example.calculator;

import UI.Diagram;
import UI.UICalculator;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        new UICalculator().start(new Stage());
        new Diagram().start(new Stage());
    }

    public static void main(String[] args) {
        launch();
    }
}