package de.adesso.workshop.spring.modulith.employees.internal.foreign;

import org.jmolecules.ddd.types.Association;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import de.adesso.workshop.spring.modulith.companies.CompanyCreatedEvent;
import de.adesso.workshop.spring.modulith.companies.CompanyDeletedEvent;
import de.adesso.workshop.spring.modulith.companies.CompanyUpdatedEvent;
import de.adesso.workshop.spring.modulith.employees.internal.Employees;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompanyListener {

    private final ExternalCompanies externalCompanies;
    private final Employees employees;

    @ApplicationModuleListener
    void on(CompanyCreatedEvent event) {
        externalCompanies.save(new ExternalCompany(event.id(), event.name(), event.address()));
    }

    @ApplicationModuleListener
    void on(CompanyDeletedEvent event) {
        var deletedId = new ExternalCompany.ExternalCompanyId(event.deletedCompany());
        var retrievedEmployees = employees.findAllByEmployer(Association.forId(deletedId));
        retrievedEmployees.forEach(employee -> employee.changeEmployer(event.transferEmployeesTo()));
        employees.saveAll(retrievedEmployees);
        externalCompanies.deleteById(deletedId);
    }

    @ApplicationModuleListener
    void on(CompanyUpdatedEvent event) {
        var updatedCompany = externalCompanies.findById(new ExternalCompany.ExternalCompanyId(event.companyId())).orElseThrow();
        updatedCompany.setName(event.name());
        externalCompanies.save(updatedCompany);
    }
}
