package org.Fab.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskFields {
    NAME(1, "Название"),
    DESCRIPTION(2, "Описание"),
    STATUS(3, "Статус"),
    DATE(4, "Дата выполнения");

    private final int number;
    private final String fieldName;
}
