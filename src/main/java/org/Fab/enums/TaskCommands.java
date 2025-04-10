package org.Fab.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskCommands {
    ADD(1, "add"),
    LIST(2, "list"),
    EDIT(3, "edit"),
    DELETE(4, "delete"),
    FILTER(5, "filter"),
    SORT(6, "sort"),
    EXIT(7, "exit");

    private final int number;
    private final String command;
}