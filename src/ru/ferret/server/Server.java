package ru.ferret.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private ArrayList<Chat> chats = new ArrayList<>();
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);


    public ArrayList<Chat> getChats(){return chats;}
    public String joinChat(String name, String password, ClientHandler client){
        String result = "";
        Chat chat = searchChat(name);
        if (chat !=null){
            if(chat.getPass().equals(password)) {
                chat.addClient(client);
                result="connected";}
            else result="password";
        } else result="chat";
        return result;
    }

    public Chat searchChat(String name) {
        Chat existingRoom = chats.stream()
                .filter(room -> room.getName().equals(name))
                .findFirst()
                .orElse(null);
        if (existingRoom == null) {
            return null;
        }
        else {
            return existingRoom;}
    }

    public void addChat(Chat chat){
        chats.add(chat);
    }
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error creating Server");
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients, this);
                System.out.println("К серверу подключился новый пользователь!");
                clients.add(clientHandler);
                executorService.execute(clientHandler);
            } catch (IOException e) {
                System.out.println("Error start server");
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        try {
            serverSocket.close();
            executorService.shutdown();
        } catch (IOException e) {
            System.out.println("Error stop server");
            e.printStackTrace();
        }
    }

}