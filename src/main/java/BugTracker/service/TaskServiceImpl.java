package BugTracker.service;

import BugTracker.db.*;
import BugTracker.web.dto.TaskDTO;
import BugTracker.web.dto.TaskPatchDTO;
import BugTracker.web.dto.TaskQueryParams;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для работы с задачами в БД
 * Все методы также проверяют, верно ли указан id проекта,
 * и выбрасывают исключение если это не так
 */
@Service
public class TaskServiceImpl implements TaskService
{

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository)
    {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * Получение перечня задач проекта по параметрам
     * @param projectId
     * @param params
     * @return список задач
     */
    @Override
    public List<TaskDTO> find(Long projectId, TaskQueryParams params)
    {
        projectRepository.findById(projectId).orElseThrow(() -> ExceptionFactory.projectNotFound(projectId));
        TaskSpecification spec = new TaskSpecification(params, projectId);
        return taskRepository.findAll(spec, params.toPageable()).stream()
                             .map(TaskDTO::fromEntity)
                             .collect(Collectors.toList());
    }

    /**
     * Получение конкретной задачи в проекте
     * @param projectId
     * @param id
     * @return задача
     */
    @Override
    public Optional<TaskDTO> get(Long projectId, Long id)
    {
        return taskRepository.findById(id)
                             .map(x -> checkProjectId(x, projectId))
                             .map(TaskDTO::fromEntity);
    }

    /**
     * Обновление задачи.
     * Обновляются только поля, установленные через сеттеры.
     * @param projectId
     * @param id
     * @param data
     * @return обновлённая задача
     */
    @Override
    public Optional<TaskDTO> update(Long projectId, Long id, TaskPatchDTO data)
    {
        return taskRepository.findById(id)
                             .map(x -> checkProjectId(x, projectId))
                             .map(TaskServiceImpl::checkUpdateable)
                             .map(x -> applyPatch(x, data))
                             .map(x -> taskRepository.save(x))
                             .map(TaskDTO::fromEntity);
    }

    /**
     * Создание задачи
     * @param projectId
     * @param data
     * @return созданная задача
     */
    @Override
    @Transactional
    public TaskDTO create(Long projectId, TaskDTO data)
    {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> ExceptionFactory.projectNotFound(projectId));
        Task task = new Task();
        task.setName(data.getName());
        task.setDescription(data.getDescription());
        task.setPriority(data.getPriority());
        project.addTask(task);
        taskRepository.save(task);
        return TaskDTO.fromEntity(task);
    }

    /**
     * Удаление задачи
     * @param projectId
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long projectId, Long id)
    {
        taskRepository.findById(id)
                      .map(x -> checkProjectId(x, projectId))
                      .orElseThrow(() -> ExceptionFactory.taskNotFound(id));
        taskRepository.deleteById(id);
    }

    private static Task checkProjectId(Task task, Long projectId)
    {
        if (!task.getProject().getId().equals(projectId))
            throw ExceptionFactory.taskNotFound(task.getId());
        return task;
    }

    private static Task checkUpdateable(Task task)
    {
        if (task.getStatus() == Status.CLOSED)
            throw ExceptionFactory.taskClosed(task.getId());
        return task;
    }

    private Task applyPatch(Task task, TaskPatchDTO data)
    {
        if (data.isNameDirty())
            task.setName(data.getName());
        if (data.isDescriptionDirty())
            task.setDescription(data.getDescription());
        if (data.isPriorityDirty())
            task.setPriority(data.getPriority());
        if (data.isStatusDirty())
            task.setStatus(data.getStatus());
        return task;
    }


}
