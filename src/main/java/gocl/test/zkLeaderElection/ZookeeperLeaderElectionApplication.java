package gocl.test.zkLeaderElection;

import gocl.test.zkLeaderElection.zookeeper.candidate.Leadership;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.zookeeper.config.LeaderInitiatorFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZookeeperLeaderElectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZookeeperLeaderElectionApplication.class, args);
	}

	@Value( "${gocl.test.leader.path}" )
	private String leadershipPath;

	@Value( "${gocl.test.minds.scheduled.leader.role}" )
	private String mindsLeadershipRole;

	@Value( "${gocl.test.minds.banxicoexchrate.leader.role}" )
	private String banxicoexchrateLeadershipRole;

	@Bean("mindsLeadership")
	public Leadership mindsLeadership(){
		return new Leadership(mindsLeadershipRole);
	}

	@Bean("banxicoexchrateLeadership")
	public Leadership banxicoexchrateLeadership(){
		return new Leadership(banxicoexchrateLeadershipRole);
	}

	@Bean
	public LeaderInitiatorFactoryBean mindsLeadershipLeaderInitiator(
			CuratorFramework client, Leadership mindsLeadership) {
		return new LeaderInitiatorFactoryBean()
				.setClient(client)
				.setPath(leadershipPath)
				.setCandidate(mindsLeadership);
	}

	@Bean
	public LeaderInitiatorFactoryBean banxicoExchRateLeadershipLeaderInitiator(
			CuratorFramework client, Leadership banxicoexchrateLeadership) {
		return new LeaderInitiatorFactoryBean()
				.setClient(client)
				.setPath(leadershipPath)
				.setCandidate(banxicoexchrateLeadership);
	}

}
