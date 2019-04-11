package BugTracker.web.dto;

import BugTracker.db.Project;
import lombok.Data;

import java.util.Date;

@Data
public class ProjectDTO
{
    private Long id;
    private String name;
    private String description;
    private Date createdAt;
    private Date lastUpdatedAt;

    public static ProjectDTO fromEntity(Project p)
    {
        ProjectDTO d = new ProjectDTO();
        d.id = p.getId();
        d.name = p.getName();
        d.description = p.getDescription();
        d.createdAt = p.getCreatedAt();
        d.lastUpdatedAt = p.getLastUpdatedAt();
        return d;
    }


}
