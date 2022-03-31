package kz.iitu.itse1910.issenbayev.entity;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "projects")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Project extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "lead_dev_id")
    private User leadDev;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "updated_on")
    private LocalDate updatedOn;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_assignees",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "assignee_id")
    )
    private List<User> assignees;

    @PrePersist
    private void setCreatedOnUpdatedOn() {
        createdOn = LocalDate.now();
        updatedOn = LocalDate.now();
    }

    @AssertTrue(message = "Lead dev must have the role of lead dev")
    public boolean isLeadDevRoleConsistent() {
        return leadDev == null || leadDev.getRole().equals(User.ROLE_LEAD_DEV);
    }

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "id=" + id +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                leadDev() +
                ", leadDevUsername=" + leadDev.getUsername() +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }

    private String leadDev() {
        return leadDev == null ? ", leadDev=null" :
                ", leadDevId=" + leadDev.getId();
    }
}
