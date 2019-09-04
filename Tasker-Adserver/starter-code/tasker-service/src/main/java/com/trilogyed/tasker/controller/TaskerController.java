package com.trilogyed.tasker.controller;

import com.trilogyed.tasker.model.TaskViewModel;
import com.trilogyed.tasker.service.TaskerServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
public class TaskerController {

    @Autowired
    TaskerServiceLayer service;

    public TaskerController(TaskerServiceLayer service) {
        this.service = service;
    }

    @RequestMapping(value ="/tasks/{id}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TaskViewModel getTask(@PathVariable int id){
        return service.fetchTask(id);
    }


    @RequestMapping(value = "/tasks",method =RequestMethod.POST )
    @ResponseStatus(HttpStatus.CREATED)
    public TaskViewModel createTask(@RequestBody @Valid TaskViewModel task){
        return service.newTask(task);
    }

//got to add list all tasks
@RequestMapping(value = "/tasks/category/{category}",method =RequestMethod.GET )
@ResponseStatus(HttpStatus.OK)

public List<TaskViewModel> getTaskByCategory (@PathVariable String category) {
    return service.fetchTasksByCategory(category);
}

 //got to add add by category
 @RequestMapping(value = "/tasks",method =RequestMethod.GET )
 @ResponseStatus(HttpStatus.OK)

 public List<TaskViewModel> getManufacturer () {
     return service.fetchAllTasks();
 }

}
