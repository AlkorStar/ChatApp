package ru.ferret.server;


import java.util.ArrayList;

public class Chat {
    private String password;
    private String nameChat;
    private ArrayList<ClientHandler> clentsChat = new ArrayList<>();
    private int countClient;


    public Chat(String nameChat, String password, ClientHandler creator) {
        this.nameChat = nameChat;
        this.password = password;
        this.clentsChat.add(creator);
        countClient ++;
    }
    public Chat(String nameChat, String password) {
        this.nameChat = nameChat;
        this.password = password;
    }

    public ArrayList<ClientHandler> getClient() {
        return clentsChat;
    }

    public String getName() {
        return nameChat;
    }

    public void setName(String nameChat) {
        this.nameChat = nameChat;
    }

    public String getPass() {
        return password;
    }

    public void setPass(String password) {
        this.password = password;
    }

    public void addClient(ClientHandler client) {
        countClient ++;
        clentsChat.add(client);
    }
    public int getCountClient(){return countClient;}
    public void dropClient(ClientHandler client) {
        countClient --;
        clentsChat.remove(client);
    }
}
