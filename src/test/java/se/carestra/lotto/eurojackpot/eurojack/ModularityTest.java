package se.carestra.lotto.eurojackpot.eurojack;


import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTest {
    @Test
    void verifyApplicationModules() {
        ApplicationModules modules = ApplicationModules.of(EurojackApplication.class);

        modules.forEach(System.out::println);
        modules.verify();
    }
}
