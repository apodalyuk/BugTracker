package BugTracker.web.dto;

import BugTracker.db.Status;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Класс, в который мапятся параметры Get-запроса к задачам.
 * Пагинация и сортировка включены сюда же.
 */
@Data
public class TaskQueryParams
{
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate minDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate maxDate;

    private Integer minPriority;

    private Integer maxPriority;

    private Status status;

    @Min(1)
    @NotNull
    private Integer page = 1;

    @Min(value = 1)
    @Max(value = 100)
    @NotNull
    private Integer perPage = 20;

    @NotNull
    @Setter(AccessLevel.NONE)
    private TaskSort sort = TaskSort.DATE_DESC;

    public void setSort(String sort)
    {
        this.sort = TaskSort.get(sort);
    }

    public Pageable toPageable()
    {
        return PageRequest.of(page - 1, perPage, sort.toSort());
    }

    /**
     * Enum для вариантов сортировки
     */
    public enum TaskSort
    {
        DATE_ASC("createdAt", ASC),
        DATE_DESC("createdAt", DESC),
        PRIORITY_ASC("priority", ASC),
        PRIORITY_DESC("priority", DESC);

        private final String field;
        private final Sort.Direction dir;

        TaskSort(String field, Sort.Direction dir)
        {
            this.field = field;
            this.dir = dir;
        }

        public Sort toSort()
        {
            return Sort.by(dir, field);
        }

        /**
         * Позволяем распарсить enum из строкового параметра с любыми разделителями
         * и в любом регистре
         * "Date_Asc", "dateasc" и "date:ASC" будут работать одинаково
         *
         * @param value строка с направлением сортировки
         * @return соответствующий экземпляр enum
         * @throws IllegalArgumentException если строку не удаётся разобрать
         */
        public static TaskSort get(String value)
        {
            switch (value.toUpperCase().replaceAll("[^A-Z]+", ""))
            {
                case "DATEASC":
                    return DATE_ASC;
                case "DATEDESC":
                    return DATE_DESC;
                case "PRIORITYASC":
                    return PRIORITY_ASC;
                case "PRIORITYDESC":
                    return PRIORITY_DESC;
            }
            throw new IllegalArgumentException("Unknown sort value:" + value);
        }

    }


}
