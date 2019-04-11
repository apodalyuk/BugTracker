package BugTracker.service;

import BugTracker.db.Project;
import BugTracker.db.ProjectRepository;
import BugTracker.web.dto.ProjectDTO;
import BugTracker.web.dto.ProjectPatchDTO;
import BugTracker.web.dto.ProjectQueryParams;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ProjectServiceTest
{

    @MockBean
    private ProjectRepository repoMock;

    private ProjectService service;

    @Before
    public void setUp() throws Exception
    {
        service = new ProjectServiceImpl(repoMock);
    }

    @Test
    public void find()
    {
        when(repoMock.findAll(any(Pageable.class))).thenReturn(Page.empty());
        service.find(new ProjectQueryParams());
        verify(repoMock).findAll(any(Pageable.class));
    }

    @Test
    public void get()
    {
        service.get(1L);
        verify(repoMock).findById(1L);
    }

    @Test
    public void update()
    {

        ProjectPatchDTO data = new ProjectPatchDTO();
        data.setName("name");
        data.setDescription("descr");

        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        when(repoMock.findById(1L)).thenReturn(Optional.of(new Project()));

        service.update(1L, data);

        verify(repoMock).findById(1L);
        verify(repoMock).save(projectCaptor.capture());
        Project p = projectCaptor.getValue();
        assertEquals("Name записано", p.getName(), data.getName());
        assertEquals("Description записано",p.getDescription(), data.getDescription());
    }

    @Test
    public void create()
    {
        ProjectDTO data = new ProjectDTO();
        data.setName("name");
        data.setDescription("descr");
        when(repoMock.save(any())).thenReturn(new Project());

        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);

        service.create(data);

        verify(repoMock).save(projectCaptor.capture());
        Project p = projectCaptor.getValue();
        assertEquals("Name записано", p.getName(), data.getName());
        assertEquals("Description записано",p.getDescription(), data.getDescription());
    }

    @Test
    public void delete()
    {
        service.delete(1L);
        verify(repoMock).deleteById(1L);
    }
}