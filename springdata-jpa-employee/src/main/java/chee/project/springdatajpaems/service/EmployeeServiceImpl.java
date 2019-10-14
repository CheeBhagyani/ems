package chee.project.springdatajpaems.service;

import chee.project.springdatajpaems.repository.AssignProjectTaskRepository;
import chee.project.springdatajpaems.repository.EmployeeRepository;
import chee.rentcloud.ems.model.AssignProjectTask;
import chee.rentcloud.ems.model.Employee;
import chee.rentcloud.ems.model.Project;
import chee.rentcloud.ems.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AssignProjectTaskRepository assignProjectTaskRepository;

    @Autowired
    RestTemplate restTemplate;

    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(Integer id) {
        return employeeRepository.findById(id);
    }

    public List<AssignProjectTask> saveProjectTask(List<AssignProjectTask> assignProjectTasks){
        return assignProjectTaskRepository.saveAll(assignProjectTasks);
    }

    public List<Project> getProjects(Integer eid){
        List<AssignProjectTask> assignTasks = assignProjectTaskRepository.findByEid(eid);
        System.out.println("1" + assignTasks);
        String projectIds = assignTasks.stream().map(s->String.valueOf(s.getPid())).collect(Collectors.joining(","));
        System.out.println("2" + projectIds);
        if(projectIds.equals(null)||projectIds.equals("")){
            System.out.println("null");
            return null;
        }
        else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());
            HttpEntity<Project> projectHttpEntity = new HttpEntity<Project>(httpHeaders);
            System.out.println("http://localhost:8081/ems/project/" + projectIds);
            ResponseEntity<List> responseEntity = restTemplate.exchange("http://localhost:8081/ems/project/{id}", HttpMethod.GET, projectHttpEntity, List.class, projectIds);

            return responseEntity.getBody();

        }
    }

    public List<Task> getTasks(Integer pid){
        List<AssignProjectTask> assignTasks = assignProjectTaskRepository.findByPid(pid);
        String taskIds = assignTasks.stream().map(s->String.valueOf(s.getTid())).collect(Collectors.joining(","));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());
        HttpEntity<Task> taskHttpEntity = new HttpEntity<Task>(httpHeaders);
        ResponseEntity<List> responseEntity = restTemplate.exchange("http://localhost:8082/ems/task/{id}", HttpMethod.GET, taskHttpEntity, List.class, taskIds);

        return responseEntity.getBody();

    }

}
