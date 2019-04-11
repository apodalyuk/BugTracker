package BugTracker.web;

import BugTracker.db.Status;
import BugTracker.web.dto.TaskQueryParams;

import java.time.LocalDate;

public final class TaskQueryParamsBuilder
{
    private TaskQueryParams taskQueryParams;

    private TaskQueryParamsBuilder() { taskQueryParams = new TaskQueryParams(); }

    public static TaskQueryParamsBuilder getInstance() { return new TaskQueryParamsBuilder(); }

    public TaskQueryParamsBuilder reset()
    {
        taskQueryParams = new TaskQueryParams();
        return this;
    }

    public TaskQueryParamsBuilder withMinDate(LocalDate minDate)
    {
        taskQueryParams.setMinDate(minDate);
        return this;
    }

    public TaskQueryParamsBuilder withMaxDate(LocalDate maxDate)
    {
        taskQueryParams.setMaxDate(maxDate);
        return this;
    }

    public TaskQueryParamsBuilder withMinPriority(Integer minPriority)
    {
        taskQueryParams.setMinPriority(minPriority);
        return this;
    }

    public TaskQueryParamsBuilder withMaxPriority(Integer maxPriority)
    {
        taskQueryParams.setMaxPriority(maxPriority);
        return this;
    }

    public TaskQueryParamsBuilder withStatus(Status status)
    {
        taskQueryParams.setStatus(status);
        return this;
    }

    public TaskQueryParamsBuilder withPage(Integer page)
    {
        taskQueryParams.setPage(page);
        return this;
    }

    public TaskQueryParamsBuilder withPerPage(Integer perPage)
    {
        taskQueryParams.setPerPage(perPage);
        return this;
    }

    public TaskQueryParamsBuilder withSort(String sort)
    {
        taskQueryParams.setSort(sort);
        return this;
    }

    public TaskQueryParams build() { return taskQueryParams; }
}
