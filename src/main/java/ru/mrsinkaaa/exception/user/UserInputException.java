package ru.mrsinkaaa.exception.user;

import ru.mrsinkaaa.exception.ApplicationException;
import ru.mrsinkaaa.exception.ErrorMessage;

public class UserInputException extends ApplicationException {
    public UserInputException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
