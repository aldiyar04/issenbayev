package kz.iitu.itse1910.issenbayev.dto.project.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ProjectCreationReq {
    private String name;
    private String description;
}
