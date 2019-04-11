package BugTracker.web.dto;


import BugTracker.db.Status;
import BugTracker.db.Task;
import lombok.Data;

import java.util.Date;

@Data
public class TaskDTO
{

    private Long id;
    private String name;
    private String description;
    private Status status;
    private Date createdAt;
    private Date lastUpdatedAt;
    private Long projectId;
    private Integer priority;


    public static TaskDTO fromEntity(Task t)
    {
        TaskDTO d = new TaskDTO();
        d.id = t.getId();
        d.name = t.getName();
        d.description = t.getDescription();
        d.status = t.getStatus();
        d.createdAt = t.getCreatedAt();
        d.lastUpdatedAt = t.getLastUpdatedAt();
        d.projectId = t.getProject().getId();
        d.priority = t.getPriority();
        return d;
    }
}
