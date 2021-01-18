package gocl.test.zkLeaderElection.schedule;

import gocl.test.zkLeaderElection.zookeeper.candidate.Leadership;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BanxicoExchrateTask {

    @Autowired
    @Qualifier("banxicoexchrateLeadership")
    private Leadership leadership;

    @Scheduled(fixedDelay = 1000)
    public void getExchangeRate () {
        if (this.leadership.isLeader()) {
            log.info(" GetExchangeRate {} ", System.currentTimeMillis());
        }
    }
}
