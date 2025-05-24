package de.adesso.workshop.spring.modulith;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModuleTests {

    @Test
    void verifyModuleStructure() {
        ApplicationModules.of(WorkshopSpringModulithApplication.class).verify();
    }
}
