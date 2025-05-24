package de.adesso.workshop.spring.modulith.companies;

import java.util.UUID;

public record CompanyCreatedEvent(
    UUID id,
    String name,
    String address
) {
}
