package org.myhstry.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private MockMvc mockMvc;
	
	private String site = "http://localhost:" + port + "/";

	@Test
	public void testHomepage() {
		assertThat(this.restTemplate.getForObject(site, String.class)).contains("Hello World");
	}
	
	@Test
	public void testLogin() throws Exception {
		// Get Login Page
		mockMvc.perform(get("/login")).andExpect(status().isOk());
		// Submit Login Details
		// Success/Failure Responses
	}

}
