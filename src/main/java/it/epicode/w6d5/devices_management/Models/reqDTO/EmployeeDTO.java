package it.epicode.w6d5.devices_management.Models.reqDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record EmployeeDTO(
        @NotNull(message = "'username' field is required'")
        @NotBlank(message = "'username' field cannot be empty'")
        @Pattern(regexp = "^\\S*$", message = "'username' field doesn't allow white spaces")
        String username,
        @NotNull(message = "'firstName' field is required'")
        @NotBlank(message = "'firstName' field cannot be empty'")
        String firstName,
        @NotNull(message = "'lastName' field is required'")
        @NotBlank(message = "'lastName' field cannot be empty'")
        String lastName,
        @NotNull(message = "'email' field is required'")
        @NotBlank(message = "'email' field cannot be empty'")
        @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "malformed 'email' field")
        String email
) {}
