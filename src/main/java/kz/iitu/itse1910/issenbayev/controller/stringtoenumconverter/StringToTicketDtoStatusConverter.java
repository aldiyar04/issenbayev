package kz.iitu.itse1910.issenbayev.controller.stringtoenumconverter;

import kz.iitu.itse1910.issenbayev.controller.api.TicketApi;
import kz.iitu.itse1910.issenbayev.controller.dto.ticket.response.TicketDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTicketDtoStatusConverter implements Converter<String, TicketDto.Status> {
    @Override
    public TicketDto.Status convert(String source) {
        switch (source) {
            case TicketApi.Status.NEW:
                return TicketDto.Status.NEW;
            case TicketApi.Status.ASSIGNED:
                return TicketDto.Status.ASSIGNED;
            case TicketApi.Status.IN_PROGRESS:
                return TicketDto.Status.IN_PROGRESS;
            case TicketApi.Status.SUBMITTED:
                return TicketDto.Status.SUBMITTED;
            case TicketApi.Status.EXTRA_WORK_REQUIRED:
                return TicketDto.Status.EXTRA_WORK_REQUIRED;
            case TicketApi.Status.RESOLVED:
                return TicketDto.Status.RESOLVED;
            default:
                throw new IllegalArgumentException("Invalid ticket status: " + source);
        }
    }
}
