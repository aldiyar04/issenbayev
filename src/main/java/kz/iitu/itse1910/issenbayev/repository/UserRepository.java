package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;

import javax.persistence.QueryHint;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<User> findAll(@NonNull Pageable pageable);

    // TODO: use metamodel field instead of hardcoded role "Lead Dev":
    @Query("select u from User u where u.role = 'Lead Dev' and " +
            "(select count(p) from Project p where p.leadDev = u) = 0")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<User> findUnassignedLeadDevs(Pageable pageable);

    @Query("select u from User u where u.role = 'Lead Dev' and " +
            "(select count(p) from Project p where p.leadDev = u) = 1")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<User> findAssignedLeadDevs(Pageable pageable);

    @Query("select u from User u where u.role = 'Developer' and " +
            "(select count(assignee) from Project p join p.assignees assignee where assignee = u) = 0")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<User> findUnassignedDevelopers(Pageable pageable);

    @Query("select u from User u where u.role = 'Developer' and " +
            "(select count(assignee) from Project p join p.assignees assignee where assignee = u) = 1")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<User> findAssignedDevelopers(Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
