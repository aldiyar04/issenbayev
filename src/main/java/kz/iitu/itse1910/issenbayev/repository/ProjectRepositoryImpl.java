package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository {
    private final EntityManager entityManager;

    public ProjectRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Project> findAllPaginated(int offset, int size) {
        return entityManager.createQuery("select p from Project p order by p.id", Project.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .setHint("org.hibernate.cacheable", "true")
                .getResultList();
    }

    @Override
    public Optional<Project> findById(Long id) {
        Project project =  entityManager.createQuery("select p from Project p where p.id = :id", Project.class)
                .setParameter("id", id)
                .setHint("org.hibernate.cacheable", "true")
                .getSingleResult();
        return Optional.ofNullable(project);
    }

    @Override
    public void save(Project project) {
        entityManager.merge(project);
    }

    @Override
    public void delete(Project project) {
        entityManager.remove(project);
    }
}
