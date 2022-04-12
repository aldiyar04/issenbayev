package kz.iitu.itse1910.issenbayev.controller;

import kz.iitu.itse1910.issenbayev.controller.annotation.CompoundRequestParam;
import kz.iitu.itse1910.issenbayev.controller.annotation.MethodWithCompoundRequestParams;
import kz.iitu.itse1910.issenbayev.dto.ticket.request.TicketCreationReq;
import kz.iitu.itse1910.issenbayev.dto.ticket.request.TicketFilterReq;
import kz.iitu.itse1910.issenbayev.dto.ticket.request.TicketUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketDto;
import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketPaginatedResp;
import kz.iitu.itse1910.issenbayev.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/projects/{projectId}/tickets")
    @MethodWithCompoundRequestParams
    public ResponseEntity<TicketPaginatedResp> getTickets(Pageable pageable,
                                                          @PathVariable("projectId") long projectId,
                                                          @CompoundRequestParam TicketFilterReq filterReq) {
        System.out.println(filterReq);
        TicketPaginatedResp resp = ticketService.getTickets(pageable, projectId);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/projects/{projectId}/tickets/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable("projectId") long projectId,
                                                   @PathVariable("id") long id) {
        TicketDto ticket = ticketService.getById(projectId, id);
        return ResponseEntity.ok(ticket);
    }

    @PostMapping("/projects/{projectId}/tickets")
    public ResponseEntity<TicketDto> createTicket(@PathVariable("projectId") long projectId,
                                                  @RequestBody TicketCreationReq creationReq) {
        TicketDto createdTicket = ticketService.create(projectId, creationReq);
        return ResponseEntity.ok(createdTicket);
    }

    @PutMapping("/projects/{projectId}/tickets/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable("projectId") long projectId,
                                                  @PathVariable("id") long id,
                                                  @RequestBody TicketUpdateReq updateReq) {
        TicketDto updatedTicket = ticketService.update(projectId, id, updateReq);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/projects/{projectId}/tickets/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable("projectId") long projectId,
                                          @PathVariable("id") long id) {
        ticketService.delete(projectId, id);
        return ResponseEntity.noContent().build();
    }
}
