package com.dreamgames.backendengineeringcasestudy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BackendEngineeringCaseStudyApplicationTests {

    @Test
    void contextLoads() {
        // Just checks if the context loads with the test profile and H2.
    }

}
