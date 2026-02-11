
package server;

import common.*;

import java.io.*;
import java.net.Socket;

public class Room extends Thread {
    private Socket p1, p2;

    public Room(Socket p1, Socket p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in1 = new ObjectInputStream(p1.getInputStream());
            ObjectOutputStream out1 = new ObjectOutputStream(p1.getOutputStream());

            ObjectInputStream in2 = new ObjectInputStream(p2.getInputStream());
            ObjectOutputStream out2 = new ObjectOutputStream(p2.getOutputStream());

            Message m1 = (Message) in1.readObject();
            Message m2 = (Message) in2.readObject();

            GameService service = new GameService();
            Result r1 = service.compare(m1.getChoice(), m2.getChoice());
            Result r2 = (r1 == Result.WIN) ? Result.LOSE :
                        (r1 == Result.LOSE) ? Result.WIN : Result.DRAW;

            out1.writeObject(new Message(r1, "Bạn chọn " + m1.getChoice()
                    + " | Đối thủ chọn " + m2.getChoice()));
            out2.writeObject(new Message(r2, "Bạn chọn " + m2.getChoice()
                    + " | Đối thủ chọn " + m1.getChoice()));

            p1.close();
            p2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}