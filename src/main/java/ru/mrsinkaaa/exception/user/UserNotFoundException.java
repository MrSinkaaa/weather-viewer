package ru.mrsinkaaa.exception.user;

import ru.mrsinkaaa.exception.ApplicationException;
import ru.mrsinkaaa.exception.ErrorMessage;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException() {
        super(ErrorMessage.USER_NOT_FOUND);
    }
}
