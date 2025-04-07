package org.Fab;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@EqualsAndHashCode
@Getter
@Setter
public class Task {
    private final int id;
    private Status status;
    private String name;
    private String description;
    private Date date;

    Task(int id, Status status, String name, String description, Date date) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
        return "id:" + id + "\nСтатус: " + status + "\nНазвание: " + name +
                "\nОписание: " + description + "\nКрайний срок: " + format.format(date);
    }
}