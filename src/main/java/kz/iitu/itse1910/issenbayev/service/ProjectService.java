package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectCreationReq;
import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectSummaryResp;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResp> getAllProjects(Pageable pageable);
    void create(ProjectCreationReq creationReq);
    void update(ProjectUpdateReq updateReq);
    void delete(Long id);
}
