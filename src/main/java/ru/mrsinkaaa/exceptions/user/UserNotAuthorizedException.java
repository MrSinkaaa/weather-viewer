package ru.mrsinkaaa.exceptions.user;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class UserNotAuthorizedException extends ApplicationException {

    public UserNotAuthorizedException() {
        super(ErrorMessage.USER_NOT_AUTHORIZED);
    }
}
