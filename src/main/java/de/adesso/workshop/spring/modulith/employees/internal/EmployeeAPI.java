package de.adesso.workshop.spring.modulith.employees.internal;

import java.time.LocalDate;
import java.util.UUID;

public interface EmployeeAPI {

    CompleteEmployeeDTO createEmployee(String firstName, String lastName, LocalDate dateOfBirth, UUID companyId);

    CompleteEmployeeDTO getEmployee(UUID id);

    CompleteEmployeeDTO changeEmployer(UUID employeeId, UUID companyId);

    CompleteEmployeeDTO changeName(UUID employeeId, String firstName, String lastName);

    void deleteEmployee(UUID employeeId);
}
