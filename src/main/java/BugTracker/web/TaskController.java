package BugTracker.web;

import BugTracker.service.ExceptionFactory;
import BugTracker.service.TaskService;
import BugTracker.web.dto.TaskDTO;
import BugTracker.web.dto.TaskPatchDTO;
import BugTracker.web.dto.TaskQueryParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@Slf4j
@RequestMapping("/projects/{projectId}/tasks/")
public class TaskController
{
    private final TaskService service;

    public TaskController(TaskService service)
    {
        this.service = service;
    }

    @GetMapping("/")
    public List<TaskDTO> list(@Valid TaskQueryParams params, @PathVariable Long projectId)
    {
        log.info(params.toString());
        return service.find(projectId, params);
    }

    @GetMapping("{id}")
    public TaskDTO show(@PathVariable Long id, @PathVariable Long projectId)
    {
        return service.get(projectId, id).orElseThrow(() -> ExceptionFactory.taskNotFound(id));
    }

    @PatchMapping("{id}")
    public TaskDTO patch(@PathVariable Long id, @RequestBody TaskPatchDTO data, @PathVariable Long projectId)
    {
        return service.update(projectId, id, data).orElseThrow(() -> ExceptionFactory.taskNotFound(id));
    }

    @PostMapping("/")
    public ResponseEntity<TaskDTO> post(@RequestBody @Valid TaskDTO data, @PathVariable Long projectId)
    {
        TaskDTO task = service.create(projectId, data);
        URI link = linkTo(methodOn(TaskController.class).show(task.getId(), task.getProjectId())).toUri();
        return ResponseEntity.created(link).body(task);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id, @PathVariable Long projectId)
    {
        service.delete(projectId, id);
    }
}
