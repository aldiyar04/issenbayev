package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAllPaginated(int offset, int size);
    Optional<User> findById(Long id);
    void save(User user);
    void delete(User user);
}
