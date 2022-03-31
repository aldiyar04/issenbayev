package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;

import javax.persistence.QueryHint;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<Project> findAll(@NonNull Pageable pageable);
    boolean existsByName(String name);
}
