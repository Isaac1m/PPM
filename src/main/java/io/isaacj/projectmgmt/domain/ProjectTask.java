package io.isaacj.projectmgmt.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
public class ProjectTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Please include a project summary")
    private String summary;

    //ManyToOne with Backlog
    @Column(updatable = false)
    private String projectSequence;
    private Integer priority;
    private String acceptanceCriteria;
    private String status;
    private Date dueDate;
    private Date created_at;
    private Date updated_at;

    @PrePersist
    public void onCreate() {
        this.created_at = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        this.updated_at = new Date();
    }

    @Override
    public String toString() {
        return "ProjectTask{" +
                "id=" + id +
                ", summary='" + summary + '\'' +
                ", projectSequence='" + projectSequence + '\'' +
                ", priority=" + priority +
                ", acceptanceCriteria='" + acceptanceCriteria + '\'' +
                ", status='" + status + '\'' +
                ", dueDate=" + dueDate +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}

