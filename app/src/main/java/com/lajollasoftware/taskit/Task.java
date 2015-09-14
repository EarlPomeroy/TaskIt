package com.lajollasoftware.taskit;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by epomeroy on 9/13/15.
 */
public class Task implements Serializable{
    private String name;
    private Date dueDate;
    private boolean done;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return name;
    }
}
