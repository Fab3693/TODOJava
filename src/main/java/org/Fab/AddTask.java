package org.Fab;

public class AddTask {
    public void printTaskCreated(Task task) {
        System.out.println("Задача успешно создана!");
        System.out.println("id:" + task.getId() + "\nСтатус: " + task.getStatus() + "\nНазвание: " + task.getName() +
                "\nОписание: " + task.getDescription() + "\nКрайний срок: " + task.getDate()); //может быть трабл
    }
}
