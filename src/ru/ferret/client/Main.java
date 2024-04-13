package ru.ferret.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.ferret.controller.ChatWinController;
import ru.ferret.controller.OpenWinController;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("OpenWin.fxml"));
            stage.setTitle("The rapid Ferret - Client"); // установка заголовка
            stage.setScene(new Scene(root, 500, 600)); // установка размера
            stage.setResizable(false);
            stage.getIcons().add(new Image("file:./src/photo.png")); //новый путь
            if(OpenWinController.client != null){
                stage.setOnCloseRequest(event -> {
                    OpenWinController.client.close();
                });
            }
            stage.show();
        }catch (IOException e){
            System.out.println("Error");
        }

    }
    @Override
    public void stop() {
        // Stop the timer when the application is closed
        if(ChatWinController.timer != null){
            ChatWinController.timer.cancel();
        }
    }
    public void start(Stage stage, String file, int width, int high) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(file));
        stage.setTitle("The rapid Ferret"); // установка заголовка
        stage.setScene(new Scene(root, width, high)); // установка размера
        stage.setOnCloseRequest(event -> {
            OpenWinController.client.close();
        });
        stage.show();
    }
}
