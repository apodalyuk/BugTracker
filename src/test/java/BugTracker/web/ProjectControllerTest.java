package BugTracker.web;

import BugTracker.AppRunner;
import BugTracker.TestUtils;
import BugTracker.service.ProjectService;
import BugTracker.web.dto.ProjectDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class ProjectControllerTest
{
    private MockMvc mockMvc;

    @MockBean
    private ProjectService serviceMock;

    @Before
    public void setUp()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProjectController(serviceMock)).build();
    }

    @Test
    public void shouldGetList() throws Exception
    {
        mockMvc.perform(get("/projects/"))
               .andExpect(status().isOk());
        verify(serviceMock).find(any());
    }

    @Test
    public void shouldGet() throws Exception
    {
        ProjectDTO project = new ProjectDTO();
        project.setId(1L);
        when(serviceMock.get(project.getId())).thenReturn(Optional.of(project));

        mockMvc.perform(get("/projects/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(project.getId()));
        verify(serviceMock).get(1L);

        mockMvc.perform(get("/projects/2"))
               .andExpect(status().isNotFound());
        verify(serviceMock).get(2L);
    }

    @Test
    public void shouldPost() throws Exception
    {
        ProjectDTO project = new ProjectDTO();
        project.setId(1L);
        when(serviceMock.create(any())).thenReturn(project);

        ProjectDTO post = new ProjectDTO();
        post.setName("name");

        mockMvc.perform(post("/projects/").contentType(MediaType.APPLICATION_JSON)
                                          .content(TestUtils.asJsonString(post)))
               .andExpect(status().isCreated())
               .andExpect(redirectedUrlPattern("http://*/projects/1"));
        verify(serviceMock).create(any());
    }

    @Test
    public void shouldPatch() throws Exception
    {
        ProjectDTO project = new ProjectDTO();
        project.setId(1L);
        when(serviceMock.update(eq(project.getId()), any())).thenReturn(Optional.of(project));

        mockMvc.perform(patch("/projects/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
               .andExpect(status().isOk());
        verify(serviceMock).update(eq(1L), any());

        mockMvc.perform(patch("/projects/2").contentType(MediaType.APPLICATION_JSON).content("{}"))
               .andExpect(status().isNotFound());
        verify(serviceMock).update(eq(2L), any());
    }

    @Test
    public void shouldDelete() throws Exception
    {
        mockMvc.perform(delete("/projects/1"))
               .andExpect(status().isOk());
        verify(serviceMock).delete(1L);
    }


}