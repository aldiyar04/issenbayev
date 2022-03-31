package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectCreationReq;
import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectSummaryResp;
import kz.iitu.itse1910.issenbayev.entity.Project;
import kz.iitu.itse1910.issenbayev.repository.ProjectRepository;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class ProjectServiceImplTest {
    @Mock
    ProjectRepository projectRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    ProjectServiceImpl projectServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllProjects() {
        Pageable pageable = PageRequest.of(0, 5);
        when(projectRepository.findAll(pageable)).thenReturn(null);

        List<ProjectSummaryResp> result = projectServiceImpl.getAllProjects(pageable);
        Assertions.assertEquals(List.of(new ProjectSummaryResp()), result);
    }

    @Test
    void testCreate() {
        when(projectRepository.existsByName(anyString())).thenReturn(true);

        projectServiceImpl.create(ProjectCreationReq.builder().name("Bug Tracker").description("Description").build());
    }

    @Test
    void testUpdate() {
        when(projectRepository.existsByName(anyString())).thenReturn(true);

        projectServiceImpl.update(new ProjectUpdateReq(1L, "name", "description"));
    }

    @Test
    void testDelete() {
        projectServiceImpl.delete(1L);
    }
}
