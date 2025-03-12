package org.Fab;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum TaskCommands {
    ADD(1, "add") {
        void execute(HashSet<Task> tasks, Scanner scanner) throws DateTimeParseException {

        }
    },
    LIST(2, "list") {
        @Override
        void execute(HashSet<Task> tasks, Scanner scanner) {

        }
    },
    EDIT(3, "edit") {
        @Override
        void execute(HashSet<Task> tasks, Scanner scanner) {
            if (tasks.isEmpty()) {
                System.out.println("Список задач пуст. Вы можете создать задачу, выбрав команду \"ADD\"");
                return;
            }
            Set<Task> filteredTasks = tasks.stream()
                    .filter(task -> task.getName().startsWith("qwe"))
                    .filter(task -> task.getDescription().length() <= 10)
                    .collect(Collectors.toSet());

            System.out.println("Укажите название задачи, которую хотите отредактировать:");
            String taskName = "";
            while (taskName.trim().isEmpty()) {
                boolean taskFound = false;
                taskName = scanner.nextLine();
                for (Task task : tasks) {
                    if (Objects.equals(task.getName(), taskName)) {
                        taskFound = true;
                        System.out.println("Задача найдена: " + task.getName());
                        System.out.println("Выберите, что бы вы хотели отредактировать:");

                        Class<?> taskClass = Task.class;
                        Field[] fields = taskClass.getDeclaredFields();
                        HashMap<Integer, String> fieldsAndNumbers = new HashMap<>();
                        int fieldNumber = 1;
                        for (Field field : fields) {
                            if (!field.getName().equals("id")) {
                                fieldsAndNumbers.put(fieldNumber, field.getName());
                                System.out.println(fieldNumber + ". " + field.getName());
                                fieldNumber++;
                            }
                        }

                        String fieldInput = "";
                        while (fieldInput.trim().isEmpty()) {
                            fieldInput = scanner.nextLine();
                            int number = 0;
                            try {
                                number = Integer.parseInt(fieldInput);
                            } catch (NumberFormatException e) {
                                for (Map.Entry<Integer, String> entry : fieldsAndNumbers.entrySet()) {
                                    if (Objects.equals(entry.getValue(), fieldInput)) {
                                        number = entry.getKey();
                                    }
                                }
                            }
                            try {
                                switch (number) { //*При добавлении новых кейсов нужно обращать внимание на порядок полей. Рекомендуется каждое новое поле добавлять после существующих, в противном случае придётся менять порядок кейсов.*/
                                    case 1:
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
                                        break;
                                    case 2:
                                        System.out.print("Введите новое значение для поля \"name\": ");
                                        String nameInput = scanner.nextLine();
                                        task.setName(nameInput);
                                        System.out.println("Поле 'name' успешно изменено!");
                                        break;
                                    case 3:
                                        System.out.print("Введите новое значение для поля \"description\": ");
                                        String descriptionInput = scanner.nextLine();
                                        task.setDescription(descriptionInput);
                                        System.out.println("Поле 'description' успешно изменено!");
                                        break;
                                    case 4:
                                        System.out.print("Введите новое значение для поля 'date' (в формате dd:MM:yyyy): ");
                                        String dateInput = scanner.nextLine();
                                        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
                                        task.setDate(format.parse(dateInput));
                                        System.out.println("Поле 'date' успешно изменено!");
                                        break;
                                    default:
                                        System.out.println("Неверный номер или имя поля. Попробуйте ещё раз.");
                                        fieldInput = "";
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Неверный ввод. Пожалуйста, введите номер поля.");
                                fieldInput = "";
                            } catch (IllegalArgumentException e) {
                                System.out.println("Неверное значение для статуса. Допустимые значения: TODO, IN_PROGRESS, DONE.");
                                fieldInput = "";
                            } catch (ParseException e) {
                                System.out.println("Неверный формат даты, ипользуйте формат dd:MM:yyyy.");
                                fieldInput = "";
                            }
                        }
                        break;
                    }
                }
                if (!taskFound) {
                    System.out.println("Задача с названием '" + taskName + "' не найдена.");
                }
            }
        }
    },
    DELETE(4, "delete") {
        @Override
        void execute(HashSet<Task> tasks, Scanner scanner) {
            if (!tasks.isEmpty()) {
                System.out.println("Список задач пуст. Вы можете создать задачу, выбрав команду \"ADD\"");
                return;
            }
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

    abstract void execute(HashSet<Task> tasks, Scanner scanner);
}