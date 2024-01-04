package ru.mrsinkaaa.exceptions.user;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class UserInputException extends ApplicationException {
    public UserInputException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
