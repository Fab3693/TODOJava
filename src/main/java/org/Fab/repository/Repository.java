package org.Fab.repository;

import lombok.Getter;
import lombok.Setter;
import org.Fab.entity.Task;

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

    public Task findTaskByName(String taskName) {
        for (Task task : tasks) {
            if (Objects.equals(task.getName(), taskName))
                return task;
        }
        return null;
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }
}