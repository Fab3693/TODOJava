package org.Fab;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.greeting();

        while (true) {
            String input = controller.consoleInput();
            TaskCommands command = Service.getCommand(input);
            if (command == TaskCommands.EXIT) {
                break;
            }
            if (command != null) {
                controller.executeCommand(command);
            } else {
                System.out.println("Неизвестная команда, попробуйте ещё раз.");
            }
            controller.showAllCommands();
        }
    }
}