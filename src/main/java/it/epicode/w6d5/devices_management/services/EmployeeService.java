package it.epicode.w6d5.devices_management.services;

import it.epicode.w6d5.devices_management.Models.entities.Employee;
import it.epicode.w6d5.devices_management.Models.reqDTO.EmployeeDTO;
import it.epicode.w6d5.devices_management.Models.resDTO.DeleteRes;
import it.epicode.w6d5.devices_management.exceptions.BadRequestException;
import it.epicode.w6d5.devices_management.exceptions.NotFoundException;
import it.epicode.w6d5.devices_management.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRp;

    public Page<Employee> getAll(Pageable pageable) {
        return employeeRp.findAll(pageable);
    }

    public Employee findById(UUID id) throws NotFoundException {
        return employeeRp.findById(id).orElseThrow(
                () -> new NotFoundException("employee with id='" + id + "' not found")
        );
    }

    public Employee create(EmployeeDTO employeeDTO) throws BadRequestException {
        Employee e = new Employee(employeeDTO.username(), employeeDTO.firstName(), employeeDTO.lastname(), employeeDTO.email());
        try {
            return employeeRp.save(e);
        } catch (DataIntegrityViolationException exception) {
            throw new BadRequestException("'email' field sent already exists. Cannot create");
        }
    }

    public Employee update(EmployeeDTO employeeDTO, UUID id) throws BadRequestException {
        Employee employee = employeeRp.findById(id).orElseThrow(
                () -> new BadRequestException("employee with id='" + id + "' doesn't exist. cannot update")
        );
        employee.setUsername(employeeDTO.username());
        employee.setFirstName(employeeDTO.firstName());
        employee.setLastName(employeeDTO.lastname());
        employee.setEmail(employeeDTO.email());
        try {
            return employeeRp.save(employee);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("'email' field sent already exists. Cannot update");
        }
    }

    public Employee updateAfterUpload(Employee employee, String url) {
        employee.setProfilePictureUrl(url);
        return employeeRp.save(employee);
    }

    public DeleteRes delete(UUID id) throws BadRequestException {
        Employee employee = employeeRp.findById(id).orElseThrow(
                () -> new BadRequestException("employee with id='" + id + "' doesn't exist. cannot delete")
        );
        employeeRp.delete(employee);
        return new DeleteRes("employee with id='" + id + "' has been correctly deleted");
    }


}
