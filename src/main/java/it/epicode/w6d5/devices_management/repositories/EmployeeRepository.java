package it.epicode.w6d5.devices_management.repositories;

import it.epicode.w6d5.devices_management.Models.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID>, PagingAndSortingRepository<Employee, UUID> {
    @Query("SELECT e.email FROM Employee e")
    public List<String> getAllEmails();

    @Query("SELECT e.username FROM Employee e")
    public List<String> getAllUsernames();
}
