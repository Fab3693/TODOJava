package org.Fab;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.*;

@Getter
@AllArgsConstructor
public enum TaskCommandsTest {
    ADD(1, "add") {
        void execute(HashSet<Task> tasks, Scanner scanner) throws DateTimeParseException {
            System.out.println("Укажите название задачи:");
            String name = "";
            while (name.trim().isEmpty()) {
                name = scanner.nextLine();
            }
            System.out.println("Укажите описание задачи:");
            String description = "";
            while (description.trim().isEmpty()) {
                description = scanner.nextLine();
            }
            System.out.println("Укажите срок выполнения задачи в формате \"ДД:ММ:ГГГГ\":");
            SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
            Date date = null;
            while (date == null) {
                try {
                    date = format.parse(scanner.nextLine());
                } catch (ParseException e) {
                    System.out.println("Неверный формат даты, используйте формат dd:MM:yyyy.");
                }
            }
            Status status = Status.TODO;
            String uniqueID = UUID.randomUUID().toString();
            tasks.add(new Task(uniqueID, status, name, description, date));
            System.out.println("Задача успешно создана!");
            System.out.println("id:" + uniqueID + "\nСтатус: " + status + "\nНазвание: " + name + "\nОписание: " + description + "\nКрайний срок: " + format.format(date));
        }
    },
    LIST(2, "list") {
        @Override
        void execute(HashSet<Task> tasks, Scanner scanner) {
            if (!tasks.isEmpty()) {
                tasks.forEach(System.out::println);
            } else {
                System.out.println("Список задач пуст. Вы можете создать задачу, выбрав команду \"ADD\"");
            }
        }
    },
    EDIT(3, "edit") {
        @Override
        void execute(HashSet<Task> tasks, Scanner scanner) {
            if (tasks.isEmpty()) {
                System.out.println("Список задач пуст. Вы можете создать задачу, выбрав команду \"ADD\"");
                return;
            }

            String taskName = promptForTaskName(scanner);
            Task task = findTaskByName(tasks, taskName);

            if (task == null) {
                System.out.println("Задача с названием '" + taskName + "' не найдена.");
                return;
            }

            System.out.println("Задача найдена: " + task.getName());
            editTaskFields(task, scanner);
        }

        // Методы вынесены в конец класса

        private String promptForTaskName(Scanner scanner) {
            System.out.println("Укажите название задачи, которую хотите отредактировать:");
            String taskName = "";
            while (taskName.trim().isEmpty()) {
                taskName = scanner.nextLine();
            }
            return taskName;
        }

        private Task findTaskByName(HashSet<Task> tasks, String taskName) {
            for (Task task : tasks) {
                if (Objects.equals(task.getName(), taskName)) {
                    return task;
                }
            }
            return null;
        }

        private void editTaskFields(Task task, Scanner scanner) {
            System.out.println("Выберите, что бы вы хотели отредактировать:");

            HashMap<Integer, String> fieldsAndNumbers = getEditableFields();
            fieldsAndNumbers.forEach((key, value) -> System.out.println(key + ". " + value));

            String fieldInput = "";
            while (fieldInput.trim().isEmpty()) {
                fieldInput = scanner.nextLine();
                int fieldNumber = parseFieldInput(fieldInput, fieldsAndNumbers);

                if (fieldNumber == -1) {
                    System.out.println("Неверный номер или имя поля. Попробуйте ещё раз.");
                    fieldInput = "";
                    continue;
                }

                try {
                    editField(task, scanner, fieldNumber);
                } catch (IllegalArgumentException | ParseException e) {
                    System.out.println(e.getMessage());
                    fieldInput = "";
                }
            }
        }

        private HashMap<Integer, String> getEditableFields() {
            Class<?> taskClass = Task.class;
            Field[] fields = taskClass.getDeclaredFields();
            HashMap<Integer, String> fieldsAndNumbers = new HashMap<>();
            int fieldNumber = 1;

            for (Field field : fields) {
                if (!field.getName().equals("id")) {
                    fieldsAndNumbers.put(fieldNumber, field.getName());
                    fieldNumber++;
                }
            }

            return fieldsAndNumbers;
        }

        private int parseFieldInput(String fieldInput, HashMap<Integer, String> fieldsAndNumbers) {
            try {
                return Integer.parseInt(fieldInput);
            } catch (NumberFormatException e) {
                for (Map.Entry<Integer, String> entry : fieldsAndNumbers.entrySet()) {
                    if (Objects.equals(entry.getValue(), fieldInput)) {
                        return entry.getKey();
                    }
                }
            }
            return -1; // Неверный ввод
        }

        private void editField(Task task, Scanner scanner, int fieldNumber) throws ParseException {
            switch (fieldNumber) {
                case 1:
                    editStatusField(task, scanner);
                    break;
                case 2:
                    editNameField(task, scanner);
                    break;
                case 3:
                    editDescriptionField(task, scanner);
                    break;
                case 4:
                    editDateField(task, scanner);
                    break;
                default:
                    throw new IllegalArgumentException("Неверный номер поля.");
            }
        }

        private void editStatusField(Task task, Scanner scanner) {
            boolean validStatus = false;
            while (!validStatus) {
                System.out.print("Введите новое значение для поля \"status\" (TODO, IN_PROGRESS, DONE): ");
                String statusInput = scanner.nextLine();
                try {
                    task.setStatus(Status.valueOf(statusInput.toUpperCase()));
                    System.out.println("Поле 'status' успешно изменено!");
                    validStatus = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверное значение для статуса. Допустимые значения: TODO, IN_PROGRESS, DONE.");
                }
            }
        }

        private void editNameField(Task task, Scanner scanner) {
            System.out.print("Введите новое значение для поля \"name\": ");
            String nameInput = scanner.nextLine();
            task.setName(nameInput);
            System.out.println("Поле 'name' успешно изменено!");
        }

        private void editDescriptionField(Task task, Scanner scanner) {
            System.out.print("Введите новое значение для поля \"description\": ");
            String descriptionInput = scanner.nextLine();
            task.setDescription(descriptionInput);
            System.out.println("Поле 'description' успешно изменено!");
        }

        private void editDateField(Task task, Scanner scanner) throws ParseException {
            System.out.print("Введите новое значение для поля 'date' (в формате dd:MM:yyyy): ");
            String dateInput = scanner.nextLine();
            SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
            task.setDate(format.parse(dateInput));
            System.out.println("Поле 'date' успешно изменено!");
        }
    },
    DELETE(4, "delete") {
        @Override
        void execute(HashSet<Task> tasks, Scanner scanner) {
            if (!tasks.isEmpty()) {
                System.out.println("Укажите название задачи для удаления:");
                String name = "";
                while (name.trim().isEmpty()) {
                    name = scanner.nextLine();
                    for (Task task : tasks) {
                        if (Objects.equals(task.getName(), name)) {
                            tasks.remove(task);
                            System.out.println("Задача: " + task.getName() + " успешно удалена!");
                        }
                    }
                }
            }
            System.out.println("Список задач пуст. Вы можете создать задачу, выбрав команду \"ADD\"");
        }
    }, //*Не работает*/
    FILTER(5, "filter") {
        @AllArgsConstructor
        @Getter
        private enum Filter {
            DATE(1, "date") {
                @Override
                void execute(HashSet<Task> tasks, Scanner scanner) {
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

                            if (filteredTasks.isEmpty()) {
                                System.out.println("Задачи не найдены.");
                            } else {
                                System.out.println("Найденные задачи:");
                                filteredTasks.forEach(System.out::println);
                            }
                            break;
                        } catch (ParseException e) {
                            System.out.println("Неверный формат даты, используйте формат dd:MM:yyyy.");
                        }
                    }
                }
            },
            STATUS(2, "status") {
                @Override
                void execute(HashSet<Task> tasks, Scanner scanner) {
                    System.out.println("Доступные статусы:");
                    Arrays.stream(Status.values()).forEach(status1 ->
                            System.out.println(status1.getNumber() + ". " + status1.getName())
                    );

                    Status status = null;
                    while (status == null) {
                        System.out.print("Введите значение фильтра по полю \"status\" (номер или название): ");
                        String input = scanner.nextLine().trim();

                        if (input.isEmpty()) {
                            System.out.println("Ввод не может быть пустым. Пожалуйста, попробуйте снова.");
                            continue;
                        }

                        try {
                            int number = Integer.parseInt(input);
                            status = Arrays.stream(Status.values())
                                    .filter(s -> s.getNumber() == number)
                                    .findFirst()
                                    .orElse(null);
                        } catch (NumberFormatException e) {
                            status = Arrays.stream(Status.values())
                                    .filter(s -> s.getName().equalsIgnoreCase(input))
                                    .findFirst()
                                    .orElse(null);
                        }

                        if (status == null) {
                            System.out.println("Неверный статус. Пожалуйста, попробуйте снова.");
                        }
                    }

                    Status finalStatus = status;
                    List<Task> filteredTasks = tasks.stream()
                            .filter(task -> task.getStatus().equals(finalStatus))
                            .toList();

                    if (filteredTasks.isEmpty()) {
                        System.out.println("Задачи не найдены.");
                    } else {
                        System.out.println("Найденные задачи:");
                        filteredTasks.forEach(System.out::println);
                    }
                }
            };
            private final int number;
            private final String name;

