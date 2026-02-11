package server;

import common.Choice;

import java.io.IOException;
import java.util.*;

public class Room {

	private List<PlayerHandler> players =
	        Collections.synchronizedList(new ArrayList<>());

	private List<PlayerHandler> eliminatedPlayers =
	        Collections.synchronizedList(new ArrayList<>());

	private Map<PlayerHandler, Choice> choices =
	        Collections.synchronizedMap(new HashMap<>());

    private boolean gameStarted = false;
    
    public synchronized void addPlayer(PlayerHandler player) {
        players.add(player);
        broadcast(player.getName() + " đã tham gia.");

        if (!gameStarted && players.size() >= 3) {
            gameStarted = true;   
            new Thread(() -> {
                try {
                    startGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    public int getPlayerCount() {
        return players.size();
    }

    public synchronized void startGame() throws IOException {

        if (players.size() < 3) return;

        gameStarted = true;

        broadcast("===== GAME BẮT ĐẦU =====");

        while (players.size() > 1) {

            if (players.size() == 2) {
                play1vs1();
                return;
            }

            playRound();
        }

        if (players.size() == 1) {
            broadcast("🏆 NGƯỜI THẮNG CUỐI: " + players.get(0).getName());
        }
    }

    private void playRound() throws IOException {

        choices.clear();

        broadcast("\n===== VÒNG MỚI =====");

        List<PlayerHandler> currentPlayers = new ArrayList<>(players);

        for (PlayerHandler p : currentPlayers) {
            p.send("Nhập (KEO / BUA / BAO):");
        }

        for (PlayerHandler p : currentPlayers) {
            Choice c = p.receiveChoice();
            choices.put(p, c);
        }

        long keo = choices.values().stream().filter(c -> c == Choice.KEO).count();
        long bua = choices.values().stream().filter(c -> c == Choice.BUA).count();
        long bao = choices.values().stream().filter(c -> c == Choice.BAO).count();

        // Trường hợp hòa
        if ((keo > 0 && bua > 0 && bao > 0) ||
            (keo > 0 && bua == 0 && bao == 0) ||
            (bua > 0 && keo == 0 && bao == 0) ||
            (bao > 0 && keo == 0 && bua == 0)) {

            broadcast("⚖ Hòa! Chơi lại.");
            return;
        }

        List<PlayerHandler> losers = new ArrayList<>();

        // KEO vs BUA
        if (keo > 0 && bua > 0 && bao == 0) {
            for (var e : choices.entrySet())
                if (e.getValue() == Choice.KEO)
                    losers.add(e.getKey());
        }

        // BUA vs BAO
        else if (bua > 0 && bao > 0 && keo == 0) {
            for (var e : choices.entrySet())
                if (e.getValue() == Choice.BUA)
                    losers.add(e.getKey());
        }

        // KEO vs BAO
        else if (keo > 0 && bao > 0 && bua == 0) {
            for (var e : choices.entrySet())
                if (e.getValue() == Choice.BAO)
                    losers.add(e.getKey());
        }

        players.removeAll(losers);
        eliminatedPlayers.addAll(losers);

        broadcastScoreBoard();
    }

    private void play1vs1() throws IOException {

        PlayerHandler p1 = players.get(0);
        PlayerHandler p2 = players.get(1);

        while (true) {

            p1.send("Nhập lựa chọn:");
            p2.send("Nhập lựa chọn:");

            Choice c1 = p1.receiveChoice();
            Choice c2 = p2.receiveChoice();

            if (c1 == c2) {
                broadcast("Hòa! Chơi lại.");
                continue;
            }

            PlayerHandler winner =
                    ((c1 == Choice.KEO && c2 == Choice.BAO) ||
                     (c1 == Choice.BUA && c2 == Choice.KEO) ||
                     (c1 == Choice.BAO && c2 == Choice.BUA))
                    ? p1 : p2;

            broadcast("🏆 NGƯỜI THẮNG: " + winner.getName());
            break;
        }
    }

    private void broadcastScoreBoard() {

        StringBuilder sb = new StringBuilder();
        sb.append("\n===== BẢNG KẾT QUẢ =====\n");

        for (var entry : choices.entrySet()) {
            PlayerHandler p = entry.getKey();
            sb.append(p.getName())
              .append(" chọn ")
              .append(entry.getValue())
              .append(players.contains(p) ? " - CÒN\n" : " - BỊ LOẠI\n");
        }

        sb.append("========================\n");

        broadcast(sb.toString());
    }

    private void broadcast(String msg) {
        for (PlayerHandler p : players)
            p.send(msg);
        for (PlayerHandler p : eliminatedPlayers)
            p.send(msg);
    }
}
