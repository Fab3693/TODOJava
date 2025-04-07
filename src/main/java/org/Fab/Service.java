package org.Fab;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Service {
    private Repository repository;
    private TaskCommands command;

    public Service(Repository repository) {
        this.repository = repository;
    }

    public TaskCommands getCommand(String input) {
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

//    public void executeCommand(TaskCommands command, Scanner scanner) {
//        switch (command) {
//            case ADD:
//                addTask();
//                break;
//            case LIST:
//                listTasks();
//                break;
//            case EDIT:
//                editTask(scanner);
//                break;
//            case DELETE:
//                deleteTask(scanner);
//                break;
//            case FILTER:
//                filterTasks(scanner);
//                break;
//            case SORT:
//                sortTasks(scanner);
//                break;
//            case EXIT:
//                exit();
//                break;
//        }
//    }

    public void addTask(String name, String description, Date date) {
        AddTask addTask = new AddTask();
        Status status = Status.TODO;
        int uniqueID = repository.getLastId();
        repository.setLastId(++uniqueID);
        Task task = new Task(uniqueID, status, name, description, date);
        repository.addTask(task);
        addTask.printTaskCreated(task);
    }

    public void listTasks() {
        this.repository.getTasks().forEach(System.out::println);
    }

    public void editTask(String taskName, InputHandler inputHandler) {
        TaskEditor taskEditor = new TaskEditor();
        System.out.println("Укажите название задачи, которую хотите отредактировать:");
        Task task = repository.findTaskByName(taskName);
        if (task == null) {
            System.out.println("Задача не найдена.");
            return;
        }
        System.out.println("Задача найдена: " + task.getName());
        System.out.println("Текущие данные задачи:");
        System.out.println(task);
        taskEditor.editTaskFields(task, inputHandler);
    }

    public void deleteTask(InputHandler inputHandler) {
        System.out.println("Укажите название задачи для удаления:");
        String name = inputHandler.getNonEmptyInput();
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

    public void filterTasks(InputHandler inputHandler) {
        HashSet<Task> tasks = repository.getTasks();
        FilterTasks filterTasks = new FilterTasks();


        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }

        System.out.println("Выберите, по какому признаку нужна фильтрация:");
        System.out.println("1. По дате");
        System.out.println("2. По статусу");

        int choice = inputHandler.getIntInput(1, 2);

        switch (choice) {
            case 1:
                filterTasks.filterByDate(tasks, scanner);
                break;
            case 2:
                filterTasks.filterByStatus(tasks, scanner);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    public void sortTasks(Scanner scanner) {
        HashSet<Task> tasks = repository.getTasks();
        SortTasks sortTasks = new SortTasks();

        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }

        System.out.println("Выберите, по какому признаку нужна сортировка:");
        System.out.println("1. По дате");
        System.out.println("2. По статусу");

        int choice = sortTasks.getIntInput(scanner, 1, 2);

        switch (choice) {
            case 1:
                sortTasks.sortByDate(tasks);
                break;
            case 2:
                sortTasks.sortByStatus(tasks);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    public void exit() {
        System.out.println("До свидания!");
        System.exit(0);
    }

    public boolean checkTaskListIsEmpty() {
        return this.repository.getTasks().isEmpty();
    }


}