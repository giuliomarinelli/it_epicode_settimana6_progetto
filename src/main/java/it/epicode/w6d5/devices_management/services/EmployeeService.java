package it.epicode.w6d5.devices_management.services;

import it.epicode.w6d5.devices_management.Models.entities.Employee;
import it.epicode.w6d5.devices_management.Models.reqDTO.EmployeeDTO;
import it.epicode.w6d5.devices_management.Models.resDTO.DeleteRes;
import it.epicode.w6d5.devices_management.exceptions.BadRequestException;
import it.epicode.w6d5.devices_management.exceptions.InternalServerErrorException;
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

    @Autowired
    private EmailService emailSvc;

    public Page<Employee> getAll(Pageable pageable) {
        return employeeRp.findAll(pageable);
    }

    public Employee findById(UUID id) throws NotFoundException {
        return employeeRp.findById(id).orElseThrow(
                () -> new NotFoundException("employee with id = '" + id + "' not found")
        );
    }

    public Employee create(EmployeeDTO employeeDTO) throws BadRequestException, InternalServerErrorException {
        Employee e = new Employee(employeeDTO.username(), employeeDTO.firstName(), employeeDTO.lastName(), employeeDTO.email());
        try {
            employeeRp.save(e);
            emailSvc.sendEmail(e.getEmail(), "Welcome in Devices Management API",
                    "Hello " + e.getFirstName() +
                            "\n\nYour employee resource creation (id = '" + e.getId() + "') was performed successfully.\n\n" +
                            "Thank you for joining!\n\nAdmin"
            );
            return e;
        } catch (DataIntegrityViolationException exception) {
            if (employeeRp.getAllEmails().contains(e.getEmail()))
                throw new BadRequestException("'email' field sent already exists, cannot create");
            if (employeeRp.getAllUsernames().contains(e.getUsername()))
                throw new BadRequestException("'username' field sent already exists, cannot create");
            throw new InternalServerErrorException("Data integrity violation. " + exception.getMessage());
        }
    }

    public Employee update(EmployeeDTO employeeDTO, UUID id) throws BadRequestException, InternalServerErrorException {
        Employee employee = employeeRp.findById(id).orElseThrow(
                () -> new BadRequestException("employee with id = '" + id + "' doesn't exist, cannot update")
        );
        employee.setUsername(employeeDTO.username());
        employee.setFirstName(employeeDTO.firstName());
        employee.setLastName(employeeDTO.lastName());
        employee.setEmail(employeeDTO.email());
        try {
            employeeRp.save(employee);
            emailSvc.sendEmail(employee.getEmail(), "Your resource has been updated successfully",
                    "Hello " + employee.getFirstName() +
                            "\n\nYour employee resource update (id = '" + id + "') was performed successfully.\n\n" +
                            "Have a nice day!\n\nAdmin"
            );
            return employee;
        } catch (DataIntegrityViolationException e) {
            if (employeeRp.getAllEmails().contains(employee.getEmail()) || employeeRp.getAllUsernames().contains(employee.getUsername()))
                throw new BadRequestException("'email' or 'username' fields sent already exist, cannot update");
            throw new InternalServerErrorException("Data integrity violation. " + e.getMessage());
        }
    }

    public Employee updateAfterUpload(Employee employee, String url) {
        employee.setProfilePictureUrl(url);
        employeeRp.save(employee);
        emailSvc.sendEmail(employee.getEmail(), "Your resource has been updated successfully",
                "Hello " + employee.getFirstName() +
                        "\n\nYour employee resource's (id = '" + employee.getId() + "') profile picture was uploaded successfully.\n\n" +
                        "Have a nice day!\n\nAdmin"
        );
        return employee;
    }

    public DeleteRes delete(UUID id) throws BadRequestException, InternalServerErrorException {
        Employee employee = employeeRp.findById(id).orElseThrow(
                () -> new BadRequestException("employee with id = '" + id + "' doesn't exist, cannot delete")
        );
        try {
            employeeRp.delete(employee);
            emailSvc.sendEmail(employee.getEmail(), "Your resource has been deleted successfully",
                    "Hello " + employee.getFirstName() +
                            "\n\nYour employee resource deletion (id = '" + id + "') was performed successfully.\n\n" +
                            "Have a nice day!\n\nAdmin"
            );
        } catch (DataIntegrityViolationException e) {
            if (!employee.getDevices().isEmpty())
                throw new BadRequestException("employee you are trying to delete is referenced by one or more devices," +
                        " please delete all referencing devices before deleting employee; you can find all devices " +
                        "assigned to an employee from '/devices?employeeId=<value for employee's id>'");
            throw new InternalServerErrorException("Data integrity violation. " + e.getMessage());
        }
        return new DeleteRes("employee with id = '" + id + "' has been correctly deleted");
    }


}
