package tma.datraining.unitTest.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tma.datraining.model.Time;
import tma.datraining.repository.TimeRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class TimeRepositoryTest {

	@Autowired
	private TimeRepository timeRepository;

	UUID id1 = UUID.fromString("9adbfc3b-642b-4abb-a4e3-31976c43c39e");
	UUID id2 = UUID.fromString("accdb0f3-e347-4722-b6bd-51dd81f2aad2");
	UUID wrongId = UUID.fromString("cfd45a61-94c7-4df7-a1a3-22692e4db104");

	@Before
	public void init() {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Time time1 = new Time(8, 3, 2018, time, time);
		time1.setTimeId(id1);
		Time time2 = new Time(9, 4, 2018, time, time);
		time2.setTimeId(id2);
		timeRepository.save(time1);
		timeRepository.save(time2);
	}

	@Test
	public void testGetAllTime() throws Exception {
		Iterable<Time> results = timeRepository.findAll();
		assertNotNull(results);
		assertThat(((Collection<Time>) results).size()).isEqualTo(2);
	}

	@Test
	public void testGetTimeById() throws Exception {
		Time result = timeRepository.findByTimeId(id1);
		assertNotNull(result);
		assertThat(result.getMonth()).isEqualTo(8);
	}

	@Test
	public void testGetTimeByWrongId() throws Exception{
		Time result = timeRepository.findByTimeId(wrongId);
		assertNull(result);
	}

	@Test
	public void testGetTimesByMonth() throws Exception{
		List<Time> results = timeRepository.findByMonth(9);
		assertNotNull(results);
		assertThat(results.size()).isEqualTo(1);
	}
	
	@Ignore
	@Test
	public void testAddTime() throws Exception{
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Time time1 = new Time(10, 4, 2018, time, time);
		time1.setTimeId(UUID.randomUUID());
		Time result = timeRepository.save(time1);
		assertNotNull(result);
		assertThat(result.getYear()).isEqualTo(2018);
	}
	
	@Test
	public void testUpdateTime() throws Exception{
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Time time1 = new Time(10, 4, 2018, null, time);
		time1.setTimeId(id1);
		Time result = timeRepository.save(time1);
		assertNotNull(result);
		assertEquals(10, result.getMonth());
		Iterable<Time> results = timeRepository.findAll();
		assertThat(((Collection<Time>) results).size()).isEqualTo(2);
	}
	
	@Test
	public void testDeleteTime() throws Exception{
		timeRepository.deleteById(id1);
		Time result = timeRepository.findByTimeId(id1);
		assertNull(result);
		Iterable<Time> results = timeRepository.findAll();
		assertThat(((Collection<Time>) results).size()).isEqualTo(1);
	}
}
