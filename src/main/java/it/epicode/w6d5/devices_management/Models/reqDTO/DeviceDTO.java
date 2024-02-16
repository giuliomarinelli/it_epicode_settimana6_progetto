package it.epicode.w6d5.devices_management.Models.reqDTO;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DeviceDTO(
        @NotNull
        Boolean available,
        @NotNull
        Boolean underMaintenance,
        @NotNull
        Boolean neglected,
        @Pattern(regexp = "^[A-Z]+(?:_[A-Z]+)*$", message = "Malformed 'type' field, allowed exact-match values are SMARTPHONE, TABLET, LAPTOP, DOMOTIC_DEVICE, " +
                "DIGITAL_CAMERA, SMART_CARD, DESKTOP_COMPUTER, TV, OTHERS")
        @Size(min = 2, max = 16, message = "Malformed 'type' field, allowed exact-match values are SMARTPHONE, TABLET, LAPTOP, DOMOTIC_DEVICE, " +
                "DIGITAL_CAMERA, SMART_CARD, DESKTOP_COMPUTER, TV, OTHERS")
        String type
) {}


