package kz.iitu.itse1910.issenbayev.dto.project.response;

import kz.iitu.itse1910.issenbayev.entity.Project;
import kz.iitu.itse1910.issenbayev.feature.mapper.ProjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class ProjectPaginatedResp {
    private final List<ProjectDto> projectDtos;
    private final int totalPages;

    public static ProjectPaginatedResp fromProjectPage(Page<Project> projectPage) {
        List<ProjectDto> projectDtos = projectPage.getContent().stream()
                .map(ProjectMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
        return new ProjectPaginatedResp(projectDtos, projectPage.getTotalPages());
    }
}
