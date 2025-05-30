package org.Fab.enums;

import lombok.Getter;

@Getter
public enum Status {
    TODO(1, "todo"),
    IN_PROGRESS(2, "in_progress"),
    DONE(3, "done");

    private final String name;
    private final int number;

    Status(int number, String name) {
        this.name = name;
        this.number = number;
    }
}