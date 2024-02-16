package it.epicode.w6d5.devices_management.controllers;

import com.cloudinary.Cloudinary;
import it.epicode.w6d5.devices_management.Models.entities.Device;
import it.epicode.w6d5.devices_management.Models.entities.Employee;
import it.epicode.w6d5.devices_management.Models.reqDTO.AssignDeviceToEmployeeDTO;
import it.epicode.w6d5.devices_management.Models.reqDTO.DeviceDTO;
import it.epicode.w6d5.devices_management.Models.reqDTO.EmployeeDTO;
import it.epicode.w6d5.devices_management.Models.resDTO.DeleteRes;
import it.epicode.w6d5.devices_management.exceptions.BadRequestException;
import it.epicode.w6d5.devices_management.exceptions.NotFoundException;
import it.epicode.w6d5.devices_management.exceptions.ValidationMessages;
import it.epicode.w6d5.devices_management.services.DeviceService;
import it.epicode.w6d5.devices_management.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@RestController
public class DeviceController {
    @Autowired
    private DeviceService deviceSvc;


    @GetMapping("/devices")
    public Page<Device> getEmployees(Pageable pageable, @RequestParam(required = false) UUID employeeId) {
        if (employeeId == null || employeeId.toString().trim().isEmpty()) {
            return deviceSvc.getAll(pageable);
        }
        return deviceSvc.getByEmployeeId(pageable, employeeId);
    }

    @GetMapping("/devices/{id}")
    public Device getById(@PathVariable UUID id) throws NotFoundException {
        return deviceSvc.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/devices")
    public Device create(@RequestBody @Validated DeviceDTO deviceDTO, BindingResult validation) throws BadRequestException {
        if (validation.hasErrors())
            throw new BadRequestException(ValidationMessages.generateValidationErrorMessage(validation));
        return deviceSvc.create(deviceDTO);
    }

    @PutMapping("/devices/{id}")
    public Device update(@RequestBody @Validated DeviceDTO deviceDTO, @PathVariable UUID id, BindingResult validation) throws BadRequestException {
        if (validation.hasErrors())
            throw new BadRequestException(ValidationMessages.generateValidationErrorMessage(validation));
        return deviceSvc.update(deviceDTO, id);
    }

    /* NON è possibile fare una chiamata patch con body vuoto, quindi almeno un valore deve essere passato via body,
        altrimenti andrebbe usato il metodo get che però è incoerente con il tipo di operazione
     */
    @PatchMapping("/devices/{id}/assign-employee")
    public Device assignDevicetoEmployee(@RequestBody AssignDeviceToEmployeeDTO assignDeviceToEmployeeDTO, @PathVariable UUID id) throws BadRequestException {
        try {
            return deviceSvc.assignDeviceToEmployee(UUID.fromString(assignDeviceToEmployeeDTO.employeeId()), id);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("'employeeId' field is malformed since it doesn't respect the Universal Unique ID pattern");
        }
    }

    @DeleteMapping("/devices/{id}")
    public DeleteRes delete(@PathVariable UUID id) throws BadRequestException {
        return deviceSvc.delete(id);
    }

}
