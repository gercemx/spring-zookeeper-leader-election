package gocl.test.zkLeaderElection.schedule;

import gocl.test.zkLeaderElection.zookeeper.candidate.Leadership;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTasks {

    @Autowired
    @Qualifier("mindsLeadership")
    private Leadership leadership;

    private final static int MAX_EXECUTIONS = 10;

    private int cont = 0;

    @Scheduled(fixedDelay = 2000)
    public void scheduleTask1 () {
        if (this.leadership.isLeader()) {
            log.info(" Starting scheduleTask1 {} - {} ", ++cont, System.currentTimeMillis());

            // Reject leadership after MAX_EXECUTIONS
            if (cont == MAX_EXECUTIONS) {
                this.leadership.yield();
                this.cont = 0;
            }
        }
    }
}
