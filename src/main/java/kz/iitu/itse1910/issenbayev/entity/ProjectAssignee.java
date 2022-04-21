package kz.iitu.itse1910.issenbayev.entity;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "project_assignees")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ProjectAssignee extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "assignee")
    private String assignee;
}
