package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectCreationReq;
import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectResp;
import kz.iitu.itse1910.issenbayev.entity.Project;
import kz.iitu.itse1910.issenbayev.feature.exception.ApiExceptionDetailHolder;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordAlreadyExistsException;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.feature.mapper.ProjectMapper;
import kz.iitu.itse1910.issenbayev.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public List<ProjectResp> getAllProjects(Pageable pageable) {
        List<Project> projectEntities = projectRepository.findAll(pageable).toList();
        return projectEntities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ProjectResp toResponse(Project project) {
        return ProjectMapper.INSTANCE.toResponse(project);
    }

    public void create(ProjectCreationReq creationReq) {
        throwIfNameAlreadyTaken(creationReq.getName());

        Project project = Project.builder()
                .name(creationReq.getName())
                .description(creationReq.getDescription())
                .build();
        projectRepository.save(project);
    }

    public void update(ProjectUpdateReq updateReq) {
        Project project = getByIdOrThrowNotFound(updateReq.getId());

        String newName = updateReq.getName();
        String newDescription = updateReq.getDescription();

        if (StringUtils.hasText(newName)) {
            throwIfNameAlreadyTaken(newName);
            project.setName(newName);
        }
        if (StringUtils.hasText(newDescription)) {
            project.setDescription(newDescription);
        }
        projectRepository.save(project);
    }

    public void delete(Long id) {
        Project project = getByIdOrThrowNotFound(id);
        projectRepository.delete(project);
    }

    private Project getByIdOrThrowNotFound(long id) {
        ApiExceptionDetailHolder exceptionDetailHolder = ApiExceptionDetailHolder.builder()
                .field(ProjectResp.FIELD_ID)
                .message("Project with id " + id + " does not exist.")
                .build();
        return projectRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(exceptionDetailHolder));
    }

    private void throwIfNameAlreadyTaken(String projectName) {
        if (projectRepository.existsByName(projectName)) {
            ApiExceptionDetailHolder exceptionDetailHolder = ApiExceptionDetailHolder.builder()
                    .field(ProjectResp.FIELD_NAME)
                    .message("Project with name \"" + projectName + "\" does not exist.")
                    .build();
            throw new RecordAlreadyExistsException(exceptionDetailHolder);
        }
    }
}
