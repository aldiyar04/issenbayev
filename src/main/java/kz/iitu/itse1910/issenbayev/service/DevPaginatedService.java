package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.helper.pagination.PageRequest;

import java.util.List;

public interface DevPaginatedService {
    List<User> getAllFreeLeads(PageRequest pageRequest);
    List<User> getAllFreeDevs(PageRequest pageRequest);
    List<User> getAllBusyDevs(PageRequest pageRequest);

    List<User> searchFreeLeadsByUsername(String unameQuery, PageRequest pageRequest);
    List<User> searchFreeDevsByUsername(String unameQuery, PageRequest pageRequest);
}
