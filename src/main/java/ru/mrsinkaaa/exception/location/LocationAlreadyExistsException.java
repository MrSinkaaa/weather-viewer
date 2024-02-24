package ru.mrsinkaaa.exception.location;

import ru.mrsinkaaa.exception.ApplicationException;
import ru.mrsinkaaa.exception.ErrorMessage;

public class LocationAlreadyExistsException extends ApplicationException {

    public LocationAlreadyExistsException() {
        super(ErrorMessage.LOCATION_ALREADY_EXISTS);
    }
}
