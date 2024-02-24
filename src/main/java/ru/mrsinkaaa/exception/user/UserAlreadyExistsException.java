package ru.mrsinkaaa.exception.user;

import ru.mrsinkaaa.exception.ApplicationException;
import ru.mrsinkaaa.exception.ErrorMessage;

public class UserAlreadyExistsException extends ApplicationException {

    public UserAlreadyExistsException() {
        super(ErrorMessage.USER_ALREADY_EXISTS);
    }
}
