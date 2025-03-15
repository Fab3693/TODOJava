package org.Fab;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class SortTasks {
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

    static void sortByDate(HashSet<Task> tasks) {
        List<Task> sortedTasks = tasks.stream()
                .sorted(Comparator.comparing(Task::getDate))
                .toList();

        System.out.println("Задачи, отсортированные по дате:");
        sortedTasks.forEach(System.out::println);
    }

    static void sortByStatus(HashSet<Task> tasks) {
        List<Task> sortedTasks = tasks.stream()
                .sorted(Comparator.comparing(Task::getStatus))
                .toList();

        System.out.println("Задачи, отсортированные по статусу:");
        sortedTasks.forEach(System.out::println);
    }
}
