package chee.project.springdatajpaems.repository;

import chee.rentcloud.ems.model.AssignProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignProjectTaskRepository extends JpaRepository<AssignProjectTask, Integer> {
    List<AssignProjectTask> findByEid(Integer eid);
    List<AssignProjectTask> findByPid(Integer pid);
}
