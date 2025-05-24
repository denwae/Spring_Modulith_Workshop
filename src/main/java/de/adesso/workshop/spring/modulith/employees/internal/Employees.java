package de.adesso.workshop.spring.modulith.employees.internal;


import java.util.List;
import java.util.Optional;

import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Repository;

import de.adesso.workshop.spring.modulith.employees.internal.foreign.ExternalCompany;

public interface Employees extends Repository<Employee, Employee.EmployeeId>, AssociationResolver<Employee, Employee.EmployeeId> {

    Employee save(Employee employee);

    Optional<Employee> findById(Employee.EmployeeId id);

    List<Employee> findAllByEmployer(Association<ExternalCompany, ExternalCompany.ExternalCompanyId> association);

    List<Employee> saveAll(Iterable<Employee> employees);

    void deleteById(Employee.EmployeeId employeeId);
}
