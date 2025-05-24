package de.adesso.workshop.spring.modulith.employees.internal.foreign;


import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Repository;


public interface ExternalCompanies extends
    Repository<ExternalCompany, ExternalCompany.ExternalCompanyId>,
    AssociationResolver<ExternalCompany, ExternalCompany.ExternalCompanyId> {

    ExternalCompany save(ExternalCompany company);

    void deleteById(ExternalCompany.ExternalCompanyId deletedId);
}
