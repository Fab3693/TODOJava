/*
 * 1. Изменять задачу нужно в Service или Repository?
 * 2. Можно ли получать задачу в Controller для вывода информации о ней?
 * */

package org.Fab;

import lombok.Getter;
import lombok.Setter;
import org.Fab.enums.Status;
import org.Fab.enums.TaskCommands;
import org.Fab.enums.TaskFields;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Getter
@Setter
public class Controller {
    private final Service service;
    private final Scanner scanner = new Scanner(System.in);
    ;
    private final SimpleDateFormat format;
    private static final String DEFAULT_DATE_PATTERN = "dd:MM:yyyy";
    public static final String FIRST_VALUE = "1";

    public Controller(Service service) {
        this.service = service;
        this.format = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
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
        return getNonEmptyInput(); /*console.nextLine();*/
    }

    public TaskCommands getCommand(String input) {
        return service.getCommand(input);
    }

    private String getNonEmptyInput() {
        String input = "";
        while (input.isEmpty()) {
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private Date parseDate() {
        while (true) {
            try {
                return format.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.println("Неверный формат даты, используйте формат dd:MM:yyyy.");
            }
        }
    }

    public void executeCommand(TaskCommands command) {
        switch (command) {
            case ADD:
                addTask();
                break;
            case LIST:
                listTasks();
                break;
            case EDIT:
                editTask();
                break;
            case DELETE:
                deleteTask();
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

    private void addTask() {
        System.out.println("Укажите название задачи:");
        String name = getNonEmptyInput();
        System.out.println("Укажите описание задачи:");
        String description = getNonEmptyInput();
        System.out.println("Укажите срок выполнения задачи в формате \"ДД:ММ:ГГГГ\":");
        Date date = parseDate();
        Status status = Status.TODO;
        service.addTask(name, description, date, status);
        System.out.println("Задача успешно создана!");
    }

    private void listTasks() {
        service.listTasks().forEach(System.out::println);
    }

    private void editTask() {
        System.out.println("Укажите название задачи, которую хотите отредактировать:");
        String taskName = getNonEmptyInput();
        Task task = service.returnIfTaskExists(taskName);
        if (task == null) {
            System.out.println("Задача не найдена.");
            return;
        }
        System.out.println("Задача найдена: " + task.getName());
        System.out.println("Текущие данные задачи:");
        System.out.println(task);
        System.out.println("Выберите поле для редактирования:");
        Arrays.stream(TaskFields.values()).forEach(taskFields ->
                System.out.println(taskFields.getNumber() + ". " + taskFields.getFieldName()));
        TaskFields field = service.getTaskField(getNonEmptyInput());
        if (field == null) {
            System.out.println("Неверный ввод. Пожалуйста, введите номер поля.");
            return;
        }
        switchFields(task, field);
    }

    private void switchFields (Task task, TaskFields field){
        switch (field) {
            case NAME:
                System.out.println("Текущее название задачи: " + task.getName());
                System.out.println("Введите новое название задачи:");
                String newName = getNonEmptyInput();
                service.editTaskName(task, newName);
                System.out.println("Название задачи успешно изменено на: " + newName);
                break;
            case DESCRIPTION:
                System.out.println("Текущее описание задачи: " + task.getDescription());
                System.out.println("Введите новое описание задачи:");
                String newDescription = getNonEmptyInput();
                service.editTaskDescription(task, newDescription);
                System.out.println("Описание задачи успешно изменено.");
                break;
            case STATUS:
                System.out.println("Текущий статус задачи: " + task.getStatus());
                System.out.println("Введите новый статус задачи (TODO, IN_PROGRESS, DONE):");
                String newStatus = getNonEmptyInput().toUpperCase();
                service.editTaskStatus(task, newStatus);
                break;
            case DATE:
                System.out.println("Текущая дата выполнения задачи: " + task.getDate());
                System.out.println("Введите новую дату выполнения задачи (в формате dd:MM:yyyy):");
                Date newDate = parseDate();
                service.editTaskDate(task, newDate);
                System.out.println("Дата выполнения задачи успешно изменена на: " + newDate);
                break;
        }
    }

    private void deleteTask() {
        System.out.println("Укажите название задачи для удаления:");
        String taskName = getNonEmptyInput();
        service.deleteTask(taskName);
    }

    private void filterTasks() {
        System.out.println("Выберите, по какому признаку нужна фильтрация:");
        printFieldsForSortOrFilter();
        String input = getNonEmptyInput();
        TaskFields field = service.getTaskField(input);
        if (field == null){
            System.out.println("Укажите корректный признак фильтрации.");
            return;
        }
        List <Task> filtredTasks = null;
        switch (field){
            case STATUS:
                System.out.println("Доступные статусы: TODO, IN_PROGRESS, DONE");
                while (filtredTasks == null){
                    String filterStatus = getNonEmptyInput();
                    filtredTasks = service.filterTasksByStatus(filterStatus);
                }

                if (filtredTasks.isEmpty()){
                    System.out.println("Задачи не найдены.");
                    break;
                }
                printFilteredOrSortedTasks(filtredTasks);
                break;
            case DATE:
                System.out.print("Введите значение фильтра по полю \"date\" (в формате dd:MM:yyyy): ");
                Date filterDate = parseDate();
                filtredTasks = service.filterTasksByDate(filterDate);
                if (filtredTasks.isEmpty()){
                    System.out.println("Задачи не найдены.");
                    break;
                }
                printFilteredOrSortedTasks(filtredTasks);
                break;
        }
    }

    private void sortTasks() {
        System.out.println("Выберите, по какому признаку нужна сортировка:");
        printFieldsForSortOrFilter();
        String input = getNonEmptyInput();
        TaskFields field = service.getTaskField(input);
        List <Task> sortedTasks;
        switch (field){
            case STATUS:
                sortedTasks = service.sortByStatus();
                printFilteredOrSortedTasks(sortedTasks);
                break;
            case DATE:
                sortedTasks = service.sortByDate();
                printFilteredOrSortedTasks(sortedTasks);
                break;
        }
    }

    void printFilteredOrSortedTasks(List<Task> filteredTasks) {
        if (filteredTasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            System.out.println("Найденные задачи:");
            filteredTasks.forEach(System.out::println);
        }
    }

    private void printFieldsForSortOrFilter() {
        Arrays.stream(TaskFields.values())
                .filter(field -> field.getNumber() == 3 || field.getNumber() == 4)
                .forEach(field -> System.out.println(field.getNumber() + ". " + field.getFieldName()));
    }

    private void exit() {
        System.out.println("До свидания!");
        service.exit();
    }

}