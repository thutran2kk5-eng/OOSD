package server;

import common.Choice;

import java.io.*;
import java.net.Socket;

public class PlayerHandler implements Runnable {

    private Socket socket;
    private Room room;
    private BufferedReader in;
    private PrintWriter out;
    private String name;

    public PlayerHandler(Socket socket, Room room) throws IOException {
        this.socket = socket;
        this.room = room;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        out.println("Nhập tên của bạn:");
        this.name = in.readLine();
    }

    public String getName() {
        return name;
    }

    public void send(String message) {
        out.println(message);
    }

    public Choice receiveChoice() throws IOException {
        while (true) {
            String input = in.readLine();

            if (input == null) continue;

            try {
                return Choice.valueOf(input.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                send("Lựa chọn không hợp lệ! Nhập lại (KEO/BUA/BAO):");
            }
        }
    }


    @Override
    public void run() {
        
    }
}
