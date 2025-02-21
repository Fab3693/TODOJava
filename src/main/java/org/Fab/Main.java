package org.Fab;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        HashSet<Task> tasks = new HashSet<>();
        //tasks.add(new Task("123", Status.TODO, "qwe", "qwe", new Date(2000,12,12)));
        Scanner console = new Scanner(System.in);
        String inputValue = "2";
        System.out.println("Здравствуйте! Выберите действие.");

        while (Command.getByNumber(inputValue) != Command.EXIT) {
            Arrays.stream(Command.values()).forEach(command -> System.out.println(command.getNumber() + ". " + command.getCommand()));
            inputValue = console.nextLine();
            try {
                Command command = Command.getByNumber(inputValue);
                Objects.requireNonNull(command).execute(tasks, console);
            } catch (IllegalArgumentException | NullPointerException e){
                System.out.println("Неизвестная команда, попробуйте ещё раз.");
            }
        }
    }
}