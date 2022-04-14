package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectDto;
import kz.iitu.itse1910.issenbayev.dto.ticket.request.TicketCreationReq;
import kz.iitu.itse1910.issenbayev.dto.ticket.request.TicketUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketDto;
import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketPaginatedResp;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.entity.Project;
import kz.iitu.itse1910.issenbayev.entity.Ticket;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.feature.apiexception.ApiException;
import kz.iitu.itse1910.issenbayev.feature.apiexception.ApiExceptionDetailHolder;
import kz.iitu.itse1910.issenbayev.feature.apiexception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.feature.mapper.TicketMapper;
import kz.iitu.itse1910.issenbayev.repository.ProjectRepository;
import kz.iitu.itse1910.issenbayev.repository.TicketRepository;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public TicketPaginatedResp getTickets(Pageable pageable, long projectId) {
        getProjectByIdOrThrowNotFound(projectId);
        Page<Ticket> ticketPage = ticketRepository.findAll(pageable, projectId);
        return TicketPaginatedResp.fromTicketPage(ticketPage);
    }

    @Transactional(readOnly = true)
    public TicketDto getById(long projectId, long id) {
        Project project = getProjectByIdOrThrowNotFound(projectId);
        Ticket ticket = getByIdOrThrowNotFound(id);
        throwIfTicketNotBelongsToProject(ticket, project);
        return toDto(ticket);
    }

    public TicketDto create(long projectId, TicketCreationReq creationReq) {
        Project project = getProjectByIdOrThrowNotFound(projectId);
        User submitter = getUserByIdOrThrowNotFound(creationReq.getSubmitterId());

        Ticket ticket = toEntity(creationReq);
        ticket.setProject(project);
        ticket.setSubmitter(submitter);

        Ticket savedTicket = ticketRepository.save(ticket);
        return toDto(savedTicket);
    }

    public TicketDto update(long projectId, long id, TicketUpdateReq updateReq) {
        Project project = getProjectByIdOrThrowNotFound(projectId);
        Ticket ticket = getByIdOrThrowNotFound(id);
        throwIfTicketNotBelongsToProject(ticket, project);

        String newTitle = updateReq.getTitle();
        String newDescription = updateReq.getDescription();
        Long assigneeId = updateReq.getAssigneeId();
        Ticket.Type newType = toEntityType(updateReq.getType());
        Ticket.Status newStatus = toEntityStatus(updateReq.getStatus());
        Ticket.Priority newPriority = toEntityPriority(updateReq.getPriority());

        // TODO: add field validation in Ticket DTOs  (for type, status, priority, etc.)
        if (StringUtils.hasText(newTitle)) {
            ticket.setTitle(newTitle);
        }
        if (StringUtils.hasText(newDescription)) {
            ticket.setDescription(newDescription);
        }
        if (assigneeId != null) {
            User assignee = getUserByIdOrThrowNotFound(assigneeId);
            throwIfInappropriateRole(assignee);
            ticket.setAssignee(assignee);
        }
        if (newType != null) {
            ticket.setType(newType);
        }
        if (newStatus != null) {
            ticket.setStatus(newStatus);
        }
        if (newPriority != null) {
            ticket.setPriority(newPriority);
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        return toDto(updatedTicket);
    }

    public void delete(long projectId, long id) {
        Project project = getProjectByIdOrThrowNotFound(projectId);
        Ticket ticket = getByIdOrThrowNotFound(id);
        throwIfTicketNotBelongsToProject(ticket, project);
        ticketRepository.delete(ticket);
    }

    private Ticket getByIdOrThrowNotFound(long id) {
        ApiExceptionDetailHolder exDetailHolder = ApiExceptionDetailHolder.builder()
                .field(TicketDto.Field.ID)
                .message("Ticket with id " + id + " does not exist")
                .build();
        return ticketRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(exDetailHolder));
    }

    private TicketDto toDto(Ticket ticket) {
        return TicketMapper.INSTANCE.entityToDto(ticket);
    }

    private Ticket toEntity(TicketCreationReq creationReq) {
        return TicketMapper.INSTANCE.creationReqToEntity(creationReq);
    }

    private Ticket.Type toEntityType(TicketDto.Type dtoType) {
        return TicketMapper.INSTANCE.toEntityType(dtoType);
    }

    private Ticket.Status toEntityStatus(TicketDto.Status dtoStatus) {
        return TicketMapper.INSTANCE.toEntityStatus(dtoStatus);
    }

    private Ticket.Priority toEntityPriority(TicketDto.Priority dtoPriority) {
        return TicketMapper.INSTANCE.toEntityPriority(dtoPriority);
    }

    private Project getProjectByIdOrThrowNotFound(long id) {
        ApiExceptionDetailHolder exDetailHolder = ApiExceptionDetailHolder.builder()
                .field(ProjectDto.Field.ID)
                .message("Project with id " + id + " does not exist")
                .build();
        return projectRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(exDetailHolder));
    }

    private void throwIfTicketNotBelongsToProject(Ticket ticket, Project project) {
        if (!ticket.getProject().getId().equals(project.getId())) {
            String exMsg = String.format("Project with id %d doesn't have ticket with id %d",
                    project.getId(), ticket.getId());
            throw new ApiException(exMsg);
        }
    }

    private User getUserByIdOrThrowNotFound(long id) {
        ApiExceptionDetailHolder exDetailHolder = ApiExceptionDetailHolder.builder()
                .field(ProjectDto.Field.ID)
                .message("User with id " + id + " does not exist")
                .build();
        return userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(exDetailHolder));
    }

    private void throwIfInappropriateRole(User assignee) {
        if (assignee.getRole().equals(User.Role.MANAGER)) {
            String exMsg = String.format("User with role '%s' cannot be an assignee. " +
                    "Only users of the following roles can: '%s', '%s', '%s'.", assignee.getRole(),
                    UserDto.Role.DEVELOPER, UserDto.Role.LEAD_DEV, UserDto.Role.ADMIN);
            ApiExceptionDetailHolder exDetailHolder = ApiExceptionDetailHolder.builder()
                    .field(UserDto.Field.ROLE)
                    .message(exMsg)
                    .build();
            throw new ApiException(exDetailHolder);
        }
    }
}
