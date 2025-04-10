package org.Fab;

import lombok.*;
import org.Fab.enums.Status;

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

    public Task(Status status, String name, String description, Date date, Repository repository) {
        this.id = setUniqueID(repository);
        this.status = status;
        this.name = name;
        this.description = description;
        this.date = date;
        setUniqueID(repository);
    }

    private int setUniqueID(Repository repository){
        int uniqueID = repository.getLastId();
        repository.setLastId(uniqueID);
        return repository.getLastId();
    }

    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
        return "id:" + id + "\nСтатус: " + status + "\nНазвание: " + name +
                "\nОписание: " + description + "\nКрайний срок: " + format.format(date);
    }
}