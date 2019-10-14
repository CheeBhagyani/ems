package chee.project.springdatajpaproject.controller;

import chee.project.springdatajpaproject.service.ProjectServiceImpl;
import chee.rentcloud.ems.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/ems")
public class ProjectController {
    @Autowired
    private ProjectServiceImpl projectService;

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('read_profile')")
    public List<Project> getAll(Optional<Integer> id) {
        return projectService.findAll();
    }

    @RequestMapping(value = "/project", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_admin')")
    public Project save(@RequestBody Project project) {
        return projectService.save(project);
    }

    @RequestMapping(value = "/project/{id}",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('read_profile')")
    public List<Project> getProject(@PathVariable List<Integer> id){
        return projectService.getProjectsList(id);
    }
}
