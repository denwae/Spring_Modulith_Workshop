package de.adesso.workshop.spring.modulith.companies;

import java.util.UUID;

import de.adesso.workshop.spring.modulith.companies.internal.Company;
import de.adesso.workshop.spring.modulith.companies.internal.foreign.employees.ExternalEmployee;

public record CompleteCompanyDTO(
    UUID id,
    String name,
    String address,
    AdministratorDTO administrator
) {

    public static CompleteCompanyDTO from(Company company, ExternalEmployee administrator) {
        return new CompleteCompanyDTO(
            company.getId().id(),
            company.getName(),
            company.getAddress(),
            company.getAdministrator() != null ? AdministratorDTO.from(administrator) : null
        );
    }

    public record AdministratorDTO(
        UUID id,
        String firstName,
        String lastName,
        String email
    ){
        public static AdministratorDTO from(ExternalEmployee administrator) {
            return new AdministratorDTO(
                administrator.getId().id(),
                administrator.getFirstName(),
                administrator.getLastName(),
                administrator.getEmail()
            );
        }
    }
}
