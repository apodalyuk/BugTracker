package BugTracker.service;

public class ExceptionFactory
{
    public static ResourceNotFoundException projectNotFound(Long id)
    {
        return new ResourceNotFoundException("Project " + id + " not found");
    }

    public static ResourceNotFoundException taskNotFound(Long id)
    {
        return new ResourceNotFoundException("Task " + id + " not found");
    }

    public static ModificationForbiddenException taskClosed(Long id)
    {
        return new ModificationForbiddenException("Task " + id + " is already closed, unable to modify");
    }
}
