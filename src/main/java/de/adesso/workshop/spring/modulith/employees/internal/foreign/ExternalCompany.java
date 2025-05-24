package de.adesso.workshop.spring.modulith.employees.internal.foreign;

import java.util.UUID;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class ExternalCompany implements AggregateRoot<ExternalCompany, ExternalCompany.ExternalCompanyId> {

    @Id
    private ExternalCompanyId id;
    @Setter
    private String name;
    private String address;

    public ExternalCompany(UUID id, String name, String address) {
        this.id = new ExternalCompanyId(id);
        this.name = name;
        this.address = address;
    }

    public record ExternalCompanyId(UUID id) implements Identifier{}
}
