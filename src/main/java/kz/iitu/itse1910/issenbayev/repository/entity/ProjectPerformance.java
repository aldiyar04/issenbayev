package kz.iitu.itse1910.issenbayev.repository.entity;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_performances")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ProjectPerformance extends BaseEntity {
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "num_assignees")
    private Integer numAssignees;

    @Column(name = "mean_num_assignees")
    private Long meanNumAssignees;

    @Column(name = "mean_num_assignees_updated_at")
    private LocalDateTime meanNumAssigneesUpdatedAt;

    @Column(name = "tickets_performance")
    private Long ticketsPerformance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    @Builder
    public ProjectPerformance(Long id, Long version, String name, Integer numAssignees, Long meanNumAssignees,
                              LocalDateTime meanNumAssigneesUpdatedAt, Long ticketsPerformance) {
        super(id, version);
        this.name = name;
        this.numAssignees = numAssignees;
        this.meanNumAssignees = meanNumAssignees;
        this.meanNumAssigneesUpdatedAt = meanNumAssigneesUpdatedAt;
        this.ticketsPerformance = ticketsPerformance;
    }
}
