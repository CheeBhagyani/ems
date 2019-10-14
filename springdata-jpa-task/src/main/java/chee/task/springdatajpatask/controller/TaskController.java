package chee.task.springdatajpatask.controller;

import chee.rentcloud.ems.model.Task;
import chee.task.springdatajpatask.service.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/ems")
public class TaskController {
    @Autowired
    private TaskServiceImpl taskService;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('read_profile')")
    public List<Task> getAll(Optional<Integer> id) {
        return taskService.findAll();
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_admin')")
    public Task save(@RequestBody Task task) {
        return taskService.save(task);
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('read_profile')")
    public List<Task> getTask(@PathVariable List<Integer> id){
        return taskService.getTasksList(id);
    }
}
