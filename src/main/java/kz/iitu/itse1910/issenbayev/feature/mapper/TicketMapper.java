package kz.iitu.itse1910.issenbayev.feature.mapper;

import kz.iitu.itse1910.issenbayev.dto.ticket.request.TicketCreationReq;
import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketDto;
import kz.iitu.itse1910.issenbayev.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    @Mapping(target = TicketDto.Field.PROJECT_ID, source = Ticket.Field.PROJECT + "." + BaseEntity.Field.ID)
    @Mapping(target = TicketDto.Field.PROJECT_NAME, source = Ticket.Field.PROJECT + "." + Project.Field.NAME)
    @Mapping(target = TicketDto.Field.ASSIGNEE_ID, source = Ticket.Field.ASSIGNEE + "." + BaseEntity.Field.ID)
    @Mapping(target = TicketDto.Field.ASSIGNEE_USERNAME, source = Ticket.Field.ASSIGNEE + "." + User.Field.USERNAME)
    @Mapping(target = TicketDto.Field.SUBMITTER_ID, source = Ticket.Field.SUBMITTER + "." + BaseEntity.Field.ID)
    @Mapping(target = TicketDto.Field.SUBMITTER_USERNAME, source = Ticket.Field.SUBMITTER + "." + User.Field.USERNAME)
    TicketDto entityToDto(Ticket ticket);

    Ticket creationReqToEntity(TicketCreationReq creationReq);

    TicketDto.Type toDtoType(Ticket.Type entityType);
    Ticket.Type toEntityType(TicketDto.Type dtoType);
    TicketDto.Status toDtoStatus(Ticket.Status entityStatus);
    Ticket.Status toEntityStatus(TicketDto.Status dtoStatus);
    TicketDto.Priority toDtoPriority(Ticket.Priority entityPriority);
    Ticket.Priority toEntityPriority(TicketDto.Priority dtoPriority);
}
