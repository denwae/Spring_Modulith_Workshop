package de.adesso.workshop.spring.modulith.companies.internal.foreign.employees;

import org.jmolecules.ddd.types.Association;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import de.adesso.workshop.spring.modulith.companies.EmployeeChangedEventInterface;
import de.adesso.workshop.spring.modulith.companies.EmployeeCreatedEventInterface;
import de.adesso.workshop.spring.modulith.companies.EmployeeDeletedEventInterface;
import de.adesso.workshop.spring.modulith.companies.internal.Companies;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeListener {

    private final ExternalEmployees repository;
    private final Companies companies;

    @ApplicationModuleListener
    void on(EmployeeCreatedEventInterface event) {
        repository.save(ExternalEmployee.fromEvent(event));
    }

    @ApplicationModuleListener
    void on(EmployeeChangedEventInterface event) {
        var employee = repository.findById(new ExternalEmployee.ExternalEmployeeId(event.getId())).orElseThrow();
        employee.setFirstName(event.getFirstName());
        employee.setLastName(event.getLastName());
        employee.setEmail(event.getEmail());
        repository.save(employee);
    }

    @ApplicationModuleListener
    void on(EmployeeDeletedEventInterface event) {
        var retrievedCompanies = companies.findAllByAdministrator(Association.forId(new ExternalEmployee.ExternalEmployeeId(event.getId())));
        retrievedCompanies.forEach(company -> company.setAdministrator(null));
        companies.saveAll(retrievedCompanies);
    }
}
