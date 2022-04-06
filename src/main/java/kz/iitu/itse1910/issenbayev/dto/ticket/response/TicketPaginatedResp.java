package kz.iitu.itse1910.issenbayev.dto.ticket.response;

import kz.iitu.itse1910.issenbayev.entity.Ticket;
import kz.iitu.itse1910.issenbayev.feature.mapper.TicketMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class TicketPaginatedResp {
    private final List<TicketDto> ticketDtos;
    private final int totalPages;

    public static TicketPaginatedResp fromTicketPage(Page<Ticket> ticketPage) {
        List<TicketDto> ticketDtos = ticketPage.getContent().stream()
                .map(TicketMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
        return new TicketPaginatedResp(ticketDtos, ticketPage.getTotalPages());
    }
}
