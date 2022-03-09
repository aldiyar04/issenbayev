package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAllPaginated(int offset, int size);
    Optional<Project> findById(Long id);
    void save(Project project);
    void delete(Project project);
}
