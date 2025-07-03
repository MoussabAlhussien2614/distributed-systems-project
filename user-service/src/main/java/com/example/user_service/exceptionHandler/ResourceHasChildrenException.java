package com.example.user_service.exceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceHasChildrenException extends RuntimeException{

    public ResourceHasChildrenException(String message) {
        super(message);
    }
}
