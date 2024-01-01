package ru.mrsinkaaa.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    USER_ALREADY_EXISTS("User with this login already exists", HttpServletResponse.SC_CONFLICT),
    USER_WRONG_CREDENTIALS("Login or password is not corrected", HttpServletResponse.SC_UNAUTHORIZED),
    USER_NOT_AUTHORIZED("User is not authorized", HttpServletResponse.SC_FORBIDDEN),

    LOCATION_ALREADY_EXISTS("Location with this name already exists", HttpServletResponse.SC_CONFLICT),
    LOCATION_NOT_FOUND("Location with this name not found", HttpServletResponse.SC_NOT_FOUND);

    private final String message;
    private final int status;


}
