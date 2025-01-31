package io.isaacj.projectmgmt.services;

import io.isaacj.projectmgmt.domain.Backlog;
import io.isaacj.projectmgmt.domain.Project;
import io.isaacj.projectmgmt.domain.ProjectTask;
import io.isaacj.projectmgmt.exceptions.ProjectNotFoundException;
import io.isaacj.projectmgmt.repositories.BacklogRepository;
import io.isaacj.projectmgmt.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    ProjectTaskRepository projectTaskRepository;

    @Autowired
    BacklogRepository backlogRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        try {
            //        Exceptions: 404 - Project not found
//        PT to be added to a specific project where project != null and Backlog exists
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
//        Set the backlog to the project task
            projectTask.setBacklog(backlog);
//        Project sequence should be like IDprj-01, IDprj-1,...,IDprj-n
            Integer sequence = backlog.getPTSequence();
//        Update the backlog sequence
            sequence++;

            backlog.setPTSequence(sequence);

//      Add sequence to project task
            projectTask.setProjectSequence(projectIdentifier + "-" + sequence);

            projectTask.setProjectIdentifier(projectIdentifier);

//        Initial priority when priority = null
            if(projectTask.getPriority() == null) {
                projectTask.setPriority(3);
            }
//        Initial status when status = null

            if(projectTask.getStatus() == "" || projectTask.getStatus() == null ) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);



        } catch (Exception e) {
            throw new ProjectNotFoundException("Project not found.");
        }

    }

    public Iterable<ProjectTask> findBacklogById(String backlog_id) {
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id).orElseThrow(() -> new ProjectNotFoundException("Project not found."));
    }

    public ProjectTask findPTByProjectSequence(String backlogId, String ptId){

//        Make sure we're searching on the right backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
        if(backlog == null) {
            throw new ProjectNotFoundException("Project with ID '"+backlogId+"' does not exist.");
        }

//        Make sure project task exists

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ptId);
        if(projectTask == null) {
            throw new ProjectNotFoundException("Project task with ID '"+ptId+"' does not exist.");
        }

//        Make sure the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlogId)) {
            throw new ProjectNotFoundException("Project task '"+ptId+"' does not exist in project '"+backlogId+"'");
        }

        return projectTask;
    }

     public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String ptId) {
         ProjectTask projectTask = findPTByProjectSequence(backlogId, ptId);

         projectTask = updatedTask;

         return projectTaskRepository.save(projectTask);
     }

     public void deletePTByProjectSequence(String backlogId, String ptId) {
        ProjectTask projectTask = findPTByProjectSequence(backlogId, ptId);

        projectTaskRepository.delete(projectTask);

     }
}
