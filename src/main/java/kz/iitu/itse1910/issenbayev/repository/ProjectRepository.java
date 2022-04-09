package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<Project> findAll(@NonNull Pageable pageable);
    boolean existsByName(String name);
}
//public interface ProjectRepository {
//    Page<Project> findAll(@NonNull Pageable pageable);
//    Optional<Project> findById(long id);
//    Project save(Project project);
//    Project delete(Project project);
//    boolean existsByName(String name);
//}
