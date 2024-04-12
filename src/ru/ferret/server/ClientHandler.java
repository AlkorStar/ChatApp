package ru.ferret.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ArrayList<ClientHandler> clients;
    private PrintWriter output;
    private Chat chat;
    private Server server;
    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients, Server server) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {//true
                String message = input.readLine();
                if (message == null) {
                    break;
                }
                handleMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Пользователь отключился от сервера");
                clientSocket.close();
                clients.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageToChat(String message) {
        // Отправить сообщение только пользователям из чата
        String[] parts = message.split("-");
        Chat chat = server.searchChat(parts[0]);
        if (chat != null){
            for (ClientHandler client : chat.getClient()) {
                client.output.println(parts[1]);
            }
        }
    }
    public void sendImage(String message){
        // Отправить сообщение только пользователям из чата
        String[] parts = message.split("-");
        Chat chat = server.searchChat(parts[0]);
        if (chat != null){
            for (ClientHandler client : chat.getClient()) {
                client.output.println("image " + parts[1]);
            }
        }
    }
    public void sendCount(Chat chat){
        for (ClientHandler client : chat.getClient()) {
            client.output.println("count "+ chat.getCountClient());
        }
    }
    private void sendEndIm(String chatName) {
        Chat chat = server.searchChat(chatName);
        if (chat != null){
            for (ClientHandler client : chat.getClient()) {
                client.output.println("endIm ");
            }
        }
    }
    public void handleMessage(String message) {
        String[] parts = message.split(" ");
        String command = parts[0];
        if (command.equals("createChat")) {
            String chatName = parts[1];
            String password = parts[2];
            if (server.searchChat(chatName)==null){
                chat = new Chat(chatName, password, this);
                server.addChat(chat);
                System.out.println("Был создан и добавлен чат с названием " + chatName + " и паролем " + password);
                this.output.println("success");
                sendCount(chat);
            }else this.output.println("exist");
        } else if (command.equals("joinChat")) {
            String chatName = parts[1];
            String password = parts[2];
            String nameClient = parts[3];
            String resJoin = server.joinChat(chatName, password, this);
            if(resJoin.equals("password")){
                this.output.println("password");
            } else if (resJoin.equals("chat")) {
                this.output.println("chat");
            } else {
                Chat chat = server.searchChat(chatName);
                sendCount(chat);
                this.output.println("count "+ chat.getCountClient());
                for (ClientHandler client : chat.getClient()) {
                        client.output.println("Пользователь " + nameClient + " вошел в чат");
                }
            }
        } else if(command.equals("image")){
            parts = message.split("_");
            sendImage(parts[1]);
        } else if (command.equals("endIm")) {
            parts = message.split("_");
            sendEndIm(parts[1]);
        } else {
            parts = message.split("_");
            sendMessageToChat(parts[1]);
            if (parts[0].equals("bye ")){
                parts = parts[1].split("-");
                Chat chat = server.searchChat(parts[0]);
                if(chat!=null){
                    chat.dropClient(this);
                    System.out.println("пользователь вышел из чата");
                    if(chat.getClient().isEmpty()){
                        server.getChats().remove(chat);
                        System.out.println("Чат "+parts[0]+" был удален");
                    }else {
                        sendCount(chat);
                    }
                }
            }else if (parts[0].equals("hello ")) {
                parts = parts[1].split("-");
                Chat chat = server.searchChat(parts[0]);
                if(chat!=null) {sendCount(chat);}
            }
        }
    }
}

