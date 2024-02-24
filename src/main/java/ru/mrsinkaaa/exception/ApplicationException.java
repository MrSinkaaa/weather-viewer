package ru.mrsinkaaa.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApplicationException extends RuntimeException {

    private final ErrorMessage errorMessage;


}
