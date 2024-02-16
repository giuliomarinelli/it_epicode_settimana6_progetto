package it.epicode.w6d5.devices_management.services;

import it.epicode.w6d5.devices_management.Models.entities.Device;
import it.epicode.w6d5.devices_management.Models.entities.Employee;
import it.epicode.w6d5.devices_management.Models.enums.DeviceType;
import it.epicode.w6d5.devices_management.Models.reqDTO.DeviceDTO;
import it.epicode.w6d5.devices_management.Models.resDTO.DeleteRes;
import it.epicode.w6d5.devices_management.exceptions.BadRequestException;
import it.epicode.w6d5.devices_management.exceptions.NotFoundException;
import it.epicode.w6d5.devices_management.repositories.DeviceRepository;
import it.epicode.w6d5.devices_management.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRp;

    @Autowired
    private EmployeeRepository employeeRp;

    @Autowired
    private EmailService emailSvc;


    public Page<Device> getAll(Pageable pageable) {
        return deviceRp.findAll(pageable).map(res -> {
            if (res.getEmployee() != null) {
                res.setEmployeeId(res.getEmployee().getId());
            } else {
                res.setEmployeeId(null);
            }
            return res;
        });
    }

    public Page<Device> getByEmployeeId(Pageable pageable, UUID employeeId) {
        return deviceRp.getByEmployeeId(pageable, employeeId).map(res -> {
            if (res.getEmployee() != null) {
                res.setEmployeeId(res.getEmployee().getId());
            } else {
                res.setEmployeeId(null);
            }
            return res;
        });
    }

    public Device findById(UUID id) throws NotFoundException {
        return deviceRp.findById(id).orElseThrow(
                () -> new NotFoundException("device with id = '" + id + "' not found")
        );
    }

    public Device create(DeviceDTO deviceDTO) throws BadRequestException {
        Device device = new Device(deviceDTO.available(), deviceDTO.underMaintenance(),
                deviceDTO.neglected(), deviceDTO.type());
        return deviceRp.save(device);
    }

    public Device update(DeviceDTO deviceDTO, UUID id) throws BadRequestException {
        Device device = deviceRp.findById(id).orElseThrow(
                () -> new BadRequestException("device with id = '" + id + "' doesn't exist, cannot update")
        );
        if (device.getEmployee() != null) {
            device.setEmployeeId(device.getEmployee().getId());
        } else {
            device.setEmployeeId(null);
        }
        device.setAvailable(deviceDTO.available());
        device.setUnderMaintenance(deviceDTO.underMaintenance());
        device.setNeglected(deviceDTO.neglected());
        try {
            device.setType(DeviceType.valueOf(deviceDTO.type()));
        } catch (IllegalArgumentException e) { // controllo aggiuntivo alla validazione
            throw new BadRequestException("Malformed 'type' field, allowed exact-match values are SMARTPHONE, TABLET, LAPTOP, DOMOTIC_DEVICE," +
                    " DIGITAL_CAMERA, SMART_CARD, DESKTOP_COMPUTER, TV, OTHERS");
        }

        deviceRp.save(device);

        if (device.getEmployee() != null) {
            emailSvc.sendEmail(device.getEmployee().getEmail(), "Your resource has been updated successfully",
                    "Hello " + device.getEmployee().getFirstName() +
                            "\n\nYour device resource (id = '" + device.getId() + "', type = '" + device.getType().toString() +
                            "') was updated successfully.\n\n" +
                            "Have a nice day!\n\nAdmin"
            );
        }
        return device;

    }
    /* Non essendoci indicazioni specifiche, ho lasciato libera la possibilità di ri-assegnare
        un dispositivo già assegnato ad un employee ad un altro employee
     */
    public Device assignDeviceToEmployee(UUID employeeId, UUID deviceId) throws BadRequestException {
        Employee employee = employeeRp.findById(employeeId).orElseThrow(
                () -> new BadRequestException("employee with id = '" + employeeId + "' doesn't exist, cannot assign device to employee")
        );
        Device device = deviceRp.findById(deviceId).orElseThrow(
                () -> new BadRequestException("device with id = '" + deviceId + "' doesn't exist, cannot assign device to employee")
        );
        device.setEmployee(employee);
        device.setEmployeeId(employeeId);
        device.setAssigned(true);
        deviceRp.save(device);
        if (device.getEmployee() != null) {
            emailSvc.sendEmail(device.getEmployee().getEmail(), "A new device resource was assigned to your employee resource",
                    "Hello " + device.getEmployee().getFirstName() +
                            "\n\nA new device resource (id = '" + deviceId + "', type = '" + device.getType().toString() +
                            "') was assigned to your employee resource (id = '" + employeeId + "').\n\n" +
                            "Have a nice day!\n\nAdmin"
            );
        }
        return device;
    }

    public DeleteRes delete(UUID id) throws BadRequestException {
        Device device = deviceRp.findById(id).orElseThrow(
                () -> new BadRequestException("device with id = '" + id + "' doesn't exist, cannot delete")
        );
        deviceRp.delete(device);
        if (device.getEmployee() != null) {
            emailSvc.sendEmail(device.getEmployee().getEmail(), "Your device has been successfully deleted",
                    "Hello " + device.getEmployee().getFirstName() +
                            "\n\nYour device resource (id = '" + id +
                            "') has been successfully deleted.\n" +
                            "employee resource id = '" + device.getEmployee().getId() + "'.\n\n" +
                            "Have a nice day!\n\nAdmin"
            );
        }
        return new DeleteRes("device with id = '" + id + "' has been correctly deleted");
    }


}
