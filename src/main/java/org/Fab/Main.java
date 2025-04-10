package org.Fab;

import org.Fab.enums.TaskCommands;

public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository();
        Service service = new Service(repository);
        Controller controller = new Controller(service);

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