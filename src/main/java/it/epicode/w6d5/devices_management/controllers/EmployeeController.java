package it.epicode.w6d5.devices_management.controllers;

import com.cloudinary.Cloudinary;
import it.epicode.w6d5.devices_management.Models.entities.Employee;
import it.epicode.w6d5.devices_management.Models.reqDTO.EmployeeDTO;
import it.epicode.w6d5.devices_management.Models.resDTO.DeleteRes;
import it.epicode.w6d5.devices_management.exceptions.BadRequestException;
import it.epicode.w6d5.devices_management.exceptions.InternalServerErrorException;
import it.epicode.w6d5.devices_management.exceptions.NotFoundException;
import it.epicode.w6d5.devices_management.exceptions.ValidationMessages;
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
public class EmployeeController {

    @Autowired
    private EmployeeService employeeSvc;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/employees")
    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeSvc.getAll(pageable);
    }

    @GetMapping("/employees/{id}")
    public Employee getById(@PathVariable UUID id) throws NotFoundException {
        return employeeSvc.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/employees")
    public Employee create(@RequestBody @Validated EmployeeDTO employeeDTO, BindingResult validation) throws BadRequestException, InternalServerErrorException {
        if (validation.hasErrors())
            throw new BadRequestException(ValidationMessages.generateValidationErrorMessage(validation));
        return employeeSvc.create(employeeDTO);
    }

    @PutMapping("/employees/{id}")
    public Employee update(@RequestBody @Validated EmployeeDTO employeeDTO, @PathVariable UUID id, BindingResult validation) throws BadRequestException, InternalServerErrorException {
        if (validation.hasErrors())
            throw new BadRequestException(ValidationMessages.generateValidationErrorMessage(validation));
        return employeeSvc.update(employeeDTO, id);
    }

    @PatchMapping("/employees/{id}/upload-profile-picture")
    public Employee upload(@PathVariable UUID id, @RequestParam("file") MultipartFile file) throws IOException, NotFoundException {
        Employee employee = employeeSvc.findById(id);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), new HashMap()).get("url");
        System.out.println(file.getName());
        return employeeSvc.updateAfterUpload(employee, url);
    }

    @DeleteMapping("/employees/{id}")
    public DeleteRes delete(@PathVariable UUID id) throws BadRequestException, InternalServerErrorException {
        return employeeSvc.delete(id);
    }


}
