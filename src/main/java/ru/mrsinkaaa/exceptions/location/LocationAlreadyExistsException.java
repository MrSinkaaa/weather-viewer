package ru.mrsinkaaa.exceptions.location;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class LocationAlreadyExistsException extends ApplicationException {

    public LocationAlreadyExistsException() {
        super(ErrorMessage.LOCATION_ALREADY_EXISTS);
    }
}
