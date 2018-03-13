package com.tassta.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private StringHolder stringHolder = new StringHolder("String value");

    @Override
    public void start(Stage primaryStage) {
        String stringHolderLabel = StringHolder.class.getSimpleName();

        TextField textField = new TextField();
        textField.setText(stringHolder.getValue());
        textField.textProperty().addListener(((observable, oldValue, newValue) -> {
            stringHolder.setValue(newValue);
            System.out.println(stringHolderLabel + ": " + stringHolder);
        }));

        StackPane root = new StackPane();
        root.getChildren().add(textField);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle(stringHolderLabel + " Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
