package BugTracker.service;

import BugTracker.db.Project;
import BugTracker.db.ProjectRepository;
import BugTracker.web.dto.ProjectDTO;
import BugTracker.web.dto.ProjectPatchDTO;
import BugTracker.web.dto.ProjectQueryParams;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для работы с проектами в БД
 */
@Service
public class ProjectServiceImpl implements ProjectService
{
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository repository)
    {
        this.projectRepository = repository;
    }

    /**
     * Поиск всех проектов по заданным параметрам
     * @param params параметрв
     * @return список проектов
     */
    @Override
    public List<ProjectDTO> find(ProjectQueryParams params)
    {
        return projectRepository.findAll(params.toPageable()).stream()
                                .map(ProjectDTO::fromEntity)
                                .collect(Collectors.toList());
    }

    /**
     * Поиск конкретного проекта по id
     * @param id
     * @return проект
     */
    @Override
    public Optional<ProjectDTO> get(Long id)
    {
        return projectRepository.findById(id).map(ProjectDTO::fromEntity);
    }

    /**
     * Обновление проекта по id.
     * Обновляются только поля, выставленные через сеттеры
     * @param id
     * @param data
     * @return обновлённый проект
     */
    @Override
    public Optional<ProjectDTO> update(Long id, ProjectPatchDTO data)
    {
        Optional<Project> project = projectRepository.findById(id);
        return project.map(x -> applyPatch(x, data))
                      .map(x -> projectRepository.save(x))
                      .map(ProjectDTO::fromEntity);
    }

    /**
     * Создание проекта.
     * @param data
     * @return созданный проект
     */
    @Override
    public ProjectDTO create(ProjectDTO data)
    {
        Project project = new Project();
        project.setName(data.getName());
        project.setDescription(data.getDescription());
        return ProjectDTO.fromEntity(projectRepository.save(project));
    }

    /**
     * Удаление проекта по id
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id)
    {
        projectRepository.deleteById(id);
    }

    private Project applyPatch(Project project, ProjectPatchDTO data)
    {
        if (data.isNameDirty())
            project.setName(data.getName());
        if (data.isDescriptionDirty())
            project.setDescription(data.getDescription());
        return project ;
    }


}
