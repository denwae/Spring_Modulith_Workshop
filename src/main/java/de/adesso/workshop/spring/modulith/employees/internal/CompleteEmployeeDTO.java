package de.adesso.workshop.spring.modulith.employees.internal;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import de.adesso.workshop.spring.modulith.employees.internal.foreign.ExternalCompany;

public record CompleteEmployeeDTO(
    UUID id,
    String firstName,
    String lastName,
    String email,
    LocalDate dateOfBirth,
    EmployerDto employer
) {

    public static CompleteEmployeeDTO from(Employee employee, ExternalCompany employer) {
        return new CompleteEmployeeDTO(
            employee.getId().id(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmail(),
            employee.getDateOfBirth(),
            EmployerDto.from(employer)
        );
    }

    public record EmployerDto(
        UUID id,
        String name,
        String address
    ) {
        public static EmployerDto from(ExternalCompany employer) {
            return new EmployerDto(
                employer.getId().id(),
                employer.getName(),
                employer.getAddress()
            );
        }
    }
}
