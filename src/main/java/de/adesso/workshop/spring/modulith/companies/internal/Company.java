package de.adesso.workshop.spring.modulith.companies.internal;

import java.util.UUID;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Identifier;

import de.adesso.workshop.spring.modulith.companies.internal.foreign.employees.ExternalEmployee;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class Company implements AggregateRoot<Company, Company.CompanyId> {

    public Company(String name, String address) {
        this.id = new CompanyId(UUID.randomUUID());
        this.name = name;
        this.address = address;
    }

    private CompanyId id;
    private String name;
    private String address;
    private Association<ExternalEmployee, ExternalEmployee.ExternalEmployeeId> administrator;

    public void setAdministrator(UUID employeeId) {
        this.administrator = employeeId != null ? Association.forId(new ExternalEmployee.ExternalEmployeeId(employeeId)) : null;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be null or empty");
        this.name = name;
    }

    public record CompanyId(UUID id) implements Identifier{}
}
