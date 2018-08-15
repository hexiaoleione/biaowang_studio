package com.hex.express.iwant.utils;

/**
 * Author: lc
 * Email: rekirt@163.com
 * Date: 6/22/16
 */
public class TestMessage {
    public static enum Event {
        header,
        
        zixun,
        xitong,
        wenshu,
        renzhen,
        falv,
        shezhi,
        guanyu,
        fenxiang,

        LoginChanged;


        private Event() {}
    }
    public int pos;
    public Event message;
    public Object obj;
    public TestMessage(Event message) {
        this.message = message;
    }
    public TestMessage(Event message, int item_pos) {
        this(message);
        this.pos = item_pos;
    }
    public TestMessage(Event message, Object item_obj) {
        this(message);
        this.obj = item_obj;
    }
}
