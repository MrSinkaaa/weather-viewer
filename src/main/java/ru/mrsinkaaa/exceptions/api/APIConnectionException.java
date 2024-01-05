package ru.mrsinkaaa.exceptions.api;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class APIConnectionException extends ApplicationException {

    public APIConnectionException() {
        super(ErrorMessage.API_CONNECTION_ERROR);
    }
}
