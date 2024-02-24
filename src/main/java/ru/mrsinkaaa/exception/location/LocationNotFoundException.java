package ru.mrsinkaaa.exception.location;

import ru.mrsinkaaa.exception.ApplicationException;
import ru.mrsinkaaa.exception.ErrorMessage;

public class LocationNotFoundException extends ApplicationException {

    public LocationNotFoundException() {
        super(ErrorMessage.LOCATION_NOT_FOUND);
    }
}
