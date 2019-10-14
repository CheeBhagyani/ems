package chee.task.springdatajpatask.service;
import chee.rentcloud.ems.model.Task;
import chee.task.springdatajpatask.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl {
    @Autowired
    private TaskRepository taskRepository;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksList(List<Integer> id){
        return taskRepository.findByIdIn(id);
    }
}
