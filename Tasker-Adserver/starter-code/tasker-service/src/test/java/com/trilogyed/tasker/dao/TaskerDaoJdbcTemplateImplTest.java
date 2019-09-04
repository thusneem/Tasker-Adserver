package com.trilogyed.tasker.dao;

import com.trilogyed.tasker.model.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TaskerDaoJdbcTemplateImplTest {
     @Autowired
     TaskerDao dao;
    @Before
    public void setUp() throws Exception {
        List<Task> tasks = dao.getAllTasks();

        tasks.stream()
                .forEach(task -> dao.deleteTask(task.getId()));
    }

    @Test
    public void createTask() {
        Task tasks = new Task();
        tasks.setDescription("create task");
        tasks.setCreateDate(LocalDate.of(2019, 3, 27));
        tasks.setDueDate(LocalDate.of(2019,4,26));
        tasks.setCategory("Business");
        tasks = dao.createTask(tasks);
        Task fromDao = dao.getTask(tasks.getId());
        assertEquals(fromDao, tasks);


    }

    @Test
    public void getTask() {
        Task tasks = new Task();
        tasks.setDescription("create task");
        tasks.setCreateDate(LocalDate.of(2019,3,20));
        tasks.setDueDate(LocalDate.of(2019,3,25));
        tasks.setCategory("Business");
        tasks = dao.createTask(tasks);
        Task fromDao = dao.getTask(tasks.getId());
        assertEquals(fromDao, tasks);
    }

    @Test
    public void getAllTasks() {
        Task tasks = new Task();
        tasks.setDescription("create task");
        tasks.setCreateDate(LocalDate.of(2019,3,20));
        tasks.setDueDate(LocalDate.of(2019,3,25));
        tasks.setCategory("Business");
        tasks = dao.createTask(tasks);

        tasks.setDescription("create task");
        tasks.setCreateDate(LocalDate.of(2019,3,20));
        tasks.setDueDate(LocalDate.of(2019,3,25));
        tasks.setCategory("Business");
        tasks = dao.createTask(tasks);

        List<Task> taskList = dao.getAllTasks();

        assertEquals(2, taskList.size());
    }

    @Test
    public void getTasksByCategory() {
        Task tasks = new Task();
        tasks.setDescription("create task");
        tasks.setCreateDate(LocalDate.of(2019,3,20));
        tasks.setDueDate(LocalDate.of(2019,3,25));
        tasks.setCategory("Business");
        tasks = dao.createTask(tasks);

        tasks.setDescription("create task");
        tasks.setCreateDate(LocalDate.of(2019,3,20));
        tasks.setDueDate(LocalDate.of(2019,3,25));
        tasks.setCategory("Business");
        tasks = dao.createTask(tasks);
    }

    @Test
    public void updateTask() {
        Task tasks = new Task();
        tasks.setDescription("create task");
        tasks.setCreateDate(LocalDate.of(2019,3,20));
        tasks.setDueDate(LocalDate.of(2019,3,25));
        tasks.setCategory("Business");
        tasks = dao.createTask(tasks);

        tasks.setDescription("task2");
        tasks.setCreateDate(LocalDate.of(2019,3,20));
        tasks.setDueDate(LocalDate.of(2019,3,25));
        tasks.setCategory("Stock");
        dao.updateTask(tasks);
        Task fromDao = dao.getTask(tasks.getId());
        assertEquals(tasks, fromDao);
    }

    @Test
    public void deleteTask() {
        Task tasks = new Task();
        tasks.setDescription("create task");
        tasks.setCreateDate(LocalDate.of(2019,3,20));
        tasks.setDueDate(LocalDate.of(2019,3,25));
        tasks.setCategory("Business");
        tasks = dao.createTask(tasks);

        tasks.setDescription("create task");
        tasks.setCreateDate(LocalDate.of(2019,3,20));
        tasks.setDueDate(LocalDate.of(2019,3,25));
        tasks.setCategory("Business");
        tasks = dao.createTask(tasks);

        dao.deleteTask(tasks.getId());
        Task fromDao = dao.getTask(tasks.getId());
        assertNull(fromDao);
    }
}