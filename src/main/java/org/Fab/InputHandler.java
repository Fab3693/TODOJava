package org.Fab;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
@Getter
@Setter
public class InputHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat format;
    private static final String DEFAULT_DATE_PATTERN = "dd:MM:yyyy";

    public InputHandler() {
        this.format = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
    }

    public String getNonEmptyInput() {
        String input;
        do {
            input = scanner.nextLine().trim();
        } while (input.isEmpty());
        return input;
    }

    public int getIntInput(int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Введите число от %d до %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Пожалуйста, введите число.");
            }
        }
    }

    public Date parseDate() {
        while (true) {
            try {
                return format.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.println("Неверный формат даты, используйте формат dd:MM:yyyy.");
            }
        }
    }
}
