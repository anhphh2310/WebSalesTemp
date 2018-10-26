package tma.datraining.unitTest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;

import tma.datraining.model.Time;
import tma.datraining.repository.TimeRepository;
import tma.datraining.service.TimeServiceImp;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TimeServiceImpTest {

	@InjectMocks
	private TimeServiceImp timeServiceImp;
	
	@Mock
	private TimeRepository timeRepository;
	
	
	UUID id1 = UUID.fromString("67a72b63-cf3d-4d3a-84d2-2ea198368225");
	UUID id2 = UUID.fromString("99bea0ae-10c4-42d1-99e5-d1f880f70a0c");
	
	@Test
	public void testGetAllTimes() throws Exception{
		List<Time> list = new ArrayList<>();
		Time time1 = new Time(9,25,2018,null,null);
		time1.setTimeId(id1);
		Time time2 = new Time(9,26,2018,null,null);
		time2.setTimeId(id2);
		list.add(time1);
		list.add(time2);
		when(timeRepository.findAll()).thenReturn(list);
		List<Time> list2 = timeServiceImp.list();
		assertNotNull(list2);
		assertEquals(2, list2.size());
		
	}
	
	@Test
	public void testGetTimeById() throws Exception{
		Time time1 = new Time(9,25,2018,null,null);
		time1.setTimeId(id1);
		when(timeRepository.findByTimeId(id1)).thenReturn(time1);
		Time time2 = timeServiceImp.get(id1);
		assertNotNull(time2);
		assertEquals(2018, time2.getYear());
	}
	
	@Test
	public void testAddTime() throws Exception{
		Time time1 = new Time(9,25,2018,null,null);
		time1.setTimeId(id1);
		when(timeRepository.save(time1)).thenReturn(time1);
		UUID id = timeServiceImp.save(time1);
		assertNotNull(id);
		assertEquals(id1, id);
		
	}

	@Test
	public void testGetTimesByYear() throws Exception{
		List<Time> list1 = new ArrayList<>();
		Time time1 = new Time(9,25,2018,null,null);
		time1.setTimeId(id1);
		list1.add(time1);
		when(timeRepository.findByYear(anyInt())).thenReturn(list1);
		List<Time> list2 = timeServiceImp.findByYear(2018);
		assertNotNull(list2);
		assertEquals(1, list2.size());
	}
}
