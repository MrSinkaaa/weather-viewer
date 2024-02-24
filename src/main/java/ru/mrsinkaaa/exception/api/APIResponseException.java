package ru.mrsinkaaa.exception.api;

import ru.mrsinkaaa.exception.ApplicationException;
import ru.mrsinkaaa.exception.ErrorMessage;

public class APIResponseException extends ApplicationException {

    public APIResponseException() {
        super(ErrorMessage.API_RESPONSE_ERROR);
    }
}
