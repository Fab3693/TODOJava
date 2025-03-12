package org.Fab;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Scanner;

@Getter
@Setter
public class Controller {
    private Scanner console;
    private Service service;

    public Controller() {
        this.console = new Scanner(System.in);
        this.service = new Service();
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
        if (service.getRepository().getTasks().isEmpty()) {
            return "1";
        }
        return console.nextLine();
    }

    public void executeCommand(TaskCommands command) {
        service.executeCommand(command, console);
    }
}