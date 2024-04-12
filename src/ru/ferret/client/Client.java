package ru.ferret.client;

import java.io.*;
import java.net.Socket;

public class Client {
    private String status =""; //админ или просто пользователь
    //админ делает трансляцию, пользователь просто смотрит
    private String serverAddress = "192.168.56.1";
    private static final int serverPort = 3443;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private String userName;
    private String chatName;
    public void setStatus(String status){
        this.status=status;
    }
    public String getStatus(){
        return status;
    }
    public String getChat() {
        return chatName;
    }

    public void setChat(String chatName) {
        this.chatName = chatName;
    }

    public Client(String SERVER_HOST, int SERVER_PORT){
        try {
            this.socket = new Socket(SERVER_HOST, SERVER_PORT);
            this.output = new PrintWriter(socket.getOutputStream(), true);
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Client(){

    }
    public Socket getSocket(){
        return socket;
    }

    public String getName(){return userName;}

    public void setName(String userName) {
        this.userName = userName;
    }

    public void sendMessage(String message) {
        output.println("send _"+chatName+"-"+userName + ": " + message);
    }
    public void sendAdmin(String command) {
        if (command.equals("bye")) {
            output.println("bye _"+chatName+"-"+"Пользователь " + userName + " вышел из чата");
            chatName = "";
        } else if (command.equals("hello")){
            output.println("hello _"+chatName+"-"+"Пользователь " + userName + " вошел в чат");
        } else if (command.equals("endIm")) {
            output.println("endIm _"+chatName);
        } else {
            //отправка изображения
            output.println("image _" + chatName + "-" + command);
        }
    }


    public String receiveMessage() {
        try {
            return input.readLine();
        } catch (IOException e) {
            System.out.println("Ошибка приема сообщения");
            return null;
        }
    }

    public void close() {
        try {
            if(status.equals("admin")) sendAdmin("endIm");
            sendAdmin("bye");
            socket.close();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createChat(String chatName, String password) {
        output.println("createChat " + chatName + " " + password);
    }

    public void joinChat(String chatName, String password) {
        output.println("joinChat " + chatName + " " + password+" "+userName);
    }

    public void setClientSocket(Socket socket) {
        this.socket=socket;
    }

    public void setStream() throws IOException {
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public BufferedReader getIn() { return input;}
}