package ru.mrsinkaaa.exception.user;

import ru.mrsinkaaa.exception.ApplicationException;
import ru.mrsinkaaa.exception.ErrorMessage;

public class UserNotAuthorizedException extends ApplicationException {

    public UserNotAuthorizedException() {
        super(ErrorMessage.USER_NOT_AUTHORIZED);
    }
}
