package it.epicode.w6d5.devices_management.Models.reqDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AssignDeviceToEmployeeDTO
        (
                @NotNull(message = "employeeId is required")
                @NotBlank(message = "employeeId is required and must not be empty")
                @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$",
                        message = "'employeeId' field is malformed since it doesn't respect the Universal Unique ID pattern"
                )

                String employeeId
        ) {
}
