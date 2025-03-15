package org.Fab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class EditTask {
    static void editTaskFields(Task task, Scanner scanner) {
        System.out.println("Выберите поле для редактирования:");
        System.out.println("1. Название");
        System.out.println("2. Описание");
        System.out.println("3. Статус");
        System.out.println("4. Дата выполнения");

        String fieldInput = Service.getNonEmptyInput(scanner);
        int fieldNumber;

        try {
            fieldNumber = Integer.parseInt(fieldInput);
        } catch (NumberFormatException e) {
            System.out.println("Неверный ввод. Пожалуйста, введите номер поля.");
            return;
        }

        switch (fieldNumber) {
            case 1:
                editTaskName(task, scanner);
                break;
            case 2:
                editTaskDescription(task, scanner);
                break;
            case 3:
                editTaskStatus(task, scanner);
                break;
            case 4:
                editTaskDate(task, scanner);
                break;
            default:
                System.out.println("Неверный номер поля. Пожалуйста, выберите от 1 до 4.");
        }
    }

    private static void editTaskName(Task task, Scanner scanner) {
        System.out.println("Текущее название задачи: " + task.getName());
        System.out.println("Введите новое название задачи:");
        String newName = Service.getNonEmptyInput(scanner);
        task.setName(newName);
        System.out.println("Название задачи успешно изменено на: " + newName);
    }

    private static void editTaskDescription(Task task, Scanner scanner) {
        System.out.println("Текущее описание задачи: " + task.getDescription());
        System.out.println("Введите новое описание задачи:");
        String newDescription = Service.getNonEmptyInput(scanner);
        task.setDescription(newDescription);
        System.out.println("Описание задачи успешно изменено.");
    }

    private static void editTaskStatus(Task task, Scanner scanner) {
        System.out.println("Текущий статус задачи: " + task.getStatus());
        System.out.println("Введите новый статус задачи (TODO, IN_PROGRESS, DONE):");
        String newStatusInput = Service.getNonEmptyInput(scanner).toUpperCase();

        try {
            Status newStatus = Status.valueOf(newStatusInput);
            task.setStatus(newStatus);
            System.out.println("Статус задачи успешно изменен на: " + newStatus);
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный статус. Допустимые значения: TODO, IN_PROGRESS, DONE.");
        }
    }

    private static void editTaskDate(Task task, Scanner scanner) {
        System.out.println("Текущая дата выполнения задачи: " + task.getDate());
        System.out.println("Введите новую дату выполнения задачи (в формате dd:MM:yyyy):");
        String newDateInput = Service.getNonEmptyInput(scanner);

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
            Date newDate = format.parse(newDateInput);
            task.setDate(newDate);
            System.out.println("Дата выполнения задачи успешно изменена на: " + newDate);
        } catch (ParseException e) {
            System.out.println("Неверный формат даты, используйте формат dd:MM:yyyy.");
        }
    }
}
