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


    public Page<Employee> getAll(Pageable pageable) {
        return employeeRp.findAll(pageable);
    }

    public Employee findById(UUID id) throws NotFoundException {
        return employeeRp.findById(id).orElseThrow(
                () -> new NotFoundException("employee with id='" + id + "' not found")
        );
    }

    public Employee create(EmployeeDTO employeeDTO) throws BadRequestException, InternalServerErrorException {
        Employee e = new Employee(employeeDTO.username(), employeeDTO.firstName(), employeeDTO.lastName(), employeeDTO.email());
        try {
            return employeeRp.save(e);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMessage().contains("uk_j9xgmd0ya5jmus09o0b8pqrpb") || exception.getMessage().contains("email"))
                throw new BadRequestException("'email' field sent already exists. Cannot create");
            else if (exception.getMessage().contains("uk_3gqbimdf7fckjbwt1kcud141m") || exception.getMessage().contains("username"))
                throw new BadRequestException("'username' field sent already exists. Cannot create");
            throw new InternalServerErrorException("A non-specific factor causes a violation of data integrity. Cannot create");
        }
    }

    public Employee update(EmployeeDTO employeeDTO, UUID id) throws BadRequestException, InternalServerErrorException {
        Employee employee = employeeRp.findById(id).orElseThrow(
                () -> new BadRequestException("employee with id='" + id + "' doesn't exist. cannot update")
        );
        employee.setUsername(employeeDTO.username());
        employee.setFirstName(employeeDTO.firstName());
        employee.setLastName(employeeDTO.lastName());
        employee.setEmail(employeeDTO.email());
        try {
            return employeeRp.save(employee);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("uk_j9xgmd0ya5jmus09o0b8pqrpb") || e.getMessage().contains("email"))
                throw new BadRequestException("'email' field sent already exists. Cannot update");
            else if (e.getMessage().contains("uk_3gqbimdf7fckjbwt1kcud141m") || e.getMessage().contains("username"))
                throw new BadRequestException("'username' field sent already exists. Cannot update");
            throw new InternalServerErrorException("A non-specific factor causes a violation of data integrity. Cannot update");
        }
    }

    public Employee updateAfterUpload(Employee employee, String url) {
        employee.setProfilePictureUrl(url);
        return employeeRp.save(employee);
    }

    public DeleteRes delete(UUID id) throws BadRequestException, InternalServerErrorException {
        Employee employee = employeeRp.findById(id).orElseThrow(
                () -> new BadRequestException("employee with id='" + id + "' doesn't exist, cannot delete")
        );
        try {
            employeeRp.delete(employee);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("fkmflbwgq59nl79en8rjs8tsd3g") ||
                    (e.getMessage().contains("employees") && e.getMessage().contains("devices") && e.getMessage().contains("vincolo")))
                throw new BadRequestException("employee you are trying to delete is referenced by one ore more devices," +
                        " please delete all referencing devices before deleting employee; you can find all devices " +
                        "assigned to an employee from '/devices?employeeId=<value for employee's id>'");
            throw new InternalServerErrorException("A non-specific factor causes a violation of data integrity. Cannot delete");
        }
        return new DeleteRes("employee with id='" + id + "' has been correctly deleted");
    }


}
