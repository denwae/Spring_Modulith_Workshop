package de.adesso.workshop.spring.modulith.companies.internal;


import java.util.List;
import java.util.Optional;

import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Repository;

import de.adesso.workshop.spring.modulith.companies.internal.foreign.employees.ExternalEmployee;

public interface Companies extends Repository<Company, Company.CompanyId> {

    Company save(Company company);

    List<Company> saveAll(Iterable<Company> companies);

    Optional<Company> findById(Company.CompanyId id);

    List<Company> findAllByAdministrator(Association<ExternalEmployee, ExternalEmployee.ExternalEmployeeId> association);

    void deleteById(Company.CompanyId id);
}
