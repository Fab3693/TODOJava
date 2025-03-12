package org.Fab;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
public class Service {
    private Repository repository;
    private TaskCommands command;

    public Service() {
        this.repository = new Repository();
    }

    public static TaskCommands getCommand(String input) {
        if (!input.isEmpty()) {
            for (TaskCommands taskCommands : TaskCommands.values()) {
                if (taskCommands.getNumber() == Integer.parseInt(input) ||
                        taskCommands.getCommand().equalsIgnoreCase(input)) {
                    return taskCommands;
                }
            }
        }
        return null;
    }

    public void executeCommand(TaskCommands command, Scanner scanner) {
        switch (command) {
            case ADD:
                addTask(scanner);
                break;
            case LIST:
                listTasks();
                break;
            case EDIT:
                editTask();
                break;
            case DELETE:
                deleteTask(scanner);
                break;
            case FILTER:
                filterTasks();
                break;
            case SORT:
                sortTasks();
                break;
            case EXIT:
                exit();
                break;
        }
    }

    public void addTask(Scanner scanner) {
        System.out.println("Укажите название задачи:");
        String name = "";
        while (name.trim().isEmpty()) {
            name = scanner.nextLine();
        }
        System.out.println("Укажите описание задачи:");
        String description = "";
        while (description.trim().isEmpty()) {
            description = scanner.nextLine();
        }
        System.out.println("Укажите срок выполнения задачи в формате \"ДД:ММ:ГГГГ\":");
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
        Date date = null;
        while (date == null) {
            try {
                date = format.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.println("Неверный формат даты, используйте формат dd:MM:yyyy.");
            }
        }
        Status status = Status.TODO;
        String uniqueID = UUID.randomUUID().toString();
        Task task = new Task(uniqueID, status, name, description, date);
        repository.addTask(task);
        System.out.println("Задача успешно создана!");
        System.out.println("id:" + uniqueID + "\nСтатус: " + status + "\nНазвание: " + name +
                "\nОписание: " + description + "\nКрайний срок: " + format.format(date));
    }

    public void listTasks() {
        if (repository.getTasks().isEmpty()) {
            System.out.println("Список задач пуст. Вы можете создать задачу, выбрав команду \"ADD\"");
            return;
        }
        this.repository.getTasks().forEach(System.out::println);
    }

    public void editTask() {
    }

    public void deleteTask(Scanner scanner) {
        System.out.println("Укажите название задачи для удаления:");
        String name = "";
        while (name.trim().isEmpty()) {
            name = scanner.nextLine();
        }
        HashSet<Task> tasks = repository.getTasks();
        for (Task task : tasks) {
            if (Objects.equals(task.getName(), name)) {
                tasks.remove(task);
                System.out.println("Задача: " + name + " успешно удалена!");
                break;
            } else {
                System.out.println("Задача с таким именем не найдена.");
            }
        }
        if (repository.getTasks().isEmpty()) {
            System.out.println("Список задач пуст, создайте новую задачу для продолжения работы.");
        }
    }

    public void filterTasks() {
    }

    public void sortTasks() {
    }

    public void exit() {
        System.out.println("До свидания!");
        System.exit(0);
    }
}