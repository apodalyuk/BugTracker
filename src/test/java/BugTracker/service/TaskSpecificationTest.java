package BugTracker.service;

import BugTracker.AppRunner;
import BugTracker.db.*;
import BugTracker.web.dto.TaskQueryParams;
import BugTracker.web.TaskQueryParamsBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppRunner.class)
@Transactional
public class TaskSpecificationTest
{
    @Autowired
    private ProjectRepository projects;
    @Autowired
    private TaskRepository tasks;
    @PersistenceContext
    private EntityManager em;

    private Long projectId;

    @Before
    public void setUp()
    {
        Project p = new Project("Project");

        p.addTask(new Task("Task 1", Status.NEW, 1));
        p.addTask(new Task("Task 2", Status.IN_PROGRESS, 5));
        p.addTask(new Task("Task 3", Status.CLOSED, 10));
        p.addTask(new Task("Task 4", Status.IN_PROGRESS, 100));
        projectId = projects.save(p).getId();

        Query q = em.createQuery("update Task set createdAt = parsedatetime(:date, 'dd.MM.yyyy hh:mm:ss') where name = :name");
        q.setParameter("name", "Task 1")
         .setParameter("date", "01.01.2018 00:00:00")
         .executeUpdate();
        q.setParameter("name", "Task 2")
         .setParameter("date", "01.01.2018 10:00:00")
         .executeUpdate();
        q.setParameter("name", "Task 3")
         .setParameter("date", "02.10.2018 00:00:00")
         .executeUpdate();
        q.setParameter("name", "Task 4")
         .setParameter("date", "01.10.2018 23:59:59")
         .executeUpdate();

    }


    @Test
    public void testTaskSpecification() throws ParseException
    {
        //SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        TaskQueryParamsBuilder builder = TaskQueryParamsBuilder.getInstance();
        assertTasks("Priority: обе границы совпадают",
                doQuery(builder.reset()
                               .withMinPriority(1)
                               .withMaxPriority(10)
                               .build()),
                1, 2, 3);

        assertTasks("Priority: нижняя граница совпадает",
                doQuery(builder.reset()
                               .withMinPriority(1)
                               .withMaxPriority(9)
                               .build()),
                1, 2);

        assertTasks("Priority: верхняя граница совпадает",
                doQuery(builder.reset()
                               .withMinPriority(2)
                               .withMaxPriority(10)
                               .build()),
                2, 3);

        assertTasks("Priority: верхняя граница равна нижней",
                doQuery(builder.reset()
                               .withMinPriority(5)
                               .withMaxPriority(5)
                               .build()),
                2);

        assertTasks("Выбор по статусу",
                doQuery(builder.reset()
                               .withStatus(Status.IN_PROGRESS)
                               .build()),
                2, 4);

        assertTasks("Выбор по статусу и приоритету",
                doQuery(builder.reset()
                               .withStatus(Status.IN_PROGRESS)
                               .withMinPriority(6)
                               .build()),
                4);



        assertTasks("Выбор по датам",
                doQuery(builder.reset()
                               .withMinDate(LocalDate.of(2018,01,01))
                               .withMaxDate(LocalDate.of(2018,10,01))
                               .build()),
                1, 2, 4);

        assertTasksOrder("Сортировка по дате",
                doSort(builder.reset()
                              .withSort("date:asc")
                              .build()),
                1, 2, 4, 3);

        assertTasksOrder("Сортировка по дате c ограничением",
                doSort(builder.reset()
                              .withMinDate(LocalDate.of(2018,01,02))
                              .withSort("date:asc")
                              .build()),
                4, 3);


        assertTasksOrder("Сортировка по дате с пагинацией",
                doSort(builder.reset()
                              .withSort("date:desc")
                              .withPage(2)
                              .withPerPage(2)
                              .build()),
                2, 1);

        assertTasksOrder("Сортировка по приоритету",
                doSort(builder.reset()
                              .withSort("priority:desc")
                              .build()),
                4, 3, 2, 1);
    }

    private void assertTasks(String message, List<Task> actualTasks, int... tasknums)
    {
        Set<String> expected = IntStream.of(tasknums).mapToObj(x -> "Task " + x).collect(Collectors.toSet());
        Set<String> actual = actualTasks.stream().map(Task::getName).collect(Collectors.toSet());
        Assert.assertEquals(message, expected, actual);
    }

    private void assertTasksOrder(String message, List<Task> actualTasks, int... tasknums)
    {
        List<String> expected = IntStream.of(tasknums).mapToObj(x -> "Task " + x).collect(Collectors.toList());
        List<String> actual = actualTasks.stream().map(Task::getName).collect(Collectors.toList());
        Assert.assertEquals(message, expected, actual);
    }

    private List<Task> doQuery(TaskQueryParams params)
    {
        TaskSpecification spec = new TaskSpecification(params, projectId);
        return tasks.findAll(spec);
    }

    private List<Task> doSort(TaskQueryParams params)
    {
        TaskSpecification spec = new TaskSpecification(params, projectId);
        return tasks.findAll(spec, params.toPageable()).stream().collect(Collectors.toList());
    }


}