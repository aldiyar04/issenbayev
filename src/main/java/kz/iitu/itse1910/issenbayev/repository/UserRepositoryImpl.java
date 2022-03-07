package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> findAllPaginated(int offset, int size) {
        return sessionFactory.getCurrentSession()
                .createQuery("from User u order by u.id")
                .setFirstResult(offset)
                .setMaxResults(size)
                .setHint("org.hibernate.cacheable", "true")
                .list();
    }

    @Override
    public Optional<User> findById(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("from User u where u.id = :id")
                .setParameter("id", id)
                .setHint("org.hibernate.cacheable", "true")
                .uniqueResultOptional();
    }

    @Override
    public void save(User user) {
        sessionFactory.getCurrentSession()
                .saveOrUpdate(user);
    }

    @Override
    public void delete(User user) {
        sessionFactory.getCurrentSession()
                .delete(user);
    }
}
