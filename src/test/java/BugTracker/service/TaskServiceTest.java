package BugTracker.service;

import BugTracker.db.*;
import BugTracker.web.dto.TaskDTO;
import BugTracker.web.dto.TaskPatchDTO;
import BugTracker.web.dto.TaskQueryParams;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TaskServiceTest
{
    @MockBean
    private ProjectRepository projectsMock;

    @MockBean
    private TaskRepository tasksMock;

    private TaskService service;

    @Before
    public void setUp() throws Exception
    {
        service = new TaskServiceImpl(tasksMock, projectsMock);
    }

    @Test
    public void find()
    {
        when(projectsMock.findById(1L)).thenReturn(Optional.of(new Project()));
        when(tasksMock.findAll(any(TaskSpecification.class), any(Pageable.class))).thenReturn(Page.empty());
        service.find(1L, new TaskQueryParams());
        verify(tasksMock).findAll(any(TaskSpecification.class), any(Pageable.class));
        verify(projectsMock).findById(1L);

        try
        {
            service.find(2L, new TaskQueryParams());
            fail("Должно бросить ResourceNotFoundException при неверном проекте");
        }
        catch (Exception ex)
        {
            assertEquals("Должно бросить ResourceNotFoundException при неверном проекте", ResourceNotFoundException.class, ex.getClass());
        }
        verify(projectsMock).findById(2L);
    }

    @Test
    public void get()
    {
        when(tasksMock.findById(1L)).thenReturn(Optional.ofNullable(getTask(1L, 1L)));
        service.get(1L, 1L);
        verify(tasksMock).findById(1L);

        when(tasksMock.findById(2L)).thenReturn(Optional.ofNullable(getTask(2L, 1L)));
        try
        {
            service.get(2L, 2L);
            fail();
        }
        catch (Exception ex)
        {
            assertEquals("Должно бросить ResourceNotFoundException при неверном проекте", ex.getClass(), ResourceNotFoundException.class);
        }
        verify(tasksMock).findById(2L);
    }

    @Test
    public void update()
    {
        TaskPatchDTO data = new TaskPatchDTO();
        data.setName("name");
        data.setDescription("descr");
        data.setStatus(Status.NEW);
        data.setPriority(1);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        when(tasksMock.findById(1L)).thenReturn(Optional.ofNullable(getTask(1L, 1L)));
        service.update(1L, 1L, data);
        verify(tasksMock).findById(1L);
        verify(tasksMock).save(taskCaptor.capture());

        Task task = taskCaptor.getValue();
        assertEquals("Name записано", task.getName(), data.getName());
        assertEquals("Description записано",task.getDescription(), data.getDescription());
        assertEquals("Status записано", task.getStatus(), data.getStatus());
        assertEquals("Priority записано", task.getPriority(), data.getPriority());

        when(tasksMock.findById(2L)).thenReturn(Optional.ofNullable(getTask(2L, 1L)));
        try
        {
            service.update(2L, 2L, data);
            fail("Должно бросить ResourceNotFoundException при неверном проекте");
        }
        catch (Exception ex)
        {
            assertEquals("Должно бросить ResourceNotFoundException при неверном проекте", ResourceNotFoundException.class, ex.getClass());
        }
        verify(tasksMock).findById(2L);


        Task closedTask = getTask(3L, 1L);
        closedTask.setStatus(Status.CLOSED);
        when(tasksMock.findById(3L)).thenReturn(Optional.ofNullable(closedTask));
        try
        {
            service.update(1L, 3L, data);
            fail("Должно бросить ModificationForbiddenException при закрытом таске");
        }
        catch (Exception ex)
        {
            assertEquals("Должно бросить ModificationForbiddenException при закрытом таске",  ModificationForbiddenException.class, ex.getClass());
        }
        verify(tasksMock).findById(3L);
    }

    @Test
    public void create()
    {
        TaskDTO data = new TaskDTO();
        data.setName("name");
        data.setDescription("descr");
        data.setPriority(1);

        when(tasksMock.save(any())).thenReturn(new Task());
        when(projectsMock.findById(1L)).thenReturn(Optional.of(new Project()));
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        service.create(1L, data);

        verify(projectsMock).findById(1L);
        verify(tasksMock).save(taskCaptor.capture());
        Task task = taskCaptor.getValue();
        assertEquals("Name записано", task.getName(), data.getName());
        assertEquals("Description записано",task.getDescription(), data.getDescription());
        assertEquals("Status записано", task.getStatus(), Status.NEW);
        assertEquals("Priority записано", task.getPriority(), data.getPriority());

        try
        {
            service.create(2L, data);
            fail("Должно бросить ResourceNotFoundException при неверном проекте");
        }
        catch (Exception ex)
        {
            assertEquals("Должно бросить ResourceNotFoundException при неверном проекте", ResourceNotFoundException.class, ex.getClass());
        }
        verify(projectsMock).findById(2L);

    }

    @Test
    public void delete()
    {
        when(tasksMock.findById(1L)).thenReturn(Optional.ofNullable(getTask(1L, 1L)));
        service.delete(1L, 1L);
        verify(tasksMock).findById(1L);
        verify(tasksMock).deleteById(1L);

        when(tasksMock.findById(2L)).thenReturn(Optional.ofNullable(getTask(2L, 1L)));
        try
        {
            service.delete(2L, 2L);
            fail();
        }
        catch (Exception ex)
        {
            assertEquals("Должно бросить ResourceNotFoundException при неверном проекте", ex.getClass(), ResourceNotFoundException.class);
        }
        verify(tasksMock).findById(2L);
    }

    private Task getTask(Long id, Long projectId)
    {
        Task task = new Task();
        ReflectionTestUtils.setField(task, "id", id);
        Project project = new Project();
        ReflectionTestUtils.setField(project, "id", projectId);
        project.addTask(task);
        return task;
    }

}