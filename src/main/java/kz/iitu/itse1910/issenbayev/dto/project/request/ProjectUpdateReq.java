package kz.iitu.itse1910.issenbayev.dto.project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ProjectUpdateReq {
    private final String name;
    private final String description;
}