            abstract void execute(HashSet<Task> tasks, Scanner scanner);
        }

        @Override
        void execute(HashSet<Task> tasks, Scanner scanner) {
            if (!tasks.isEmpty()) {
                String input = "";
                Filter filter = null;
                System.out.println("Выберите, по какому признаку нужна фильтрация.");
                Arrays.stream(Filter.values()).forEach(filter1 -> System.out.println(filter1.getNumber() + ". " + filter1.getName()));
                while (input.trim().isEmpty()) {
                    try {
                        input = scanner.nextLine();
                        int number = Integer.parseInt(input);
                        for (Filter filter1 : Filter.values()) {
                            if (filter1.getNumber() == number) {
                                filter = filter1;
                            }
                        }
                    } catch (NumberFormatException e) {
                        for (Filter filter1 : Filter.values()) {
                            if (filter1.getName().equalsIgnoreCase(input)) {
                                filter = filter1;
                            }
                        }
                    }
                }
                try {
                    Objects.requireNonNull(filter).execute(tasks, scanner);
                } catch (IllegalArgumentException | NullPointerException e) {
                    System.out.println("Неизвестный фильр, попробуйте ещё раз.");
                }
            } else {
                System.out.println("Список задач пуст. Вы можете создать задачу, выбрав команду \"ADD\"");
            }
        }
    },
    SORT(6, "sort") {
        @AllArgsConstructor
        @Getter
        private enum Filter {
            DATE(1, "date") {
                @Override
                void execute(HashSet<Task> tasks, Scanner scanner) {
                    List<Task> filteredTasks = tasks.stream().sorted(Comparator.comparing(Task::getDate)).toList();
                    filteredTasks.forEach(System.out::println);
                }
            },
            STATUS(2, "status") {
                @Override
                void execute(HashSet<Task> tasks, Scanner scanner) {
                    List<Task> filteredTasks = tasks.stream().sorted(Comparator.comparing(Task::getStatus)).toList();
                    filteredTasks.forEach(System.out::println);
                }
            };
            private final int number;
            private final String name;

            abstract void execute(HashSet<Task> tasks, Scanner scanner);
        }

        @Override
        void execute(HashSet<Task> tasks, Scanner scanner) {
            if (!tasks.isEmpty()) {
                String input = "";
                Filter filter = null;
                System.out.println("Выберите, по какому признаку нужна сортировка.");
                Arrays.stream(Filter.values()).forEach(filter1 -> System.out.println(filter1.getNumber() + ". " + filter1.getName()));
                while (input.trim().isEmpty()) {
                    try {
                        input = scanner.nextLine();
                        int number = Integer.parseInt(input);
                        for (Filter filter1 : Filter.values()) {
                            if (filter1.getNumber() == number) {
                                filter = filter1;
                            }
                        }
                    } catch (NumberFormatException e) {
                        for (Filter filter1 : Filter.values()) {
                            if (filter1.getName().equalsIgnoreCase(input)) {
                                filter = filter1;
                            }
                        }
                    }
                }
                try {
                    Objects.requireNonNull(filter).execute(tasks, scanner);
                } catch (IllegalArgumentException | NullPointerException e) {
                    System.out.println("Неизвестный признак, попробуйте ещё раз.");
                }
            } else {
                System.out.println("Список задач пуст. Вы можете создать задачу, выбрав команду \"ADD\"");
            }
        }
    },
    EXIT(7, "exit") {
        @Override
        void execute(HashSet<Task> tasks, Scanner scanner) {
            System.out.println("До свидания!");
            System.exit(0);
        }
    };

    private final int number;
    private final String command;

    public static TaskCommands getByNumber(String input) {
        try {
            int number = Integer.parseInt(input);
            for (TaskCommands taskCommands : TaskCommands.values()) {
                if (taskCommands.getNumber() == number) {
                    return taskCommands;
                }
            }
        } catch (NumberFormatException e) {
            for (TaskCommands taskCommands : TaskCommands.values()) {
                if (taskCommands.getCommand().equalsIgnoreCase(input)) {
                    return taskCommands;
                }
            }
        }
        return null;
    }

    abstract void execute(HashSet<Task> tasks, Scanner scanner);
}