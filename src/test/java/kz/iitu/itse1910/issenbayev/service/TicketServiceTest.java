package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.ticket.request.TicketCreationReq;
import kz.iitu.itse1910.issenbayev.dto.ticket.request.TicketUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketDto;
import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketPaginatedResp;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.entity.Project;
import kz.iitu.itse1910.issenbayev.entity.Ticket;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.feature.exception.apiexception.ApiException;
import kz.iitu.itse1910.issenbayev.feature.exception.apiexception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.feature.mapper.TicketMapper;
import kz.iitu.itse1910.issenbayev.repository.ProjectRepository;
import kz.iitu.itse1910.issenbayev.repository.TicketRepository;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import kz.iitu.itse1910.issenbayev.service.testdata.ProjectTestData;
import kz.iitu.itse1910.issenbayev.service.testdata.TicketTestData;
import kz.iitu.itse1910.issenbayev.service.testdata.UserTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TicketServiceTest {
    @Mock
    TicketRepository ticketRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    TicketService underTest;

    TicketTestData.Entity tickets = new TicketTestData.Entity();
    TicketTestData.Dto ticketDtos = new TicketTestData.Dto();
    ProjectTestData.Entity projects = new ProjectTestData.Entity();
    UserTestData.Entity users = new UserTestData.Entity();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTickets_caseSuccess() {
        // given
        long projectId = 1L;
        List<Ticket> ticketsOfProject1 = tickets.getTicketsOfProject1();
        List<TicketDto> ticketsDtosOfProject1 = ticketDtos.getTicketsOfProject1();
        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findAll(pageRequest, projectId)).thenReturn(new PageImpl<>(ticketsOfProject1));

        // when
        List<TicketDto> result = underTest.getTickets(pageRequest, projectId).getTicketDtos();

        // then
        assertThat(result).isEqualTo(ticketsDtosOfProject1);
    }

    @Test
    void testGetTickets_caseProjectNotFound() {
        // given
        long projectId = -1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
            underTest.getTickets(pageRequest, projectId);
        }).isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testGetById_caseSuccessful() {
        // given
        long projectId = 1L;
        long id = 1L;
        TicketDto expected = ticketDtos.getTicket1();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findById(id)).thenReturn(Optional.of(tickets.getTicket1()));

        // when
        TicketDto result = underTest.getById(projectId, id);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testGetById_caseProjectNotFound() {
        // given
        long projectId = -1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            underTest.getById(projectId, anyLong());
        }).isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testGetById_caseNotFound() {
        // given
        long projectId = 1L;
        long id = -1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            underTest.getById(projectId, id);
        }).isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testGetById_caseTicketNotBelongsToProject() {
        // given
        long projectId = 1L;
        long id = 3L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findById(id)).thenReturn(Optional.of(tickets.getTicket3()));

        // when, then
        assertThatThrownBy(() -> {
            underTest.getById(projectId, id);
        }).isInstanceOf(ApiException.class);
    }

    @Test
    void testCreate_caseSuccessful() {
        // given
        String title = "title";
        String description = "description";
        Project project1 = projects.getProject1();
        User submitter = users.getAdmin();
        String type = Ticket.Type.BUG;
        String status = Ticket.Status.NEW;
        String priority = Ticket.Priority.MEDIUM;
        Ticket ticket = Ticket.builder()
                .title(title)
                .description(description)
                .project(project1)
                .submitter(submitter)
                .type(type)
                .status(status)
                .priority(priority)
                .build();
        TicketDto expected = TicketMapper.INSTANCE.toDto(ticket);
        when(projectRepository.findById(project1.getId())).thenReturn(Optional.of(project1));
        when(userRepository.findById(submitter.getId())).thenReturn(Optional.of(submitter));
        when(ticketRepository.save(any())).thenReturn(ticket);

        // when
        TicketCreationReq creationReq = TicketCreationReq.builder()
                .title(title)
                .description(description)
                .submitterId(submitter.getId())
                .type(type)
                .status(status)
                .priority(priority)
                .build();
        TicketDto result = underTest.create(project1.getId(), creationReq);

        // then
        ArgumentCaptor<Ticket> ticketArgCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(ticketArgCaptor.capture());
        Ticket savedTicket = ticketArgCaptor.getValue();
        assertThat(savedTicket).isEqualTo(ticket);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testCreate_caseProjectNotFound() {
        // given
        long projectId = -1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            underTest.create(projectId, any());
        }).isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testCreate_caseSubmitterNotFound() {
        // given
        long projectId = 1L;
        long submitterId = -1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(userRepository.findById(submitterId)).thenReturn(Optional.empty());

        // when, then
        TicketCreationReq creationReq = TicketCreationReq.builder()
                .submitterId(submitterId)
                .build();
        assertThatThrownBy(() -> {
            underTest.create(projectId, creationReq);
        }).isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testUpdate_caseSuccessful() {
        // given
        Project project1 = projects.getProject1();
        long projectId = 1L;
        long id = 1L;
        String newTitle = "New Title";
        String newDescription = "New description...";
        User assignee = users.getDeveloper1();
        String newType = Ticket.Type.OTHER;
        String newStatus = Ticket.Status.EXTRA_WORK_REQUIRED;
        String newPriority = Ticket.Priority.HIGH;
        Ticket updatedTicket = Ticket.builder()
                .id(id)
                .title(newTitle)
                .description(newDescription)
                .project(project1)
                .assignee(assignee)
                .type(newType)
                .status(newStatus)
                .priority(newPriority)
                .build();
        TicketDto expected = TicketMapper.INSTANCE.toDto(updatedTicket);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project1));
        when(ticketRepository.findById(id)).thenReturn(Optional.of(tickets.getTicket1()));
        when(userRepository.findById(assignee.getId())).thenReturn(Optional.of(users.getDeveloper1()));

        when(ticketRepository.save(any())).thenReturn(updatedTicket);

        // when
        TicketUpdateReq updateReq = TicketUpdateReq.builder()
                .title(newTitle)
                .description(newDescription)
                .assigneeId(assignee.getId())
                .type(newType)
                .status(newStatus)
                .priority(newPriority)
                .build();
        TicketDto result = underTest.update(projectId, id, updateReq);

        // then
        ArgumentCaptor<Ticket> ticketArgCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(ticketArgCaptor.capture());
        Ticket savedTicket = ticketArgCaptor.getValue();
        assertThat(savedTicket).isEqualTo(updatedTicket);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testUpdate_caseProjectNotFound() {
        // given
        long projectId = -1L;
        long id = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            underTest.update(projectId, id, any());
        }).isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testUpdate_caseNotFound() {
        // given
        long projectId = 1L;
        long id = -1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            underTest.update(projectId, id, any());
        }).isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testUpdate_caseTicketNotBelongsToProject() {
        // given
        long projectId = 1L;
        long id = 3L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findById(id)).thenReturn(Optional.of(tickets.getTicket3()));

        // when, then
        assertThatThrownBy(() -> {
            underTest.update(projectId, id, any());
        }).isInstanceOf(ApiException.class);
    }

    @Test
    void testUpdate_caseAssigneeNotFound() {
        // given
        long projectId = 1L;
        long id = 1L;
        long assigneeId = -1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findById(id)).thenReturn(Optional.of(tickets.getTicket1()));
        when(userRepository.findById(assigneeId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            TicketUpdateReq updateReq = TicketUpdateReq.builder()
                    .assigneeId(assigneeId)
                    .build();
            underTest.update(projectId, id, updateReq);
        }).isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void update_shouldThrowApiException_whenAssigneeRoleManager() {
        // given
        long projectId = 1L;
        long id = 1L;
        User assignee = users.getManager();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findById(id)).thenReturn(Optional.of(tickets.getTicket1()));
        when(userRepository.findById(assignee.getId())).thenReturn(Optional.of(assignee));

        // when, then
        assertThatThrownBy(() -> {
            TicketUpdateReq updateReq = TicketUpdateReq.builder()
                    .assigneeId(assignee.getId())
                    .build();
            underTest.update(projectId, id, updateReq);
        }).isInstanceOf(ApiException.class);
    }

    @Test
    void testDelete_caseSuccessful() {
        // given
        long projectId = 1L;
        Ticket ticket1 = tickets.getTicket1();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findById(ticket1.getId())).thenReturn(Optional.of(ticket1));

        // when
        underTest.delete(projectId, ticket1.getId());

        // then
        ArgumentCaptor<Ticket> ticketArgCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).delete(ticketArgCaptor.capture());
        Ticket deletedTicket = ticketArgCaptor.getValue();
        assertThat(deletedTicket).isEqualTo(ticket1);
    }

    @Test
    void testDelete_caseProjectNotFound() {
        // given
        long projectId = -1L;
        long id = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            underTest.delete(projectId, id);
        }).isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testDelete_caseNotFound() {
        // given
        long projectId = 1L;
        long id = -1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            underTest.delete(projectId, id);
        }).isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testDelete_caseTicketNotBelongsToProject() {
        // given
        long projectId = 1L;
        long id = 3L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projects.getProject1()));
        when(ticketRepository.findById(id)).thenReturn(Optional.of(tickets.getTicket3()));

        // when, then
        assertThatThrownBy(() -> {
            underTest.delete(projectId, id);
        }).isInstanceOf(ApiException.class);
    }
}