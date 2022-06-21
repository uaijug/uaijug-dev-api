package br.com.uaijug.uaijugdevapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Recurso jã existente")
public class ResourceAlreadyExistsException extends Exception {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
