package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.repository.entity.ProjectPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface ProjectPerformanceRepository extends JpaRepository<ProjectPerformance, Long> {
    @Query("select p from ProjectPerformance p where p.name = :name")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Optional<ProjectPerformance> findByName(String name);
}
