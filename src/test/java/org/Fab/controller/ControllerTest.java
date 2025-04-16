package org.Fab.controller;

import org.Fab.enums.TaskCommands;
import org.Fab.service.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class ControllerTest {
    private SimpleDateFormat dateFormat;
    @Mock
    private Service service;
    @InjectMocks
    private Controller controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(Service.class);
        controller = new Controller(service);
        dateFormat = new SimpleDateFormat("dd:MM:yyyy");
    }

    @Test
    void greetingTest(){
        controller.greeting();
    }

    @Test
    void consoleInputTest(){
        when(service.checkTaskListIsEmpty()).thenReturn(true);
        assertEquals("1", controller.consoleInput());
    }

    @Test
    void getCommandTest() {
        assertEquals(TaskCommands.ADD, service.getCommand("ADD"));
    }
}
