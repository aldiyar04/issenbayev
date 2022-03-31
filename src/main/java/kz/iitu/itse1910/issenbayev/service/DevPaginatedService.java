package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DevPaginatedService {
    List<User> getAllFreeLeads(Pageable pageable);
    List<User> getAllFreeDevs(Pageable pageable);
    List<User> getAllBusyDevs(Pageable pageable);

    List<User> searchFreeLeadsByUsername(String unameQuery, Pageable pageable);
    List<User> searchFreeDevsByUsername(String unameQuery, Pageable pageable);
}
