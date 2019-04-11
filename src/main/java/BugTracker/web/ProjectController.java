package BugTracker.web;

import BugTracker.service.ExceptionFactory;
import BugTracker.service.ProjectService;
import BugTracker.web.dto.ProjectDTO;
import BugTracker.web.dto.ProjectPatchDTO;
import BugTracker.web.dto.ProjectQueryParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/projects/")
@Slf4j
public class ProjectController
{
    private final ProjectService service;

    public ProjectController(ProjectService handler)
    {
        this.service = handler;
    }

    @GetMapping("/")
    public List<ProjectDTO> list(@Valid ProjectQueryParams params)
    {
        List<ProjectDTO> projects = service.find(params);
        log.info(params.toString());
        return projects;
    }

    @GetMapping("{id}")
    public ProjectDTO show(@PathVariable Long id)
    {
        return service
                .get(id)
                .orElseThrow(() -> ExceptionFactory.projectNotFound(id));
    }

    @PatchMapping("{id}")
    public ProjectDTO patch(@PathVariable Long id, @RequestBody ProjectPatchDTO data)
    {
        return service
                .update(id, data)
                .orElseThrow(() -> ExceptionFactory.projectNotFound(id));
    }

    @PostMapping("/")
    public ResponseEntity<ProjectDTO> post(@RequestBody ProjectDTO data)
    {
        ProjectDTO project = service.create(data);
        URI link = linkTo(methodOn(ProjectController.class).show(project.getId())).toUri();
        return ResponseEntity.created(link).body(project);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id)
    {
        service.delete(id);
    }
}
