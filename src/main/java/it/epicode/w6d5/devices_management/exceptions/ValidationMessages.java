package it.epicode.w6d5.devices_management.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class ValidationMessages {
    public static String generateValidationErrorMessage(BindingResult validation) {
        return validation.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
    }
}
