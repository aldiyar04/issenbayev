package kz.iitu.itse1910.issenbayev.controller;

import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectCreationReq;
import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectDto;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectPaginatedResp;
import kz.iitu.itse1910.issenbayev.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ProjectPaginatedResp getProjects(Pageable pageable) {
        return projectService.getProjects(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDto getProjectById(@PathVariable("id") long id) {
        return projectService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProjectDto createProject(@RequestBody ProjectCreationReq creationReq) {
        return projectService.create(creationReq);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDto updateProject(@PathVariable("id") long id,
                                                    @RequestBody ProjectUpdateReq updateReq) {
        return projectService.update(id, updateReq);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable("id") long id) {
        projectService.delete(id);
    }
}
