package server;

import common.*;

public class GameService {

    public Result compare(Choice c1, Choice c2) {
        if (c1 == c2) return Result.DRAW;

        if ((c1 == Choice.KEO && c2 == Choice.BAO) ||
            (c1 == Choice.BAO && c2 == Choice.BUA) ||
            (c1 == Choice.BUA && c2 == Choice.KEO)) {
            return Result.WIN;
        }

        return Result.LOSE;
    }
}