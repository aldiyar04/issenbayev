package kz.iitu.itse1910.issenbayev.feature.mapper;

import kz.iitu.itse1910.issenbayev.dto.project.request.ProjectCreationReq;
import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectDto;
import kz.iitu.itse1910.issenbayev.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(target = ProjectDto.Field.LEAD_DEV, source = Project.Field.LEAD_DEV)
    ProjectDto entityToDto(Project entity);

    Project creationReqToEntity(ProjectCreationReq creationReq);
}
