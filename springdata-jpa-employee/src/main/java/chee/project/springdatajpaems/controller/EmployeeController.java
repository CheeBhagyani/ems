package chee.project.springdatajpaems.controller;

import chee.project.springdatajpaems.service.EmployeeServiceImpl;
import chee.rentcloud.ems.model.AssignProjectTask;
import chee.rentcloud.ems.model.Employee;
import chee.rentcloud.ems.model.Project;
import chee.rentcloud.ems.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/ems")
public class EmployeeController {
    @Autowired
    EmployeeServiceImpl employeeService;

    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_admin')")
    public Employee save(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('read_profile')")
    public List<Employee> getAll(Optional<Integer> id) {
        return employeeService.findAll();
    }

    @PutMapping("/employee/{id}")
    @PreAuthorize("hasAuthority('update_profile')")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Integer id) {
        return employeeService.findById(id)
                .map(employee -> {
                    employee.setEname(newEmployee.getEname());
                    employee.setEmail(newEmployee.getEmail());
                    employee.setContact(newEmployee.getContact());

                    return employeeService.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setEid(id);
                    return employeeService.save(newEmployee);
                });
    }


    @RequestMapping(value = "/assign",method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_admin')")
    public List<AssignProjectTask> saveAssignProjectTask(@RequestBody List<AssignProjectTask> assignProjectTasks){
        return employeeService.saveProjectTask(assignProjectTasks);
    }

    @RequestMapping(value = "/employee/{id}",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('read_profile')")
    public Optional<Employee> getEmployee(@PathVariable Integer id){
        return employeeService.findById(id);
    }

    @RequestMapping(value = "/employee/{id}/projects",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('read_profile')")
    public List<Project> getProjectIds(@PathVariable Integer id){
        return employeeService.getProjects(id);
    }

    @RequestMapping(value = "/employee/{eid}/project/{pid}/tasks",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('read_profile')")
    public List<Task> getTaskIds(@PathVariable Integer eid, @PathVariable Integer pid){
        return employeeService.getTasks(pid);
    }

}
