package de.adesso.workshop.spring.modulith.companies.internal.foreign.employees;

import java.util.Optional;
import java.util.UUID;

import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Repository;

public interface ExternalEmployees extends
    Repository<ExternalEmployee, ExternalEmployee.ExternalEmployeeId>,
    AssociationResolver<ExternalEmployee, ExternalEmployee.ExternalEmployeeId>
{

    ExternalEmployee save(ExternalEmployee employee);

    Optional<ExternalEmployee> findById(ExternalEmployee.ExternalEmployeeId id);
}
