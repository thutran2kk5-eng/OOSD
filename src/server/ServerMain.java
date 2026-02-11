package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5050);
            System.out.println("Server đang chạy...");

            while (true) {
                Socket p1 = serverSocket.accept();
                System.out.println("Player 1 vào phòng");

                Socket p2 = serverSocket.accept();
                System.out.println("Player 2 vào phòng");

                new Room(p1, p2).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}