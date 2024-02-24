package ru.mrsinkaaa.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mrsinkaaa.config.AppConfig;

import javax.servlet.http.HttpServletResponse;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    USER_ALREADY_EXISTS("User with this login already exists", HttpServletResponse.SC_CONFLICT),
    USER_WRONG_CREDENTIALS("Login or password is not corrected", HttpServletResponse.SC_UNAUTHORIZED),
    USER_WRONG_LOGIN("Login length is less than " + AppConfig.getProperty("login.length"), HttpServletResponse.SC_BAD_REQUEST),
    USER_WRONG_PASSWORD("Password length is less than " + AppConfig.getProperty("password.length"), HttpServletResponse.SC_BAD_REQUEST),
    USER_NOT_FOUND("User not found", HttpServletResponse.SC_NOT_FOUND),
    USER_NOT_AUTHORIZED("User is not authorized", HttpServletResponse.SC_FORBIDDEN),


    LOCATION_ALREADY_EXISTS("Location with this name already saved", HttpServletResponse.SC_CONFLICT),
    LOCATION_NOT_FOUND("Location with this name not found", HttpServletResponse.SC_NOT_FOUND),

    API_CONNECTION_ERROR("API connection error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
    API_RESPONSE_ERROR("API response error", HttpServletResponse.SC_BAD_REQUEST);

    private final String message;
    private final int status;
}
