package com.trilogyed.tasker.service;
import com.trilogyed.tasker.dao.TaskerDao;
import com.trilogyed.tasker.dao.TaskerDaoJdbcTemplateImpl;
import com.trilogyed.tasker.model.Task;
import com.trilogyed.tasker.model.TaskViewModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class TaskerServiceLayerTest {
    TaskerServiceLayer service;
    TaskerDao dao;
    RestTemplate restTemplate;
    DiscoveryClient discoveryClient;
//
//    @Value("${adServerName}")
//    private String adServerName;
//
//    @Value("${serviceProtocol}")
//    private String serviceProtocol;
//
//    @Value("${servicePath}")
//    private String servicePath;
    @Before
    public void setUp() throws Exception {
        setUpTaskerDaoMock();
        setUpRestTemplateMock();
        setUpDiscoveryClientMock();

        service = new TaskerServiceLayer(dao,discoveryClient,restTemplate,"adService","http://","/ad");
    }

    private void setUpRestTemplateMock() {

        restTemplate = mock(RestTemplate.class);

        doReturn("ABCDEFGH").when(restTemplate).getForObject("http://localhost:6107/ad", String.class);
    }

    private void setUpDiscoveryClientMock() {

        discoveryClient = mock(DiscoveryClient.class);

        // discoveryClient returns a LinkedList of DefaultServiceInstances with hostName and portNumber
        List<ServiceInstance> instances = new LinkedList<>();

        DefaultServiceInstance defaultServiceInstance = new DefaultServiceInstance("","","localhost",6107,true);

        instances.add(defaultServiceInstance);

        //doReturn(instances).when(discoveryClient).getInstances("adserver-service");
        doReturn(instances).when(discoveryClient).getInstances("adService");
    }
    private void setUpTaskerDaoMock(){
        dao = mock(TaskerDaoJdbcTemplateImpl.class);
        Task task = new Task();
        task.setId(2);
        task.setDescription("abcdef");
        task.setCreateDate(LocalDate.of(2019,3,25));
        task.setDueDate(LocalDate.of(2019,4,1));
        task.setCategory("xyz");

        Task task2 = new Task();
        task2.setDescription("abcdef");
        task2.setCreateDate(LocalDate.of(2019,3,25));
        task2.setDueDate(LocalDate.of(2019,4,1));
        task2.setCategory("xyz");


        List<Task> taskList = new ArrayList<>();
        taskList.add(task);

        doReturn(task).when(dao).createTask(task2);
        doReturn(task).when(dao).getTask(2);
        doReturn(taskList).when(dao).getAllTasks();
        doReturn(taskList).when(dao).getTasksByCategory(task.getCategory());


    }
    @Test
    public void getAd() {
        List<ServiceInstance> instances = discoveryClient.getInstances("adService");
        String Advertisement = restTemplate.getForObject("http://localhost:6107/ad", String.class);
    }

    @Test
    public void fetchTask() {
        TaskViewModel tvm = new TaskViewModel();
        //tvm.setId(2);
        tvm.setDescription("abcdef");
        tvm.setCreateDate(LocalDate.of(2019,3,25));
        tvm.setDueDate(LocalDate.of(2019,4,1));
        tvm.setCategory("xyz");
//        tvm.setAdvertisement("ABCDEFGH");
        tvm = service.newTask(tvm);

        //find task
        TaskViewModel taskFromService = service.fetchTask(tvm.getId());
        assertEquals(tvm,taskFromService);
    }

    @Test
    public void fetchAllTasks() {
        TaskViewModel tvm = new TaskViewModel();

        tvm.setDescription("abcdef");
        tvm.setCreateDate(LocalDate.of(2019,3,25));
        tvm.setDueDate(LocalDate.of(2019,4,1));
        tvm.setCategory("xyz");
        tvm.setAdvertisement("ABCDEFGH");
        tvm = service.newTask(tvm);

        //tvm.setId(2);
//        tvm.setDescription("abcdef");
//        tvm.setCreateDate(LocalDate.of(2019,3,25));
//        tvm.setDueDate(LocalDate.of(2019,4,1));
//        tvm.setCategory("xyz");
//        tvm = service.newTask(tvm);

        List<TaskViewModel> taskFromService = service.fetchAllTasks();
        assertEquals(1,taskFromService.size());


    }

    @Test
    public void fetchTasksByCategory() {
        TaskViewModel tvm = new TaskViewModel();
        tvm.setId(2);
        tvm.setDescription("abcdef");
        tvm.setCreateDate(LocalDate.of(2019,3,25));
        tvm.setDueDate(LocalDate.of(2019,4,1));
        tvm.setCategory("xyz");
        tvm.setAdvertisement("ABCDEFGH");
        tvm = service.newTask(tvm);

//        tvm.setId(2);
//        tvm.setDescription("abcdef");
//        tvm.setCreateDate(LocalDate.of(2019,3,25));
//        tvm.setDueDate(LocalDate.of(2019,4,1));
//        tvm.setCategory("xyz");
//        tvm = service.newTask(tvm);

        List<TaskViewModel> taskFromService = service.fetchTasksByCategory(tvm.getCategory());
        assertEquals(1,taskFromService.size());
    }

    @Test
    public void newTask() {
        TaskViewModel tvm = new TaskViewModel();
        tvm.setId(2);
        tvm.setDescription("abcdef");
        tvm.setCreateDate(LocalDate.of(2019,3,25));
        tvm.setDueDate(LocalDate.of(2019,4,1));
        tvm.setCategory("xyz");
        tvm.setAdvertisement("ABCDEFGH");
        tvm = service.newTask(tvm);

        //find task
        TaskViewModel taskFromService = service.fetchTask(tvm.getId());
        assertEquals(tvm,taskFromService);
    }

//    @Test
//    public void deleteTask() {
//        TaskViewModel tvm = new TaskViewModel();
//        tvm.setId(2);
//        tvm.setDescription("abcdef");
//        tvm.setCreateDate(LocalDate.of(2019,3,25));
//        tvm.setDueDate(LocalDate.of(2019,4,1));
//        tvm.setCategory("xyz");
//        tvm.setAdvertisement("dnekjkwl");
//        tvm = service.newTask(tvm);
//
//        TaskViewModel taskFromService = service.fetchTask(tvm.getId());
//        assertNull(taskFromService);
//    }

//    @Test
//    public void updateTask() {
//        TaskViewModel tvm = new TaskViewModel();
//        tvm.setId(2);
//        tvm.setDescription("abcdef");
//        tvm.setCreateDate(LocalDate.of(2019,3,25));
//        tvm.setDueDate(LocalDate.of(2019,4,1));
//        tvm.setCategory("xyz");
//
//         tvm = service.newTask(tvm);
//
//        tvm.setId(2);
//        tvm.setDescription("abcdef");
//        tvm.setCreateDate(LocalDate.of(2019,3,25));
//        tvm.setDueDate(LocalDate.of(2019,4,1));
//        tvm.setCategory("xyz");
//
//        service.updateTask(tvm);
//
//        TaskViewModel taskfromservice = service.fetchTask(tvm.getId());
//        assertEquals(tvm, taskfromservice);
//    }
}