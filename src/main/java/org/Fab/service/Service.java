package org.Fab.service;

import lombok.Getter;
import lombok.Setter;
import org.Fab.repository.Repository;
import org.Fab.entity.Task;
import org.Fab.enums.Status;
import org.Fab.enums.TaskCommands;
import org.Fab.enums.TaskFields;

import java.util.*;

@Getter
@Setter
public class Service {
    private Repository repository;
    private TaskCommands command;

    public Service(Repository repository) {
        this.repository = repository;
    }

    public TaskCommands getCommand(String input) {
        if (input == null || input.isEmpty()) return null;
        for (TaskCommands command : TaskCommands.values()) {
            if (command.getCommand().equalsIgnoreCase(input)) {
                return command;
            }
        }
        try {
            int num = Integer.parseInt(input);
            for (TaskCommands command : TaskCommands.values()) {
                if (command.getNumber() == num) {
                    return command;
                }
            }
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

    public TaskFields getTaskField(String fieldInput) {
        if (fieldInput == null || fieldInput.isEmpty()) return null;
        for (TaskFields field : TaskFields.values()) {
            if (field.getFieldName().equalsIgnoreCase(fieldInput)) {
                return field;
            }
        }
        try {
            int num = Integer.parseInt(fieldInput);
            for (TaskFields field : TaskFields.values()) {
                if (field.getNumber() == num) {
                    return field;
                }
            }
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

    Status getStatus(String input) {
        if (input == null || input.isEmpty()) return null;
        for (Status status : Status.values()) {
            if (status.getName().equalsIgnoreCase(input)) {
                return status;
            }
        }
        try {
            int num = Integer.parseInt(input);
            for (Status status : Status.values()) {
                if (status.getNumber() == num) {
                    return status;
                }
            }
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

    public Status publicGetStatus(String input) {
        return getStatus(input);
    }

    public void addTask(String name, String description, Date date, Status status) {
        Task task = new Task(status, name, description, date, repository);
        repository.addTask(task);
    }

    public HashSet<Task> listTasks() {
        return repository.getTasks();
    }

    public void editTaskName(Task task, String newName) {
        task.setName(newName);
    }

    public void editTaskDescription(Task task, String newDescription) {
        task.setDescription(newDescription);
    }

    public void editTaskStatus(Task task, String newStatusString) {
        Status newStatus = getStatus(newStatusString);
        if (newStatus == null) {
            System.out.println("Неверный статус. Допустимые значения: " +
                    Arrays.toString(Status.values()) + " или их номера (1-3)");
            return;
        }
        task.setStatus(newStatus);
        System.out.println("Статус задачи успешно изменен на: " + newStatus);
    }

    public void editTaskDate(Task task, Date newDate) {
        task.setDate(newDate);
    }

    public void deleteTask(String taskName) {
        for (Task task : repository.getTasks()) {
            if (Objects.equals(task.getName(), taskName)) {
                repository.removeTask(task);
                System.out.println("Задача: " + taskName + " успешно удалена!");
                break;
            } else {
                System.out.println("Задача с таким именем не найдена.");
            }
        }
        if (repository.getTasks().isEmpty()) {
            System.out.println("Список задач пуст, создайте новую задачу для продолжения работы.");
        }
    }

    public List<Task> filterTasksByDate(Date filterDate) {
        return repository.getTasks().stream()
                .filter(task -> task.getDate().equals(filterDate))
                .toList();
    }

    public List<Task> filterTasksByStatus(String filterStatus) {
        Status status = getStatus(filterStatus);
        if (status == null) {
            return null;
        }
        return repository.getTasks().stream()
                .filter(task -> task.getStatus() == status)
                .toList();
    }

    public List<Task> sortByDate() {
        return repository.getTasks().stream()
                .sorted(Comparator.comparing(Task::getDate))
                .toList();
    }

    public List<Task> sortByStatus() {
        return repository.getTasks().stream()
                .sorted(Comparator.comparing(Task::getStatus))
                .toList();
    }

    public void exit() {
        System.exit(0);
    }

    public Task returnIfTaskExists(String taskName) {
        return repository.findTaskByName(taskName);
    }

    public boolean checkTaskListIsEmpty() {
        return repository.getTasks().isEmpty();
    }

}