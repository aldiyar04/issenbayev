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

    @Query("select p.leadDev.email, p.name, " +
            "t.title, t.type, t.priority, t.targetResolutionDate, t.createdAt " +
            "from Ticket t inner join fetch Project p on t.project = p and p.leadDev is not null")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<Object[]> findUnassignedTickets();

    @Query("select p.leadDev.email, p.name, " +
            "t.title, t.type, t.status, t.priority, t.targetResolutionDate " +
            "from Ticket t inner join fetch Project p on t.project = p and p.leadDev is not null " +
            "where t.targetResolutionDate < current_date")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<Object[]> findOverdueTickets();
}
