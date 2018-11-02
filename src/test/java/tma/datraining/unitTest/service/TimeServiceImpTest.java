package tma.datraining.unitTest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tma.datraining.model.Time;
import tma.datraining.repository.TimeRepository;
import tma.datraining.service.TimeServiceImp;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class TimeServiceImpTest {

	@InjectMocks
	private TimeServiceImp timeServiceImp;
	
	@Mock
	private TimeRepository timeRepository;
	
	
	UUID id1 = UUID.fromString("67a72b63-cf3d-4d3a-84d2-2ea198368225");
	UUID id2 = UUID.fromString("99bea0ae-10c4-42d1-99e5-d1f880f70a0c");
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
//	@Ignore
	@Test
	public void testGetAllTimes() throws Exception{
		List<Time> list = new ArrayList<>();
		Time time1 = new Time(9,3,2018,null,null);
		time1.setTimeId(id1);
		Time time2 = new Time(6,3,2018,null,null);
		time2.setTimeId(id2);
		list.add(time1);
		list.add(time2);
		when(timeRepository.findAll()).thenReturn(list);
		List<Time> list2 = timeServiceImp.list();
		assertNotNull(list2);
		assertEquals(2, list2.size());
		
	}
	
	@Ignore
	@Test
	public void testGetTimeById() throws Exception{
		Time time1 = new Time(9,3,2018,null,null);
		time1.setTimeId(id1);
		when(timeRepository.findByTimeId(any(UUID.class))).thenReturn(time1);
		Time time2 = timeServiceImp.get(id1);
		assertNotNull(time2);
		assertEquals(2018, time2.getYear());
	}

	@Ignore
	@Test
	public void testGetTimesByYear() throws Exception{
		List<Time> list1 = new ArrayList<>();
		Time time1 = new Time(9,3,2018,null,null);
		time1.setTimeId(id1);
		list1.add(time1);
		when(timeRepository.findByYear(anyInt())).thenReturn(list1);
		List<Time> list2 = timeServiceImp.findByYear(2018);
		assertNotNull(list2);
		assertEquals(1, list2.size());
	}
	
	@Ignore
	@Test
	public void testAddTime() throws Exception{
		Time time1 = new Time(9,3,2018,null,null);
		time1.setTimeId(id1);
		when(timeRepository.save(any(Time.class))).thenReturn(time1);
		Time result = timeServiceImp.save(time1);
		assertNotNull(result);
		assertEquals(result.getTimeId(), id1);
		
	}
	
	@Ignore
	@Test
	public void testUpdateTime() throws Exception{
		Time time1 = new Time(9,3,2019,null,null);
		time1.setTimeId(id1);
		when(timeRepository.save(any(Time.class))).thenReturn(time1);
		Time result = timeServiceImp.update(id1, time1);
		assertNotNull(result);
		assertEquals(result.getYear(), 2019);
	}
	
	@Test
	public void testDeleteTime() throws Exception{
		Time time1 = new Time(9,3,2019,null,null);
		time1.setTimeId(id1);
		when(timeRepository.findByTimeId(any(UUID.class))).thenReturn(time1);
		doNothing().when(timeRepository).delete(any(Time.class));
		UUID id = timeServiceImp.delete(id1);
		assertNotNull(id);
		assertEquals(id, time1.getTimeId());
	}
}
