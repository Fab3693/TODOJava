package org.Fab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class FilterTasks {
    static int getIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Введите число от " + min + " до " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Пожалуйста, введите число.");
            }
        }
    }

    static void filterByDate(HashSet<Task> tasks, Scanner scanner) {
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
        format.setLenient(false);

        while (true) {
            System.out.print("Введите значение фильтра по полю \"date\" (в формате dd:MM:yyyy): ");
            String input = scanner.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("Ввод не может быть пустым. Пожалуйста, попробуйте снова.");
                continue;
            }

            try {
                Date filterDate = format.parse(input);
                List<Task> filteredTasks = tasks.stream()
                        .filter(task -> task.getDate().equals(filterDate))
                        .toList();

                printFilteredTasks(filteredTasks);
                break;
            } catch (ParseException e) {
                System.out.println("Неверный формат даты, используйте формат dd:MM:yyyy.");
            }
        }
    }

    static void filterByStatus(HashSet<Task> tasks, Scanner scanner) {
        System.out.println("Доступные статусы:");
        System.out.println("1. TODO");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. DONE");

        int statusChoice = getIntInput(scanner, 1, 3);
        Status status = Status.values()[statusChoice - 1];

        List<Task> filteredTasks = tasks.stream()
                .filter(task -> task.getStatus() == status)
                .toList();

        printFilteredTasks(filteredTasks);
    }

    private static void printFilteredTasks(List<Task> filteredTasks) {
        if (filteredTasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            System.out.println("Найденные задачи:");
            filteredTasks.forEach(System.out::println);
        }
    }
}
