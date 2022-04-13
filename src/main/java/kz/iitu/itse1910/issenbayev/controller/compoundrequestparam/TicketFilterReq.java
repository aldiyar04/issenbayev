package kz.iitu.itse1910.issenbayev.controller.compoundrequestparam;

import kz.iitu.itse1910.issenbayev.controller.api.TicketApi;
import kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.annotation.RequestParamName;
import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
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
