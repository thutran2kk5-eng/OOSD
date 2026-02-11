package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import common.Choice;

public class ClientService {

    public void start() {

        try {
            Socket socket = new Socket("localhost", 5050);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner sc = new Scanner(System.in);

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                String input = sc.nextLine();
                out.println(input);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
