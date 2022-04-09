package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TicketRepositoryImpl implements TicketRepository {
    @Override
    public Page<Ticket> findAll(Pageable pageable, Long projectId) {
        return null;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Ticket save(Ticket ticket) {
        return null;
    }

    @Override
    public void delete(Ticket ticket) {

    }
}
