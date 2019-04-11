package BugTracker.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "project")
public class Project
{
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    @NotBlank
    private String name;

    @Getter
    @Setter
    @Column
    private String description;

    @Getter
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Getter
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt = new Date();

    @Getter
    @OneToMany(mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Task> tasks = new ArrayList<Task>();

    public Project()
    {
    }

    public Project(@NotBlank String name)
    {
        this.name = name;
    }

    public void addTask(Task task)
    {
        tasks.add(task);
        task.setProject(this);
    }

    public void addTasks(Collection<Task> tasks)
    {
        for (Task t : tasks)
        {
            addTask(t);
        }
    }

    @PreUpdate
    public void setLastUpdate()
    {
        this.lastUpdatedAt = new Date();
    }
}
