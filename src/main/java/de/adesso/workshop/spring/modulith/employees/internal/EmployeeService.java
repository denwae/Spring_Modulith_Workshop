package de.adesso.workshop.spring.modulith.employees.internal;

import java.time.LocalDate;
import java.util.UUID;

import org.jmolecules.ddd.annotation.Service;
import org.springframework.context.ApplicationEventPublisher;

import de.adesso.workshop.spring.modulith.employees.internal.events.EmployeeChangedEvent;
import de.adesso.workshop.spring.modulith.employees.internal.events.EmployeeCreatedEvent;
import de.adesso.workshop.spring.modulith.employees.internal.events.EmployeeDeletedEvent;
import de.adesso.workshop.spring.modulith.employees.internal.foreign.ExternalCompanies;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService implements EmployeeAPI{

    private final Employees employeeRepository;
    private final ExternalCompanies companiesRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public CompleteEmployeeDTO createEmployee(String firstName, String lastName, LocalDate birthDate, UUID employerId) {
        var employee = employeeRepository.save(new Employee(firstName, lastName, birthDate, employerId));
        eventPublisher.publishEvent(new EmployeeCreatedEvent(
            employee.getId().id(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmail()
        ));
        return CompleteEmployeeDTO.from(employee, companiesRepository.resolve(employee.getEmployer()).orElseThrow());
    }

    @Override
    public CompleteEmployeeDTO getEmployee(UUID id) {
        var employee = employeeRepository.findById(new Employee.EmployeeId(id)).orElseThrow();
        return CompleteEmployeeDTO.from(employee, companiesRepository.resolve(employee.getEmployer()).orElseThrow());
    }

    @Override
    @Transactional
    public CompleteEmployeeDTO changeEmployer(UUID employeeId, UUID companyId) {
        var employee = employeeRepository.findById(new Employee.EmployeeId(employeeId)).orElseThrow();
        employee.changeEmployer(companyId);
        employeeRepository.save(employee);
        return CompleteEmployeeDTO.from(
            employee,
            companiesRepository.resolve(employee.getEmployer()).orElseThrow()
        );
    }

    @Override
    @Transactional
    public CompleteEmployeeDTO changeName(UUID employeeId, String firstName, String lastName) {
        var employee = employeeRepository.findById(new Employee.EmployeeId(employeeId)).orElseThrow();
        employee.updateName(firstName, lastName);
        employeeRepository.save(employee);
        eventPublisher.publishEvent(new EmployeeChangedEvent(
            employee.getId().id(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmail()
        ));
        return CompleteEmployeeDTO.from(
            employee,
            companiesRepository.resolve(employee.getEmployer()).orElseThrow()
        );
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID employeeId) {
        employeeRepository.deleteById(new Employee.EmployeeId(employeeId));
        eventPublisher.publishEvent(new EmployeeDeletedEvent(employeeId));
    }
}
