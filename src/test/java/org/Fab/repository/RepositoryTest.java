package org.Fab.repository;

import org.Fab.entity.Task;
import org.Fab.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RepositoryTest {
    @Mock
    private Repository mockRepository;
    private Task testTask;
    private HashSet<Task> testTasks;

    @BeforeEach
    void setUp() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        Date testDate = dateFormat.parse("12:12:1111");
        testTasks = new HashSet<>();
        testTask = new Task(Status.TODO, "TaskName", "TaskDescription", testDate, mockRepository);
    }

    @Test
    void addTaskTest() {
        testTasks.add(testTask);
        assertTrue(testTasks.contains(testTask));
    }

    @Test
    void removeTaskTest() {
        testTasks.add(testTask);
        testTasks.remove(testTask);
        assertEquals(0, testTasks.size());
    }
}
