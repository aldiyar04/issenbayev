package kz.iitu.itse1910.issenbayev.entity;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ticket extends BaseEntity {
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "submitter_id")
    private User submitter;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "priority")
    private String priority;

    @Column(name = "target_res_date")
    private LocalDate targetResolutionDate;

    @Column(name = "actual_res_date")
    private LocalDate actualResolutionDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private boolean isOverdue;

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", version=" + version +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", projectId=" + project.getId() +
                ", projectName=" + project.getName() +
                assignee() +
                submitter() +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", targetResolutionDate=" + targetResolutionDate +
                ", actualResolutionDate=" + actualResolutionDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    private String assignee() {
        return assignee == null ? ", assignee=null" :
                ". assigneeId=" + assignee.getId();
    }

    private String submitter() {
        return submitter == null ? ", submitter=null" :
                ", submitterId=" + submitter.getId();
    }
}
