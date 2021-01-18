package gocl.test.zkLeaderElection;

import gocl.test.AbstractIntegrationTest;
import gocl.test.zkLeaderElection.zookeeper.candidate.Leadership;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
class ZookeeperLeaderElectionApplicationTests extends AbstractIntegrationTest {

	@Value("${spring.cloud.zookeeper.connect-string}")
	private String zookeeperConnectString;

	private static final String MINDS_BEAN_NAME = "mindsLeadership";

	@Autowired
	@Qualifier(MINDS_BEAN_NAME)
	private Leadership mindsLeadOne;

	@Autowired
	@Qualifier("banxicoexchrateLeadership")
	private Leadership banxicoLeadOne;

	private static SpringApplicationBuilder instanceTwo;

	@BeforeEach
	public void initialize() {
		if (instanceTwo == null) {
			instanceTwo = createInstance();
		}
	}

	@SneakyThrows
	@Test
	void zookeeperIsRunning() {

		String [] addressParts = zookeeperConnectString.split(":");

		try (
				Socket socket = new Socket(addressParts[0], Integer.parseInt(addressParts[1]));
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
		) {

			// send request
			out.print("stat");
			out.flush();

			// read response
			StringBuilder sb = new StringBuilder();
			String line;

			while((line = in.readLine()) != null) {
				sb.append(line).append(System.getProperty("line.separator"));
			}

			// check that version of Zookeeper is the same as in the container used
			final String response = sb.toString();
			assertTrue(response.contains("Zookeeper version: 3.6.2"));
			log.info(response);
		}
	}

	@Test
	public void uniqueLeaderTest() throws Exception {
		Leadership mindLeadTwo = getLeadership(instanceTwo, MINDS_BEAN_NAME);

		// validate only one leader
		Assertions.assertTrue(mindsLeadOne.isLeader() ^ mindLeadTwo.isLeader(),
			"There is more than one leader");
	}

	@Test
	public void distributedLeadersTest() throws Exception {

		//Reject instanceOne leadership for mindsSchedule process
		mindsLeadOne.yield();
		Leadership mindsLeadTwo = getLeadership(instanceTwo, MINDS_BEAN_NAME);

		//Wait one sec for the leadership to change
		Thread.sleep(1000);

		// Validate one leadership in each instance
		Assertions.assertTrue(banxicoLeadOne.isLeader() && mindsLeadTwo.isLeader() ,
			"Leadership undistributed");
	}

	private SpringApplicationBuilder createInstance() {
		SpringApplicationBuilder instance = new SpringApplicationBuilder(ZookeeperLeaderElectionApplication.class);
		instance.run();
		return instance;
	}

	private Leadership getLeadership(SpringApplicationBuilder instance, String beanName) {
		return instance.context().getBean(beanName,Leadership.class);
	}

}
