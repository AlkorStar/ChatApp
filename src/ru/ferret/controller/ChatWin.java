package ru.ferret.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.ferret.client.Main;
import ru.ferret.server.Chat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ChatWin {
    private volatile boolean stopThread = false;
    @FXML
    private Label byeMess;
    @FXML
    private Label chatInfo;
    @FXML
    private ImageView screen;
    @FXML
    private ImageView byeFerr;
    @FXML
    private Button send;
    @FXML
    private Button exit;
    @FXML
    private Button capt;
    @FXML
    private TitledPane chatField;
    @FXML
    private TextField mess;
    @FXML
    private VBox vbox_messages;
    private boolean stateIm = true;
    public static Timer timer;


    @FXML
    public void initialize() {
        chatField.setText("Чат "+OpenWin.client.getChat());
        if(!OpenWin.client.getStatus().equals("admin")){
            capt.setVisible(false);
        }else{
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(stateIm) {
                        try {
                            Robot robot = new Robot();
                            BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                            sendScreen(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 0, 1); // отправлять снимок экрана каждые сколько-то миллисекунд
        }
        capt.setOnAction(event -> {
            stateIm = !stateIm;
        });
        send.setOnAction(event -> {
            String message = mess.getText();
            if (!message.isEmpty()) {
            OpenWin.client.sendMessage(message);
            mess.clear();
            }
        });

        exit.setOnAction(event -> {
            stopThread = true;
            if(OpenWin.client.getStatus().equals("admin")) {
                timer.cancel();
                OpenWin.client.sendAdmin("endIm");
            }
            //создание нового окна с чатом
            exit.getScene().getWindow().hide();
            Stage stage = new Stage();
            Main main = new Main();
            stage.getIcons().add(new Image("file:./src/photo.png"));
            OpenWin.client.sendAdmin("bye");
            OpenWin.client.setStatus("");
            OpenWin.client.setChat("");
            try {
                main.start(stage, "logIn.fxml",500, 600);
            } catch (Exception e) {
                System.out.println("Не получилось открыть окно");
            }
        });


        Thread thread = new Thread(() -> {
            while (!stopThread && OpenWin.client.getSocket().isConnected()) {
                try {
                    String message = OpenWin.client.getIn().readLine();
                    String[] parts = message.split(" ");
                    if(parts[0].equals("count")){
                        Platform.runLater(() -> chatInfo.setText("Число участников: "+ parts[1]));
                    } else if (parts[0].equals("image")) {
                        String base64Image = parts[1];
                        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                        BufferedImage bufferedImage = ImageIO.read(bis);
                        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                        Platform.runLater(() -> screen.setImage(image));
                    } else if (parts[0].equals("endIm")) {
                        screen.setVisible(false);
                        byeFerr.setVisible(true);
                        byeMess.setVisible(true);
                    } else{
                        Label label_message = new Label(message);
                        Platform.runLater(() -> vbox_messages.getChildren().add(label_message));
                    }
                } catch (IOException e) {
                    OpenWin.client.close();
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    private void sendScreen(BufferedImage image) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            OpenWin.client.sendAdmin(base64Image); //image _chatName-brbrbrbrImage
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
