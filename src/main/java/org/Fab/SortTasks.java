package org.Fab;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class SortTasks {

     void sortByDate(HashSet<Task> tasks) {
        List<Task> sortedTasks = tasks.stream()
                .sorted(Comparator.comparing(Task::getDate))
                .toList();

        System.out.println("Задачи, отсортированные по дате:");
        sortedTasks.forEach(System.out::println);
    }

     void sortByStatus(HashSet<Task> tasks) {
        List<Task> sortedTasks = tasks.stream()
                .sorted(Comparator.comparing(Task::getStatus))
                .toList();

        System.out.println("Задачи, отсортированные по статусу:");
        sortedTasks.forEach(System.out::println);
    }
}