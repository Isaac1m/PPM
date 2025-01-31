package io.isaacj.projectmgmt.web;

import io.isaacj.projectmgmt.domain.Project;
import io.isaacj.projectmgmt.services.MapValidationErrorsService;
import io.isaacj.projectmgmt.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    MapValidationErrorsService mapValidationErrorsService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {

        ResponseEntity<?> errorMap = mapValidationErrorsService.mapValidationErrors(result);
        if(errorMap != null) return errorMap;
        
        projectService.saveOrUpdateProject(project);
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId) {
        Project project = projectService.findProjectById(projectId);
        return new ResponseEntity<Project>(project, HttpStatus.OK) ;
    }

    @GetMapping("")
    public Iterable<Project> findAllProjects(){
        return projectService.findAll();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return new ResponseEntity<String>("Project deleted successfully", HttpStatus.OK);
    }

}
