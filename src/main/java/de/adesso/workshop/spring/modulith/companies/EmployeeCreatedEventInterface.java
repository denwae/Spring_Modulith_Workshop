package de.adesso.workshop.spring.modulith.companies;

import java.util.UUID;

public interface EmployeeCreatedEventInterface {

    UUID getId();
    String getFirstName();
    String getLastName();
    String getEmail();
}
