package BugTracker.web;

import BugTracker.TestUtils;
import BugTracker.db.Status;
import BugTracker.service.TaskService;
import BugTracker.web.dto.TaskDTO;
import BugTracker.web.dto.TaskQueryParams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class TaskControllerTest
{

    private MockMvc mockMvc;

    @MockBean
    private TaskService serviceMock;

    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(serviceMock)).build();
    }

    @Test
    public void shouldGetList() throws Exception
    {
        mockMvc.perform(get("/projects/1/tasks/"))
               .andExpect(status().isOk());
        verify(serviceMock).find(eq(1L), any());
    }

    @Test
    public void shouldGet() throws Exception
    {
        TaskDTO task = new TaskDTO();
        task.setId(1L);
        task.setProjectId(1L);
        when(serviceMock.get(1L, 1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/projects/1/tasks/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(task.getId()));
        verify(serviceMock).get(1L, 1L);

        mockMvc.perform(get("/projects/2/tasks/1"))
               .andExpect(status().isNotFound());
        verify(serviceMock).get(2L, 1L);

        mockMvc.perform(get("/projects/1/tasks/2"))
               .andExpect(status().isNotFound());
        verify(serviceMock).get(1L, 2L);
    }

    @Test
    public void shouldGetListWithParams() throws Exception
    {
        ArgumentCaptor<TaskQueryParams> paramCaptor = ArgumentCaptor.forClass(TaskQueryParams.class);
        mockMvc.perform(get("/projects/1/tasks/?page=1&perPage=2" +
                "&sort=date:desc&minDate=2001-02-03&maxDate=2004-05-06" +
                "&minPriority=10&maxPriority=100&status=IN_PROGRESS"))
               .andExpect(status().isOk());
        verify(serviceMock).find(eq(1L), paramCaptor.capture());
        TaskQueryParams params = paramCaptor.getValue();
        Assert.assertEquals("Параметр page", Integer.valueOf(1), params.getPage());
        Assert.assertEquals("Параметр perPage", Integer.valueOf(2), params.getPerPage());
        Assert.assertEquals("Параметр sort", TaskQueryParams.TaskSort.DATE_DESC, params.getSort());
        Assert.assertEquals("Параметр minDate", LocalDate.of(2001,02,03), params.getMinDate());
        Assert.assertEquals("Параметр maxDate", LocalDate.of(2004,05,06), params.getMaxDate());
        Assert.assertEquals("Параметр minPriority", Integer.valueOf(10), params.getMinPriority());
        Assert.assertEquals("Параметр maxPriority", Integer.valueOf(100), params.getMaxPriority());
        Assert.assertEquals("Параметр status", Status.IN_PROGRESS, params.getStatus());
    }

    @Test
    public void shouldPost() throws Exception
    {
        TaskDTO task = new TaskDTO();
        task.setId(1L);
        task.setProjectId(1L);
        when(serviceMock.create(any(), any())).thenReturn(task);

        TaskDTO post = new TaskDTO();
        post.setName("name");
        post.setPriority(1);
        post.setStatus(Status.NEW);

        mockMvc.perform(post("/projects/1/tasks/").contentType(MediaType.APPLICATION_JSON)
                                                  .content(TestUtils.asJsonString(post)))
               .andExpect(status().isCreated())
               .andExpect(redirectedUrlPattern("http://*/projects/1/tasks/1"));
        verify(serviceMock).create(eq(1L), any());
    }

    @Test
    public void shouldPatch() throws Exception
    {
        TaskDTO task = new TaskDTO();
        task.setId(1L);
        task.setProjectId(1L);
        when(serviceMock.update(eq(task.getProjectId()), eq(task.getId()), any())).thenReturn(Optional.of(task));

        mockMvc.perform(patch("/projects/1/tasks/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
               .andExpect(status().isOk());
        verify(serviceMock).update(eq(1L), eq(1L), any());

        mockMvc.perform(patch("/projects/2/tasks/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
               .andExpect(status().isNotFound());
        verify(serviceMock).update(eq(2L), eq(1L), any());

        mockMvc.perform(patch("/projects/1/tasks/2").contentType(MediaType.APPLICATION_JSON).content("{}"))
               .andExpect(status().isNotFound());
        verify(serviceMock).update(eq(1L), eq(2L), any());
    }

    @Test
    public void shouldDelete() throws Exception
    {
        mockMvc.perform(delete("/projects/1/tasks/1"))
               .andExpect(status().isOk());
        verify(serviceMock).delete(1L, 1L);
    }
}