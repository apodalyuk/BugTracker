package BugTracker.web.dto;

import lombok.Data;

@Data
public class ProjectPatchDTO
{
    private String name;
    private String description;
    private boolean nameDirty;
    private boolean descriptionDirty;

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
}
