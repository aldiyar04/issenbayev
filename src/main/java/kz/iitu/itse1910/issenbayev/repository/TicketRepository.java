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

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Page<Ticket> findAll(@NonNull Pageable pageable);
}
