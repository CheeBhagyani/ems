package chee.project.springdatajpaproject.service;

import chee.project.springdatajpaproject.repository.ProjectRepository;
import chee.rentcloud.ems.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl {
    @Autowired
    private ProjectRepository projectRepository;

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsList(List<Integer> id){
        return projectRepository.findByIdIn(id);
    }
}
