package se.carestra.lotto.eurojackpot.eurojack;


import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModularityTest {
  @Test
  void verifyApplicationModules() {
    ApplicationModules modules = ApplicationModules.of(EurojackApplication.class);

    modules.forEach(System.out::println);
    modules.verify();
  }

  @Test
  void createDocumentation() {
    ApplicationModules modules = ApplicationModules.of(EurojackApplication.class);
    Documenter documenter = new Documenter(modules);

    assertNotNull(documenter);
    documenter.writeDocumentation();
  }
}
