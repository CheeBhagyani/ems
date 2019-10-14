package chee.spring.smsui.controller;

import chee.rentcloud.ems.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
@EnableOAuth2Sso
public class UIController extends WebSecurityConfigurerAdapter {

    @Autowired
    RestTemplate restTemplate;

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
        http.sessionManagement().maximumSessions(1);
    }

    @PreAuthorize("hasRole('ROLE_admin') or hasRole('ROLE_operator')")
    @RequestMapping(value = "/")
    public String loadHome() {
        return "home";
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "/newEmployee")
    public String loadEmp() {
        return "newEmployee";
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "/newProject")
    public String loadNewProject() {
        return "newProject";
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "/newTask")
    public String loadNewTask() {
        return "newTask";
    }

    @PreAuthorize("hasRole('ROLE_admin') or hasRole('ROLE_operator')")
    @RequestMapping(value = "/employee")
    public String loadDetails(Model model) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());

        HttpEntity<Employee> studentHttpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<Employee[]> responseEntity = restTemplate.exchange("http://localhost:8080/ems/employees", HttpMethod.GET, studentHttpEntity, Employee[].class);
            model.addAttribute("employees", responseEntity.getBody());
        } catch (HttpStatusCodeException e) {
            ResponseEntity responseEntity = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
        }

        return "employee";
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    public String saveEmployee(@ModelAttribute Employee employee, Model model){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());
        HttpEntity<Employee> employeeHttpEntity = new HttpEntity<Employee>(employee,httpHeaders);


        try {
            ResponseEntity<Employee> responseEntity = restTemplate.exchange("http://localhost:8080/ems/employee", HttpMethod.POST,employeeHttpEntity,Employee.class);
        } catch (HttpStatusCodeException se){
            ResponseEntity responseEntity = ResponseEntity.status(se.getRawStatusCode()).headers(se.getResponseHeaders()).body(se.getResponseBodyAsString());
            model.addAttribute("error",responseEntity);
        }
        return "redirect:employee";
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "/project", method = RequestMethod.POST)
    public String saveProject(@ModelAttribute Project project, Model model){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());
        HttpEntity<Project> projectHttpEntity = new HttpEntity<Project>(project, httpHeaders);

        try {
            ResponseEntity<Project> responseEntity = restTemplate.exchange("http://localhost:8081/ems/project", HttpMethod.POST,projectHttpEntity,Project.class);
        } catch (HttpStatusCodeException se){
            ResponseEntity responseEntity = ResponseEntity.status(se.getRawStatusCode()).headers(se.getResponseHeaders()).body(se.getResponseBodyAsString());
            model.addAttribute("error",responseEntity);
        }
        return "redirect:project";
    }

    @PreAuthorize("hasRole('ROLE_admin') or hasRole('ROLE_operator')")
    @RequestMapping(value = "/project")
    public String loadProjects(Model model) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());

        HttpEntity<Project> projectHttpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<Project[]> responseEntity = restTemplate.exchange("http://localhost:8081/ems/projects", HttpMethod.GET, projectHttpEntity, Project[].class);
            model.addAttribute("projects", responseEntity.getBody());
        } catch (HttpStatusCodeException e) {
            ResponseEntity responseEntity = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
        }
        return "project";
    }

    @PreAuthorize("hasRole('ROLE_admin') or hasRole('ROLE_operator')")
    @RequestMapping(value = "/task")
    public String loadTasks(Model model) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());

        HttpEntity<Task> taskHttpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<Task[]> responseEntity = restTemplate.exchange("http://localhost:8082/ems/tasks", HttpMethod.GET, taskHttpEntity, Task[].class);
            model.addAttribute("tasks", responseEntity.getBody());
        } catch (HttpStatusCodeException e) {
            ResponseEntity responseEntity = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
        }

        return "task";
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public String saveTask(@ModelAttribute Task task, Model model){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());
        HttpEntity<Task> taskHttpEntity = new HttpEntity<Task>(task, httpHeaders);
        try {
            ResponseEntity<Task> responseEntity = restTemplate.exchange("http://localhost:8082/ems/task", HttpMethod.POST,taskHttpEntity,Task.class);
        } catch (HttpStatusCodeException se){
            ResponseEntity responseEntity = ResponseEntity.status(se.getRawStatusCode()).headers(se.getResponseHeaders()).body(se.getResponseBodyAsString());
            model.addAttribute("error",responseEntity);
        }
        return "redirect:task";
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "/operations")
    public String saveOperations(Model model){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());
        HttpEntity<Employee> employeeHttpEntity = new HttpEntity<Employee>(httpHeaders);
        HttpEntity<Task> taskHttpEntity = new HttpEntity<>(httpHeaders);
        HttpEntity<Project> projectHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Employee[]> responseEntityEmp = restTemplate.exchange("http://localhost:8080/ems/employees", HttpMethod.GET, employeeHttpEntity, Employee[].class);
            model.addAttribute("employees", responseEntityEmp.getBody());
            ResponseEntity<Task[]> responseEntityTask = restTemplate.exchange("http://localhost:8082/ems/tasks", HttpMethod.GET, taskHttpEntity, Task[].class);
            model.addAttribute("tasks", responseEntityTask.getBody());
            ResponseEntity<Project[]> responseEntityProject = restTemplate.exchange("http://localhost:8081/ems/projects", HttpMethod.GET, projectHttpEntity, Project[].class);
            model.addAttribute("projects", responseEntityProject.getBody());
        }catch (HttpStatusCodeException se){
            ResponseEntity responseEntity = ResponseEntity.status(se.getRawStatusCode()).headers(se.getResponseHeaders()).body(se.getResponseBodyAsString());
            model.addAttribute("error",responseEntity);
        }
        return "operations";
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "/assign",method = RequestMethod.POST)
    public String assignProject(@ModelAttribute AssignProjectTaskList assignTaskList, Model model){
        System.out.println("inside");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());
        List<AssignProjectTask> assignTask = new ArrayList<>();
        assignTaskList.getTid().forEach((t)->{
            AssignProjectTask task = new AssignProjectTask();
            task.setEid(assignTaskList.getEid());
            task.setPid(assignTaskList.getPid());
            task.setTid(t);
            assignTask.add(task);
            System.out.println(task);
        });

        HttpEntity<List> assignTaskHttpEntity = new HttpEntity<List>(assignTask,httpHeaders);
        try {
            ResponseEntity<List> responseEntity = restTemplate.exchange("http://localhost:8080/ems/assign", HttpMethod.POST,assignTaskHttpEntity,List.class);
        }catch (HttpStatusCodeException se){
            ResponseEntity responseEntity = ResponseEntity.status(se.getRawStatusCode()).headers(se.getResponseHeaders()).body(se.getResponseBodyAsString());
            model.addAttribute("error",responseEntity);
        }

        return "redirect:operations";
    }

    @PreAuthorize("hasRole('ROLE_admin') or hasRole('ROLE_operator')")
    @RequestMapping(value = "/employees/{id}")
    public String getEmployee(@PathVariable Integer id , Model model){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());
        HttpEntity<Employee> employeeHttpEntity = new HttpEntity<Employee>(httpHeaders);

        try {
            ResponseEntity<Employee> responseEntity1 = restTemplate.exchange("http://localhost:8080/ems/employee/{id}", HttpMethod.GET,employeeHttpEntity,Employee.class,id);
            model.addAttribute("employee",responseEntity1.getBody());
            ResponseEntity<Project[]> responseEntity2 = restTemplate.exchange("http://localhost:8080/ems/employee/{id}/projects", HttpMethod.GET,employeeHttpEntity,Project[].class,id);
            model.addAttribute("projects",responseEntity2.getBody());
        }catch (HttpStatusCodeException se){
            ResponseEntity responseEntity = ResponseEntity.status(se.getRawStatusCode()).headers(se.getResponseHeaders()).body(se.getResponseBodyAsString());
            model.addAttribute("error",responseEntity);
        }

        return "employeeInfo";
    }

    @PreAuthorize("hasRole('ROLE_admin') or hasRole('ROLE_operator')")
    @RequestMapping(value = "/employees/{id}/projects")
    public String getEmployeeProjects(@PathVariable Integer id ,Model model){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());
        HttpEntity<Employee> employeeHttpEntity = new HttpEntity<Employee>(httpHeaders);
        try {
            ResponseEntity<Project[]> responseEntity = restTemplate.exchange("http://localhost:8080/ems/employee/{id}/projects", HttpMethod.GET,employeeHttpEntity,Project[].class,id);
            model.addAttribute("projects",responseEntity.getBody());
        }catch (HttpStatusCodeException se){
            ResponseEntity responseEntity = ResponseEntity.status(se.getRawStatusCode()).headers(se.getResponseHeaders()).body(se.getResponseBodyAsString());
            model.addAttribute("error",responseEntity);
        }

        return "employeeInfo";
    }

    @PreAuthorize("hasRole('ROLE_admin') or hasRole('ROLE_operator')")
    @RequestMapping(value = "/employees/{eid}/projects/{pid}/tasks")
    public String getEmployeeProjects(@PathVariable Integer eid ,@PathVariable Integer pid,Model model){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessTokenConfigurer.getToken());
        HttpEntity<Employee> employeeHttpEntity = new HttpEntity<Employee>(httpHeaders);

        try {
            ResponseEntity<Task[]> responseEntity = restTemplate.exchange("http://localhost:8080/ems/employee/{eid}/project/{pid}/tasks", HttpMethod.GET,employeeHttpEntity,Task[].class,eid,pid);
            model.addAttribute("tasks",responseEntity.getBody());
        }catch (HttpStatusCodeException se){
            ResponseEntity responseEntity = ResponseEntity.status(se.getRawStatusCode()).headers(se.getResponseHeaders()).body(se.getResponseBodyAsString());
            model.addAttribute("error",responseEntity);
        }

        return "taskInfo";
    }
}