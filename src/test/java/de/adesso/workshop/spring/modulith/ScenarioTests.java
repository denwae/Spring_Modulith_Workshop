package de.adesso.workshop.spring.modulith;

import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.adesso.workshop.spring.modulith.companies.CompleteCompanyDTO;
import de.adesso.workshop.spring.modulith.companies.internal.CompanyAPI;
import de.adesso.workshop.spring.modulith.employees.internal.CompleteEmployeeDTO;
import de.adesso.workshop.spring.modulith.employees.internal.EmployeeService;

@SpringBootTest
class ScenarioTests {

    @Autowired
    private CompanyAPI companyService;
    @Autowired
    private EmployeeService employeeService;

    private final String employeeFirstName = "Max";
    private final String employeeLastName = "Mustermann";
    private final LocalDate employeeDateOfBirth = LocalDate.of(2000, 1, 1);

    private final String companyName = "Company";
    private final String companyAddress = "123 Fake Street";

    @Test
    void afterCreatingACompanyAndAdministratorAnAdministratorCanBeAssigned() {
        var company = companyService.createCompany(companyName, companyAddress);

        var tempEmployee = new CompleteEmployeeDTO[1];
        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> assertThatCode(() -> tempEmployee[0] = employeeService.createEmployee(
                    employeeFirstName,
                    employeeLastName,
                    employeeDateOfBirth,
                    company.id()
                )
            ).doesNotThrowAnyException());
        var employee = tempEmployee[0];
        assertThat(employee.employer().id()).isEqualTo(company.id());
        assertThat(employee.employer().name()).isEqualTo(companyName);
        assertThat(employee.employer().address()).isEqualTo(companyAddress);

        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> assertThatCode(() -> companyService.setAdministrator(company.id(), employee.id())).doesNotThrowAnyException()
        );

        var tempCompany = new CompleteCompanyDTO[1];

        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(() ->
            assertThatCode(() -> tempCompany[0] = companyService.getCompany(company.id()))
                .doesNotThrowAnyException()
        );
        var retrievedCompany = tempCompany[0];
        assertThat(retrievedCompany.administrator().id()).isEqualTo(employee.id());
        assertThat(retrievedCompany.administrator().firstName()).isEqualTo(employeeFirstName);
        assertThat(retrievedCompany.administrator().lastName()).isEqualTo(employeeLastName);
        String employeeEmail = "Max.Mustermann@mail.com";
        assertThat(retrievedCompany.administrator().email()).isEqualTo(employeeEmail);
    }

    @Test
    void whenUpdatingTheNameOfAnAdministratorTheChangesAreReflectedInTheCompany() {
        var company = companyService.createCompany(companyName, companyAddress);

        var tempEmployee = new CompleteEmployeeDTO[1];
        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> assertThatCode(() -> tempEmployee[0] = employeeService.createEmployee(
                    employeeFirstName,
                    employeeLastName,
                    employeeDateOfBirth,
                    company.id()
                )
            ).doesNotThrowAnyException());
        var employee = tempEmployee[0];

        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(() ->
            companyService.setAdministrator(company.id(), employee.id())
        );

        var updatedEmployee = employeeService.changeName(employee.id(), "Erika", "Musterfrau");
        assertThat(updatedEmployee.firstName()).isEqualTo("Erika");
        assertThat(updatedEmployee.lastName()).isEqualTo("Musterfrau");
        assertThat(updatedEmployee.email()).isEqualTo("Erika.Musterfrau@mail.com");


        var tempCompany = new CompleteCompanyDTO[1];
        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> assertThatCode(() -> tempCompany[0] = companyService.getCompany(company.id()))
                .doesNotThrowAnyException()
        );
        var retrievedCompany = tempCompany[0];

        assertThat(retrievedCompany.administrator().id()).isEqualTo(employee.id());
        assertThat(retrievedCompany.administrator().firstName()).isEqualTo(updatedEmployee.firstName());
        assertThat(retrievedCompany.administrator().lastName()).isEqualTo(updatedEmployee.lastName());
        assertThat(retrievedCompany.administrator().email()).isEqualTo(updatedEmployee.firstName() + "." + updatedEmployee.lastName() + "@mail.com");
    }

    @Test
    void whenUpdatingACompanyTheChangesAreReflectedInTheEmployees() {
        var company = companyService.createCompany(companyName, companyAddress);

        var tempEmployeeId = new UUID[1];
        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> assertThatCode(() -> tempEmployeeId[0] = employeeService.createEmployee(
                    employeeFirstName,
                    employeeLastName,
                    employeeDateOfBirth,
                    company.id()
                ).id()
            ).doesNotThrowAnyException());
        var employeeId = tempEmployeeId[0];

        var newCompanyName = "New Company Name";
        assertThatThrownBy(() -> companyService.updateName(company.id(), ""));
        var updatedCompany = companyService.updateName(company.id(), newCompanyName);
        assertThat(updatedCompany.name()).isEqualTo(newCompanyName);

        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> assertThat(employeeService.getEmployee(employeeId).employer().name()).isEqualTo(newCompanyName)
        );
    }

    @Test
    void whenACompanyIsDeletedAllEmployeesAreTransferredToANewCompany() {
        var initialEmployer = companyService.createCompany(companyName, companyAddress);
        var differentEmployer = companyService.createCompany("Another Company", "465 Unreal Street");
        var newEmployer = companyService.createCompany("New Company", "987 Real Street");

        var tempEmployees = new UUID[4];
        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> assertThatCode(() -> {
                tempEmployees[0] = employeeService.createEmployee("0", "0", employeeDateOfBirth, initialEmployer.id()).id();
                tempEmployees[1] = employeeService.createEmployee("1", "1", employeeDateOfBirth, initialEmployer.id()).id();
                tempEmployees[2] = employeeService.createEmployee("2", "2", employeeDateOfBirth, differentEmployer.id()).id();
                tempEmployees[3] = employeeService.createEmployee("3", "3", employeeDateOfBirth, differentEmployer.id()).id();
            }).doesNotThrowAnyException()
        );

        companyService.deleteCompany(initialEmployer.id(), newEmployer.id());

        assertThatThrownBy(() -> companyService.getCompany(initialEmployer.id()));

        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> {
                assertThat(employeeService.getEmployee(tempEmployees[0]).employer().id()).isEqualTo(newEmployer.id());
                assertThat(employeeService.getEmployee(tempEmployees[1]).employer().id()).isEqualTo(newEmployer.id());
                assertThat(employeeService.getEmployee(tempEmployees[2]).employer().id()).isEqualTo(differentEmployer.id());
                assertThat(employeeService.getEmployee(tempEmployees[3]).employer().id()).isEqualTo(differentEmployer.id());
            }
        );
    }

    @Test
    void whenAnAdministratorIsDeletedTheCompanyHasNoAdministratorAnymore() {
        var company = companyService.createCompany(companyName, companyAddress);

        var tempEmployee = new CompleteEmployeeDTO[1];
        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> assertThatCode(() -> tempEmployee[0] = employeeService.createEmployee(
                    employeeFirstName,
                    employeeLastName,
                    employeeDateOfBirth,
                    company.id()
                )
            ).doesNotThrowAnyException());
        var employee = tempEmployee[0];

        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> assertThatCode(() -> companyService.setAdministrator(company.id(), employee.id())).doesNotThrowAnyException()
        );

        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(() ->
            assertThat(companyService.getCompany(company.id()).administrator()).isNotNull()
        );

        employeeService.deleteEmployee(employee.id());
        assertThatThrownBy(() ->  employeeService.getEmployee(employee.id()));

        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(() ->
            assertThat(companyService.getCompany(company.id()).administrator()).isNull()
        );
    }

    @Test
    void whenAnEmployeesEmployersIsChangedTheEmployerDataWillChange() {
        var initialEmployer = companyService.createCompany(companyName, companyAddress);
        var newEmployer = companyService.createCompany("New Employer", "987 Real Street");

        var tempEmployee = new CompleteEmployeeDTO[1];

        await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(50)).untilAsserted(
            () -> assertThatCode(() -> tempEmployee[0] = employeeService.createEmployee(
                employeeFirstName,
                employeeLastName,
                employeeDateOfBirth,
                initialEmployer.id())
            ).doesNotThrowAnyException()
        );
        var employee = tempEmployee[0];

        var updatedEmployee = employeeService.changeEmployer(employee.id(), newEmployer.id());
        assertThat(updatedEmployee.employer().id()).isEqualTo(newEmployer.id());
        assertThat(updatedEmployee.employer().name()).isEqualTo(newEmployer.name());
        assertThat(updatedEmployee.employer().address()).isEqualTo(newEmployer.address());
    }
}
