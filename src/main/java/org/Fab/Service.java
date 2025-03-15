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
                editTaskNew(scanner);
                break;
            case DELETE:
                deleteTask(scanner);
                break;
            case FILTER:
                filterTasks(scanner);
                break;
            case SORT:
                sortTasks(scanner);
                break;
            case EXIT:
                exit();
                break;
        }
    }

    public void addTask(Scanner scanner) {
        System.out.println("Укажите название задачи:");
        String name = getNonEmptyInput(scanner);
        System.out.println("Укажите описание задачи:");
        String description = getNonEmptyInput(scanner);
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
        int uniqueID = repository.getLastId();
        repository.setLastId(++uniqueID);
        Task task = new Task(uniqueID, status, name, description, date);
        repository.addTask(task);
        System.out.println("Задача успешно создана!");
        System.out.println("id:" + uniqueID + "\nСтатус: " + status + "\nНазвание: " + name +
                "\nОписание: " + description + "\nКрайний срок: " + format.format(date));
    }

    public void listTasks() {
        this.repository.getTasks().forEach(System.out::println);
    }

    public void editTask(Scanner scanner) {

    }/*Pizdec*/

    public void editTaskNew(Scanner scanner) {
        System.out.println("Укажите название задачи, которую хотите отредактировать:");
        Task task = repository.findTaskbyName(getNonEmptyInput(scanner));
        if (task == null) {
            System.out.println("Задача не найдена.");
            return;
        }
        System.out.println("Задача найдена: " + task.getName());
        System.out.println("Текущие данные задачи:");
        System.out.println(task);
        EditTask.editTaskFields(task, scanner);
    }

    public void deleteTask(Scanner scanner) {
        System.out.println("Укажите название задачи для удаления:");
        String name = getNonEmptyInput(scanner);
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

    public void filterTasks(Scanner scanner) {
        HashSet<Task> tasks = repository.getTasks();

        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }

        System.out.println("Выберите, по какому признаку нужна фильтрация:");
        System.out.println("1. По дате");
        System.out.println("2. По статусу");

        int choice = FilterTasks.getIntInput(scanner, 1, 2);

        switch (choice) {
            case 1:
                FilterTasks.filterByDate(tasks, scanner);
                break;
            case 2:
                FilterTasks.filterByStatus(tasks, scanner);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    public void sortTasks(Scanner scanner) {
        HashSet<Task> tasks = repository.getTasks();

        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }

        System.out.println("Выберите, по какому признаку нужна сортировка:");
        System.out.println("1. По дате");
        System.out.println("2. По статусу");

        int choice = SortTasks.getIntInput(scanner, 1, 2);

        switch (choice) {
            case 1:
                SortTasks.sortByDate(tasks);
                break;
            case 2:
                SortTasks.sortByStatus(tasks);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    public void exit() {
        System.out.println("До свидания!");
        System.exit(0);
    }

    public boolean checkEmpty() {
        return this.repository.getTasks().isEmpty();
    }

    protected static String getNonEmptyInput(Scanner scanner) {
        String input = "";
        while (input.trim().isEmpty()) {
            input = scanner.nextLine();
        }
        return input;
    }
}