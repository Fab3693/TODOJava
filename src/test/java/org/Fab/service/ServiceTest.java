package org.Fab.service;

import org.Fab.entity.Task;
import org.Fab.enums.Status;
import org.Fab.enums.TaskCommands;
import org.Fab.enums.TaskFields;
import org.Fab.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {
    private Task testTask;
    private Date testDate;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
    @InjectMocks
    private Service mockservice;
    @Mock
    private Repository mockRepository;

    @BeforeEach
    void setUp() throws ParseException {
        testDate = dateFormat.parse("12:12:1111");
        testTask = new Task(Status.TODO, "TaskName", "TaskDescription", testDate, new Repository());
    }

    @Test
    void testGetCommand() {
        assertEquals(TaskCommands.ADD, mockservice.getCommand("1"));
        assertEquals(TaskCommands.ADD, mockservice.getCommand("ADD"));
        assertNull(mockservice.getCommand("999"));
    }

    @Test
    void testGetTaskField() {
        assertEquals(TaskFields.NAME, mockservice.getTaskField("1"));
        assertEquals(TaskFields.NAME, mockservice.getTaskField("Название"));
        assertNull(mockservice.getTaskField("qwerty"));
        assertNull(mockservice.getTaskField("999"));
    }

    @Test
    void testGetStatus() {
        assertEquals(Status.TODO, mockservice.getStatus("1"));
        assertEquals(Status.TODO, mockservice.getStatus("TODO"));
        assertNull(mockservice.getTaskField("qwerty"));
        assertNull(mockservice.getStatus("999"));
    }

    @Test
    void testAddTask() {
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        mockservice.addTask("New", "Desc", testDate, Status.TODO);
        verify(mockRepository).addTask(taskCaptor.capture());
        Task savedTask = taskCaptor.getValue();
        assertEquals("New", savedTask.getName());
        assertEquals("Desc", savedTask.getDescription());
        assertEquals(testDate, savedTask.getDate());
        assertEquals(Status.TODO, savedTask.getStatus());
    }

    @Test
    void testListTasks() {
        when(mockRepository.getTasks()).thenReturn(new HashSet<>(Set.of(testTask)));
        Set<Task> tasks = mockservice.listTasks();
        assertEquals(1, tasks.size());
        assertTrue(tasks.contains(testTask));
    }

    @Test
    void testEditMethods() {
        mockservice.editTaskName(testTask, "NewName");
        assertEquals("NewName", testTask.getName());

        mockservice.editTaskDescription(testTask, "NewDesc");
        assertEquals("NewDesc", testTask.getDescription());

        mockservice.editTaskStatus(testTask, "IN_PROGRESS");
        assertEquals(Status.IN_PROGRESS, testTask.getStatus());

        mockservice.editTaskDate(testTask, testDate);
        assertEquals(testDate, testTask.getDate());
    }

    @Test
    void testDeleteTask() {
        when(mockRepository.getTasks()).thenReturn(new HashSet<>(Set.of(testTask)));
        mockservice.deleteTask("Test");
        when(mockRepository.getTasks()).thenReturn(new HashSet<>());
        assertTrue(mockservice.checkTaskListIsEmpty());
    }

    @Test
    void testFilterAndSortMethods() throws ParseException {
        Task task1 = new Task(Status.TODO, "Task1", "Desc", testDate, new Repository());
        testDate = dateFormat.parse("13:10:2222");
        Task task2 = new Task(Status.DONE, "Task2", "Desc", testDate, new Repository());

        when(mockRepository.getTasks()).thenReturn(new HashSet<>(Set.of(task1, task2)));

        List<Task> filtered = mockservice.filterTasksByStatus("DONE");
        assertEquals(1, filtered.size());
        assertEquals(task2, filtered.getFirst());

        filtered = mockservice.filterTasksByDate(testDate);
        assertEquals(1, filtered.size());
        assertEquals(task2, filtered.getFirst());

        List<Task> sorted = mockservice.sortByDate();
        assertEquals(task1, sorted.get(0));
        assertEquals(task2, sorted.get(1));

        sorted = mockservice.sortByStatus();
        assertEquals(task1, sorted.get(0));
        assertEquals(task2, sorted.get(1));
    }

    @Test
    void testReturnIfTaskExists() {
        when(mockRepository.findTaskByName("Test")).thenReturn(testTask);
        when(mockRepository.findTaskByName("NotExist")).thenReturn(null);
        assertEquals(testTask, mockservice.returnIfTaskExists("Test"));
        assertNull(mockservice.returnIfTaskExists("NotExist"));
    }

    @Test
    void testCheckTaskListIsEmpty() {
        when(mockRepository.getTasks()).thenReturn(new HashSet<>());
        assertTrue(mockservice.checkTaskListIsEmpty());
        when(mockRepository.getTasks()).thenReturn(new HashSet<>(Set.of(testTask)));
        assertFalse(mockservice.checkTaskListIsEmpty());
    }
}