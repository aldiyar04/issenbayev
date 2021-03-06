package kz.iitu.itse1910.issenbayev.feature.mapper;

import kz.iitu.itse1910.issenbayev.dto.project.response.ProjectDto;
import kz.iitu.itse1910.issenbayev.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(target = "leadDevId", source = "leadDev.id")
    @Mapping(target = "leadDevUname", source = "leadDev.username")
    ProjectDto toDto(Project project);
}
