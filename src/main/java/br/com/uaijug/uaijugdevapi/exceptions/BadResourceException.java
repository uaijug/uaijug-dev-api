package br.com.uaijug.uaijugdevapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Recurso Mal Formatado")
public class BadResourceException extends Exception {

    public BadResourceException(String message) {
        super(message);
    }

    public void addErrorMessage(String message) {
        new BadResourceException(message);
    }
}
