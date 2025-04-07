package org.Fab;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TaskEditor {

    void editTaskFields(Task task, InputHandler inputHandler) {
        System.out.println("Выберите поле для редактирования:");
        System.out.println("1. Название");
        System.out.println("2. Описание");
        System.out.println("3. Статус");
        System.out.println("4. Дата выполнения");

        String fieldInput = inputHandler.getNonEmptyInput();
        int fieldNumber;

        try {
            fieldNumber = Integer.parseInt(fieldInput);
        } catch (NumberFormatException e) {
            System.out.println("Неверный ввод. Пожалуйста, введите номер поля.");
            return;
        }

        switch (fieldNumber) {
            case 1:
                editTaskName(task, inputHandler);
                break;
            case 2:
                editTaskDescription(task, inputHandler);
                break;
            case 3:
                editTaskStatus(task, inputHandler);
                break;
            case 4:
                editTaskDate(task, inputHandler);
                break;
            default:
                System.out.println("Неверный номер поля. Пожалуйста, выберите от 1 до 4.");
        }
    }

    void editTaskName(Task task, InputHandler inputHandler) {
        System.out.println("Текущее название задачи: " + task.getName());
        System.out.println("Введите новое название задачи:");
        String newName = inputHandler.getNonEmptyInput();
        task.setName(newName);
        System.out.println("Название задачи успешно изменено на: " + newName);
    }

    void editTaskDescription(Task task, InputHandler inputHandler) {
        System.out.println("Текущее описание задачи: " + task.getDescription());
        System.out.println("Введите новое описание задачи:");
        String newDescription = inputHandler.getNonEmptyInput();
        task.setDescription(newDescription);
        System.out.println("Описание задачи успешно изменено.");
    }

    void editTaskStatus(Task task, InputHandler inputHandler) {
        System.out.println("Текущий статус задачи: " + task.getStatus());
        System.out.println("Введите новый статус задачи (TODO, IN_PROGRESS, DONE):");
        String newStatusInput = inputHandler.getNonEmptyInput().toUpperCase();

        try {
            Status newStatus = Status.valueOf(newStatusInput);
            task.setStatus(newStatus);
            System.out.println("Статус задачи успешно изменен на: " + newStatus);
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный статус. Допустимые значения: TODO, IN_PROGRESS, DONE.");
        }
    }

    void editTaskDate(Task task, InputHandler inputHandler) {
        System.out.println("Текущая дата выполнения задачи: " + task.getDate());
        System.out.println("Введите новую дату выполнения задачи (в формате dd:MM:yyyy):");

        Date newDate = inputHandler.parseDate();
        task.setDate(inputHandler.parseDate());

        System.out.println("Дата выполнения задачи успешно изменена на: " + newDate);
    }
}