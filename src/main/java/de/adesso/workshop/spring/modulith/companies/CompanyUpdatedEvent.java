package de.adesso.workshop.spring.modulith.companies;

import java.util.UUID;

public record CompanyUpdatedEvent(
    UUID companyId,
    String name
) {
}
