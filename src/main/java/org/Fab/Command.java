package org.Fab;


public enum Command {
    ADD (1,"add"),
    LIST (2,"list"),
    EDIT (3,"edit"),
    DELETE (4,"delete"),
    FILTER (5,"filter"),
    SORT (6,"sort"),
    EXIT (7,"exit");

    private final int number;
    Command(int number, String add){
        this.number = number;
    }

    public int getNumber (){return number;}
}
