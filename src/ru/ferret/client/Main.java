package ru.ferret.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.ferret.controller.ChatWin;
import ru.ferret.controller.OpenWin;

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
            stage.getIcons().add(new Image("file:/C:/Users/Екатерина/IdeaProjects/ChatApp/photo.png"));
            if(OpenWin.client != null){
                stage.setOnCloseRequest(event -> {
                    OpenWin.client.close();
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
        if(ChatWin.timer != null){
            ChatWin.timer.cancel();
        }
    }
    public void start(Stage stage, String file, int width, int high) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(file));
        stage.setTitle("The rapid Ferret"); // установка заголовка
        stage.setScene(new Scene(root, width, high)); // установка размера
        stage.setOnCloseRequest(event -> {
            OpenWin.client.close();
        });
        stage.show();
    }
}
