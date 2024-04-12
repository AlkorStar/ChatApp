package ru.ferret.server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        Server server = new Server(3443);
        String IP = getIP();
        if (IP!= null){
            System.out.println("Server is running, IP-add: " + IP);
        }
        else {
            System.out.println("Server is running, but IP does not found");
        }
        server.start();

    }
    public static String getIP(){
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            return datagramSocket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
