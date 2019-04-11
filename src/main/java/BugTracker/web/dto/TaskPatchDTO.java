package BugTracker.web.dto;

import BugTracker.db.Status;
import lombok.Data;

@Data
public class TaskPatchDTO
{
    private String name;
    private String description;
    private Status status;
    private Integer priority;
    private boolean nameDirty;
    private boolean descriptionDirty;
    private boolean statusDirty;
    private boolean priorityDirty;

    public void setName(String name)
    {
        this.nameDirty = true;
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.descriptionDirty = true;
        this.description = description;
    }

    public void setStatus(Status status)
    {
        this.statusDirty = true;
        this.status = status;
    }

    public void setPriority(Integer priority)
    {
        this.priorityDirty = true;
        this.priority = priority;
    }
}
