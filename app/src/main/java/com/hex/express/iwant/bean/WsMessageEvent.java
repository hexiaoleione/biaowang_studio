package com.hex.express.iwant.bean;

public class WsMessageEvent {
    public int type;
    public String message;

    public WsMessageEvent(int type, String message) {
        this.type = type;
        this.message = message;
    }
}
