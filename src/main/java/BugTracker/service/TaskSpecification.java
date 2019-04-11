package BugTracker.service;

import BugTracker.Utils;
import BugTracker.db.Task;
import BugTracker.web.dto.TaskQueryParams;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Класс для формирования критериев запросов в БД на основании {@link TaskQueryParams}
 */
public class TaskSpecification implements Specification<Task>
{

    private TaskQueryParams params;
    private Long projectId;

    public TaskSpecification(TaskQueryParams params, Long projectId)
    {
        this.params = params;
        this.projectId = projectId;
    }

    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("projectId"), projectId));
        if (params.getMinDate() != null)
        {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), Utils.convertToDate(params.getMinDate())));
        }
        if (params.getMaxDate() != null)
        {
            //Чтобы найти "по дату включительно", ищем таймстамп меньше (даты +1 день)
            Date maxDate = Utils.convertToDate(params.getMaxDate().plus(1, ChronoUnit.DAYS));
            predicates.add(cb.lessThan(root.get("createdAt"), maxDate));
        }
        if (params.getMinPriority() != null)
        {
            predicates.add(cb.greaterThanOrEqualTo(root.get("priority"), params.getMinPriority()));
        }
        if (params.getMaxPriority() != null)
        {
            predicates.add(cb.lessThanOrEqualTo(root.get("priority"), params.getMaxPriority()));
        }
        if (params.getStatus() != null)
        {
            predicates.add(cb.equal(root.get("status"), params.getStatus()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));

    }
}
