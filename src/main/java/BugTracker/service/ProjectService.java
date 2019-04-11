package BugTracker.service;

import BugTracker.web.dto.ProjectDTO;
import BugTracker.web.dto.ProjectPatchDTO;
import BugTracker.web.dto.ProjectQueryParams;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProjectService
{
    List<ProjectDTO> find(ProjectQueryParams params);

    Optional<ProjectDTO> get(Long id);

    Optional<ProjectDTO> update(Long id, ProjectPatchDTO data);

    ProjectDTO create(ProjectDTO data);

    @Transactional
    void delete(Long id);
}
