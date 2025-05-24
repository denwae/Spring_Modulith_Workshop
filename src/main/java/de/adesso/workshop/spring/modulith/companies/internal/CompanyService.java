package de.adesso.workshop.spring.modulith.companies.internal;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import de.adesso.workshop.spring.modulith.companies.CompanyCreatedEvent;
import de.adesso.workshop.spring.modulith.companies.CompanyDeletedEvent;
import de.adesso.workshop.spring.modulith.companies.CompanyUpdatedEvent;
import de.adesso.workshop.spring.modulith.companies.CompleteCompanyDTO;
import de.adesso.workshop.spring.modulith.companies.internal.foreign.employees.ExternalEmployee;
import de.adesso.workshop.spring.modulith.companies.internal.foreign.employees.ExternalEmployees;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService implements CompanyAPI {

    private final Companies companies;
    private final ApplicationEventPublisher eventPublisher;
    private final ExternalEmployees externalEmployees;

    @Override
    @Transactional
    public CompleteCompanyDTO createCompany(String name, String address) {
        var company = companies.save(new Company(name, address));
        eventPublisher.publishEvent(new CompanyCreatedEvent(company.getId().id(), company.getName(), company.getAddress()));
        return CompleteCompanyDTO.from(company, null);
    }

    @Override
    @Transactional
    public CompleteCompanyDTO getCompany(UUID id) {
        var company = companies.findById(new Company.CompanyId(id)).orElseThrow();
        var administrator = company.getAdministrator() != null ? externalEmployees.resolve(company.getAdministrator()).orElseThrow() : null;
        return CompleteCompanyDTO.from(company, administrator);
    }

    @Override
    @Transactional
    public CompleteCompanyDTO setAdministrator(UUID companyId, UUID employeeId) {
        var company = companies.findById(new Company.CompanyId(companyId)).orElseThrow();
        var employee = externalEmployees.findById(new ExternalEmployee.ExternalEmployeeId(employeeId)).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        company.setAdministrator(employee.getId().id());
        companies.save(company);
        var administrator = company.getAdministrator() != null ? externalEmployees.resolve(company.getAdministrator()).orElseThrow() : null;
        return CompleteCompanyDTO.from(company, administrator);
    }

    @Override
    @Transactional
    public CompleteCompanyDTO updateName(UUID companyId, String name) {
        var company = companies.findById(new Company.CompanyId(companyId)).orElseThrow();
        company.setName(name);
        companies.save(company);
        eventPublisher.publishEvent(new CompanyUpdatedEvent(company.getId().id(), company.getName()));
        var administrator = company.getAdministrator() != null ? externalEmployees.resolve(company.getAdministrator()).orElseThrow() : null;
        return CompleteCompanyDTO.from(company, administrator);
    }

    @Override
    @Transactional
    public void deleteCompany(UUID companyToDelete, UUID employeesTransferredTo) {
        companies.deleteById(new Company.CompanyId(companyToDelete));
        eventPublisher.publishEvent(new CompanyDeletedEvent(companyToDelete, employeesTransferredTo));
    }
}
