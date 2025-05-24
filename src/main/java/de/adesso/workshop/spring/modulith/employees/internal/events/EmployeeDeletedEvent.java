package de.adesso.workshop.spring.modulith.employees.internal.events;

import java.util.UUID;

import de.adesso.workshop.spring.modulith.companies.EmployeeDeletedEventInterface;

public class EmployeeDeletedEvent implements EmployeeDeletedEventInterface {

    private UUID employeeId;

    public EmployeeDeletedEvent(UUID employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public UUID getId() {
        return employeeId;
    }
}
