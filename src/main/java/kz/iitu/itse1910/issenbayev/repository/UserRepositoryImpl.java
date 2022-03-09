package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository{
    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findAllPaginated(int offset, int size) {
        return entityManager.createQuery("select u from User u order by u.id", User.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .setHint("org.hibernate.cacheable", "true")
                .getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        User user =  entityManager.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", id)
                .setHint("org.hibernate.cacheable", "true")
                .getSingleResult();
        return Optional.ofNullable(user);
    }

    @Override
    public void save(User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }
}
