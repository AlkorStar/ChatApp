package ru.ferret.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ru.ferret.client.Client;
import ru.ferret.client.Main;

import java.io.*;
import java.net.Socket;

public class OpenWin {
    // порт
    private static final int SERVER_PORT = 3443;
    // адрес сервера
    //private static final String SERVER_HOST = "192.168.0.188";
    public static Client client;
    /*----------------------------------------------------*/
    // Поля, что ссылаются на объекты внутри дизайна
    @FXML
    private TextField SERVER_HOST;
    @FXML
    private Button connect;
    @FXML
    private ImageView ferret;
    @FXML
    private ImageView loadFer;
    @FXML
    private Label stateCon;

    @FXML
    public void initialize() {
        connect.setOnAction(event -> {
            boolean success = false;
            //Пытаемся связаться с хорьком
            if(!SERVER_HOST.getText().isEmpty()){
                for (int i = 0; i < 5; i++) {
                    try {
                        client = new Client();
                        // подключаемся к серверу
                        client.setClientSocket(new Socket(SERVER_HOST.getText(), SERVER_PORT));
                        client.setStream();
                        success = true;
                        System.out.println("Client is connected");
                        if (success) {
                            break;
                        }
                    } catch (IOException e) {
                        stateCon.setText("Пытаемся связаться с хорьком");
                        System.out.println("Технические шоколадки на сервере. Хорек убежал " + i);
                    }
                    if (!success) {
                        sleep(1000);
                    }

                }
                if (!success) {
                    stateCon.setText("Error: Хорек не отвечает");
                    ferret.setVisible(false);
                }
                else{
                    //Создание нового окна
                    connect.getScene().getWindow().hide();
                    Main main = new Main();
                    Stage stage = new Stage();
                    stage.setResizable(false);
                    stage.getIcons().add(new Image("file:/C:/Users/Екатерина/IdeaProjects/ChatApp/photo.png"));
                    try {
                        //478, 396
                        main.start(stage, "logIn.fxml", 500, 600);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else {
                stateCon.setText("Error: Введите IP-адрес сервера");
            }
        });
    }
    void sleep(int milisec) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < start + milisec){

        }
    }
}
