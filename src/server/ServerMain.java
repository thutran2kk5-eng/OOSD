package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(5050);
        System.out.println("Server đang chạy...");

        Room room = new Room();

        while (true) {
            Socket socket = serverSocket.accept();
            PlayerHandler player = new PlayerHandler(socket, room);
            room.addPlayer(player);
            new Thread(player).start();
        }
    }
}
