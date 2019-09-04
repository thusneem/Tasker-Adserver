package com.trilogyed.tasker.service;
import org.springframework.cloud.client.discovery.DiscoveryClient;
//import com.netflix.discovery.DiscoveryClient;
import com.trilogyed.tasker.dao.TaskerDao;
import com.trilogyed.tasker.model.Task;
import com.trilogyed.tasker.model.TaskViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Component
@RefreshScope
public class TaskerServiceLayer {

    TaskerDao dao;

    @Autowired
    private DiscoveryClient discoveryClient;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${adServerName}")
    private String adServerName;

    @Value("${serviceProtocol}")
    private String serviceProtocol;

    @Value("${servicePath}")
    private String servicePath;
    @Autowired
    public TaskerServiceLayer(TaskerDao dao){
        this.dao = dao;
    }

    public TaskerServiceLayer(TaskerDao dao,DiscoveryClient discoveryclient,RestTemplate resttemplate,String adServerName, String serviceProtocol,String servicePath){
        this.dao = dao;
        this.discoveryClient = discoveryclient;
        this.restTemplate = resttemplate;
        this.adServerName = adServerName;
        this.serviceProtocol = serviceProtocol;
        this.servicePath = servicePath;
    }


    @RequestMapping(value = "/ad", method = RequestMethod.GET)
    public String getAd() {
        List<ServiceInstance> instances = discoveryClient.getInstances(adServerName);
        //System.out.println( instances.get(0).getHost());
        //System.out.println(instances.get(0).getPort());
        String Advertisement = serviceProtocol + instances.get(0).getHost() + ":" + instances.get(0).getPort() + servicePath;
        //System.out.println(Advertisement);
        String getAdvertisement = restTemplate.getForObject(Advertisement, String.class);
        //System.out.println(getAdvertisement);
        return getAdvertisement;
    }
    public TaskViewModel fetchTask(int id) {
    Task task = dao.getTask(id);
    if(task ==  null)
        return null;
    else
        return buildTaskViewModel(task);

    }

    public List<TaskViewModel> fetchAllTasks() {
        List<Task> taskList = dao.getAllTasks();
        //List<Console> consolesList = consoleDao.getManufacturer(manufacturer);
        List<TaskViewModel> ViewModelList = new ArrayList<>();

        taskList.stream()
                .forEach(t -> {
                    TaskViewModel tvm = buildTaskViewModel(t);
                    ViewModelList.add(tvm);
                });
        return ViewModelList;

    }

    public List<TaskViewModel> fetchTasksByCategory(String category) {
        List<Task> taskList = dao.getTasksByCategory(category);
        if(taskList ==  null)
            return null;
        else{
            List<TaskViewModel> ViewModelList = new ArrayList<>();

            taskList.stream()
                    .forEach(t -> {
                        TaskViewModel tvm = buildTaskViewModel(t);
                        ViewModelList.add(tvm);
                    });
            return ViewModelList;
        }

    }


    public TaskViewModel newTask(TaskViewModel taskViewModel) {
        Task task = new Task();
        //TaskViewModel tvm = new TaskViewModel();
        task.setDescription(taskViewModel.getDescription());
        task.setCreateDate(taskViewModel.getCreateDate());
        task.setDueDate(taskViewModel.getDueDate());
        task.setCategory(taskViewModel.getCategory());
        //task.setAdvertisement(getAd());
        task = dao.createTask(task);
        taskViewModel.setId(task.getId());
        taskViewModel.setAdvertisement(getAd());

        // TODO - get ad from Adserver and put in taskViewModel
        return taskViewModel;
    }


    // Helper methods
    private TaskViewModel buildTaskViewModel (Task task){
        TaskViewModel tvm = new TaskViewModel();
        tvm.setId(task.getId());
        tvm.setDescription(task.getDescription());
        tvm.setCreateDate(task.getCreateDate());
        tvm.setDueDate(task.getDueDate());
        tvm.setCategory(task.getCategory());
        tvm.setAdvertisement(getAd());

        return tvm;
    }
}
