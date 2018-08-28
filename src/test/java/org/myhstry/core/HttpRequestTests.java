package org.myhstry.core;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(Suite.class)
@SuiteClasses({ MyhstryApplicationTests.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTests {
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
 	@Test
	public void test() {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class)).contains("Welcome to myHstry");
	}
}
