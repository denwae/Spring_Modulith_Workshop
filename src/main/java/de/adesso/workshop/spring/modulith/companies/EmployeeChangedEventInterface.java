package de.adesso.workshop.spring.modulith.companies;

import java.util.UUID;

public interface EmployeeChangedEventInterface {

    UUID getId();
    String getFirstName();
    String getLastName();
    String getEmail();
}
