package ru.mrsinkaaa.exceptions.location;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class LocationNotFoundException extends ApplicationException {

    public LocationNotFoundException() {
        super(ErrorMessage.LOCATION_NOT_FOUND);
    }
}
