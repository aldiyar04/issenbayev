package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Page<Ticket> findAll(Pageable pageable, Long projectId);
    Optional<Ticket> findById(Long id);
    Ticket save(Ticket ticket);
    void delete(Ticket ticket);
}
