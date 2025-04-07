package org.Fab;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

@Getter
@Setter
public class Controller {
    private final Service service;
    private final InputHandler inputHandler;
    public static final String FIRST_VALUE = "1";

    public Controller(Service service, InputHandler inputHandler) {
        this.service = service;
        this.inputHandler = inputHandler;
    }

    public void greeting() {
        System.out.println("Здравствуйте! Список задач пуст. " +
                "Для продолжения работы оздайте новую задачу.");
    }

    public void showAllCommands() {
        Arrays.stream(TaskCommands.values()).forEach(taskCommands ->
                System.out.println(taskCommands.getNumber() + ". " + taskCommands.getCommand()));
    }

    public String consoleInput() {
        if (service.checkTaskListIsEmpty()) {
            return FIRST_VALUE;
        }
        return inputHandler.getNonEmptyInput(); /*console.nextLine();*/
    }

    public TaskCommands getCommand(String input) {
        return service.getCommand(input);
    }

    public void addTask() {
        System.out.println("Укажите название задачи:");
        String name = inputHandler.getNonEmptyInput();
        System.out.println("Укажите описание задачи:");
        String description = inputHandler.getNonEmptyInput();
        System.out.println("Укажите срок выполнения задачи в формате \"ДД:ММ:ГГГГ\":");
        Date date = inputHandler.parseDate();
        service.addTask(name, description, date);
    }

    public void editTask(InputHandler inputHandler) {
        System.out.println("Укажите название задачи, которую хотите отредактировать:");
        String taskName = inputHandler.getNonEmptyInput();
        Task task = repository.findTaskByName(inputHandler.getNonEmptyInput());
        if (task == null) {
            System.out.println("Задача не найдена.");
            return;
        }
        System.out.println("Задача найдена: " + task.getName());
        System.out.println("Текущие данные задачи:");
        System.out.println(task);
        service.editTask(taskName, inputHandler);
    }

    public void deleteTask(InputHandler inputHandler) {
        System.out.println("Укажите название задачи для удаления:");
        String name = inputHandler.getNonEmptyInput();
        service.deleteTask(inputHandler);
    }
    public void filterTasks(InputHandler inputHandler) {
        service.filterTasks(inputHandler);
    }
    public void sortTasks(InputHandler inputHandler) {

    }

    public void executeCommand(TaskCommands command) {
        switch (command) {
            case ADD:
                addTask();
                break;
            case LIST:
                service.listTasks();
                break;
            case EDIT:
                editTask(inputHandler);
                break;
            case DELETE:
                deleteTask(inputHandler);
                break;
            case FILTER:
                filterTasks(inputHandler);
                break;
            case SORT:
                sortTasks(inputHandler);
                break;
            case EXIT:
                service.exit();
                break;
        }

    }
}