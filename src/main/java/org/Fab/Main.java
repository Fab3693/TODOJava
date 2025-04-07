package org.Fab;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler();
        Repository repository = new Repository();
        Service service = new Service(repository);
        Controller controller = new Controller(service, inputHandler);

        controller.greeting();

        while (true) {
            String input = controller.consoleInput();
            TaskCommands command = controller.getCommand(input);
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