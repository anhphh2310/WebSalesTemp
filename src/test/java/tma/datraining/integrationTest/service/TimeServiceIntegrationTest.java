package tma.datraining.integrationTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tma.datraining.model.Time;
import tma.datraining.service.TimeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeServiceIntegrationTest {

	@Autowired
	private TimeService timeService;
	
	@Test
	public void testGetAllTime() throws Exception {
		List<Time> results = timeService.list();
		assertNotNull(results);
		assertThat(results.size()).isNotEqualTo(0);
	}
	
	@Test
	public void testGetTimeById() throws Exception{
		
	}

}
