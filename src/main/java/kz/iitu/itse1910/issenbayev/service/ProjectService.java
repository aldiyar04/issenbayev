package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectCreationReq;
import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectDto;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectPaginatedResp;
import kz.iitu.itse1910.issenbayev.entity.Project;
import kz.iitu.itse1910.issenbayev.feature.exception.apiexception.ApiExceptionDetailHolder;
import kz.iitu.itse1910.issenbayev.feature.exception.apiexception.RecordAlreadyExistsException;
import kz.iitu.itse1910.issenbayev.feature.exception.apiexception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.feature.mapper.ProjectMapper;
import kz.iitu.itse1910.issenbayev.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public ProjectPaginatedResp getProjects(Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAll(pageable);
        return ProjectPaginatedResp.fromProjectPage(projectPage);
    }

    @Transactional(readOnly = true)
    public ProjectDto getById(long id) {
        Project project = getByIdOrThrowNotFound(id);
        return toDto(project);
    }

    public ProjectDto create(ProjectCreationReq creationReq) {
        throwIfNameAlreadyTaken(creationReq.getName());

        Project project = Project.builder()
                .name(creationReq.getName())
                .description(creationReq.getDescription())
                .build();
        Project savedProject = projectRepository.save(project);
        return toDto(savedProject);
    }

    public ProjectDto update(long id, ProjectUpdateReq updateReq) {
        Project project = getByIdOrThrowNotFound(id);

        String newName = updateReq.getName();
        String newDescription = updateReq.getDescription();

        if (StringUtils.hasText(newName)) {
            throwIfNameAlreadyTaken(newName);
            project.setName(newName);
        }
        if (StringUtils.hasText(newDescription)) {
            project.setDescription(newDescription);
        }
        Project updatedProject = projectRepository.save(project);
        return toDto(updatedProject);
    }

    public void delete(Long id) {
        Project project = getByIdOrThrowNotFound(id);
        projectRepository.delete(project);
    }

    private ProjectDto toDto(Project project) {
        return ProjectMapper.INSTANCE.toDto(project);
    }

    private void throwIfNameAlreadyTaken(String projectName) {
        if (projectRepository.existsByName(projectName)) {
            ApiExceptionDetailHolder exceptionDetailHolder = ApiExceptionDetailHolder.builder()
                    .field(ProjectDto.Field.NAME)
                    .message("Project with name \"" + projectName + "\" does not exist")
                    .build();
            throw new RecordAlreadyExistsException(exceptionDetailHolder);
        }
    }

    private Project getByIdOrThrowNotFound(long id) {
        ApiExceptionDetailHolder exDetailHolder = ApiExceptionDetailHolder.builder()
                .field(ProjectDto.Field.ID)
                .message("Project with id " + id + " does not exist")
                .build();
        return projectRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(exDetailHolder));
    }
}
