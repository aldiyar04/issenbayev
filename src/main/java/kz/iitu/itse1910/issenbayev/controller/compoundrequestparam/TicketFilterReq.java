package kz.iitu.itse1910.issenbayev.controller.compoundrequestparam;

import kz.iitu.itse1910.issenbayev.controller.api.TicketApi;
import kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.annotation.RequestParamName;
import kz.iitu.itse1910.issenbayev.controller.dto.ticket.response.TicketDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class TicketFilterReq {
    @RequestParamName(TicketApi.Filter.TYPE)
    private final TicketDto.Type type;

    @RequestParamName(TicketApi.Filter.STATUS)
    private final TicketDto.Status status;

    @RequestParamName(TicketApi.Filter.PRIORITY)
    private final TicketDto.Priority priority;

    @RequestParamName(TicketApi.Filter.IS_ASSIGNED)
    private final Boolean isAssigned;

    @RequestParamName(TicketApi.Filter.IS_OVERDUE)
    private final Boolean isOverdue;
}
