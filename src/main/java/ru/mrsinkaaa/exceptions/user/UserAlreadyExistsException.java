package ru.mrsinkaaa.exceptions.user;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class UserAlreadyExistsException extends ApplicationException {

    public UserAlreadyExistsException() {
        super(ErrorMessage.USER_ALREADY_EXISTS);
    }
}
