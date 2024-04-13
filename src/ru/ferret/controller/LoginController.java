package ru.ferret.controller;

import javafx.application.Platform;
import javafx.scene.control.TextField;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ru.ferret.client.Main;

import java.io.IOException;

public class LoginController {
    // Поля, что ссылаются на объекты внутри дизайна
    @FXML
    private ImageView ferret;
    @FXML
    private TextField nameField;
    @FXML
    private TextField password;
    @FXML
    private TextField nameChat;
    @FXML
    private Button newChat;
    @FXML
    private Button oldChat;
    @FXML
    private Label emptyName;
    @FXML
    private Label emptyChat;
    @FXML
    private Label emptyPass;
    @FXML
    public void initialize() {
        newChat.setOnAction(event -> {
            checkField();
            if(!(nameField.getText().isEmpty()||nameChat.getText().isEmpty()||password.getText().isEmpty())){
                OpenWinController.client.setName(nameField.getText());
                emptyName.setText("");
                //создание чата
                OpenWinController.client.createChat(nameChat.getText(), password.getText());
                try {
                    String message = OpenWinController.client.getIn().readLine();
                    if(message.equals("exist")){
                        Platform.runLater(() -> emptyChat.setText("Такой чат уже существует"));
                        emptyChat.setVisible(true);
                        System.out.println("Такой чат уже существует");
                    }else {
                        OpenWinController.client.setStatus("admin");
                        OpenWinController.client.sendAdmin("hello");
                        OpenWinController.client.setChat(nameChat.getText());
                        createChatWin(newChat);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        oldChat.setOnAction(event ->{
            checkField();
            if(!(nameField.getText().isEmpty()||nameChat.getText().isEmpty()||password.getText().isEmpty())){
                OpenWinController.client.setName(nameField.getText());
                emptyName.setText("");
                //добавление к чату
                OpenWinController.client.joinChat(nameChat.getText(), password.getText());
                try {
                    String message = OpenWinController.client.getIn().readLine();
                    if(message.equals("password")){
                        Platform.runLater(() -> {
                            emptyPass.setText("Пароль неверный");
                            emptyPass.setVisible(true);
                        });
                        System.out.println("Пароль неверный");
                    } else if (message.equals("chat")) {
                        Platform.runLater(() -> {
                            emptyChat.setText("Чата не существует");
                            emptyChat.setVisible(true);
                        });
                        System.out.println("Чата не существует");
                    } else {
                        OpenWinController.client.setStatus("user");
                        OpenWinController.client.setChat(nameChat.getText());
                        createChatWin(oldChat);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void createChatWin(Button button) {
        //создание нового окна с чатом
        button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setResizable(true);
        stage.getIcons().add(new Image("file:./src/photo.png"));
        Main main = new Main();
        try {
            main.start(stage, "ChatWin.fxml", 900, 600);
        } catch (Exception e) {
            System.out.println("Не получилось открыть окно");
        }
    }

    private void checkField() {
        if(nameField.getText().isEmpty()){
            emptyName.setText("Введите имя!");
        }
        else{emptyName.setVisible(false);}
        if(nameChat.getText().isEmpty()){
            emptyChat.setText("Введите название!");
        }
        else{emptyChat.setVisible(false);}
        if(password.getText().isEmpty()){
            emptyPass.setText("Введите пароль!");
        }
        else{emptyPass.setVisible(false);}
    }

}