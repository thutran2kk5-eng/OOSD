package client;

import common.*;

import java.io.*;
import java.net.Socket;

public class ClientService {

    public void play(Choice choice) {
        try {
            Socket socket = new Socket("localhost", 5050);

            ObjectOutputStream out =
                    new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in =
                    new ObjectInputStream(socket.getInputStream());

            out.writeObject(new Message(choice));

            Message result = (Message) in.readObject();
            System.out.println(result.getInfo());
            System.out.println("KẾT QUẢ: " + result.getResult());

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}