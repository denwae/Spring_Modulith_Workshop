package de.adesso.workshop.spring.modulith.employees.internal.events;

import java.util.UUID;

import de.adesso.workshop.spring.modulith.companies.EmployeeChangedEventInterface;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmployeeChangedEvent implements EmployeeChangedEventInterface {

    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String email;

    @Override
    public UUID getId() {
        return id;
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
