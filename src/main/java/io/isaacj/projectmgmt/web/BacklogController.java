package io.isaacj.projectmgmt.web;

import io.isaacj.projectmgmt.domain.ProjectTask;
import io.isaacj.projectmgmt.services.MapValidationErrorsService;
import io.isaacj.projectmgmt.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/backlog")
@CrossOrigin
public class  BacklogController {

    @Autowired
    ProjectTaskService projectTaskService;

    @Autowired
    MapValidationErrorsService mapValidationErrorsService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlog_id) {

        ResponseEntity<?> errorMap = mapValidationErrorsService.mapValidationErrors(result);

        if(errorMap != null){
            return errorMap;
        }

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask);

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);

    }

    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id) {
        return projectTaskService.findBacklogById(backlog_id);
    }

    @GetMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> getProjetcTask(@PathVariable String backlogId, @PathVariable String ptId) {
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlogId, ptId);
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String backlogId, @PathVariable String ptId) {
        ResponseEntity<?> errorMap = mapValidationErrorsService.mapValidationErrors(result);
        if(errorMap != null ) return errorMap;

        ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask, backlogId, ptId);

        return new ResponseEntity<ProjectTask>(updatedProjectTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String ptId) {
        projectTaskService.deletePTByProjectSequence(backlogId, ptId);

        return new ResponseEntity<String>("Project task '"+ptId+"' deleted successfully.", HttpStatus.OK);
    }
}
