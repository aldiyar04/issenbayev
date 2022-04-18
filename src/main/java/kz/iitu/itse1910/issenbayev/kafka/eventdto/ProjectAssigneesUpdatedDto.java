package kz.iitu.itse1910.issenbayev.kafka.eventdto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ProjectAssigneesUpdatedDto {
    private String projectName;
    private String assigneeUsername;
    private Boolean wasAssignedOrUnassigned;
}
