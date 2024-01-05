package ru.mrsinkaaa.exceptions.api;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class APIResponseException extends ApplicationException {

    public APIResponseException() {
        super(ErrorMessage.API_RESPONSE_ERROR);
    }
}
