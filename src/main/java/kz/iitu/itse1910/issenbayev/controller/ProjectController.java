package kz.iitu.itse1910.issenbayev.controller;

import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectCreationReq;
import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectDto;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectPaginatedResp;
import kz.iitu.itse1910.issenbayev.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<ProjectPaginatedResp> getProjects(Pageable pageable) {
        ProjectPaginatedResp resp = projectService.getProjects(pageable);
        return ResponseEntity.ok().body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable("id") long id) {
        ProjectDto project = projectService.getById(id);
        return ResponseEntity.ok().body(project);
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectCreationReq creationReq) {
        ProjectDto project = projectService.create(creationReq);
        return ResponseEntity.ok().body(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable("id") long id,
                                                    @RequestBody ProjectUpdateReq updateReq) {
        ProjectDto project = projectService.update(id, updateReq);
        return ResponseEntity.ok().body(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable("id") long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
