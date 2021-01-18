package gocl.test.zkLeaderElection.zookeeper.candidate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.leader.Candidate;
import org.springframework.integration.leader.Context;

import java.util.UUID;

@Slf4j
public class Leadership implements Candidate {

    private String leadershipRole;

    private Context leaderCtx;

    private final String ID;

    public Leadership() {
        this.ID = UUID.randomUUID().toString();
    }

    public Leadership(String leadershipRole) {
        this();
        this.leadershipRole = leadershipRole;
    }

    @Override
    public String getRole() {
        return this.leadershipRole;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void onGranted(Context ctx) throws InterruptedException {
        // TODO: If there will be some extra time, it could be nice to keep synchronized the
        //  onGranted, onRevoked and isLeader functions
        this.leaderCtx = ctx;
        log.info(" {} leadership granted ", this.getId());
    }

    @Override
    public void onRevoked(Context ctx) {
        this.leaderCtx = null;
        log.info(" {} leadership revoked ", this.getId());
    }

    // TODO: May we check for different roles here?
    public boolean isLeader() {
        return this.leaderCtx != null && this.leaderCtx.isLeader();
    }

    public void yield() {
        this.leaderCtx.yield();
    }
}

