package BugTracker.service;

import BugTracker.web.dto.TaskDTO;
import BugTracker.web.dto.TaskPatchDTO;
import BugTracker.web.dto.TaskQueryParams;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TaskService
{
    List<TaskDTO> find(Long projectId, TaskQueryParams params);

    Optional<TaskDTO> get(Long projectId, Long id);

    Optional<TaskDTO> update(Long projectId, Long id, TaskPatchDTO data);

    @Transactional
    TaskDTO create(Long projectId, TaskDTO data);

    @Transactional
    void delete(Long projectId, Long id);
}
