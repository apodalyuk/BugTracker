package BugTracker.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Task
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
    @Setter
    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @Getter
    @Setter
    @Column
    @NotNull
    @Min(value = 1)
    private Integer priority;

    @Getter
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Getter
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt = new Date();

    @Getter
    @Column(name = "project_id", insertable = false, updatable = false)
    private Long projectId;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public Task()
    {
    }

    public Task(@NotBlank String name, @NotNull Status status, @NotNull @Min(value = 1) Integer priority)
    {
        this.name = name;
        this.status = status;
        this.priority = priority;
    }

    @PreUpdate
    public void setLastUpdate()
    {
        this.lastUpdatedAt = new Date();
    }
}
