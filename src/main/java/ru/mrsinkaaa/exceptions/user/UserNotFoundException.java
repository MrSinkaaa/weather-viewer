package ru.mrsinkaaa.exceptions.user;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException() {
        super(ErrorMessage.USER_NOT_FOUND);
    }
}
