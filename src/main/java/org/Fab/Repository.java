package org.Fab;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;

@Getter
@Setter
public class Repository {
    private HashSet<Task> tasks;
    private int lastId;

    public Repository() {
        this.tasks = new HashSet<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task findTaskbyName(String taskName){
        for (Task task : tasks){
            if (Objects.equals(task.getName(), taskName))
                return task;
        }
        return null;
    }
}