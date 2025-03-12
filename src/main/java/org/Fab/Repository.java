package org.Fab;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
public class Repository {
    private HashSet<Task> tasks;

    public Repository() {
        createTaskList();
    }

    public void createTaskList() {
        this.tasks = new HashSet<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
}
