package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;

import javax.persistence.QueryHint;
import java.util.Optional;

public interface UserRepository {
    Page<User> findAll(Pageable pageable);
    Page<User> findAll(Pageable pageable, String role);

    Page<User> findUnassignedLeadDevs(Pageable pageable);
    Page<User> findAssignedLeadDevs(Pageable pageable);
    Page<User> findUnassignedDevelopers(Pageable pageable);
    Page<User> findAssignedDevelopers(Pageable pageable);

    Optional<User> findById(long id);
    User insert(User user);
    User update(User user);
    void delete(User user);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
