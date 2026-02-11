package common;

import java.io.Serializable;

public class Message implements Serializable {
    private Choice choice;
    private Result result;
    private String info;

    public Message(Choice choice) {
        this.choice = choice;
    }

    public Message(Result result, String info) {
        this.result = result;
        this.info = info;
    }

    public Choice getChoice() {
        return choice;
    }

    public Result getResult() {
        return result;
    }

    public String getInfo() {
        return info;
    }
}