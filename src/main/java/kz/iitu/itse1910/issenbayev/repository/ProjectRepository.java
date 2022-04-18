package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.repository.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<Project> findAll(@NonNull Pageable pageable);
    boolean existsByName(String name);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query("select distinct p from Project p join fetch p.assignees")
    List<Project> findAllWithAssignees();
}
