package kz.iitu.itse1910.issenbayev.dto.ticket.request;

import kz.iitu.itse1910.issenbayev.controller.api.TicketApi;
import kz.iitu.itse1910.issenbayev.dto.RequestParamName;
import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketDto;
import lombok.Data;

@Data
public class TicketFilterReq {
    @RequestParamName(TicketApi.Filter.TYPE)
    private TicketDto.Type type;

    @RequestParamName(TicketApi.Filter.STATUS)
    private TicketDto.Status status;

    @RequestParamName(TicketApi.Filter.PRIORITY)
    private TicketDto.Priority priority;

    @RequestParamName(TicketApi.Filter.IS_ASSIGNED)
    private Boolean isAssigned;

    @RequestParamName(TicketApi.Filter.IS_OVERDUE)
    private Boolean isOverdue;
}
