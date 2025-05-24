package de.adesso.workshop.spring.modulith.companies.internal.foreign.employees;

import java.util.UUID;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;

import de.adesso.workshop.spring.modulith.companies.EmployeeCreatedEventInterface;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class ExternalEmployee implements AggregateRoot<ExternalEmployee, ExternalEmployee.ExternalEmployeeId> {

    private ExternalEmployeeId id;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String email;

    public ExternalEmployee(UUID id, String firstName, String lastName, String email) {
        this.id = new ExternalEmployeeId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public static ExternalEmployee fromEvent(EmployeeCreatedEventInterface event) {
        return new ExternalEmployee(
            event.getId(),
            event.getFirstName(),
            event.getLastName(),
            event.getEmail()
        );
    }

    public record ExternalEmployeeId(UUID id) implements Identifier {}
}
