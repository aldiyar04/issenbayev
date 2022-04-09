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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
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

    @PrePersist
    private void setCreatedOnUpdatedOn() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @Builder
    public Ticket(Long id, Long version, String title, String description, Project project,
                  User assignee, User submitter, String type, String status, String priority,
                  LocalDate targetResolutionDate, LocalDate actualResolutionDate,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, version);
        this.title = title;
        this.description = description;
        this.project = project;
        this.assignee = assignee;
        this.submitter = submitter;
        this.type = type;
        this.status = status;
        this.priority = priority;
        this.targetResolutionDate = targetResolutionDate;
        this.actualResolutionDate = actualResolutionDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", version=" + version +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", projectId=" + project.getId() +
                ", assigneeId=" + (assignee == null ? "null" : "" + assignee.getId()) +
                ", submitterId=" + (submitter == null ? "=null" : "" + submitter.getId()) +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", targetResolutionDate=" + targetResolutionDate +
                ", actualResolutionDate=" + actualResolutionDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public static class Type {
        public static final String BUG = "Bug";
        public static final String VULNERABILITY = "Vulnerability";
        public static final String FEATURE_REQUEST = "Feature Request";
        public static final String REFACTORING = "Refactoring";
        public static final String OTHER = "Other";
    }

    public static class Status {
        public static final String NEW = "New";
        public static final String ASSIGNED = "Assigned";
        public static final String IN_PROGRESS = "In Progress";
        public static final String SUBMITTED = "Submitted";
        public static final String EXTRA_WORK_REQUIRED = "Extra Work Required";
        public static final String RESOLVED = "Resolved";
    }

    public static class Priority {
        public static final String CRITICAL = "Critical";
        public static final String HIGH = "High";
        public static final String MEDIUM = "Medium";
        public static final String LOW = "Low";
        public static final String NONE = "None";
    }
}
