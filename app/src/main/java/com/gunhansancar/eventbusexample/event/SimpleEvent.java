package com.gunhansancar.eventbusexample.event;

import java.util.Date;

/**
 * Created by gunhansancar on 06/04/16.
 */
public class SimpleEvent {
    private Date date;
    private int id;

    public SimpleEvent(int id) {
        this.id = id;
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
}
