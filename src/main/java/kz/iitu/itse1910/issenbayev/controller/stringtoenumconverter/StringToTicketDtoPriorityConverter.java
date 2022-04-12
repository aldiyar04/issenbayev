package kz.iitu.itse1910.issenbayev.controller.stringtoenumconverter;

import kz.iitu.itse1910.issenbayev.controller.api.TicketApi;
import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTicketDtoPriorityConverter implements Converter<String, TicketDto.Priority> {
    @Override
    public TicketDto.Priority convert(String source) {
        switch (source) {
            case TicketApi.Priority.CRITICAL:
                return TicketDto.Priority.CRITICAL;
            case TicketApi.Priority.HIGH:
                return TicketDto.Priority.HIGH;
            case TicketApi.Priority.MEDIUM:
                return TicketDto.Priority.MEDIUM;
            case TicketApi.Priority.LOW:
                return TicketDto.Priority.LOW;
            default:
                throw new IllegalArgumentException("Invalid ticket priority: " + source);
        }
    }
}
