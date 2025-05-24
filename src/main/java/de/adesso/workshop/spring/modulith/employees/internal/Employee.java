package de.adesso.workshop.spring.modulith.employees.internal;

import java.time.LocalDate;
import java.util.UUID;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Identifier;

import de.adesso.workshop.spring.modulith.employees.internal.foreign.ExternalCompany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Employee implements AggregateRoot<Employee, Employee.EmployeeId> {

    private EmployeeId id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private Association<ExternalCompany, ExternalCompany.ExternalCompanyId> employer;

    public Employee(String firstName, String lastName, LocalDate dateOfBirth, UUID employerId) {
        if(firstName == null || firstName.isBlank()) throw new IllegalArgumentException("firstName cannot be null or blank");
        if(lastName == null || lastName.isBlank()) throw new IllegalArgumentException("lastName cannot be null or blank");
        if(dateOfBirth == null) throw new IllegalArgumentException("birthDate cannot be null");
        if(employerId == null) throw new IllegalArgumentException("employedBy cannot be null");

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = firstName + "." + lastName + "@mail.com";
        this.dateOfBirth = dateOfBirth;
        this.employer = Association.forId(new ExternalCompany.ExternalCompanyId(employerId));
        this.id = new EmployeeId(UUID.randomUUID());
    }

    public void updateName(String firstName, String lastName) {
        if(firstName == null || firstName.isBlank()) throw new IllegalArgumentException("firstName cannot be null or blank");
        if(lastName == null || lastName.isBlank()) throw new IllegalArgumentException("lastName cannot be null or blank");
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = firstName + "." + lastName + "@mail.com";
    }

    public void changeEmployer(UUID newEmployerId) {
        this.employer = Association.forId(new ExternalCompany.ExternalCompanyId(newEmployerId));
    }

    public record EmployeeId(UUID id) implements Identifier{}
}
