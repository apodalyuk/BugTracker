package BugTracker.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ModificationForbiddenException extends RuntimeException
{
    public ModificationForbiddenException(String message)
    {
        super(message);
    }
}
