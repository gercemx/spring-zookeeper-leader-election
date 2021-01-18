package gocl.test;

import gocl.test.zkLeaderElection.ZookeeperLeaderElectionApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles(value = "test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = ZookeeperLeaderElectionApplication.class
)
public abstract class AbstractIntegrationTest extends TestContextInitializer {

    @BeforeEach
    void cleanup() {

        //TODO: Any need to cleanup Zookeeper server?
    }
}
