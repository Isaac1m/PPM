package io.isaacj.projectmgmt.services;

import io.isaacj.projectmgmt.domain.Backlog;
import io.isaacj.projectmgmt.domain.Project;
import io.isaacj.projectmgmt.exceptions.ProjectIdException;
import io.isaacj.projectmgmt.repositories.BacklogRepository;
import io.isaacj.projectmgmt.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if(project.getId() == null ){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId() != null ) {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        } catch(Exception e) {
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exits");
        }
    }

    public Project findProjectById(String projectId) {

            Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
      if(project == null) {
            throw new ProjectIdException("Project with ID '"+projectId.toUpperCase()+"' does not exist");
        }
        return project;
    }

    public Iterable<Project> findAll() {
        return projectRepository.findAll();
    }

    public void deleteProject(String id) {

        Project project = projectRepository.findByProjectIdentifier(id.toUpperCase());
        if(project == null) {
            throw new ProjectIdException("Project does not exist");
        }
        projectRepository.delete(project);
    }
}
