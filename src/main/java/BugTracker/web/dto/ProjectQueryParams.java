package BugTracker.web.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProjectQueryParams
{
    @Min(1)
    @NotNull
    private Integer page = 1;

    @Min(1)
    @Max(100)
    @NotNull
    private Integer perPage = 20;

    public Pageable toPageable()
    {
        return PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.DESC, "id"));
    }
}
