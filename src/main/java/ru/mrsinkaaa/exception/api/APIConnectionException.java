package ru.mrsinkaaa.exception.api;

import ru.mrsinkaaa.exception.ApplicationException;
import ru.mrsinkaaa.exception.ErrorMessage;

public class APIConnectionException extends ApplicationException {

    public APIConnectionException() {
        super(ErrorMessage.API_CONNECTION_ERROR);
    }
}
