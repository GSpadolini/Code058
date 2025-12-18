package com.code058;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppFX extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Carga el archivo FXML que creamos antes
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/code058/view/main.fxml"));
        Parent root = loader.load();

        com.code058.controller.MainFXController controller = loader.getController();

        controller.setModelo(new com.code058.model.GestorDeDatos());

        scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Gestión Online Store - Code058");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
