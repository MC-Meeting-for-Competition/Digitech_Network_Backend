package kr.hs.sdh.digitechnetwork;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "google.oauth.client-id=test-client-id",
    "google.oauth.client-secret=test-client-secret",
    "google.oauth.redirect-uri=http://localhost:8081/api/v1/auth/google/callback"
})
class DigitechNetworkApplicationTests {

    @Test
    void contextLoads() {
        // 컨텍스트가 정상적으로 로드되는지 확인
    }

}
