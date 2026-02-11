package client;

import common.Choice;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Choice choice = null;

        while (choice == null) {
            try {
                System.out.println("Nhập lựa chọn (KEO / BUA / BAO): ");
                choice = Choice.valueOf(sc.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập lại.");
            }
        }

        ClientService service = new ClientService();
        service.play(choice);
    }
}
