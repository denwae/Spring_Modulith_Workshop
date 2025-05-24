package de.adesso.workshop.spring.modulith.companies.internal;

import java.util.UUID;

import de.adesso.workshop.spring.modulith.companies.CompleteCompanyDTO;

public interface CompanyAPI {

    CompleteCompanyDTO createCompany(String name, String address);

    CompleteCompanyDTO getCompany(UUID id);

    CompleteCompanyDTO setAdministrator(UUID companyId, UUID employeeId);

    CompleteCompanyDTO updateName(UUID companyId, String name);

    void deleteCompany(UUID id, UUID employeesTransferredTo);
}
