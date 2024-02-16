package it.epicode.w6d5.devices_management.services;

import it.epicode.w6d5.devices_management.Models.entities.Device;
import it.epicode.w6d5.devices_management.Models.entities.Employee;
import it.epicode.w6d5.devices_management.Models.enums.DeviceType;
import it.epicode.w6d5.devices_management.Models.reqDTO.DeviceDTO;
import it.epicode.w6d5.devices_management.Models.reqDTO.EmployeeDTO;
import it.epicode.w6d5.devices_management.exceptions.BadRequestException;
import it.epicode.w6d5.devices_management.exceptions.NotFoundException;
import it.epicode.w6d5.devices_management.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class DeviceService {

    @Autowired
    DeviceRepository deviceRp;


    public Page<Device> getAll(Pageable pageable) {
        return deviceRp.findAll(pageable).map(res -> {
            res.setEmployeeId(res.getEmployee().getId());
            return res;
        });
    }

    public Page<Device> getByEmployeeId(Pageable pageable, UUID employeeId) {
        return deviceRp.getByEmployeeId(pageable, employeeId).map(res -> {
            res.setEmployeeId(res.getEmployee().getId());
            return res;
        });
    }

    public Device findById(UUID id) throws NotFoundException {
        return deviceRp.findById(id).orElseThrow(
                () -> new NotFoundException("device with id='" + id + "' not found")
        );
    }

    public Device create(DeviceDTO deviceDTO) throws BadRequestException {
        Device device = new Device(deviceDTO.available(), deviceDTO.underMaintenance(),
                deviceDTO.neglected(), deviceDTO.type());
        return deviceRp.save(device);
    }

    public Device update(DeviceDTO deviceDTO, UUID id) throws BadRequestException {
        Device device = deviceRp.findById(id).orElseThrow(
                () -> new BadRequestException("device with id='" + id + "' doesn't exist. cannot update")
        );
        device.setEmployeeId(device.getEmployee().getId());
        device.setAvailable(deviceDTO.available());
        device.setUnderMaintenance(deviceDTO.underMaintenance());
        device.setNeglected(deviceDTO.neglected());
        try {
            device.setType(DeviceType.valueOf(deviceDTO.type()));
        } catch (IllegalArgumentException e) { // controllo aggiuntivo alla validazione
            throw new BadRequestException("Malformed 'type' field, allowed exact-match values are SMARTPHONE, TABLET, LAPTOP, DOMOTIC_DEVICE," +
                    " DIGITAL_CAMERA, SMART_CARD, DESKTOP_COMPUTER, TV, OTHERS");
        }
        return deviceRp.save(device);

    }

    public Device assignDeviceToEmployee


}
