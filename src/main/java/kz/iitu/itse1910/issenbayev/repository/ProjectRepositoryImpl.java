package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ProjectRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Project> findAllPaginated(int offset, int size) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Project p order by p.id")
                .setFirstResult(offset)
                .setMaxResults(size)
                .setHint("org.hibernate.cacheable", "true")
                .list();
    }

    @Override
    public Optional<Project> findById(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Project p where p.id = :id")
                .setParameter("id", id)
                .setHint("org.hibernate.cacheable", "true")
                .uniqueResultOptional();
    }

    @Override
    public void save(Project project) {
        sessionFactory.getCurrentSession()
                .saveOrUpdate(project);
    }

    @Override
    public void delete(Project project) {
        sessionFactory.getCurrentSession()
                .delete(project);
    }
}
