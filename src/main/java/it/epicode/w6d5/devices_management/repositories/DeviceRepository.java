package it.epicode.w6d5.devices_management.repositories;

import it.epicode.w6d5.devices_management.Models.entities.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID>, PagingAndSortingRepository<Device, UUID> {
    @Query("SELECT d FROM Device d WHERE d.employee.id = :employeeId")
    public Page<Device> getByEmployeeId(Pageable pageable, UUID employeeId);
}
