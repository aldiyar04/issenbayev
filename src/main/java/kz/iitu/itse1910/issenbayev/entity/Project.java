package kz.iitu.itse1910.issenbayev.entity;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "projects")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
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

    @OneToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "project")
    private List<Ticket> tickets;

    @PrePersist
    private void setCreatedOnUpdatedOn() {
        createdOn = LocalDate.now();
        updatedOn = LocalDate.now();
    }

    @Builder
    public Project(Long id, Long version, String name, String description, User leadDev,
                   LocalDate createdOn, LocalDate updatedOn, List<User> assignees, List<Ticket> tickets) {
        super(id, version);
        this.name = name;
        this.description = description;
        this.leadDev = leadDev;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.assignees = assignees;
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "id=" + id +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", leadDev=" + leadDev() +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }

    private String leadDev() {
        return leadDev == null ? "null" : "" + leadDev.getId();
    }
}
