package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectCreationReq;
import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectSummaryResp;
import kz.iitu.itse1910.issenbayev.entity.Project;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordAlreadyExistsException;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.feature.mapper.ProjectMapper;
import kz.iitu.itse1910.issenbayev.repository.ProjectRepository;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS)
    public List<ProjectSummaryResp> getAllProjects(Pageable pageable) {
        List<Project> projectEntities = projectRepository.findAll(pageable).toList();
        return projectEntities.stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    private ProjectSummaryResp toSummaryResponse(Project project) {
        return ProjectMapper.INSTANCE.toSummaryResponse(project);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {RecordAlreadyExistsException.class}, timeout = 2)
    public void create(ProjectCreationReq creationReq) {
        throwIfNameAlreadyTaken(creationReq.getName());

        User leadDev = userRepository.findById(creationReq.getLeadDevId()).orElse(null);
        List<User> assignees = userRepository.findAllById(creationReq.getAssigneeIds());

        Project project = Project.builder()
                .name(creationReq.getName())
                .description(creationReq.getDescription())
                .leadDev(leadDev)
                .assignees(assignees)
                .build();
        projectRepository.save(project);
    }

    @Override
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

    @Override
    public void delete(Long id) {
        Project project = getByIdOrThrowNotFound(id);
        projectRepository.delete(project);
    }

    private Project getByIdOrThrowNotFound(long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Project with id " + id + " does not exist"));
    }

    private void throwIfNameAlreadyTaken(String projectName) {
        if (projectRepository.existsByName(projectName)) {
            throw new RecordAlreadyExistsException("Project with name " + projectName + " already exists");
        }
    }
}
