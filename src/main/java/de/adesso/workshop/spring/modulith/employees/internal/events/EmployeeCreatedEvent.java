package de.adesso.workshop.spring.modulith.employees.internal.events;

import java.util.UUID;

import de.adesso.workshop.spring.modulith.companies.EmployeeCreatedEventInterface;

public class EmployeeCreatedEvent implements EmployeeCreatedEventInterface {

    private final UUID employeeId;
    private final String firstName;
    private final String lastName;
    private final String email;

    public EmployeeCreatedEvent(UUID employeeId, String firstName, String lastName, String email) {
        if(employeeId == null) throw new NullPointerException("Employee id cannot be null");
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public UUID getId() {
        return employeeId;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
