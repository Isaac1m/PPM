package io.isaacj.projectmgmt.services;

import io.isaacj.projectmgmt.domain.Project;
import io.isaacj.projectmgmt.exceptions.ProjectIdException;
import io.isaacj.projectmgmt.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toLowerCase());
            return projectRepository.save(project);
        } catch(Exception e) {
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exits");
        }
    }
}
